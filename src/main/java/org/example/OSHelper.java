package org.example;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OSHelper {
    public static String getOS(){
        return System.getProperty("os.name").toLowerCase();
    }
    public static void createFile(String data, String path) throws IOException {
        File file = new File(path);
        Files.write(data.getBytes(), file);
    }
    public static String readFile(File file) throws IOException {
        BufferedReader reader = Files.newReader(file, Charsets.UTF_8);
        StringBuilder builder = new StringBuilder();
        reader.lines().forEach(line -> builder.append(line).append("\n"));
        reader.close();
        return builder.toString().strip();
    }
    public static boolean deleteFile(String path) throws IOException {
        File file = new File(path);
        return file.delete();
    }
    public static String readFromResources(String name) {
        try {
            File file = new File(getFileResourcePath(name));
            return readFile(file);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    // https://mkyong.com/java/java-read-a-file-from-resources-folder/
    public static URI getFileResourcePath(String fileName) throws URISyntaxException {
        ClassLoader classLoader = OSHelper.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }
        return resource.toURI();
    }
}