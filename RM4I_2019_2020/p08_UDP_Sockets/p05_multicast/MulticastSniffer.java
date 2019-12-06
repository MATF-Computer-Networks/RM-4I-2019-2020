package p05_multicast;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

class MulticastSniffer {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress group = InetAddress.getByName("all-systems.mcast.net");
        int port = 4000;

        MulticastSocket ms = null;
        try {
            ms = new MulticastSocket(port);
            ms.joinGroup(group);
            byte[] buffer = new byte[8192];

            //noinspection InfiniteLoopStatement
            while (true) {
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                ms.receive(dp);
                String s = new String(dp.getData(), StandardCharsets.ISO_8859_1);
                System.out.println(s.substring(0, s.indexOf(0)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (ms != null) {
                try {
                    ms.leaveGroup(group);
                    ms.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
    }
}
