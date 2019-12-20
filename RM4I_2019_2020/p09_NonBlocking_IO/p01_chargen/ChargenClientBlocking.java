package p01_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

class ChargenClientBlocking {
	public static void main(String[] args) {
		String host = "localhost";
		int port = 19000;
		
		try {
			SocketAddress address = new InetSocketAddress(host, port);

			// The channel is opened in blocking mode, so the next line of code won’t 
			// execute until the connection is established. 
			SocketChannel client = SocketChannel.open(address);
			
			// With a channel you write directly to the channel itself. 
			// Rather than writing byte arrays, you write ByteBuffer objects.
			// We’ve got a pretty good idea that the lines of text are 74 ASCII 
			// characters long (72 printable characters followed by a carriage 
			// return/linefeed pair).
			// read() and write() methods on buffers work with smart buffers rather
			// than byte arrays. Those smart buffers are part of new Java I/O.
			ByteBuffer buffer = ByteBuffer.allocate(74);
			
			// There are ways to extract a byte array from a ByteBuffer that can then be
			// written on a traditional OutputStream such as System.out. However, it’s more 
			// informative to stick with a pure, channel-based solution. Such a solution 
			// requires wrapping the OutputStream System.out in a channel.
			WritableByteChannel out = Channels.newChannel(System.out);
			
			// Pass this ByteBuffer object to the channel’s read() method. The channel
			// fills this buffer with the data it reads from the socket. It returns the
			// number of bytes it successfully read and stored in the buffer.
			while (client.read(buffer) != -1) {
				// You can then write the data that was read onto this output channel
				// connected to System.out. However, before you do that you have to
				// "flip" (not literally) the buffer so that the output channel starts
				// from the beginning of the data that was read rather than the end
				// These operations will be explained in the non-blocking client example.
				buffer.flip();
				out.write(buffer);

				// "Clearing" the buffer prepares it for read operation
				buffer.clear();
			}
			
			// So far, this is just an alternate vision of a program that could have
			// easily been written using streams. The really new feature comes if you
			// want the client to do something besides copying all input to output.
		
		} catch (IOException ex) {
			ex.printStackTrace( );
		}

	}

}
