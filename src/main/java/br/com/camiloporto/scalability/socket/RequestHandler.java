package br.com.camiloporto.scalability.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by camiloporto on 9/7/17.
 */
public class RequestHandler implements Runnable {

    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        try {
            System.out.println("starting handling req..");
            Thread.sleep(10000L);
            System.out.println("long running task finished");
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String clientRequest = in.readLine();
            System.out.println("client req.: " + clientRequest);
            out.println("echoing: " + clientRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
