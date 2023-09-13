package org.example.app;

import org.example.BashHelper;
import picocli.CommandLine;

import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "bash", description = "Allow to run bash command",
        mixinStandardHelpOptions = true)
public class Bash implements Callable<Integer> {
    public Bash(){}
    @CommandLine.Option(names = {"-c", "--command"},
            description = "Your bash command and its arguments.\n Example 'bash -cmd echo,hello' -> hello",
            split = ",")
    private String[] commands;
    @Override
    public Integer call() throws Exception {
        int exitCode = BashHelper.runBashCommand(commands);
        return exitCode;
    }
}
