package p03_simple_http;

import java.io.*;
import java.net.Socket;

class SimpleHttpClient {

	public static void main(String[] args) {

		try (Socket s = new Socket("localhost", 12345);
			 PrintWriter out = new PrintWriter(s.getOutputStream());
			 BufferedReader in = new BufferedReader(
			 		new InputStreamReader(
			 			new BufferedInputStream(s.getInputStream())
					)
			 )
		) {
			System.err.println("Connected to server.");

			// Oversimplified HTTP request, normally we would send proper headers

			out.println("index.html");
			//out.println("serverfile.txt");
			out.flush();

			// Read response
			String line;
			while ((line = in.readLine()) != null)
				System.out.println(line);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
