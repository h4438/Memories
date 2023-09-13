package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public class KeyNoteTest extends TestCase {

    @Test
    public void testParseLinks(){
        String[] inputs =
                {"https://www.baeldung.com/java-ternary-operator;" +
                        "https://www.tutorialkart.com/java/java-system-setin/#gsc.tab=0",
                        "https://www.guru99.com/where-clause.html", "", ";https://www.baeldung.com/jacoco",
                        ";https://www.baeldung.com/java-unit-testing-best-practices;https://www.baeldung.com/jacoco;" +
                                "https://www.logicbig.com/how-to/junit/java-test-user-command-line-input.html"};
        int[] expectedLength = {2,1,0,1,3};
        assertEquals(inputs.length, expectedLength.length);
        for(int i = 0; i < inputs.length; ++i){
            String[] links = KeyNote.parseLinks(inputs[i]);
            assertEquals(links.length, expectedLength[i]);
        }
    }

    @Test
    public void testParseCompileLinks(){
        String[] links = {"https://www.baeldung.com/java-ternary-operator",
                "https://www.baeldung.com/java-unit-testing-best-practices", "https://www.baeldung.com/jacoco",
                "https://www.logicbig.com/how-to/junit/java-test-user-command-line-input.html"};
        KeyNote note = KeyNote.builder().links(links).build();
        String linksStr = KeyNote.compileLinks(note);
        String[] a = KeyNote.parseLinks(linksStr);
        assertEquals(a.length, links.length);
    }

    @Test
    public void testCreateFromInput(){
        // id, note, description, links, theme
        String[] notes = {"test", "a note"};
        String[] desc = {"A simple test", ""};
        String[] links = {"tutor1;tutor2", "tutor"};
        String[] themes = {"java", "python"};
        for(int i = 0; i < notes.length; ++i){
            String aCase = notes[i]+"\n"+desc[i]+"\n"+themes[i]+"\n"+links[i];
            System.setIn(new ByteArrayInputStream(aCase.getBytes()));
            KeyNote note = KeyNote.createFromInput(System.in);
            assertEquals(note.getNote(), notes[i]);
            assertEquals(note.getDescription(), desc[i]);
            assertEquals(note.getLinks().length, KeyNote.parseLinks(links[i]).length);
            assertEquals(note.getLinks().length, links[i].split(";").length);
            assertEquals(note.getTheme(), themes[i]);
        }
    }

    @Test
    public void testCreateFromFile() {
        // id, note, description, links, theme
        String[] notes = {"test", "a note", "Blank"};
        String[] desc = {"A simple test", "REHL simple easy test", "\n"};
        String[] links = {"tutor1;tutor2", "tutor", "\n"};
        String[] themes = {"java", "python", "empty"};
        String path = "./simple.md";
        KeyNote fileNote = null;
        KeyNote baseNote = null;
        for (int i = 0; i < notes.length; ++i) {
            String aCase = notes[i]+"\n"+desc[i]+"\n"+themes[i]+"\n"+links[i];
            System.setIn(new ByteArrayInputStream(aCase.getBytes()));
            baseNote = KeyNote.createFromInput(System.in);
            try {
                OSHelper.createFile(baseNote.toMDFormat(), path);
                fileNote = KeyNote.parseFromFilePath(path, true);
                assertEquals(fileNote.getId(), baseNote.getId());
                assertEquals(fileNote.getNote(), baseNote.getNote());
                assertEquals(fileNote.getDescription(), baseNote.getDescription());
                assertEquals(fileNote.getTheme(), baseNote.getTheme());
                assertEquals(fileNote.getCreatedOn(), baseNote.getCreatedOn());
                assertEquals(fileNote.getLinks().length, baseNote.getLinks().length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
