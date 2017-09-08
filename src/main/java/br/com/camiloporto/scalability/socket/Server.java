package br.com.camiloporto.scalability.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Opa");
            }
            finally {
                clientSocket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("finishing server");
    }
}
