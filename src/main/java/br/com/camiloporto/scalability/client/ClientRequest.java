package br.com.camiloporto.scalability.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by camiloporto on 9/10/17.
 */
public class ClientRequest implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientRequest.class.getName());

    private String request;

    public ClientRequest(String request) {
        this.request = request;
    }

    private void sendRequest(final String taskId) throws IOException {
        Socket clientSocket = null;
        try {
            //FIXME pass host as arg...
            clientSocket = new Socket("localhost", 9090);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            long t1 = System.currentTimeMillis();
            log("request sent");
            out.println(taskId);
            String serverData = in.readLine();
            long t2 = System.currentTimeMillis();
            log(String.format("received response |%s", (t2-t1)));
//            System.err.println(id() + " : received response:\n " + serverData);

            //FIXME include timestamp of begin and end of client request. print response time with timestamp...
//            System.out.println(((t2 - t1) / 1000));

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String msg) {
//        logger.info(String.format("%s | %s | %tc | %s",
//                ClientRequest.class.getName(),
//                Thread.currentThread().getName(),
//                System.currentTimeMillis(),
//                msg));
        System.out.println(String.format("%s|%s|%tc|%s",
                ClientRequest.class.getName(),
                Thread.currentThread().getName(),
                System.currentTimeMillis(),
                msg));
    }

    private long id() {
        return Thread.currentThread().getId();
    }

    @Override
    public void run() {
        try {
            sendRequest(this.request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
