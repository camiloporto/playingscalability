package br.com.camiloporto.scalability.tasks;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by camiloporto on 9/10/17.
 */
public class IOIntensiveTask implements Task {

    private URI uri;

    public IOIntensiveTask(URI uri) {
        this.uri = uri;
    }

    @Override
    public byte[] execute() {
        try {
            URL url = uri.toURL();
            InputStream inputStream = url.openStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(byteArray);
            int nextByte = in.read();
            while (nextByte != -1) {
                out.write(nextByte);
                nextByte = in.read();
            }
            in.close();
            out.close();
            int responseLength = byteArray.toByteArray().length;
            return Integer.toString(responseLength).getBytes();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
