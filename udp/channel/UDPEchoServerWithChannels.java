package channel;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UDPEchoServerWithChannels {

	public final static int PORT = 1024;
	public final static int MAX_PACKET_SIZE = 65507;

	public static void main(String[] args) {
		try {
			DatagramChannel channel = DatagramChannel.open();
			DatagramSocket socket = channel.socket();
			SocketAddress address = new InetSocketAddress(PORT);
			socket.bind(address);
			ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
			while (true) {
				SocketAddress client = channel.receive(buffer);
				buffer.flip();
				System.out.println(client + " says ");
				channel.send(buffer, client);
				buffer.clear();
			}
		} catch (SocketException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
