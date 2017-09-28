package br.com.camiloporto.scalability.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by camiloporto on 9/27/17.
 */
public class MultiThreadedServer implements Runnable {

    private int port;

    public MultiThreadedServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        List<Thread> threadRequestHandlerList = new LinkedList<>();
        ServerSocket serverSocket = null;
        try {
            System.out.println("starting server on port " + port);
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("could not start server: " + e);
            System.exit(-1);
        }

            try {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    RequestHandler requestHandler = new RequestHandler(clientSocket);
                    Thread threadHandler = new Thread(requestHandler, "RequestHandler");
                    threadRequestHandlerList.add(threadHandler);
                    threadHandler.start();
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
            }
        System.out.println("waiting for request to complete...");
        for(Thread t : threadRequestHandlerList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                System.err.println("error while waiting thread. " + e);
            }
        }
        System.out.println("all request completed. bye!");
    }
}
