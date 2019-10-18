package p03_resource_getter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SourceReader {

	private static final String URL_STRING = "http://poincare.matf.bg.ac.rs/~ivan_ristovic";
	
	
	public static void main(String[] args) throws IOException {

		URL u = new URL(URL_STRING);

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(u.openStream())
		)) {

			String line;
			while ((line = in.readLine()) != null)
				System.out.println(line);

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		URLConnection conn = u.openConnection();
		String encoding = conn.getContentEncoding();
		if (encoding == null)
			encoding = "UTF-8";
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						conn.getInputStream(),
						encoding
				)
		)) {
			// Because the characters are encoded, we use UTF-8
			// now so all characters can be properly visualized
			// conn.getContentEncoding() can also be used but it
			// is not so stable, returns mostly null

			String line;
			while ((line = in.readLine()) != null)
				System.out.println(line);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
