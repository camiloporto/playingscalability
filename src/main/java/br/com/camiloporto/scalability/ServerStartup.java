package br.com.camiloporto.scalability;

import br.com.camiloporto.scalability.server.MultiThreadedServer;

/**
 * Created by camiloporto on 9/27/17.
 */
public class ServerStartup {

    private static final int DEFAULT_PORT = 9090;
    private static final String UNBOUND_MULTITHREADED = "multithreaded";
    private static final String DEFAULT_SERVER_ARCHITECTURE = UNBOUND_MULTITHREADED;


    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        String serverArchiteture = DEFAULT_SERVER_ARCHITECTURE;
        if(args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            }
            catch (NumberFormatException e) {
                System.err.println("invalid port number " + args[0]);
                System.exit(-1);
            }
        }
        if(args.length > 1) {
            serverArchiteture = args[1];
        }

        startServer(serverArchiteture, port);

    }

    private static void startServer(String serverArchiteture, int port) {
        Runnable server = createServer(serverArchiteture, port);
        Thread serverThread = new Thread(server, "ServerThread-" + serverArchiteture);
        serverThread.start();
    }

    private static Runnable createServer(String serverArchiteture, int port) {
        if(serverArchiteture == null) {
            return new MultiThreadedServer(port);
        }
        if(UNBOUND_MULTITHREADED.equals(serverArchiteture)) {
            return new MultiThreadedServer(port);
        }

        throw new IllegalArgumentException("unknown Server Architecture: " + serverArchiteture);
    }
}
