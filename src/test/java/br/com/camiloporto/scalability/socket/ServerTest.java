package br.com.camiloporto.scalability.socket;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

/**
 * Created by camiloporto on 9/7/17.
 */
public class ServerTest {

    @Test
    public void shouldAcceptConnections() throws IOException, InterruptedException {
        Thread.sleep(60 * 1000);

        Server server = new Server(9090);
        Thread serverThread = new Thread(server);
        serverThread.start();
        sendRequest("t1");
        sendRequest("t4");
        sendRequest("t1");
        sendRequest("t3");
        sendRequest("t2");
        sendRequest("t4");
        sendRequest("t1");
        sendRequest("t3");
        sendRequest("t4");
        sendRequest("t1");

        Thread.sleep(5 * 50 * 1000);

    }

    private void sendRequest(final String taskId) throws IOException {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        Socket clientSocket = null;
                        try {
                            clientSocket = new Socket("localhost", 9090);
                            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            out.println(taskId);
                            String serverData = in.readLine();
                            System.out.println(id() + " " + serverData);

                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    private long id() {
                        return Thread.currentThread().getId();
                    }
                }
        ).start();
    }

}
