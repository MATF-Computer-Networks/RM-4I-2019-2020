package p01_client_sockets_intro;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class ServerSocketsIntro {

    public static void main(String[] args) {
        int port = 9000;

        try (ServerSocket server = new ServerSocket(port)) {

            // Accept clients and open sockets towards them - blocking call
            Socket client = server.accept();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (ServerSocket server = new ServerSocket()) {
            // Bind server manually if port isn't specified via constructor
            server.bind(new InetSocketAddress(port));

            // Usually servers are infinite loops
            //noinspection InfiniteLoopStatement
            while (true) {
                // Accept client
                Socket client = server.accept();
                // Do work with a client
                serve(client);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void serve(Socket client) {
        // Serve client, usually in a separate thread
    }
}
