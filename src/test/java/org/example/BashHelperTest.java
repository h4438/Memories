package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.net.URISyntaxException;


public class BashHelperTest extends TestCase {
    @Test
    public void testEcho() throws InterruptedException {
        String[] commands = {"echo Hello", "echo $(echo Whatzupp)", "echo $(pwd)"};
        for(String command: commands){
            int exitcode = BashHelper.runBashLine(command);
            assertEquals(0, exitcode);
        }
    }
    @Test
    public void testLs() throws InterruptedException {
        String[] commands = {"ls /", "ls /home/", "ls .", "ls ../", "ls ./", "ls .."};
        for(String command: commands){
            int code = BashHelper.runBashLine("ls /");
            assertEquals(0,code);
        }
    }
    @Test
    public void testRunBashScript() {
        int exitCode = -1;
        String[] names = {"bash/print.sh", "bash/hello.sh"};
        for(String name: names){
            exitCode = BashHelper.runBashScript(name);
            assertEquals(exitCode, 0);
            exitCode = -1;
        }
    }
}
