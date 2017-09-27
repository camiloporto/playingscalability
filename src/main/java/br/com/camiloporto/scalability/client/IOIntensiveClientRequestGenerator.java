package br.com.camiloporto.scalability.client;

import br.com.camiloporto.scalability.tasks.Task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by camiloporto on 9/25/17.
 */
public class IOIntensiveClientRequestGenerator {

    public static void main(String[] args) {
        long firingTime = Long.valueOf(args[0]);
        new IOIntensiveClientRequestGenerator().generateClientRequests(firingTime * 60 * 1000);
    }

    private void generateClientRequests(long firingTime) {

        Long startTime = System.currentTimeMillis();
        Long currentTime = System.currentTimeMillis();

        List<Thread> clients = new LinkedList<>();
        int tNum = 0;
        while((currentTime - startTime) < firingTime) {
            String randomURL = getRandomURL();
            ClientRequest c = new ClientRequest(Task.ID_IO_INTENSIVE_TASK + " " + randomURL);
            Thread t = new Thread(c, "RequestThread-" + (tNum++));
            clients.add(t);
            t.start();
            try {
                Thread.sleep(randonmWait(50, 100));
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

    private String getRandomURL() {
        String[] urls = {
                "http://textfiles.com/100/anonymit?" + (Math.random() * 10000000),
                "http://textfiles.com/100/apples.txt?" + (Math.random() * 10000000),
                "http://textfiles.com/100/basicom4.phk?" + (Math.random() * 10000000)
//                "http://www.google.com.br?q=" + (Math.random() * 10000000)
        };
        double random = Math.random();
        int randomIndex = (int) (random * urls.length);
        return urls[randomIndex];
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
}
