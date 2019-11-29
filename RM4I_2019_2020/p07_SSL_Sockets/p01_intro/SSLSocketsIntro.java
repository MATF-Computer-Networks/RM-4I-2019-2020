package p01_intro;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

class SSLSocketsIntro {

	@SuppressWarnings({ "null", "unused" })
	public static void main(String[] args) {
		
		// Just for demonstration purposes
		SSLSocket socket = null;
		
		// Get supported cipher suites used for the encryption, of the following:
		// SSL_RSA_WITH_RC4_128_MD5
		// SSL_RSA_WITH_RC4_128_SHA
		// TLS_RSA_WITH_AES_128_CBC_SHA
		// TLS_DHE_RSA_WITH_AES_128_CBC_SHA
		// TLS_DHE_DSS_WITH_AES_128_CBC_SHA
		// SSL_RSA_WITH_3DES_EDE_CBC_SHA
		// SSL_DHE_RSA_WITH_3DES_EDE_CBC_SHA
		// SSL_DHE_DSS_WITH_3DES_EDE_CBC_SHA
		// SSL_RSA_WITH_DES_CBC_SHA
		// SSL_DHE_RSA_WITH_DES_CBC_SHA
		// SSL_DHE_DSS_WITH_DES_CBC_SHA
		// SSL_RSA_EXPORT_WITH_RC4_40_MD5
		// SSL_RSA_EXPORT_WITH_DES40_CBC_SHA
		// SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA
		// SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA
		// SSL_RSA_WITH_NULL_MD5
		// SSL_RSA_WITH_NULL_SHA
		// SSL_DH_anon_WITH_RC4_128_MD5
		// TLS_DH_anon_WITH_AES_128_CBC_SHA
		// SSL_DH_anon_WITH_3DES_EDE_CBC_SHA
		// SSL_DH_anon_WITH_DES_CBC_SHA
		// SSL_DH_anon_EXPORT_WITH_RC4_40_MD5
		// SSL_DH_anon_EXPORT_WITH_DES40_CBC_SHA
		String[] supportedCipherSuits = socket.getSupportedCipherSuites();

		// Get enabled cipher suites used for the encryption.
		String[] enabledCipherSuites = socket.getEnabledCipherSuites();

		/*
		HandshakeCompletedListener interface allows us to to receive notifications
		about the completion of an SSL protocol handshake on a given SSL connection.
		When an SSL handshake completes, new security parameters will have been established.
		Those parameters always include the security keys used to protect messages. They
		may also include parameters associated with a new session such as authenticated
		peer identity and a new SSL cipher suite.
		*/
		class HandshakeInterfaceExample implements HandshakeCompletedListener {

			@Override
			public void handshakeCompleted(HandshakeCompletedEvent e) {
				SSLSession session = e.getSession();
				session.getProtocol();
				session.getPeerHost();
				
				// Handshake completion logic goes here, some useful methods on the event object:
				e.getSession();
				e.getSocket();
				e.getCipherSuite();
			}
			
		}
		
	}

}
