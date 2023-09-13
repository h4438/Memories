package org.example.app;
import lombok.Getter;
import lombok.Setter;
import org.example.KeyNote;
import picocli.CommandLine;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "note",
        mixinStandardHelpOptions = true, version = "1.0",
        description = "A terminal based note taking application")
public class App implements Callable<Integer> {

    @Setter
    @Getter
    private List<KeyNote> cacheNotes;

    @Getter
    private Connection connection;

    @Getter
    @Setter
    private String workPath;

    @Getter
    @Setter
    private boolean isRunning;

    @Getter
    @Setter
    private CommandLine commandLine;

    public static void main(String... args){
        int cmdExitCode = -1;
        App app = new App();
        CommandLine commandLine = new CommandLine(app)
                .addSubcommand("view", new View(app))
                .addSubcommand("edit", new Edit(app))
                .addSubcommand("bash", new Bash());
        app.setCommandLine(commandLine);
        try {
            cmdExitCode = app.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            System.out.println("Bye bye!");
            System.exit(cmdExitCode);
        }
    }

    public App(){
        this.workPath = "./";
        this.isRunning = true;
        this.connection = connectMySQL();
    }

    @Override
    public Integer call() throws Exception {
        int cmdExitCode = -1;
        Scanner scanner = new Scanner(System.in);
        while (this.isRunning()){
            System.out.print("> ");
            String command = scanner.nextLine().strip();
            cmdExitCode = commandLine.execute(command.split(" "));
        }
        return cmdExitCode;
    }

    @CommandLine.Command(name = "exit",
            description = "Exit the program")
    public void exit(){
        this.setRunning(false);
    }

    public static Connection connectMySQL(){
        try{
            String user = System.getenv("user");
            String password = System.getenv("password");
            String url = System.getenv("url");
            String db = System.getenv("db");
            StringBuilder builder = new StringBuilder("jdbc:mysql://");
            builder.append(url).append("/").append(db);
            String mysqlUrl = builder.toString();
            Connection connection = DriverManager.getConnection(mysqlUrl, user, password);
            System.out.println("MySQL connected");
            return connection;
        }
        catch (Exception exception){
            exception.printStackTrace();
            return null;
        }
    }
}
