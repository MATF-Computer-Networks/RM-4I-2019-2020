package p04_quote;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class QuoteClient {

	public static void main(String[] args) throws IOException {
		try (DatagramSocket socket = new DatagramSocket()) {
			byte[] buf = new byte[256];
			InetAddress address = InetAddress.getLocalHost();
			DatagramPacket request = new DatagramPacket(buf, buf.length, address, QuoteServer.PORT);
			socket.send(request);

			DatagramPacket response = new DatagramPacket(buf, buf.length);
			socket.receive(response);
			String received = new String(response.getData(), 0, response.getLength());
			System.out.println("Quote of the Moment: " + received);
		}
	}

}
