package p02_chat;

import java.net.*;
import java.io.*;

class ChatClient {

    // NOTE: In IntelliJ IDEA you need to allow parallel run for this class
    // To do so, open Run Configurations dialog and check `Allow parallel run`
    // for ChatClient configuration
    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost", ChatServer.SERVER_TEST_PORT);
        System.err.println("Connecting to local port: " + ChatServer.SERVER_TEST_PORT);
        client.execute();
    }


    private String hostname;
    private int port;
    private String name;


    private ChatClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }


    private void execute() {
        try {
            this.setName();

            // We cannot use try-with-resources block on client socket
            // because it would be closed immediately after dispatching
            // a thread. We leave the thread to close the socket.
            Socket socket = new Socket(this.hostname, this.port);

            System.out.println("Connected to the chat server @ " + this.hostname);

            // Dispatch threads
            new ClientReadThread(this.name, socket).start();
            new ClientWriteThread(this.name, socket).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void setName() throws IOException {
        System.out.print("Enter your name: ");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        this.name = stdin.readLine();
        // We cannot close stdin, since we will use it later
    }
}