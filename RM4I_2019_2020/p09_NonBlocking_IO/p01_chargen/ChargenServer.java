package p01_chargen;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

class ChargenServer {
	public static int DEFAULT_PORT = 19000;

	public static void main(String[] args) {
		System.out.println("Listening for connections on port " + DEFAULT_PORT);

		// Create a character rotation array
		// Rather than writing to the end of the buffer, then rewinding to the
		// beginning of the buffer and writing again, it’s easier just to start
		// with two sequential copies of the data so every line is available as
		// a contiguous sequence in the array.
		byte[] rotation = new byte[95 * 2];
		for (byte i = ' '; i <= '~'; i++) {
			rotation[i - ' '] = i;
			rotation[i + 95 - ' '] = i;
		}

		// There will be two open channels: a server channel and a client channel.
		// Both need to be processed. Both can run indefinitely. Furthermore,
		// processing the server channel will create more open client channels.
		// In the traditional approach, you assign each connection a thread, and
		// the number of threads climbs rapidly as clients connect. Instead, in
		// the new I/O API, you create a Selector that enables the program to
		// iterate over all the connections that are ready to be processed. 
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
			 Selector selector = Selector.open()
		) {

			if (!serverChannel.isOpen() || !selector.isOpen()) {
				System.err.println("The server socket channel or selector cannot be opened!");
				System.exit(1);
			}
				
			// Bind to port
			serverChannel.bind(new InetSocketAddress(DEFAULT_PORT));
			serverChannel.configureBlocking(false);
			
			// We need to register each channel with the selector that monitors it
			// using the channel’s register() method. When registering, specify the
			// operation you’re interested in using a named constant from the
			// SelectionKey class. For the server socket, the only operation of
			// interest is OP_ACCEPT; that is, is the server socket channel
			// ready to accept a new connection?
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

			//noinspection InfiniteLoopStatement
			while (true) {
				// To check whether anything is ready to be acted on, call the
				// selector’s select() method. For a long-running server, this
				// normally goes in an infinite loop
				selector.select();

				// Assuming the selector does find a ready channel, its selectedKeys() \
				// method returns a set containing one SelectionKey object for each
				// ready channel. Otherwise it returns an empty set. In either case,
				// you can loop through this with an iterator
				Set<SelectionKey> readyKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = readyKeys.iterator();
				
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					
					// If the ready channel is the server channel, the program
					// accepts a new socket channel and adds it to the selector.
					// If the ready channel is a socket channel, the program
					// writes as much of the buffer as it can onto the channel.
					// If no channels are ready, the selector waits for one.
					// One thread, the main thread, processes multiple
					// simultaneous connections.
					try {
						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel)key.channel();
							SocketChannel client = server.accept();
							
							System.out.println("Accepted connection from " + client.getRemoteAddress());
								
							// Make the client channel non-blocking to allow the server
							// to process multiple simultaneous connections
							client.configureBlocking(false);
								
							// For the client channels, you want to know something a
							// little different, specifically, whether they’re ready
							// to have data written onto them. For this, use OP_WRITE
							SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE);
								
							ByteBuffer buffer = ByteBuffer.allocate(74);
							// Copy initial line from rotation into the buffer
							buffer.put(rotation, 0, 72);
							// Store a line break at the end of the buffer
							buffer.put((byte) '\r');
							buffer.put((byte) '\n');
							// Prepare the buffer for writing
							buffer.flip();

							// Attach the buffer to the key
							clientKey.attach(buffer);
						} else if (key.isWritable()) {
							SocketChannel client = (SocketChannel) key.channel();
							ByteBuffer buffer = (ByteBuffer)key.attachment();

							// If buffer is empty, refill the buffer with the next line
							if (!buffer.hasRemaining()) {
								// Rewind back to the start of the buffer
								buffer.rewind();
								// Get the first character - so we can determine next line
								int first = buffer.get();
								// Find the new first character position in rotation
								int position = first - ' ' + 1;
								// We used `get()` so now we have to rewind() again so we can
								// put from the begining of the buffer
								buffer.rewind();
								// Copy next line data from rotation into the buffer
								buffer.put(rotation, position, 72);
								// Store a line break at the end of the buffer
								buffer.put((byte)'\r');
								buffer.put((byte)'\n');
								// Prepare the buffer for writing
								buffer.flip();
							}

							// In any case, write the buffer
							client.write(buffer);
						}
					} catch (IOException ex) {
						// In case there is an issue with one of the clients,
						// dispose resources and remove the client from consideration.
						// cancel() method also removes the channel from the selector
						key.cancel();
						try {
							key.channel().close();
						} catch (IOException cex) {
							cex.printStackTrace();
						}
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
