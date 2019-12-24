package p01_finger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

class Handler extends URLStreamHandler {

    public int getDefaultPort() {
        return FingerURLConnection.DEFAULT_PORT;
    }


    protected URLConnection openConnection(URL u) throws IOException {
        return new FingerURLConnection(u);
    }

}
