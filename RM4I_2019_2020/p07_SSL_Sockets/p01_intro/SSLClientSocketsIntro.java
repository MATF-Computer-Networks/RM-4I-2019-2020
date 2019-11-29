package p01_intro;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLSocketFactory;

class SSLClientSocketsIntro {

	/*
	Secure Sockets Layer (SSL) is a standard security technology for
	establishing an encrypted link between a server and a client—typically
	a web server (website) and a browser, or a mail server and a mail
	client (e.g., Outlook).
	SSL allows sensitive information such as credit card numbers, social
	security numbers, and login credentials to be transmitted securely.
	Normally, data sent between browsers and web servers is sent in plain
	text — leaving you vulnerable to eavesdropping. If an attacker is able
	to intercept all data being sent between a browser and a web server,
	they can see and use that information.
	More specifically, SSL is a security protocol. Protocols describe how
	algorithms should be used. In this case, the SSL protocol determines
	variables of the encryption for both the link and the data being
	transmitted.
	All browsers have the capability to interact with secured web servers
	using the SSL protocol. However, the browser and the server need what
	is called an SSL Certificate to be able to establish a secure connection.
    */

	public static void main(String[] args) throws IOException {

		// Create SSLSocket (extends Socket)
		SSLSocketFactory factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
		try (Socket socket = factory.createSocket("host_address_goes_here", 1234)) {
			// Write data
			Writer out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
			out.write("something.......");
			out.flush();
			out.close();
		}
	}

}
