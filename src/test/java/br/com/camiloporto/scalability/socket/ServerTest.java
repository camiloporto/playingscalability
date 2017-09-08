package br.com.camiloporto.scalability.socket;

import org.junit.Test;

import java.io.*;
import java.net.Socket;

/**
 * Created by camiloporto on 9/7/17.
 */
public class ServerTest {

    @Test
    public void shouldAcceptConnections() throws IOException {
        Server server = new Server(9090);
        Thread serverThread = new Thread(server);
        serverThread.start();
        //
        Socket clientSocket = new Socket("localhost", 9090);

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String serverData = in.readLine();

        System.out.println(serverData);

        clientSocket.close();

        server.stop();
    }

}
