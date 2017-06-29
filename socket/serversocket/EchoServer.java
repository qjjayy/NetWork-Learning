package serversocket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author qjj <br>
 *         服务器端收发数据（代码中存在未知的知识点）
 *
 */
public class EchoServer {

	public static int DEFAULT_PORT = 1024;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		System.out.println("Listening for connections on port" + port);

		ServerSocketChannel serverChannel;
		Selector selector = null;

		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				break;
			}
		}

		Set<SelectionKey> readyKeys = selector.selectedKeys();
		Iterator<SelectionKey> iterator = readyKeys.iterator();
		while (iterator.hasNext()) {
			SelectionKey key = iterator.next();
			iterator.remove();
			try {
				if (key.isAcceptable()) {
					ServerSocketChannel server = (ServerSocketChannel) key.channel();
					SocketChannel client = server.accept();
					System.out.println("Accepted connection from " + client);
					client.configureBlocking(false);
					SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
					ByteBuffer buffer = ByteBuffer.allocate(100);
					clientKey.attach(buffer);
				}
				if (key.isReadable()) {
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer output = (ByteBuffer) key.attachment();
					client.read(output);
				}
				if (key.isWritable()) {
					SocketChannel client = (SocketChannel) key.channel();
					ByteBuffer output = (ByteBuffer) key.attachment();
					output.flip();
					client.write(output);
					output.compact();
				}
			} catch (IOException e) {
				key.cancel();
				try {
					key.channel().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
