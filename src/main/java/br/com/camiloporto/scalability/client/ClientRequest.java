package br.com.camiloporto.scalability.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by camiloporto on 9/10/17.
 */
public class ClientRequest implements Runnable {

    private String request;

    public ClientRequest(String request) {
        this.request = request;
    }

    private void sendRequest(final String taskId) throws IOException {
                        Socket clientSocket = null;
                        try {
                            clientSocket = new Socket("localhost", 9090);
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            System.err.println(id() + " : sending request " + taskId);
                            long t1 = System.currentTimeMillis();
                            out.println(taskId);
                            String serverData = in.readLine();
                            long t2 = System.currentTimeMillis();
                            System.err.println(id() + " : received response:\n " + serverData);

                            //FIXME include timestamp of begin and end of client request. print response time with timestamp...
                            System.out.println(((t2-t1)/1000));

                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }



    }

    private long id() {
        return Thread.currentThread().getId();
    }

    @Override
    public void run() {
        try {
            sendRequest(this.request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
