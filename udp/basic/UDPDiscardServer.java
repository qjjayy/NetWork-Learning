package basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPDiscardServer {

	public static final int PORT = 1024;
	public static final int MAX_PACKET_SIZE = 65507;

	public static void main(String[] args) {
		byte[] buffer = new byte[MAX_PACKET_SIZE];
		try (DatagramSocket server = new DatagramSocket(PORT)) {
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			while (true) {
				try {
					server.receive(packet);
					String s = new String(packet.getData(), 0, packet.getLength(), "8859-1");
					System.out.println(packet.getAddress() + " at port" + packet.getPort() + " says " + s);
					packet.setLength(buffer.length); // 当缓冲区接受到数据时，其长度设置为入站数据的长度，为了不会截断后续的数据报，需要重新设置缓冲区的大小
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		} catch (SocketException e) {
			System.err.println(e);
		}
	}

}
