package p02_port_scanner;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

class PortScanner {

	public static void main(String[] args) {
		String host = "alas.matf.bg.ac.rs";

		System.out.println("Start time: " + new Date());

		for (int port = 1; port < 65536; port++) {
			
			System.out.printf("\rTesting port: %5d", port);
			
			try (Socket s = new Socket(host, port)) {
				System.out.println("\rSocket data: " + s);
				System.out.println("Found @ " + new Date());
			} catch (UnknownHostException ex) {
				ex.printStackTrace();
				break;
			} catch (IOException ex) {
				// Ignored, no server listening on port
			}
		}

		System.out.println("\rEnd time: " + new Date());
	}

}
