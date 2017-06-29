package buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * @author qjj<br>
 *         使用NIO缓冲区返回单文件服务器
 */
public class NonblockingSingleFileHTTPServer {

	private ByteBuffer contentBuffer;
	private int port = 1024;

	public NonblockingSingleFileHTTPServer( //
			ByteBuffer data, String encoding, String MIMEType, int port) {
		this.port = port;
		String header = "HTTP/1.0 200 OK\r\n" //
				+ "Server: NonblockingSingleFileHTTPServer\r\n" //
				+ "Content-length: " + data.limit() + "\r\n" //
				+ "Content-type: " + MIMEType + "\r\n\r\n";
		byte[] headerData = header.getBytes(Charset.forName("US-ASCII"));
		ByteBuffer buffer = ByteBuffer.allocate(data.limit() + headerData.length);
		buffer.put(headerData);
		buffer.put(data);
		buffer.flip();
		this.contentBuffer = buffer;
	}

	public void run() throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverChannel.socket();
		Selector selector = Selector.open();
		InetSocketAddress address = new InetSocketAddress(port);
		serverSocket.bind(address);
		serverChannel.configureBlocking(false);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		while (true) {
			selector.select();
			Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel clientChannel = server.accept();
						clientChannel.configureBlocking(false);
						clientChannel.register(selector, SelectionKey.OP_READ);
					} else if (key.isWritable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						if (buffer.hasRemaining()) {
							channel.write(buffer);
						} else {
							channel.close();
						}
					} else if (key.isReadable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(4096);
						channel.read(buffer);
						// 将通道切换为只写模式
						key.interestOps(SelectionKey.OP_WRITE);
						key.attach(contentBuffer.duplicate()); // 复制缓冲区，其实是浅复制，不过位置、限度和标记复制之后会独立变化
					}
				} catch (IOException ex) {
					key.cancel();
					try {
						key.channel().close();
					} catch (IOException e) {

					}
				}
			}
		}
	}

	public static void main(String[] args) {
		String fileName = "/Users/qjj/MyProjects/qualification/interface-http";
		String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
		Path path = Paths.get(fileName);
		try {
			byte[] data = Files.readAllBytes(path);
			ByteBuffer input = ByteBuffer.wrap(data);

			int port = 1024;
			String encoding = "UTF-8";
			NonblockingSingleFileHTTPServer server = new NonblockingSingleFileHTTPServer( //
					input, encoding, contentType, port);
			server.run();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
