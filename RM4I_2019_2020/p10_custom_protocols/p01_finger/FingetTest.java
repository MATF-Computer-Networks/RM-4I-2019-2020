package p01_finger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class FingetTest {

    // Run in terminal: finger | nc -l 12345
    // Then start the program

    public static void main(String[] args) throws IOException {
        URL url = new URL(null, "finger://localhost:12345/usernames", new Handler());
        //URL url = new URL(null, "finger://localhost:12345/admin", new Handler());
        var conn = url.openConnection();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null)
                System.out.println(line);
        }
    }
}
