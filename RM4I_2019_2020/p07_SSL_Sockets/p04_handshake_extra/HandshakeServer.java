package p04_handshake_extra;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.*;

// Secure echo server
class HandshakeServer {
	
	private final static int SSL_ECHO_PORT = 7000;

	// Paths to key store and trust store files and their passwords
	private final static String KS_PATH = "server.jks";
	private final static String KS_PASS = "changeit";
	private final static String TS_PATH = "clients.jks";
	private final static String TS_PASS = "changeit";

	
	public static void main(String[] args) throws Exception {
		(new HandshakeServer()).run(args);
	}

	
	private void run(String[] args) throws Exception {
		int port = SSL_ECHO_PORT;
		boolean clientAuth = false;

		// Parse cmd args
		for (int i = 0; i < args.length; i++) {
			if ("-port".equals(args[i]))
				port = Integer.parseInt(args[++i]);
			else if ("-clientauth".equals(args[i]))
				clientAuth = true;
			else {
				System.out.println("Usage: java HandshakeServer [-port <port>] [-clientauth]");
				return;
			}
		}

		// Set the paths
		System.setProperty("javax.net.ssl.keyStore", KS_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KS_PASS);
		if (clientAuth) {
			System.setProperty("javax.net.ssl.trustStore", TS_PATH);
			System.setProperty("javax.net.ssl.trustStorePassword", TS_PASS);
		}

		SSLServerSocketFactory factory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		System.out.println("factory created");

		SSLServerSocket server = (SSLServerSocket)factory.createServerSocket(port);
		System.out.println("Server socket created");

		if (clientAuth)
			server.setNeedClientAuth(true);

		//noinspection InfiniteLoopStatement
		while (true) {
			Socket socket = server.accept();
			Service service = new Service(socket);
			service.start();
		}
	}

	
	static class Service extends Thread {
		private Socket socket;

		
		Service(Socket socket) {
			this.socket = socket;
		}

		
		public void run() {
			System.out.println("connected - " + socket.getRemoteSocketAddress());

			try (
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))
			) {
				String s;
				while ((s = in.readLine()) != null) {
					if (!s.isBlank())
						out.write(s);
					out.newLine();
					out.flush();

					System.out.println(s);

					if (s.equals("."))
						break;
				}

				System.out.println("closing - " + socket.getRemoteSocketAddress());
			} catch (Exception e) {
				System.err.println("Service errored.");
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException ioex) {
					// Ignored
				}
			}
		}
	}
}
