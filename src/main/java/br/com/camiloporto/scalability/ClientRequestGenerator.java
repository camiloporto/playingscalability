package br.com.camiloporto.scalability;

import br.com.camiloporto.scalability.client.ClientRequest;
import br.com.camiloporto.scalability.tasks.Task;
import com.sun.corba.se.spi.servicecontext.ServiceContextData;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by camiloporto on 9/27/17.
 */
public class ClientRequestGenerator {

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int DEFAULT_FIRING_TIME = 5 * MINUTE;

    private static final String CPU_TYPE = "cpu";
    private static final String IO_TYPE = "io";
    private static final String DEFAULT_TASK_TYPE = CPU_TYPE;

    private static final int DEFAULT_REQUEST_RATIO = 5; // req/s


    public static void main(String[] args) {

        int firingTimeInMinutes = DEFAULT_FIRING_TIME;
        String taskType = DEFAULT_TASK_TYPE;
        int requestsPerSecond = DEFAULT_REQUEST_RATIO;
        if(args.length > 0) {
            try {
                firingTimeInMinutes = Integer.valueOf(args[0]) * MINUTE;
            }
            catch (NumberFormatException e) {
                System.err.println("Invalid firing time " + args[0]);
                System.exit(-1);
            }
        }

        if(args.length > 1) {
            taskType = args[1];
        }

        if(args.length > 2) {
            try {
                requestsPerSecond = Integer.valueOf(args[2]);
            }
            catch (NumberFormatException e) {
                System.err.println("Invalid request ratio " + args[0]);
                System.exit(-1);
            }
        }

        fireRequests(firingTimeInMinutes, taskType, requestsPerSecond);
    }

    private static void fireRequests(int firingTime, String taskType, int requestsPerSecond) {

        int waitingTime = SECOND / requestsPerSecond;
        long startTime = System.currentTimeMillis();
        long currentTime = System.currentTimeMillis();

        List<Thread> threadRequestList = new LinkedList<>();
        while((currentTime - startTime) < firingTime) {
            String requestContent = generateRequestContent(taskType);

            ClientRequest c = new ClientRequest(requestContent);
            Thread t = new Thread(c);
            threadRequestList.add(t);
            t.start();
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime = System.currentTimeMillis();
        }
        System.out.println("waiting threads to finish...");
        Iterator<Thread> iterator = threadRequestList.iterator();
        while (iterator.hasNext()) {
            try {
                iterator.next().join();
            } catch (InterruptedException e) {
                System.err.println("InterruptedException!");
            }
        }
        System.out.println("bye!");
    }

    private static String generateRequestContent(String taskType) {
        if(CPU_TYPE.equals(taskType)) {
            return generateCPUTaskRequest();
        }
        if(IO_TYPE.equals(taskType)) {
            return generateIOTaskRequest();
        }

        throw new IllegalArgumentException("Invalid Task type: " + taskType);
    }

    private static String generateIOTaskRequest() {
        return Task.ID_IO_INTENSIVE_TASK + " http://textfiles.com/computers/vendlist.txt";
    }

    private static String generateCPUTaskRequest() {
        return Task.ID_CPU_INTENSIVE_TASK + " " + 100000;
    }
}
