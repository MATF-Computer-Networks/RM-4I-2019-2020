package p01_URL_intro;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

class URLIntro {
	
	public static void main(String[] args) {
		// The syntax of a URL is:
		// 		protocol://userInfo@host:port/path?query#fragment
		// Here the protocol is another word for what was called the scheme
		// of the URI (scheme is the word used in the URI RFC. Protocol is
		// the word used in the Java documentation). In a URL, the protocol
		// part can be `file`, `ftp`, `http`, `https`, `magnet`, `telnet`,
		// or various other strings.The host part of a URL is the name of
		// the server that provides the resource you want. It can be a
		// hostname such as `www.oreilly.com` or an IP address, such as
		// 204.148.40.9 . The `userInfo` is optional login information for
		// the server. If present, it contains a username and, rarely, a
		// password. The port number is also optional. It’s not necessary
		// if the service is running on its default port (port 80 for HTTP
		// servers). Together, the `userInfo`, `host`, and `port` constitute
		// the `authority`. The `path` points to a particular resource on the
		// specified server. It often looks like a filesystem path such as
		// `/forum/index.php`. However, it may or may not actually map to a
		// filesystem on the server. If it does map to a filesystem, the path
		// is relative to the document root of the server, not necessarily
		// to the root of the filesystem on the server. As a rule, servers
		// that are open to the public do not show their entire filesystem to
		// clients. Rather, they show only the contents of a specified directory.
		// This directory is called the document root, and all paths and filenames
		// are relative to it. Thus, on a Unix server, all files that are available
		// to the public might be in `/var/public/html`, but to somebody connecting
		// from a remote machine, this directory looks like the root of the
		// filesystem. The query string provides additional arguments for the server.
		// It’s commonly used only in http URLs, where it contains form data for
		// input to programs running on the server. Finally, the `fragment`
		// references a particular part of the remote resource. If the remote
		// resource is HTML, the fragment identifier names an anchor in the HTML
		// document. If the remote resource is XML, the fragment identifier is an
		// XPointer. Some sources refer to the fragment part of the URL as a
		// `section`. Java rather unaccountably refers to the fragment identifier
		// as a `Ref`.

		// The URL class is the simplest way for a Java program to locate and
		// retrieve data from the network. You do not need to worry about the
		// details of the protocol being used, the format of the data being
		// retrieved, or how to communicate with the server; you simply tell
		// Java the URL and it gets the data for you.

		// The URL class is an abstraction of a Uniform Resource Locator such
		// as http://www.hamsterdance.com/ or ftp://ftp.redhat.com/pub/. It
		// extends Object class, and it is a final class that cannot be
		// subclassed. Rather than relying on inheritance to configure
		// instances for different kinds of URLs, it uses the strategy
		// design pattern (https://en.wikipedia.org/wiki/Strategy_pattern).
		// Protocol handlers are the strategies, and the URL class itself
		// forms the context through which the different strategies are
		// selected.
		// Although storing a URL as a string would be trivial, it is helpful
		// to think of URLs as objects with fields that include the scheme
		// (a.k.a. the protocol), hostname, port, path, query string, and
		// fragment identifier (a.k.a. the ref), each of which may be set
		// independently.
		URL u;

		try {
			u = new URL("http://www.matf.bg.ac.rs:8080");
			System.out.println("The host part of " + u + " is " + u.getHost());
			System.out.println("The port part of " + u + " is " + u.getPort());
			System.out.println();

			u = new URL("http://www.matf.bg.ac.rs/~ivan_ristovic/index.html");
			System.out.println("The port part of " + u + " is " + u.getPort());
			System.out.println("The default port for " + u + " is " + u.getDefaultPort());
			System.out.println("Path part of " + u + " is " + u.getPath());
			System.out.println("Authority part of " + u + " is " + u.getAuthority());
			System.out.println();

			u = new URL("mailto:ivan_ristovic@matf.bg.ac.rs");
			System.out.println("Authority part of " + u + " is " + u.getAuthority());
			System.out.println("Path part of " + u + " is " + u.getPath());
			System.out.println();

			// URLConnection is an abstract class that represents an active connection
			// to a resource specified by a URL. The URLConnection class has two
			// different but related purposes. First, it provides more control over
			// the interaction with a server (especially an HTTP server) than the URL
			// class. With a URLConnection, you can inspect the header sent by the
			// server and respond accordingly. You can set the header fields used in
			// the client request. You can use a URLConnection to download binary
			// files. Finally, a URLConnection lets you send data back to a web server
			// with POST or PUT and use other HTTP request methods.
			// Second, the URLConnection class is part of Java’s protocol handler
			// mechanism, which also includes the URLStreamHandler class. The idea
			// behind protocol handlers is simple: they separate the details of
			// processing a protocol from processing particular data types, providing
			// user interfaces, and doing the other work that a monolithic web browser
			// performs. The base URLConnection class is abstract; to implement a
			// specific protocol, you write a subclass. These subclasses can be loaded
			// at runtime by applications. For example, if the browser runs across a
			// URL with a strange scheme, such as `compress`, rather than throwing up
			// its hands and issuing an error message, it can download a protocol
			// handler for this unknown protocol and use it to communicate with the server
			u = new URL("http://poincare.matf.bg.ac.rs/~ivan_ristovic/");
			URLConnection uc = u.openConnection();
			System.out.println("Content-type: " + uc.getContentType());
			System.out.println("Content-encoding: " + uc.getContentEncoding());
			System.out.println("Date: " + new Date(uc.getDate()));
			System.out.println("Last modified: " + new Date(uc.getLastModified()));
			System.out.println("Expiration date: " + new Date(uc.getExpiration()));
			System.out.println("Content-length: " + uc.getContentLength());
			System.out.println("URL: " + uc.getURL());

		} catch (MalformedURLException ex) {
			System.err.println("Invalid URL.");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
