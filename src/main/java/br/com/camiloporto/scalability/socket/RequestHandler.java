package br.com.camiloporto.scalability.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by camiloporto on 9/7/17.
 */
public class RequestHandler implements Runnable {

    private static final Map<String, Long> taskTimeMap;

    // TODO create sample tasks with differents profile (I/O bound, CPU bound, Memory Bound). Monitor resource usage
    static {
        taskTimeMap = new HashMap<>();
        taskTimeMap.put("t1", 4L);
        taskTimeMap.put("t2", 8L);
        taskTimeMap.put("t3", 16L);
        taskTimeMap.put("t4", 32L);
    }

    private Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String clientRequest = in.readLine();
            System.out.println(id() + " client task id: " + clientRequest);
            Long timeOfTask = taskTimeMap.get(clientRequest);
            System.out.println(id() + " runing task..");
            Thread.sleep(timeOfTask * 1000);
            System.out.println(id() + " task finished");

            out.println(id() + "time of task: " + timeOfTask);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long id() {
        return Thread.currentThread().getId();
    }
}
