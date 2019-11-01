package p02_chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

class ClientWriteThread extends Thread {
    private PrintWriter toServer;
    private String name;


    ClientWriteThread(String name, Socket socket) {
        this.name = name;
        try {
            this.toServer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public void run() {
        // First send username, server expects that info
        this.toServer.println(this.name);

        // Then send input to server line by line, until `bye`
        try (Scanner sc = new Scanner(System.in)) {
            String text;
            do {
                System.out.printf("\r[%s]: ", this.name);
                text = sc.nextLine();
                this.toServer.println(text);
            } while (!text.equals("bye"));
        }
    }
}
