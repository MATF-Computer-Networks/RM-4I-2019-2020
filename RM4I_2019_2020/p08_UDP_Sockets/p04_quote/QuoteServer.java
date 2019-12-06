package p04_quote;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Date;

class QuoteServer extends Thread {
	static final int PORT = 12345;

	private final DatagramSocket socket;
	private BufferedReader in;


	public static void main(String[] args) throws IOException {
		new QuoteServer().start();
	}


	private QuoteServer() throws IOException {
		this.socket = new DatagramSocket(PORT);
		try {
			this.in = Files.newBufferedReader(Paths.get("p08_UDP_Sockets/p04_quote/one_liners.txt"));
		} catch (NoSuchFileException e){
			System.err.println("Couldn't open quote file. Serving time instead.");
		}
	}


	@Override
	public void run() {
		System.err.println("Quote server listening on port " + PORT);
		try {
			while (true) {
				byte[] buf = new byte[512];
				DatagramPacket request = new DatagramPacket(buf, buf.length);
				this.socket.receive(request);

				System.err.println("Received packet.");

				buf = getDataToSend();
				if (buf == null)
					break;

				System.err.println("Sending data to client..");
				DatagramPacket response = new DatagramPacket(buf, buf.length,
						request.getAddress(),
						request.getPort());
				this.socket.send(response);
				System.err.println("Client served.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.socket.close();
		}
	}

	private byte[] getDataToSend() throws IOException {
		String data = this.in == null ? new Date().toString() : this.in.readLine();
		return data == null ? null : data.getBytes();
	}
}
