package org.example.app;

import org.example.KeyNote;
import org.example.KeyNoteDao;
import picocli.CommandLine;
import pl.mjaron.etudes.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "view", description = "view notes",
        mixinStandardHelpOptions = true)
public class View implements Callable<Integer> {

    private App appContext;

    @CommandLine.Option(names = {"-a", "--all"},
            description = "view all notes")
    private boolean viewAll;

    @CommandLine.Option(names = {"-i", "--id"},
            description = "Find note whose id match the given value")
    private Integer noteId;

    @CommandLine.Option(names = {"-n", "--note"},
            description = "Find note whose note property LIKE the given value")
    private String noteName;

    @CommandLine.Option(names = {"-t", "--theme"},
            description = "Find note whose theme property LIKE the given value")
    private String theme;

    @CommandLine.Option(names = {"-d", "--description"},
            description = "Find note whose description property LIKE the given value")
    private String description;
    public View(App appContext){
        this.appContext = appContext;
    }

    @Override
    public Integer call() throws Exception {
        boolean noInput = noteName==null && description==null && theme==null;
        if(viewAll){
            viewAllNotes();
            return 0;
        }
        else if (!noInput) {
            KeyNote baseObject = KeyNote.builder()
                    .note(noteName != null? noteName:"")
                    .description(description != null? description:"")
                    .theme(theme != null? theme:"")
                    .build();
            viewSimilarNotes(baseObject);
            return 0;
        }
        else if(noteId != null){
            viewNoteWithID(noteId);
            return 0;
        }
        return -1;
    }
    public void cacheResultNotes(List<KeyNote> resultNotes){
        appContext.setCacheNotes(resultNotes);
    }
    public void viewAllNotes(){
        KeyNoteDao keyNoteDao = KeyNoteDao.getInstance(appContext.getConnection());
        List<KeyNote> notes = keyNoteDao.getAll();
        cacheResultNotes(notes);
        Table.render(notes, KeyNote.class).markdown().to(System.out).run();
    }
    public void viewSimilarNotes(KeyNote baseObject){
        KeyNoteDao dao = KeyNoteDao.getInstance(appContext.getConnection());
        List<KeyNote> notes = dao.findSimilarObjects(baseObject);
        cacheResultNotes(notes);
        Table.render(notes, KeyNote.class).markdown().to(System.out).run();
    }
    public void viewNoteWithID(int id) {
        KeyNoteDao dao = KeyNoteDao.getInstance(appContext.getConnection());
        List<KeyNote> result = new ArrayList<>();
        KeyNote note = dao.getById(id);
        result.add(note);
        cacheResultNotes(result);
        System.out.println(note.toMDFormat());
    }
    @CommandLine.Command(name = "cache", description = "View the previous result.")
    public void viewPreviousResult(){
        List<KeyNote> notes = this.appContext.getCacheNotes();
        if(notes == null){
            System.out.println("You haven't run the 'view' command yet!");
        }
        else {
            Table.render(notes, KeyNote.class).markdown().to(System.out).run();
        }
    }
}
