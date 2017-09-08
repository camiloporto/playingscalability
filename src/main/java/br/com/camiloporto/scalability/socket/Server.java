package br.com.camiloporto.scalability.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by camiloporto on 9/7/17.
 */
public class Server implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private boolean stop = false;

    public Server(int port) {
        this.port = port;
    }

    public void stop() throws IOException {
        System.out.println("stopping server..");
        stop = true;
    }

    @Override
    public void run() {
        stop = false;
        try {
            this.serverSocket = new ServerSocket(port);
            while(!stop) {
                Socket clientSocket = this.serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                new Thread(requestHandler).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("finishing server");
    }
}
