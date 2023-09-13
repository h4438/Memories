package org.example.app;

import org.example.BashHelper;
import org.example.KeyNote;
import org.example.KeyNoteDao;
import org.example.OSHelper;
import picocli.CommandLine;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import java.util.concurrent.Callable;


@CommandLine.Command(name = "edit", description = "edit selected note",
        mixinStandardHelpOptions = true)
public class Edit implements Callable<Integer> {
    private App appContext;

    @CommandLine.Option(names = {"-s", "--select"},
            description = "Selected index of the cache notes",
            defaultValue = "0")
    private Integer selectedIndex;

    @CommandLine.Option(names = {"-n", "--name"},
            description = "File name for the copy note")
    private String fileName;

    public Edit(App appContext){
        this.appContext = appContext;
    }

    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public KeyNote getFromCache(int index){
        List<KeyNote> cacheNotes = appContext.getCacheNotes();
        if(cacheNotes == null || cacheNotes.size() == 0){
            System.out.println("There are no cache results! Please run 'view' command.");
            return null;
        }
        return cacheNotes.get(this.selectedIndex);
    }

    public void createNoteFile(KeyNote note, String inputFileName) throws IOException {
        String fullPath = "";
        String path = appContext.getWorkPath();
        String fileName = inputFileName != null?
                inputFileName:
                note.getNote().replace(" ","_")+".md";
        if(path.endsWith("/")){
            fullPath = path+fileName;
        }
        else{
            fullPath = path+"/"+fileName;
        }
        OSHelper.createFile(note.toMDFormat(), fullPath);
    }

    @CommandLine.Command(name = "copy", description = "Copy a note to a file")
    public int copyNoteToFile(){
        int exitCode = -1;
        KeyNote note = getFromCache(selectedIndex);
        try {
            createNoteFile(note, fileName);
            BashHelper.runBashLine("ls ./");
            exitCode = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            return exitCode;
        }
    }

    @CommandLine.Command(name = "update", description = "Update a note from a file")
    public int updateNoteFromFile(){
        int exitCode = -1;
        if(this.fileName == null){
            System.out.println("Please provide a file name with -n");
            return exitCode;
        }
        try {
            KeyNoteDao dao = KeyNoteDao.getInstance(this.appContext.getConnection());
            KeyNote importedNote = KeyNote.parseFromFilePath(fileName, true);
            dao.update(importedNote);
            exitCode = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exitCode;
    }

    @CommandLine.Command(name = "new", description = "Create a new note template")
    public int createFileTemplate(){
        int exitCode = -1;
        if (this.fileName == null || this.fileName.isEmpty()){
            return exitCode;
        }
        KeyNote note = KeyNote.builder().build();
        note.setCreatedOn();
        try {
            OSHelper.createFile(note.toMDFormat(), this.fileName);
            exitCode = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exitCode;
    }

    @CommandLine.Command(name = "save", description = "Save a note file to the database")
    public int saveNote(){
        int exitCode = -1;
        if (this.fileName == null || this.fileName.isEmpty()){
            return exitCode;
        }
        try {
            KeyNote note = KeyNote.createFromFilePath(this.fileName);
            KeyNoteDao dao = KeyNoteDao.getInstance(this.appContext.getConnection());
            dao.save(note);
            System.out.println(note.toMDFormat());
            exitCode = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exitCode;
    }
}
