package buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author qjj<br>
 *         使用 NIO缓冲区的客户端
 */
public class IntgenClient {

	public static int DEFAULT_PORT = 1919;

	public static void main(String[] args) {
		int port = DEFAULT_PORT;
		String host = "localhost";

		try {
			InetSocketAddress address = new InetSocketAddress(host, port);
			SocketChannel client = SocketChannel.open(address);
			ByteBuffer buffer = ByteBuffer.allocate(4);
			IntBuffer view = buffer.asIntBuffer();

			for (int expected = 0;; expected++) {
				// SocketChannel类只有读／写ByteBuffer的方法，无法读／写任何其他类型的缓冲区
				client.read(buffer);
				int actual = view.get();
				// 必须要清楚ByteBuffer，否则缓冲区将填满
				buffer.clear();
				view.rewind();

				if (actual != expected) {
					System.err.println("Expected " + expected + "; was" + actual);
					break;
				}
				System.out.println(actual);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
