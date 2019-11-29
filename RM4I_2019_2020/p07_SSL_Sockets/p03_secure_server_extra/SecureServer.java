package p03_secure_server_extra;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

class SecureServer {
	
	// Create keys via keytool (man 1), for example:
	// $ keytool -genkey -alias ourstore -keystore mykeys.keys
	
	/* Copy pasterino from the book:
	 
	    The factory that SSLServerSocketFactory.getDefault() returns generally only supports 
	    server authentication. It does not support encryption. To get encryption as well, server-side 
	    secure sockets require more initialization and setup. Exactly how this setup is performed is
		implementation-dependent. In Sun’s reference implementation, a com.sun.net.ssl.SSLContext 
		object is responsible for creating fully configured and initialized secure server sockets. 
		The details vary from JSSE implementation to JSSE implementation, but to create a secure 
		server socket in the reference implementation, you have to:
			• Generate public keys and certificates using keytool.
			• Pay money to have your certificates authenticated by a trusted third party.
			• Create an SSLContext for the algorithm you’ll use.
			• Create a TrustManagerFactory for the source of certificate material you’ll be using.
			• Create a KeyManagerFactory for the type of key material you’ll be using.
			• Create a KeyStore object for the key and certificate database. (Sun’s default is JKS.)
			• Fill the KeyStore object with keys and certificates; for instance, by loading them
			from the filesystem using the pass phrase they’re encrypted with.
			• Initialize the KeyManagerFactory with the KeyStore and its pass phrase.
			• Initialize the context with the necessary key managers from the KeyManagerFactory,
			trust managers from the TrustManagerFactory, and a source of randomness. (The
			last two can be null if you’re willing to accept the defaults.)
	 */

	private static final int PORT = 5252;
	private static final String ALGORITHM = "SSL";

	public static void main(String[] args) {
		try {
			SSLContext context = SSLContext.getInstance(ALGORITHM);

			// • Create a KeyManagerFactory for the type of key material you’ll be using.
			// The reference implementation only supports X.509 keys
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

			// • Create a KeyStore object for the key and certificate database. (Sun’s default is JKS.)
			// Sun's default kind of key store
			KeyStore ks = KeyStore.getInstance("JKS");

			// • Fill the KeyStore object with keys and certificates; for instance, by loading them
			//   from the filesystem using the pass phrase they’re encrypted with.
			char[] password = "mypassword".toCharArray();
			ks.load(new FileInputStream("p07_SSL_Sockets/p03_secure_server_extra/jnp3e.keys"), password);

			// • Initialize the KeyManagerFactory with the KeyStore and its pass phrase.
			kmf.init(ks, password);

			// • Initialize the context
			context.init(kmf.getKeyManagers(), null, null);

			// Create the socket
			SSLServerSocketFactory factory = context.getServerSocketFactory();
			SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);

			// Enable algorithms
			// First, get all supported
			String[] supported = server.getSupportedCipherSuites();
			System.out.println(Arrays.toString(supported));
			// Then get all those which don't require authentication (_anon_)
			String[] anonCipherSuitesSupported = new String[supported.length];
			int numAnonCipherSuitesSupported = 0;
			for (String s : supported) {
				if (s.indexOf("_anon_") > 0)
					anonCipherSuitesSupported[numAnonCipherSuitesSupported++] = s;
			}

			// Combine
			String[] oldEnabled = server.getEnabledCipherSuites();
			String[] newEnabled = new String[oldEnabled.length + numAnonCipherSuitesSupported];
			System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
			System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length, numAnonCipherSuitesSupported);

			server.setEnabledCipherSuites(newEnabled);
			
			System.out.println("Starting server...");

			try {
				//noinspection InfiniteLoopStatement
				while (true) {
					// Now we can accept a secure connection
					//noinspection EmptyTryBlock
					try (Socket client = server.accept()) {
						// TODO: Process client or dispatch thread
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch ( NoSuchAlgorithmException
			    | KeyStoreException
				| CertificateException
				| IOException
				| UnrecoverableKeyException
				| KeyManagementException e ) {
			e.printStackTrace();
		}

	}

}
