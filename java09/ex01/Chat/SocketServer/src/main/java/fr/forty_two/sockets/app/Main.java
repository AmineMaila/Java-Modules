package fr.forty_two.sockets.app;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import fr.forty_two.sockets.config.SocketsApplicationConfig;
import fr.forty_two.sockets.server.Server;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1 || !args[0].startsWith("--port=")) {
            System.err.println("""
                invalid arguments:
                    usage:  java -jar target/socket-server.jar --port=<number>
            """);
            return;
        }
        String portStr = args[0].substring(7);
        int port;
        try {
            port  = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number");
            return;
        }
        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
        Server server = context.getBean("server", Server.class);
        server.start(port);
        context.close();
    }
}
