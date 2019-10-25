package p03_daytime_service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DaytimeServer {

	public final static int DEFAULT_PORT = 8765;
	
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(DEFAULT_PORT)) {

			System.err.println("Server bound to port: " + DEFAULT_PORT);

			// Server works in an infinite loop
			//noinspection InfiniteLoopStatement
			while (true) {
				System.err.println("Listening for clients...");

				// accept() is a "blocking" call
				try (Socket client = server.accept()) {
					System.err.println("Client accepted!");

					// We work with clients in a single thread - we
					// cannot have concurrent clients
					try (var out = new BufferedWriter(
							new OutputStreamWriter(client.getOutputStream()))
					) {
						Date now = new Date();
						out.write(now.toString());
						// We must send a trailing newline since client calls `readLine()`
						out.newLine();
						out.flush();
					} catch (IOException ex) {
						// ignored, broken connection
					}

					System.err.println("Client handled!");
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
