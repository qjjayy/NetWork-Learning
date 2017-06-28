package other;

import java.io.IOException;
import java.net.SocketOption;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.NetworkChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author qjj<br>
 *         列出各种通道支持的Socket选项
 */
public class OptionSupport {

	public static void main(String[] args) throws IOException {
		printOptions(SocketChannel.open());
		printOptions(ServerSocketChannel.open());
		printOptions(AsynchronousSocketChannel.open());
		printOptions(AsynchronousServerSocketChannel.open());
		printOptions(DatagramChannel.open());
	}

	private static void printOptions(NetworkChannel channel) {
		System.out.println(channel.getClass().getSimpleName() + "supports: ");
		for (SocketOption<?> option : channel.supportedOptions()) {
			try {
				System.out.println(option.name() + ": " + channel.getOption(option));
			} catch (AssertionError e) {

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println();
		try {
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
