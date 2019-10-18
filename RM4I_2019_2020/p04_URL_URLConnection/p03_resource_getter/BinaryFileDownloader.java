package p03_resource_getter;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class BinaryFileDownloader {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://www.matf.bg.ac.rs/images/matf.gif");
			URLConnection connection = url.openConnection();

			String contentType = connection.getContentType();
			int contentLength = connection.getContentLength();
			if (contentLength == -1 || contentType.startsWith("text"))
				throw new IOException("Content is not a binary file!");

			BufferedInputStream input = new BufferedInputStream(
				connection.getInputStream()
			);
			String filename = url.getFile();
			filename = filename.substring(filename.lastIndexOf('/') + 1);
			System.out.println(filename);

			try (BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream("p04_URL_URLConnection/p03_resource_getter/" + filename)
			)) {
				for (int i = 0; i < contentLength; i++) {
					int b = input.read();
					out.write(b);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
