package br.com.camiloporto.scalability.tasks;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by camiloporto on 9/10/17.
 */
public class ReadHugeFileTest {

    @Test
    public void shouldReadFileFromDisk() {
        File file = new File("src/test/resource/br/com/camiloporto/scalability/tasks/sample.txt");
        IOIntensiveTask ioTask = new IOIntensiveTask(file.toURI());
        byte[] response = ioTask.execute();
        String s = new String(response);
        System.err.println(s);
    }

    @Test
    public void testIOActivityWithSlowIO() throws URISyntaxException {

        IOIntensiveTask ioTask = new IOIntensiveTask(new URI("http://norvig.com/big.txt"));
        long t1 = System.currentTimeMillis();
        byte[] response = ioTask.execute();
        long t2 = System.currentTimeMillis();
        System.err.println("elapsed: " + ((t2-t1)/1000) + "s");
    }
}
