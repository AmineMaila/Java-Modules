package fr.forty_two.sockets.app;

import fr.forty_two.sockets.client.Client;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--server-port=")) {
            System.err.println("""
                invalid arguments:
                    usage:  java -jar target/socket-client.jar --server-port=<number>
            """);
            return;
        }
        String portStr = args[0].substring(14);
        int port;
        try {
            port  = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number");
            return;
        }
        Client client = new Client();
        client.connect(port);
    }
}
