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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class SimpleHttpServer {

	public static void main(String[] args) throws IOException {

		// Find all files inside public_html directory
		Map<Path, FileInfo> cache = new HashMap<>();
		for (Path p : Files.newDirectoryStream(Paths.get("RM4I_2019_2020/p09_NonBlocking_IO/p03_simple_http/public_html"))) {
			// For simplification, not going deeper into directory tree
			if (Files.isRegularFile(p))
				cache.put(p.getFileName(), FileInfo.get(p, StandardCharsets.UTF_8));
		}

		SimpleHttpServer server = new SimpleHttpServer(cache, 12345);
		server.startLogic();
	}

	
	private Map<String, ByteBuffer> contentBuffers;
	private int port;
	
	
	private SimpleHttpServer(Map<Path, FileInfo> cache, int port) {
		this.port = port;
		this.fillLocalCache(cache);
	}


	private void startLogic() throws IOException {
		try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
			 Selector selector = Selector.open()
		) {
			serverChannel.bind(new InetSocketAddress(this.port));
			serverChannel.configureBlocking(false);
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

			System.err.println("Server started!");

			//noinspection InfiniteLoopStatement
			while (true) {

				selector.select();

				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();
					try {
						if (key.isAcceptable()) {
							ServerSocketChannel server = (ServerSocketChannel)key.channel();
							SocketChannel channel = server.accept();
							channel.configureBlocking(false);
							channel.register(selector, SelectionKey.OP_READ);
							System.err.println("Client found. Awaiting request...");
						} else if (key.isReadable()) {
							SocketChannel channel = (SocketChannel)key.channel();
							ByteBuffer buffer = ByteBuffer.allocate(4096);
							channel.read(buffer);
							String filename = new String(buffer.array())
									.codePoints()
									.takeWhile(c -> c > 32 && c < 127)
									.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
									.toString()
									;
							System.err.println("Server received request for file: " + filename);
							if (this.contentBuffers.containsKey(filename))
								key.attach(this.contentBuffers.get(filename).duplicate());
							else
								key.attach(this.contentBuffers.get("404").duplicate());
							key.interestOps(SelectionKey.OP_WRITE);
						} else if(key.isWritable()) {
							SocketChannel channel = (SocketChannel)key.channel();
							ByteBuffer buffer = (ByteBuffer)key.attachment();
							if (buffer.hasRemaining())
								channel.write(buffer);
							else
								channel.close();
						}
					} catch(IOException ex) {
						key.cancel();
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


	private void fillLocalCache(Map<Path, FileInfo> cache) {

		// Create buffer cache for each of the files inside public_html directory
		this.contentBuffers = new HashMap<>();
		for (Map.Entry<Path, FileInfo> e : cache.entrySet()) {
			FileInfo fi = e.getValue();
			ByteBuffer data = fi.getData();
			String header = "HTTP/1.0 200 OK\r\n"
					+ "Server: SimpleHTTP v1.0\r\n"
					+ "Content-length: " + data.limit() + "\r\n"
					+ "Content-type: " + fi.getMIMEType() + "\r\n\r\n";
			byte[] headerData = header.getBytes(fi.getEncoding());

			ByteBuffer buffer = ByteBuffer.allocate(headerData.length + data.limit());
			buffer.put(headerData);
			buffer.put(data);
			buffer.flip();
			this.contentBuffers.put(e.getKey().getFileName().toString(), buffer);
		}

		// Create a special buffer to use when requested file is not found
		String nfHeader = "HTTP/1.0 404 Not found\r\n"
				+ "Server: SimpleHTTP v1.0\r\n\r\n";
		byte[] nfHeaderData = nfHeader.getBytes(StandardCharsets.UTF_8);
		ByteBuffer nfBuffer = ByteBuffer.allocate(nfHeaderData.length);
		nfBuffer.put(nfHeaderData);
		nfBuffer.flip();
		this.contentBuffers.put("404", nfBuffer);
	}
}
