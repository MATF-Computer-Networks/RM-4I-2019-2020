package p04_echo_service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class EchoServer {

	public final static int DEFAULT_PORT = 4444;

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(DEFAULT_PORT)) {
			System.err.println("Started server on port " + DEFAULT_PORT);

			// Repeatedly wait for connections, and process them
			//noinspection InfiniteLoopStatement
			while (true) {
				System.err.println("Listening for clients...");

				// Working with one client can be a very long task, so
				// we will dispatch multiple threads to handle all clients
				// concurrently - server is always available

				// We cannot put accept() into thread run() method because
				// then we will create threads continuously and not only when
				// a new client appears - very important
				Socket client = server.accept();

				// Also, we cannot use try-with-resources block on client socket
				// because it would be closed immediately after dispatching a
				// thread. We leave the thread to close the socket.

				System.err.println("Client accepted! Dispatching thread...");
				new Thread(new ClientHandlerRunnable(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
