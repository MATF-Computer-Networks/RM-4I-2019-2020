package p03_simple_http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class SimpleHttpServer {

	public static void main(String[] args) throws IOException {
		Path publicHtmlDir = Paths.get("RM4I_2019_2020/p09_NonBlocking_IO/p03_simple_http/public_html");
		SimpleHttpServer server = new SimpleHttpServer(publicHtmlDir, 12345, 5);
		server.startLogic();
	}


	private final Path publicHtmlDir;
	private final int maxCacheAliveTime;
	private final int port;
	private Map<String, ByteBuffer> responseBuffers;


	private SimpleHttpServer(Path publicHtmlDir, int port, int cacheAliveSeconds) throws IOException {
		this.publicHtmlDir = publicHtmlDir;
		this.port = port;
		this.maxCacheAliveTime = cacheAliveSeconds * 1000;
		this.fillLocalCache(this.publicHtmlDir);
	}


	private void startLogic() throws IOException {
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
			 Selector selector = Selector.open()
		) {
			serverChannel.bind(new InetSocketAddress(this.port));
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

			System.err.println("Server started!");

			long lastCacheUpdateTime = System.currentTimeMillis();
			int clients = 0;

			//noinspection InfiniteLoopStatement
			while (true) {

				// Update cache if there are no clients (we can't remove buffers in use)
				if (clients == 0 && System.currentTimeMillis() - lastCacheUpdateTime >= this.maxCacheAliveTime) {
					System.err.println("Updating server cache...");
					this.fillLocalCache(this.publicHtmlDir);
					lastCacheUpdateTime = System.currentTimeMillis();
				}

				selector.select();

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey key = it.next();
					it.remove();
					try {
						if (key.isAcceptable()) {
							this.acceptClient(key, selector);
							clients++;
						} else if (key.isReadable()) {
							this.readRequestFromClient(key);
						} else if(key.isWritable()) {
							if (this.writeToClient(key))
								clients--;
						}
					} catch(IOException ex) {
						key.cancel();
						clients--;
						try {
							key.channel().close();
						} catch (IOException cex) {
							ex.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void acceptClient(SelectionKey key, Selector selector) throws IOException {
		ServerSocketChannel server = (ServerSocketChannel)key.channel();
		SocketChannel client = server.accept();
		client.configureBlocking(false);
		client.register(selector, SelectionKey.OP_READ);
		System.err.println("Client accepted.");
	}

	private void readRequestFromClient(SelectionKey key) throws IOException {
		SocketChannel client = (SocketChannel)key.channel();

		// If we are reading from this client for the first time,
		// we create a buffer for his request
		ByteBuffer buf = (ByteBuffer)key.attachment();
		if (buf == null) {
			buf = ByteBuffer.allocate(4096);
			key.attach(buf);
		}

		System.err.println("Reading from client...");
		client.read(buf);

		String maybeCompleteRequest = new String(buf.array());
		if (maybeCompleteRequest.contains("\r\n\r\n")) {
			// Showing the collect() method, this can be done like in UDP example
			String filename = maybeCompleteRequest
					.codePoints()
					.takeWhile(c -> c > 32 && c < 127)
					.collect(StringBuilder::new,
		 					StringBuilder::appendCodePoint,
		 					StringBuilder::append)
					.toString()
					;
			System.err.println("Server received request for file: " + filename);

			// Get the response buffer
			if (this.responseBuffers.containsKey(filename))
				key.attach(this.responseBuffers.get(filename).duplicate());
			else
				key.attach(this.responseBuffers.get("404").duplicate());

			// Change mode to write - now we will send response to this client
			key.interestOps(SelectionKey.OP_WRITE);
		}
	}

	private boolean writeToClient(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel)key.channel();
		ByteBuffer buffer = (ByteBuffer)key.attachment();
		if (buffer.hasRemaining()) {
			System.err.println("Writing to client...");
			channel.write(buffer);
			return false;
		} else {
			// Per HTTP, if we are done with response, we close connection
			System.err.println("Finished working with the client.");
			channel.close();
			return true;
		}
	}

	private void fillLocalCache(Path publicHtmlDir) throws IOException {
		this.responseBuffers = new HashMap<>();

		// Find all files inside `public_html` directory
		// FIXME: For simplification, not going deeper into directory tree now
		for (Path p : Files.newDirectoryStream(publicHtmlDir)) {
			if (Files.isRegularFile(p)) {
				FileInfo fi = FileInfo.get(p, StandardCharsets.UTF_8);
				ByteBuffer responseBuffer = this.createResponseBuffer(fi);
				this.responseBuffers.put(p.getFileName().toString(), responseBuffer);
			}
		}

		// Create a special buffer to use when requested file is not found
		ByteBuffer nfBuffer = this.createNotFoundBuffer();
		this.responseBuffers.put("404", nfBuffer);
	}

	private ByteBuffer createResponseBuffer(FileInfo fi) {
		ByteBuffer data = fi.getData();
		String header = "HTTP/1.0 200 OK\r\n"
				      + "Server: SimpleHTTP v1.0\r\n"
					  + "Content-length: " + data.limit() + "\r\n"
					  + "Content-type: " + fi.getMIMEType() + "\r\n\r\n";
		byte[] headerData = header.getBytes(fi.getEncoding());
		ByteBuffer buf = ByteBuffer.allocate(headerData.length + data.limit());
		buf.put(headerData);
		buf.put(data);
		buf.flip();
		return buf;
	}

	private ByteBuffer createNotFoundBuffer() {
		String nfHeader = "HTTP/1.0 404 Not found\r\n"
						+ "Server: SimpleHTTP v1.0\r\n\r\n";
		byte[] nfHeaderData = nfHeader.getBytes(StandardCharsets.UTF_8);
		ByteBuffer nfBuffer = ByteBuffer.allocate(nfHeaderData.length);
		nfBuffer.put(nfHeaderData);
		nfBuffer.flip();
		return nfBuffer;
	}
}
