package p02_daytime_service;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

class DaytimeClient {
    private static final int PORT = 12345;
    private static final int BUF_SIZE = 1024;
    private static final String HOSTNAME = "localhost";

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {

            // Set timeout for the connection in ms, receive() is blocking
            // Setting timeout causes receive() to throw after given time
            socket.setSoTimeout(5000);

            InetAddress host = InetAddress.getByName(HOSTNAME);

            // We need to send initial "request" to server
            // Server will use this packet to see client's IP and port
            DatagramPacket request = new DatagramPacket(new byte[1], 1, host , PORT);

            // For response, we reserve a data buffer of fixed size
            DatagramPacket response = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);

            // Send and receive
            socket.send(request);
            socket.receive(response);

            // Parse result from packet
            String result = new String(response.getData(), 0,
                                       response.getLength(), StandardCharsets.US_ASCII);
            System.out.println(result);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
