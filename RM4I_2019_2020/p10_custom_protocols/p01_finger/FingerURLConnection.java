package p01_finger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

class FingerURLConnection extends URLConnection {
    public final static int DEFAULT_PORT = 12345;

    private Socket connection = null;


    FingerURLConnection(URL u) {
        super(u);
    }


    public synchronized InputStream getInputStream() throws IOException {
        if (!this.connected)
            this.connect();
        return this.connection.getInputStream();
    }

    public String getContentType() {
        return "text/plain";
    }

    @Override
    public synchronized void connect() throws IOException {
        if (!this.connected) {
            int port = url.getPort();
            if (port < 1 || port > 65535)
                port = DEFAULT_PORT;

            this.connection = new Socket(url.getHost(), port);
            OutputStream out = this.connection.getOutputStream();
            String names = this.url.getFile();
            if (names != null && !names.equals("")) {
                // delete initial /
                names = names.substring(1);
                names = URLDecoder.decode(names, StandardCharsets.US_ASCII);
                byte[] result = names.getBytes(StandardCharsets.US_ASCII);
                out.write(result);
            }
            out.write('\r');
            out.write('\n');
            out.flush();
            this.connected = true;
        }
    }
}