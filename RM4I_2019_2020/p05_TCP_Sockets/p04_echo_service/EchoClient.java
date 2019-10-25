package p04_echo_service;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EchoClient {

	// To start multiple clients in IntelliJ IDEA,
	// open Run Configurations for EchoClient and
	// check the `Allow parallel run` option

	public static void main(String[] args) {
		String hostname = "localhost";

		System.err.println("Connecting to " + hostname);
		try (Socket sock = new Socket(hostname, 4444);
			 BufferedReader networkIn =
				 new BufferedReader(new InputStreamReader(sock.getInputStream(), StandardCharsets.UTF_8));
			 BufferedWriter networkOut =
				 new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), StandardCharsets.UTF_8));
			 BufferedReader userIn =
				 new BufferedReader(new InputStreamReader(System.in))
		) {
			System.out.println("Connected to the echo server @ " + hostname);

			while (true) {
				// Read line from stdin until `exit` is sent
				String line = userIn.readLine();
				if (line.trim().equalsIgnoreCase("exit"))
					break;

				// Send to server
				networkOut.write(line);
				networkOut.newLine();
				networkOut.flush();

				// Receive echo response
				System.out.println(networkIn.readLine());
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.err.println("Disconnected from " + hostname);
	}

}
