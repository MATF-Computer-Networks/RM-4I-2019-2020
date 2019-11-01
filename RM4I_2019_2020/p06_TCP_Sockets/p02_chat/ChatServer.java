package p02_chat;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

class ChatServer {
    static final int SERVER_TEST_PORT = 12345;


    public static void main(String[] args) {
        ChatServer server = new ChatServer(SERVER_TEST_PORT);
        server.execute();
    }


    private int port;
    private Set<UserThread> users = new HashSet<>();


    private ChatServer(int port) {
        this.port = port;
    }


    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.err.println("Chat server is listening on port: " + port);

            //noinspection InfiniteLoopStatement
            while (true) {
                Socket socket = serverSocket.accept();
                System.err.println("Client connected.");

                // We dispatch a new thread for each user in the chat
                UserThread user = new UserThread(socket, this);
                this.users.add(user);
                user.start();

                // Also, we cannot use try-with-resources block on client socket
                // because it would be closed immediately after dispatching a
                // thread. We leave the thread to close the socket.
            }
        } catch (IOException ex) {
            System.err.println("Server errored: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void broadcast(String message, UserThread except) {
        this.users.stream()
            .filter(u -> u != except)
            .forEach(u -> u.sendMessage(message))
            ;
    }

    void remove(UserThread user) {
        String username = user.getNickname();
        this.users.remove(user);
        System.err.println("Client disconnected: " + username);
    }

    List<String> getUserNames() {
        return this.users.stream()
            .map(UserThread::getNickname)
            .collect(Collectors.toList())
            ;
    }
}