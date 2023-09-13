package org.example;

import junit.framework.TestCase;
import org.checkerframework.checker.units.qual.K;
import org.example.app.App;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.security.Key;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@RunWith(JUnit4.class)
public class KeyNoteDaoTest extends TestCase {
    private boolean testing;
    private KeyNoteDao keyNoteDao;

    @Before
    public void init(){
        testing = true;
        Connection connection = App.connectMySQL();
        keyNoteDao = KeyNoteDao.getInstance(connection);
    }

    @After
    public void teardown(){
        testing = false;
    }

    @Test
    public void testSetup(){
        assertTrue(keyNoteDao != null);
    }

    @Test
    public void testNonNullGetAll(){
        List<KeyNote> notes = keyNoteDao.getAll();
        for(KeyNote note:notes){
            assertTrue(note != null);
        }
    }

    @Test
    public void testNonEmptyGetAll(){
        List<KeyNote> notes = keyNoteDao.getAll();
        assertTrue(notes.size() > 0);
    }

    @Test
    public void testGetSingleKeyNote(){
        List<KeyNote> notes = keyNoteDao.getAll();
        boolean checked = false;
        for(KeyNote note: notes){
            KeyNote a = keyNoteDao.getById(note.getId());
            checked = a.getId().equals(note.getId());
            assertTrue(checked);
        }
    }

    @Test
    public void testSimpleCRUD(){
        KeyNote note = KeyNote.builder().id(78)
                .theme("YOLO").note("A test sample")
                .description("Just a simple test")
                .links(new String[] {"nothing", "a"})
                .build();
        KeyNote updateNote = KeyNote.builder().id(0).theme("ZOLO")
                .note("A nano test").description("DUUP")
                .links(new String[]{}).build();
        assertTrue(keyNoteDao.save(note));
        KeyNote anote = keyNoteDao.getById(note.getId());
        assertEquals(anote.getId(), note.getId());
        updateNote.setId(anote.getId());
        assertTrue(keyNoteDao.update(updateNote));
        assertTrue(keyNoteDao.delete(anote.getId()));
        anote = keyNoteDao.getById(note.getId());
        assertTrue(anote == null);
    }

    @Test
    public void testBuildLIKEQuery(){
        List<KeyNote> notes = new ArrayList<>();

        KeyNote sample = KeyNote.builder().id(4545).note("Hello World")
                .theme("Java").description("A SIMPLE TEST NOTE")
                .build();

        notes.add(KeyNote.builder().theme(sample.getTheme()).build());
        notes.add(KeyNote.builder().note(sample.getNote()).build());
        notes.add(KeyNote.builder().description(sample.getDescription()).build());
        notes.add(KeyNote.builder().
                theme(sample.getTheme())
                .description(sample.getDescription())
                .build());
        notes.add(sample);
        String builtNote = "";
        String queryBase = "SELECT * FROM notes WHERE ";
        String[] queries = {"theme LIKE \"%Java%\"", "note LIKE \"%Hello World%\"",
                "description LIKE \"%A SIMPLE TEST NOTE%\"",
                "description LIKE \"%A SIMPLE TEST NOTE%\" AND theme LIKE \"%Java%\"",
                "note LIKE \"%Hello World%\" AND description LIKE \"%A SIMPLE TEST NOTE%\" AND theme LIKE \"%Java%\""};

        for(int i = 0; i < notes.size(); ++i){
            builtNote = keyNoteDao.buildLIKEQuery(notes.get(i));
            assertEquals(queryBase+queries[i]+";", builtNote);
        }
    }

    @Test
    public void testFindOnKeyword(){
        List<KeyNote> notes = new ArrayList<>();
        KeyNote note = null;
        KeyNote baseNote = KeyNote.builder().id(78)
                .theme("Anime").note("Weird anime")
                .description("Another is a fun horror anime that is suitable for all ages")
                .links(new String[] {"nothing", "a"})
                .build();
        boolean added = keyNoteDao.save(baseNote);
        assertTrue(added);
        String[] keywords = {"horror", "anime", "all ages", "a fun"};
        for(String key: keywords){
            // description
            note = KeyNote.builder().description("horror").build();
            notes = keyNoteDao.findSimilarObjects(note);
            assertEquals(notes.size(), 1);
        }
        boolean deleted = keyNoteDao.delete(78);
        assertTrue(deleted);
    }

    @Test
    public void testFindSimilar(){
        List<KeyNote> notes = null;
        KeyNote baseNote = KeyNote.builder().id(78)
                .theme("Anime").note("Weird anime")
                .description("Another is a fun horror anime that is suitable for all ages")
                .links(new String[] {"nothing", "a"})
                .build();
        int[] randomIDs = {4545, 4646, 4747, 4848};
        for(int id: randomIDs){
            baseNote.setId(id);
            boolean added = keyNoteDao.save(baseNote);
            assertTrue(added);
        }
        notes = keyNoteDao.findSimilarObjects(baseNote);
        assertEquals(notes.size(), randomIDs.length);
        // test on 2/3 properties
        baseNote.setTheme("");
        notes = keyNoteDao.findSimilarObjects(baseNote);
        assertEquals(notes.size(), randomIDs.length);
        // test on 1/3 properties
        baseNote.setNote("");
        notes = keyNoteDao.findSimilarObjects(baseNote);
        assertEquals(notes.size(), randomIDs.length);
        for(int id: randomIDs){
            boolean added = keyNoteDao.delete(id);
            assertTrue(added);
        }
    }
}
