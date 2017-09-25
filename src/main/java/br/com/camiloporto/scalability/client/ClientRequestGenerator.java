package br.com.camiloporto.scalability.client;


import br.com.camiloporto.scalability.tasks.Task;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by camiloporto on 9/10/17.
 */
public class ClientRequestGenerator {

    private static final Logger logger = Logger.getLogger(ClientRequestGenerator.class.getName());

    public static void main(String[] args) throws IOException {

        long firingTime = Long.valueOf(args[0]);
        new ClientRequestGenerator().generateClientRequests(firingTime * 60 * 1000);

    }

    public void generateClientRequests(long firingTime) throws IOException {

        Long startTime = System.currentTimeMillis();
        Long currentTime = System.currentTimeMillis();

        List<Thread> clients = new LinkedList<>();
        while((currentTime - startTime) < firingTime) {
            int randomUpperBound = getRandomUpperBoundForPrimeCount(80000, 80000);
            ClientRequest c = new ClientRequest(Task.ID_CPU_INTENSIVE_TASK + " " + randomUpperBound);
            Thread t = new Thread(c);
            clients.add(t);
            t.start();
            try {
                Thread.sleep(randonmWait(200, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime = System.currentTimeMillis();
        }
        log("stopped sending fire");
        System.out.println("waiting threads to finish...");
        Iterator<Thread> iterator = clients.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().join();
            } catch (InterruptedException e) {
                System.err.println("InterruptedException!");
            }
        }
        System.out.println("bye!");
    }

    private void log(String msg) {
//        logger.info(String.format("%s | %s | %tc | %s",
//                ClientRequest.class.getName(),
//                Thread.currentThread().getName(),
//                System.currentTimeMillis(),
//                msg));
        System.out.println(String.format("%s | %s | %tc | %s",
                ClientRequest.class.getName(),
                Thread.currentThread().getName(),
                System.currentTimeMillis(),
                msg));
    }

    private long randonmWait(int begin, int end) {
        double random = Math.random();
        return (int) (random * (end - begin) + begin);
    }

    private int getRandomUpperBoundForPrimeCount(int begin, int end) {
        double random = Math.random();
        return (int) (random * (end - begin) + begin);
    }

}
