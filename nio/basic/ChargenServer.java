package basic;

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
 * @author qjj<br>
 *         使用非阻塞I/O的服务器<br>
 *         SelectionKey有一个任意类型的“附件”，它通常用于保存一个指示当前连接状态的对象。<br>
 *         这里可以将通道即将要写入网络的缓冲区存储在这个对象中
 */
public class ChargenServer {

	public static void main(String[] args) {
		int port = 1024;

		System.out.println("Listening for connections on port " + port);
		byte[] rotation = new byte[95 * 2];
		for (byte i = ' '; i <= '~'; i++) {
			rotation[i - ' '] = i;
			rotation[i + 95 - ' '] = i;
		}

		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			// 将通道绑定到一个端口，从而监听那个端口
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			// 将ServerSocketChannel设置为非阻塞模式，即如果当前没有入站连接，非阻塞的accept()几乎立即返回null
			serverChannel.configureBlocking(false);
			// Selector 允许程序迭代处理所有准备好的连接，Selector会监听所有在其上注册的通道
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT); // 服务器socket通道只要关心是否准备好接受一个新的连接
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				// 检查是否有可操作的数据，将就绪的通道添加到就绪集合中，它是一个阻塞的方法，
				// 仅当Selector上注册的通道中有多于一个就绪，或者selector的wakeup()方法被调用，
				// 或者当前线程被中断了
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 返回所有就绪的通道的SelectionKey对象
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove(); // 以免同一通道被处理两次
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						System.out.println("Accepted connection from " + client);
						// 将SocketChannel设置为非阻塞模式，即当没有数据可以读取时，非阻塞的read()会立即返回0
						client.configureBlocking(false);
						SelectionKey key2 = client.register(selector, SelectionKey.OP_WRITE); // 客户端Socket通道关心是否准备好数据可以写入通道
						ByteBuffer buffer = ByteBuffer.allocate(74);
						buffer.put(rotation, 0, 72);
						buffer.put((byte) '\r');
						buffer.put((byte) '\n');
						buffer.flip();
						key2.attach(buffer); // 将缓冲区附加到通道的键上
					} else if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						if (!buffer.hasRemaining()) {
							buffer.rewind();
							int first = buffer.get();
							buffer.rewind();
							int position = first - ' ' + 1;
							buffer.put(rotation, position, 72);
							buffer.put((byte) '\r');
							buffer.put((byte) '\n');
							buffer.flip();
						}
						client.write(buffer);
					}
				} catch (IOException e) {
					key.cancel(); // 将该健对应的通道放到取消通道集合中，selector下次不会观察它
					try {
						key.channel().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
