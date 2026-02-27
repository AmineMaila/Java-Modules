package fr.forty_two.sockets.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import fr.forty_two.sockets.models.Chatroom;
import fr.forty_two.sockets.models.User;
import fr.forty_two.sockets.services.RoomInstance;
import fr.forty_two.sockets.services.RoomsService;
import fr.forty_two.sockets.services.UsersService;

public class ClientSession implements Runnable {
    private final UsersService usersSvc;
    private final RoomsService roomsSvc;

    private final Socket socket;
    private final BufferedReader reader;
    private final PrintWriter writer;

    private final Map<ClientState, Supplier<ClientState>> actionMap = new EnumMap<>(ClientState.class);
    private ClientState currentState = ClientState.LOGIN;

    private boolean connected = true;
    private User currentUser = null;
    private RoomInstance currentRoom = null;


    public ClientSession(Socket socket, UsersService usersSvc, RoomsService roomsSvc) throws IOException {
        this.socket = socket;
        this.usersSvc = usersSvc;
        this.roomsSvc = roomsSvc;
        this.writer = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

        actionMap.put(ClientState.LOGIN, this::login);
        actionMap.put(ClientState.DASHBOARD, this::dashboard);
        actionMap.put(ClientState.ROOM_INDEX, this::listRooms);
        actionMap.put(ClientState.ROOM_CREATION, this::createRoom);
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

    private ClientState login() {
        writer.print("""
            1. signIn
            2. signUp
            3. Exit
            >""");
        try {
            String line = reader.readLine();
            int command = Integer.parseInt(line.trim());

            return switch (command) {
                case 1 -> {
                    writer.println("""
                        Enter username:
                        >""");
                    String username = reader.readLine();
                    writer.println("""
                        Enter password:
                        >""");
                    String password = reader.readLine();
                    usersSvc.signin(username, password);
                    yield ClientState.DASHBOARD;
                }
                case 2 -> {
                    writer.println("""
                        Enter username:
                        >""");
                    String username = reader.readLine();
                    writer.println("""
                        Enter password:
                        >""");
                    String password = reader.readLine();
                    usersSvc.signup(username, password);
                    yield ClientState.LOGIN;
                }
                case 3 -> {
                    yield ClientState.EXIT;
                }
                default -> {
                    writer.println("unknown command");
                    yield currentState;
                }
            }
        } catch (NumberFormatException e) {
            writer.println("invalid input");
            return currentState;
        } catch (IllegalStateException e) {
            writer.println(e.getMessage());
            return currentState;
        } catch (IOException e) {
            return ClientState.EXIT;
        }
    }

    private ClientState dashboard() {
        writer.print("""
            1. Create room
            2. Choose room
            3. Exit
            >""");
        try {
            String line = reader.readLine();
            int command = Integer.parseInt(line.trim());

            return switch (command) {
                case 1 -> {
                    yield ClientState.ROOM_CREATION;
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
            }
        } catch (NumberFormatException e) {
            writer.println("invalid input");
            return currentState;
        } catch (IOException e) {
            return ClientState.EXIT;
        }
        
    }
    
    private ClientState createRoom() {
        
    }

    private ClientState listRooms() {
        writer.println("Rooms:");
        List<Chatroom> rooms = roomsSvc.getAllRooms();
        int i = 1;
        for (Chatroom room : rooms) {
            writer.println(i + ". " + room.getName());
            i++;
        }
        writer.println(i + ". Exit\n>");

        try {
            int command = Integer.parseInt(reader.readLine());
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
        } catch (IOException e) {
            return ClientState.EXIT;
        }
    }

    private ClientState chatroom() {

    }

}
