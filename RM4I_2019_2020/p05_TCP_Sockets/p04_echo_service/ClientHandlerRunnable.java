package p04_echo_service;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandlerRunnable implements Runnable {
    private Socket client;


    ClientHandlerRunnable(Socket client) {
        this.client = client;
    }


    @Override
    public void run() {
        try (
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(this.client.getInputStream(), StandardCharsets.UTF_8)
                );
            BufferedWriter out =
                new BufferedWriter(
                    new OutputStreamWriter(this.client.getOutputStream(), StandardCharsets.UTF_8)
                )
        ) {
            // Waits for data and reads it in until connection dies
            // readLine() blocks until the server receives a new line from client
            // readline() will return null if client closes connection
            String s;
            while ((s = in.readLine()) != null) {
                out.write(s);
                out.newLine();
                out.flush();
            }

        } catch (IOException ex) {
            System.err.printf("Client handler [%2d] errored:\n", Thread.currentThread().getId());
            ex.printStackTrace();
        } finally {
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.err.printf("Client handler [%2d] finished!\n", Thread.currentThread().getId());
    }
}
