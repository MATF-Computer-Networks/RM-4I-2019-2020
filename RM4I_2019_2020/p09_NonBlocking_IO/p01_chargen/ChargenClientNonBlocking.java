package p01_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

class ChargenClientNonBlocking {
	
	public static void main(String[] args) {
		String host = "localhost";
		int port = 19000;
		
		try {
			SocketAddress address = new InetSocketAddress(host, port);
			SocketChannel client = SocketChannel.open(address);

			/*
			 A buffer is a linear, finite sequence of elements of a specific primitive type.
			 Aside from its content, the essential properties of a buffer are its capacity,
			 limit, and position:
			 - Capacity is the number of elements it contains. The capacity of a buffer
			   is never negative and never changes.
			 - Limit is the index of the first element that should not be read or written.
			   A buffer's limit is never negative and is never greater than its capacity.
			 - Position is the index of the next element to be read or written. A buffer's
			   position is never negative and is never greater than its limit.
			 */
			ByteBuffer buffer = ByteBuffer.allocate(74);

			WritableByteChannel out = Channels.newChannel(System.out);
			
			client.configureBlocking(false);
			
			// In non-blocking mode, read() may return 0 because it doesn’t read anything. 
			// Therefore the loop needs to be a little different:
			while (true) {

				// Put whatever code here you want to run every pass through the loop
				// whether anything is read or not

				// Then perform read, and check if anything is read
				int n = client.read(buffer);
				if (n > 0) {
					/*
					In addition to methods for accessing the position, limit, and capacity
					values and for marking and resetting, Buffer class also defines the following
					operations upon buffers:
    				- clear() makes a buffer ready for a new sequence of channel-read or relative put
    				  operations: It sets the limit to the capacity and the position to zero.
    				- flip() makes a buffer ready for a new sequence of channel-write or relative get
    				  operations: It sets the limit to the current position and then sets the position to zero.
    				- rewind() makes a buffer ready for re-reading the data that it already contains:
    				  It leaves the limit unchanged and sets the position to zero.
					 */

					// Prepare the buffer for writing
					buffer.flip();
					// Perform write
					out.write(buffer);
					// Prepare the buffer for reading
					buffer.clear();

				} else if (n == -1) {
					// This shouldn't happen unless the server is misbehaving.
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace( );
		}
		
	}
}
