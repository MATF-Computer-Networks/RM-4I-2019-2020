package p02_daytime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

class DaytimeTest {
    public static void main(String[] args) throws IOException {
        URL url = new URL(null, "daytime://localhost:8765", new Handler());
        var conn = url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null)
                System.out.println(line);
        }
    }
}

