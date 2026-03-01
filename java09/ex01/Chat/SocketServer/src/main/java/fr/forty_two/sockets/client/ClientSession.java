package fr.forty_two.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.forty_two.sockets.dto.MessageDTO;
import fr.forty_two.sockets.exceptions.AuthException;
import fr.forty_two.sockets.models.Chatroom;
import fr.forty_two.sockets.models.User;
import fr.forty_two.sockets.services.MessagesService;
import fr.forty_two.sockets.services.RoomInstance;
import fr.forty_two.sockets.services.RoomsService;
import fr.forty_two.sockets.services.UsersService;

public class ClientSession implements Runnable {
    private final UsersService usersSvc;
    private final RoomsService roomsSvc;
    private final MessagesService msgSvc;

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final ObjectMapper JSON = new ObjectMapper();

    private final Map<ClientState, StateFunction> actionMap = new EnumMap<>(ClientState.class);
    private ClientState currentState = ClientState.LOGIN;

    private boolean connected = true;
    private User currentUser = null;
    private RoomInstance currentRoom = null;


    public ClientSession(Socket socket, UsersService usersSvc, RoomsService roomsSvc, MessagesService msgSvc) throws IOException {
        this.socket = socket;
        this.usersSvc = usersSvc;
        this.msgSvc = msgSvc;
        this.roomsSvc = roomsSvc;
        this.writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        JSON.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        JSON.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true);

        actionMap.put(ClientState.LOGIN, this::login);
        actionMap.put(ClientState.DASHBOARD, this::dashboard);
        actionMap.put(ClientState.ROOM_INDEX, this::listRooms);
        actionMap.put(ClientState.CHATROOM, this::chatroom);
    }

    @Override
    public void run() {
        writer.println("Hello from Server!");
        try {
            while (currentState != ClientState.EXIT) {
                currentState = actionMap.get(currentState).get();
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        } finally {
            connected = false;
            if (currentRoom != null) {
                currentRoom.leave(writer);
            }
        }
    }

    private String[] readCredentials() throws IOException {
        writer.print("Enter username:\n>");
        String username = reader.readLine();
        if (username == null) return null;
        
        writer.print("Enter password:\n>");
        String password = reader.readLine();
        if (password == null) return null;
        
        return new String[]{username, password};
    }
    
    private ClientState login() throws IOException {
        writer.print("""
            1. signIn
            2. signUp
            3. Exit
            >""");
        try {
            String line = reader.readLine();
            if (line == null) {
                return ClientState.EXIT;
            }
            int command = Integer.parseInt(line.trim());

            return switch (command) {
                case 1 -> {
                    String[] credentials = readCredentials();
                    usersSvc.signin(credentials[0], credentials[1]);
                    yield ClientState.DASHBOARD;
                }
                case 2 -> {
                    String[] credentials = readCredentials();
                    usersSvc.signup(credentials[0], credentials[1]);
                    yield ClientState.LOGIN;
                }
                case 3 -> {
                    yield ClientState.EXIT;
                }
                default -> {
                    writer.println("unknown command");
                    yield currentState;
                }
            };
        } catch (NumberFormatException e) {
            writer.println("invalid input");
            return currentState;
        } catch (AuthException e) {
            writer.println(e.getMessage());
            return currentState;
        }
    }

    private ClientState dashboard() throws IOException {
        writer.print("""
            1. Create room
            2. Choose room
            3. Exit
            >""");
        try {
            String line = reader.readLine();
            if (line == null) {
                return ClientState.EXIT;
            }
            int command = Integer.parseInt(line.trim());

            return switch (command) {
                case 1 -> {
                    writer.print("Enter Room name:\n>");
                    String roomName = reader.readLine();
                    if (roomName == null) {
                        yield ClientState.EXIT;
                    }
                    roomsSvc.createRoom(roomName);
                    yield ClientState.DASHBOARD;
                }
                case 2 -> {
                    yield ClientState.ROOM_INDEX;
                }
                case 3 -> {
                    yield ClientState.EXIT;
                }
                default -> {
                    writer.println("unknown command");
                    yield currentState;
                }
            };
        } catch (NumberFormatException e) {
            writer.println("invalid input");
            return currentState;
        } catch (IllegalStateException e) {
            writer.println(e.getMessage());
            return currentState;
        }
    }

    private ClientState listRooms() throws IOException {
        writer.println("Rooms:");
        List<Chatroom> rooms = roomsSvc.getAllRooms();
        int i = 1;
        for (Chatroom room : rooms) {
            writer.println(i + ". " + room.getName());
            i++;
        }
        writer.println(i + ". Exit\n>");

        try {
            String line = reader.readLine();
            if (line == null) {
                return ClientState.EXIT;
            }

            int command = Integer.parseInt(line);
            if (command == rooms.size() + 1) {
                return ClientState.EXIT;
            }

            if (command > rooms.size() || command < 1) {
                writer.println("unknown room");
                return ClientState.DASHBOARD;
            }
            Chatroom chosenRoom = rooms.get(command - 1);

            this.currentRoom = roomsSvc.addUser(chosenRoom.getId(), writer);
            return ClientState.CHATROOM;
        } catch (NumberFormatException e) {
            writer.println("invalid input");
            return ClientState.DASHBOARD;
        }
    }

    private ClientState chatroom() throws IOException {
        
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equalsIgnoreCase("exit")) {
                writer.println("You have left the chat.");
                currentRoom.leave(writer);
                currentRoom = null;
                return ClientState.DASHBOARD;
            }
            try {
                MessageDTO msg = JSON.readValue(line, MessageDTO.class);
                msgSvc.storeMessage(msg.getMessage(), msg.getFromId(), msg.getRoomId());
                roomsSvc.broadcast(msg.getRoomId(), line);
            } catch (JsonProcessingException e) {
                writer.println("Error: invalid JSON");
                break;
            }
        }
        return ClientState.EXIT;
    }

}
