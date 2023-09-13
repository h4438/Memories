package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OSHelperTest extends TestCase {
    @Test
    public void testOS(){
        String os = OSHelper.getOS();
        assertEquals(os, "linux");
    }
    public void writeAndViewFile(String content, String path){
        String readData = "";
        try {
            OSHelper.createFile(content, path);
            readData = OSHelper.readFile(new File(path));
            assertEquals(readData, content);
            boolean removed = OSHelper.deleteFile(path);
            assertTrue(removed);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFileIO(){
        String[] contents =
                {"Hello", "#O\n## LORD\n teach hurting",
                        "Whatzup\nyolo",
                        "#Hello\nWhat boy"};
        String[] paths = {"a.txt", "a.md", "a.txt", "a.md"};
        for(int i = 0; i < contents.length; ++i){
            writeAndViewFile(contents[i], paths[i]);
        }
    }
    @Test
    public void testResourcePath(){
        try {
            URI uri = OSHelper.getFileResourcePath("bash/hello.sh");
            System.out.println(uri.toString());
            assertTrue(true);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
