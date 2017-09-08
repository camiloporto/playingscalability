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

    public Server(int port) {

        this.port = port;
    }

    public void stop() throws IOException {
        this.serverSocket.close();
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            Socket clientSocket = this.serverSocket.accept();
            RequestHandler requestHandler = new RequestHandler(clientSocket);
            Thread threadHandler = new Thread(requestHandler);
            System.out.println("waiting thread handler to handle request...");
            threadHandler.start();
            threadHandler.join();
            System.out.println("request handled. closing socket");
            clientSocket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
