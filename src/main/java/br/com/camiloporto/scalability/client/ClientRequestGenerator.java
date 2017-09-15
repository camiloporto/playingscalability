package br.com.camiloporto.scalability.client;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by camiloporto on 9/10/17.
 */
public class ClientRequestGenerator {

    public static void main(String[] args) throws IOException {


        //FIXME highlight the timestamp where clients stop sending request...

        long runningTime = Long.valueOf(args[0]);
        new ClientRequestGenerator().generateClientRequests(runningTime * 60 * 1000);

    }

    public void generateClientRequests(long runTime) throws IOException {
        Long startTime = System.currentTimeMillis();
        Long currentTime = System.currentTimeMillis();


        List<Thread> clients = new LinkedList<>();
        while((currentTime - startTime) < runTime) {
            int randomUpperBound = getRandomUpperBoundForPrimeCount(80000, 80000);
            ClientRequest c = new ClientRequest("t1 " + randomUpperBound);
            Thread t = new Thread(c);
            clients.add(t);
            t.start();
            try {
                Thread.sleep(randonmWait(200, 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime = System.currentTimeMillis();
            System.err.println("elased: " + (currentTime - startTime));
        }

        System.out.println("waiting threads...");
        Iterator<Thread> iterator = clients.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().join();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException!");
//                e.printStackTrace();
            }
        }
        System.out.println("bye");
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
