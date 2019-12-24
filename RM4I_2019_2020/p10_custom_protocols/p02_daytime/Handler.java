package p02_daytime;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

class Handler extends URLStreamHandler {

    public int getDefaultPort() {
        return DaytimeURLConnection.DEFAULT_PORT;
    }


    protected URLConnection openConnection(URL u) throws IOException {
        return new DaytimeURLConnection(u);
    }

}