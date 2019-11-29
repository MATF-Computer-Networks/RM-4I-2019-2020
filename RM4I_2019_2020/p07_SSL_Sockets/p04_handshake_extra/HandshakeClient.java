package p04_handshake_extra;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.*;

// Secure echo client
class HandshakeClient implements HandshakeCompletedListener, Runnable {
	
	private static final int SSL_ECHO_PORT = 7000;

	// Paths to key store and trust store files and their passwords
	private final static String KS_PATH = "client.jks";
	private final static String KS_PASS = "changeit";
	private final static String TS_PATH = "servers.jks";
	private final static String TS_PASS = "changeit";
	
	
	private Socket socket;

	
	public static void main(String[] args) throws Exception {
		(new HandshakeClient()).run(args);
	}


	private void run(String[] args) throws Exception {
		String host = "www.ssjava.net";
		int port = SSL_ECHO_PORT;
		boolean clientAuth = false;

		// Parse cmd args
		for (int i = 0; i < args.length; i++) {
			if ("-host".equals(args[i]))
				host = args[++i];
			else if ("-port".equals(args[i]))
				port = Integer.parseInt(args[++i]);
			else if ("-clientauth".equals(args[i]))
				clientAuth = true;
			else {
				System.out.println("Usage: java HandshakeClient -host <host> [-port <port>] [-clientauth]");
				return;
			}
		}

		System.out.println("Server - " + host + ":" + port);

		// Set the paths
		System.setProperty("javax.net.ssl.trustStore", TS_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", TS_PASS);
		if (clientAuth) {
			System.setProperty("javax.net.ssl.keyStore", KS_PATH);
			System.setProperty("javax.net.ssl.keyStorePassword", KS_PASS);
		}

		SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		System.out.println("Factory created.");

		this.socket = factory.createSocket(host, port);
		((SSLSocket)this.socket).addHandshakeCompletedListener(this);
		System.out.println("Socket created.");

		PrintWriter out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), StandardCharsets.UTF_8));
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		Thread t = new Thread(this);
		t.start();

		System.out.println("Sender started.");

		while (true) {
			String s = in.readLine();
			if (s == null)
				s = ".";

			out.println(s);
			out.flush();

			if (s.equals("."))
				break;
		}

		System.out.println("Sender exited.");

		t.join();
		out.close();
		this.socket.close();
	}

	public void run() {
		System.out.println("Receiver started.");

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), StandardCharsets.UTF_8));

			String s;
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				if (s.equals("."))
					break;
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Receiver thread: " + e);
		}

		System.out.println("Receiver exited.");
	}

	public void handshakeCompleted(HandshakeCompletedEvent event) {
		System.out.println("Handshake completed.");

		try {
			System.out.println("getCipherSuite: " + event.getCipherSuite());
			SSLSession session = event.getSession();
			System.out.println("getProtocol: " + session.getProtocol());
			System.out.println("getPeerHost: " + session.getPeerHost());
		} catch (Exception e) {
			System.err.println("Handshake failed: " + e);
		}
	}
}
