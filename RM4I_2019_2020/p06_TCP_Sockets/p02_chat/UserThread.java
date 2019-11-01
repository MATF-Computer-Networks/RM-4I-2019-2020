package p02_chat;

import java.io.*;
import java.net.*;

class UserThread extends Thread {
    private Socket sock;
    private ChatServer server;
    private PrintWriter toUser;
    private String name;


    UserThread(Socket socket, ChatServer server) {
        this.sock = socket;
        this.server = server;
    }


    public void run() {
        try {
            BufferedReader fromUser = new BufferedReader(new InputStreamReader(this.sock.getInputStream()));
            this.toUser = new PrintWriter(this.sock.getOutputStream(), true);

            // Upon connecting, print userlist and ask user for his name
            this.name = fromUser.readLine();
            this.sendMessage("Connected users: " + this.server.getUserNames());

            // Broadcast that new user has entered the chat
            this.server.broadcast("New user connected: " + this.name, this);

            // Process the user (until he leaves the chat
            String clientMessage;
            do {
                clientMessage = fromUser.readLine();
                if (clientMessage == null)
                    break;
                this.server.broadcast("[" + this.name + "]: " + clientMessage, this);
            } while (!clientMessage.equals("bye"));

            // Remove user
            this.server.remove(this);
            this.sock.close();
            this.server.broadcast(this.name + " has left the chat.", this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    void sendMessage(String message) {
        if (this.toUser != null)
            this.toUser.println(message);
    }

    String getNickname() {
        return this.name;
    }
}