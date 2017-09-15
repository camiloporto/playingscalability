package br.com.camiloporto.scalability.server;

import br.com.camiloporto.scalability.tasks.CPUIntensiveTask;
import br.com.camiloporto.scalability.tasks.IOIntensiveTask;
import br.com.camiloporto.scalability.tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
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
        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            String clientRequest = in.readLine();
            System.err.println(id() + " received request: " + clientRequest);
            Task task = createTask(clientRequest);
            System.err.println(id() + " starting task " + task.getClass().getName());
            byte[] response = task.execute();
            System.err.println(id() + " task finished. sending response to client...");
            out.println(id() + new String(response));
            System.err.println(id() + " request DONE");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out != null) out.close();
            try {
                if(in != null) in.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Task createTask(String request) {
        String[] tokens = request.split(" ");
        if(tokens[0].equals("t1")) { //CPU Sample Task
            Long upperBound = Long.parseLong(tokens[1]);
            return new CPUIntensiveTask(upperBound);
        } else if (tokens[0].equals("t2")) {
            String uri = tokens[1];
            try {
                return new IOIntensiveTask(new URI(uri));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private long id() {
        return Thread.currentThread().getId();
    }
}
