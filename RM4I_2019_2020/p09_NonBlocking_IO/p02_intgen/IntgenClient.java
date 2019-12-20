package p02_intgen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

class IntgenClient {

	public static void main(String[] args) {
		InetSocketAddress addr = new InetSocketAddress("localhost", IntgenServer.DEFAULT_PORT);

		// Client is left in `blocking` mode on purpose, since there is no other
		// work he has to do other than receive data from server
		try (SocketChannel client = SocketChannel.open(addr)) {

			// We allocate 4 bytes - enough for one integer
			ByteBuffer buffer = ByteBuffer.allocate(4);

			// We can create a "view" of the underlying bytes
			IntBuffer view = buffer.asIntBuffer();

			for (int expected = 0; ; expected++) {

				// Since read() might not fill the whole buffer,
				// we do it in a loop
				while (buffer.hasRemaining())
					client.read(buffer);

				int received = view.get();

				// Prepare for next read() call
				buffer.clear();

				// Prepare for next get() call
				view.rewind();

				if (received != expected) {
					System.err.println("Expected: " + expected + "; got: " + received);
					break;
				}

				System.out.println(received);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
