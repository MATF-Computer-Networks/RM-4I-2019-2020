package p03_echo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

class EchoServer extends Thread implements AutoCloseable {
	public static final int PORT = 12345;

	private final DatagramSocket socket;


	EchoServer() throws SocketException {
		this.socket = new DatagramSocket(PORT);
	}


	@Override
	public void run() {
		System.err.println("Server started...");

		try {
			while (true) {
				byte[] buf = new byte[256];
				try {
					// Wait for packets...
					DatagramPacket request = new DatagramPacket(buf, buf.length);
					this.socket.receive(request);

					// Send response (using same buffer from request packet)
					DatagramPacket response = new DatagramPacket(buf, buf.length,
											                     request.getAddress(), request.getPort());
					this.socket.send(response);

					// Check if "end" is received
					String received = this.getString(request.getData());
					System.err.println("Server recv: " + received);
					if (received.equalsIgnoreCase("end")) {
						System.err.println("Server received end signal.");
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			System.err.println("Server is shutting down...");
			this.close();
			System.err.println("Server successfully closed!");
		}
	}

	@Override
	public void close() {
		this.socket.close();
	}


	private String getString(byte[] buf) {
		if (buf == null)
			return null;
		String str = new String(buf, StandardCharsets.UTF_8);
		return str.substring(0, str.indexOf(0));
	}
}
