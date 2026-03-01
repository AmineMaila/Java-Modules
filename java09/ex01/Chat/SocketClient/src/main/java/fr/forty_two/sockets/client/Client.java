package fr.forty_two.sockets.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public void connect(int port) {
        String host = "localhost";
        try (Socket clientSocket = new Socket()) {

            clientSocket.connect(new InetSocketAddress(host, port), 5000);

            try (
                BufferedReader serverInputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter serverOutputWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                Scanner userInputReader = new Scanner(System.in)
            ) {
                String serverInput;
                while ((serverInput = serverInputReader.readLine()) != null) {
                    System.out.println(serverInput);
                    System.out.print("> ");
                    serverOutputWriter.append(userInputReader.nextLine());
                    serverOutputWriter.newLine();
                    serverOutputWriter.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
