package basic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author qjj<br>
 *         使用NIO写的客户端
 */
public class ChargenClient {

	public static void main(String[] args) {
		int port = 1024;
		String host = "10.12.142.1";

		try {
			InetSocketAddress address = new InetSocketAddress(host, port);
			SocketChannel client = SocketChannel.open(address);

			ByteBuffer buffer = ByteBuffer.allocate(74);
			WritableByteChannel out = Channels.newChannel(System.out);

			while (client.read(buffer) != -1) {
				buffer.flip();
				out.write(buffer);
				buffer.clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
