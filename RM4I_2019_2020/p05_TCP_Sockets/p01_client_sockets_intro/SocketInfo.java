package p01_client_sockets_intro;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

class SocketInfo {

	public static void main(String[] args) {
		try (Socket sock = new Socket("www.matf.bg.ac.rs", 80)) {
			System.out.println(sock);
			System.out.println("Connected to " + sock.getInetAddress());
			System.out.println("Port " + sock.getPort());
			System.out.println("From port " + +sock.getLocalPort());
			System.out.println("Of " + sock.getLocalAddress());
		} catch (UnknownHostException ex) {
			System.err.println("I can't find the given host");
		} catch (SocketException ex) {
			System.err.println("Could not connect to the given host.");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
