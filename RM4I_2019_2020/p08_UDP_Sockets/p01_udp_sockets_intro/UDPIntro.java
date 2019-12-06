package p01_udp_sockets_intro;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class UDPIntro {

	/*
		Java’s implementation of UDP is split into two classes:
		DatagramPacket and DatagramSocket. The DatagramPacket class stuffs bytes of
		data into UDP packets called datagrams and lets you unstuff datagrams that
		you receive. A DatagramSocket sends as well as receives UDP datagrams. To
		send data, you put the data in a DatagramPacket and send the packet using a
		DatagramSocket. To receive data, you take a DatagramPacket object from a
		DatagramSocket and then inspect the contents of the packet. In UDP,
		everything about a datagram, including the address to which it is directed,
		is included in the packet itself; the socket only needs to know the local
		port on which to listen or send.
	*/

	public static void main(String[] args) {
		// Specific local port will be used for servers, random if not specified
		try (DatagramSocket ds = new DatagramSocket()) {

			// For sending, we need to provide endpoint - receiver's IP and port
			InetAddress host = InetAddress.getByName("host");
			DatagramPacket send = new DatagramPacket(new byte[512], 512, host, 12345);

			// For receiving, we do not specify endpoint
			DatagramPacket recv = new DatagramPacket(new byte[512], 512);

			// We use DatagramSocket class to send and receive DatagramPackets
			ds.send(send);
			ds.receive(recv);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
