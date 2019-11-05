package p02_copy_text;

import java.io.*;

class CopyTextBufferedMain {

    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();

            // Helper classes which can read/write characters instead of bytes
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("p01_Streams/p02_copy_text/in.txt")
                    )
            );
            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("p01_Streams/p02_copy_text/out.txt")
                    )
            );

            char[] buf = new char[512];
            int bytesRead = 0;
            while ((bytesRead = in.read(buf)) != -1)
                out.write(buf, 0, bytesRead);

            // TODO: Compare times when using naive writing:
            /*
                int b;
                while ((b = in.read()) != -1)
                    out.write(b);
             */

            // close() flushes the stream as well - this is important in the buffered variant
            in.close();
            out.close();

            long stop = System.currentTimeMillis();
            System.out.println("Finished in: " + (stop - start));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
