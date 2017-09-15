package br.com.camiloporto.scalability.server;

import br.com.camiloporto.scalability.client.ClientRequestGenerator;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

/**
 * Created by camiloporto on 9/7/17.
 */
public class ServerTest {

    @Test
    public void shouldAcceptConnections() throws IOException, InterruptedException {

        Thread.sleep(15 * 1000);
        System.out.println("iniciando...");
        Server server = new Server(9090);
        Thread serverThread = new Thread(server);
        serverThread.start();

        new ClientRequestGenerator().generateClientRequests(100);

    }

}