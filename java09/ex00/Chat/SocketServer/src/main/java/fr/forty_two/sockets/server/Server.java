package fr.forty_two.sockets.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.springframework.stereotype.Component;

import fr.forty_two.sockets.services.UsersService;

@Component
public class Server {
    private final UsersService svc;

    public Server(UsersService svc) {
        this.svc = svc;
    }

    public void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            
            out.append("Hello from Server!\n").flush();
            String cmd = in.readLine();
            if (cmd == null || !cmd.equalsIgnoreCase("signup")) {
                out.append("Invalid command\n").flush();
                return;
            }

            out.append("Enter username:\n").flush();
            String username = in.readLine();
            out.append("Enter password:\n").flush();
            String password = in.readLine();
            try {
                svc.signup(username, password);
                out.append("Successful!\n");
            } catch (IllegalArgumentException | IllegalStateException e) {
                out.append("Signup failed: " + e.getMessage());
                out.newLine();
                out.flush();
                e.printStackTrace();
            } catch (Throwable e) {
                out.append("Server: Something went wrong.\n");
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            System.out.println("Server listening on port " +  port);
            try (Socket client = serverSocket.accept()) {
                System.out.println("Client connected!");
                handleClient(client);
            } catch (SocketException e) {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
