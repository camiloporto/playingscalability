package br.com.camiloporto.scalability.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public static void main(String[] args) {
        Server server = new Server(9090);
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    public void stop() throws IOException {
        System.err.println("stopping server..");
        stop = true;
    }

    @Override
    public void run() {
        stop = false;
        ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        try {
            this.serverSocket = new ServerSocket(port);
            while(!stop) {
                Socket clientSocket = this.serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                //FIXME tag two commits with different experiment versions
//                new Thread(requestHandler).start();
                threadPool.submit(requestHandler);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                threadPool.shutdown();
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.err.println("finishing server");
    }
}
