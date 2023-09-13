package org.example;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class BashHelper {
    public static int runBashScript(String bashFileName) {
        if (!bashFileName.endsWith(".sh")){
            System.out.println("Not a .sh file!\nPlease choose another.");
            return -1;
        }
        URI bashFileURI = null;
        try {
            bashFileURI = OSHelper.getFileResourcePath(bashFileName);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File bashFile = new File(bashFileURI);
        String[] commands = {"bash", "-c", "bash "+bashFile.getAbsolutePath()};
        int exitCode = 0;
        try {
            exitCode = runBashCommand(commands);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return exitCode;
    }
    public static int runBashLine(String command) throws InterruptedException {
        String[] commands = {"bash", "-c", command.strip()};
        int exitCode = runBashCommand(commands);
        return exitCode;
    }
    public static int runBashCommand(String[] commands) throws InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(commands);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );
        StringBuilder stringBuilder = new StringBuilder();
        bufferedReader.lines().forEach(line -> stringBuilder.append(line).append("\n"));
        System.out.println(stringBuilder.toString());
        int exitCode = process.waitFor();
        return exitCode;
    }
}
