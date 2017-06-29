package eho;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author qjj<br>
 *         基于UDP的Echo客户端，不同于基于TCP的客户端，前者无I/O流或者连接的概念，而且不能保证发送的消息总能被接收到
 *         因此，它不能简单地等待响应，而是需要准备异步地发送和接收数据<br>
 *         所以客户端创建了两个线程 ，一个用于读，一个用于写
 */
public class UDPEchoClient {
	public final static int PORT = 1024;

	public static void main(String[] args) {
		String hostname = "localhost";

		try {
			InetAddress ia = InetAddress.getByName(hostname);
			DatagramSocket socket = new DatagramSocket();
			SenderThread sender = new SenderThread(socket, ia, PORT);
			sender.start();
			ReceiverThread receiver = new ReceiverThread(socket); // 使用同一个DatagramSocket，因为会把响应发回原先发出数据的那个端口
			receiver.start();
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (SocketException e) {
			System.err.println(e);
		}

	}
}
