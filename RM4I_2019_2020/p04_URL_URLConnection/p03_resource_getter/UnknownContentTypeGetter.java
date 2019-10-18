package p03_resource_getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

public class UnknownContentTypeGetter {

	private static final String URL_STRING = "https://helloacm.com/api/random/?n=128";


	public static void main(String[] args) {
		// Sometimes we do not know what content type is the
		// URL pointing to, so here we will process some cases
		try {
			URL u = new URL(URL_STRING);
			System.out.println("I got a: " + u.getContent().getClass().getName());

			// Create an array of types, which we will pass on to
			// getContent() method. It will try to cast the content
			// to one of those classes in order
			Class<?>[] types = new Class[3];
			types[0] = String.class;
			types[1] = Reader.class;
			types[2] = InputStream.class;
			Object o = u.getContent(types);
			
			// We check the return type
			if (o instanceof String) {
				System.out.println("STRING");
				System.out.println(o);
			} else if (o instanceof Reader) {
				System.out.println("READER");
				int c;
				Reader r = (Reader) o;
				while ((c = r.read()) != -1) 
					System.out.print((char)c);
				r.close();
			} else if (o instanceof InputStream) {
				System.out.println("INPUT STREAM");
				int c;
				InputStream in = (InputStream)o;
				while ((c = in.read()) != -1) 
					System.out.write(c);
				in.close();
			} else {
				// We do not support anything other than above 3 cases
				System.out.println("Error: unexpected type " + o.getClass());
			}
		} catch (IOException ex) {
			// Catches MalformedURLException because IOException is it's superclass
			ex.printStackTrace();
		}
	}

}
