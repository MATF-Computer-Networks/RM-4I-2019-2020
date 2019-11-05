package p01_client_sockets_intro;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

class ClientSocketsIntro {

	/* A socket is a connection between two hosts. It can perform seven basic operations:
		• Connect to a remote machine
		• Send data
		• Receive data
		• Close a connection
		• Bind to a port
		• Listen for incoming data
		• Accept connections from remote machines on the bound port

	  Java’s Socket class, which is used by both clients and servers, has methods
	  that correspond to the first four of these operations. The last three operations
	  are needed only by servers, which wait for clients to connect to them. They are
	  implemented by the ServerSocket class Java programs normally use client sockets
	  in the following fashion:
		1. The program creates a new socket with a constructor.
		2. The socket attempts to connect to the remote host.
		3. Once the connection is established, the local and remote hosts get input and
		   output streams from the socket and use those streams to send data to each other.
		   This connection is full-duplex; both hosts can send and receive data simultaneously.
		   What the data means depends on the protocol; different commands are sent to an FTP
		   server than to an HTTP server. There will normally be some agreed-upon hand-shaking
		   followed by the transmission of data from one to the other.
		4. When the transmission of data is complete, one or both sides close the connection.
		   Some protocols, such as HTTP 1.0, require the connection to be closed after each
		   request is serviced. Others, such as FTP, allow multiple requests to be processed
		   in a single connection.
	 */
	
	// Read more about sockets
	// @ https://docs.oracle.com/javase/tutorial/networking/sockets/definition.html
	
	public static void main(String[] args) {
		try (Socket sock = new Socket("hostname", 80)) {

			// Send/Receive data...
			OutputStream out = new BufferedOutputStream(sock.getOutputStream());
			InputStream in = new BufferedInputStream(sock.getInputStream());

		} catch (UnknownHostException ex) {
			System.err.println("The specified hostname is unknown.");
		} catch (IOException ex) {
			System.err.println("Connection failed.");
		}
	}

}
