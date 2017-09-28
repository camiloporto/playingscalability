package br.com.camiloporto.scalability.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by camiloporto on 9/27/17.
 */
public class FixedPoolThreadedServer implements Runnable {

    private final int port;

    private final int threadPoolSize;

    public FixedPoolThreadedServer(int port, int threadPoolSize) {

        this.port = port;

        if(threadPoolSize <= 0) {
            this.threadPoolSize = Runtime.getRuntime().availableProcessors() + 1;
        }
        else {
            this.threadPoolSize = threadPoolSize;
        }
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        ExecutorService threadPool = null;
        try {
            System.out.println("starting server on port " + port);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("could not start server: " + e);
            System.exit(-1);
        }

        try {
            threadPool = Executors.newFixedThreadPool(threadPoolSize);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                threadPool.submit(requestHandler);
            }

        } catch (IOException e) {
            System.err.println("error while opening client socket " + e);
        }
        finally {
            if(serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("could not close server socket");
                }
            }
            System.out.println("waiting for request to complete...");
            threadPool.shutdown();
            try {
                threadPool.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                System.err.println("error while waiting for tasks to complete " + e);
            }
        }
        System.out.println("all request completed. bye!");
    }

}
