package p02_daytime_service;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.*;
import java.io.*;

class DaytimeServer {
    private static final int PORT = 12345;
    private static final int BUF_SIZE = 1024;
    private static final Logger audit = Logger.getLogger("requests");
    private static final Logger errors = Logger.getLogger("errors");

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    // Wait for packet...
                    DatagramPacket request = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);
                    socket.receive(request);

                    // Packet received, create date string as byte array
                    String daytime = new Date().toString();
                    byte[] data = daytime.getBytes(StandardCharsets.US_ASCII);

                    // Send back the data
                    DatagramPacket response = new DatagramPacket(data, data.length,
                            request.getAddress(), request.getPort());
                    socket.send(response);

                    // Log processed client (optional)
                    audit.info(daytime + " " + request.getAddress());
                } catch (IOException | RuntimeException ex) {
                    errors.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        } catch (IOException ex) {
            errors.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
