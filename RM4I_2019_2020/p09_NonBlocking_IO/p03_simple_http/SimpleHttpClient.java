package p03_simple_http;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class SimpleHttpClient {

	public static void main(String[] args) {

		// Clients can be written using normal TCP sockets

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
			out.print("index.html\r\n\r\n");
			//out.print("serverfile.txt\r\n\r\n");
			/*out.print(
					IntStream.range(0, 2048)
							 .mapToObj(i -> "a")
							 .collect(Collectors.joining(""))
							+ "\r\n\r\n"
			);*/
			out.flush();

			// Read response
			String line;
			while ((line = in.readLine()) != null)
				System.out.println(line);

			// FIXME: This way of reading the response is not correct.
			// This way client assumes that the response ends with new line.
			// Proper way of reading the response would be to parse the
			// `Content-length` header and read only that amount of bytes.
			// Since we are focused on server implementation, we will skip this.

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
