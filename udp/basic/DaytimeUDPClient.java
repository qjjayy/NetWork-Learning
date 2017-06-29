package basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @author qjj<br>
 *         通过UDP连接服务器
 */
public class DaytimeUDPClient {

	private static final int PORT = 1024;
	private static final String HOSTNAME = "www.baidu.com";

	public static void main(String[] args) {
		try (DatagramSocket socket = new DatagramSocket(0)) {
			socket.setSoTimeout(10000); // 超时时间的设置，UDP比TCP的意义更大，因为TCP抛出的一些异常，在UDP这会被悄无声息地处理
			InetAddress host = InetAddress.getByName(HOSTNAME);
			DatagramPacket request = new DatagramPacket(new byte[1], 1, host, PORT); // 一定要指出连接的远程主机和远程端口
			DatagramPacket response = new DatagramPacket(new byte[1024], 1024); // 这个数组要足够大，可以包含整个响应，否则会悄悄地截断响应
			socket.send(request);
			socket.receive(response);
			String result = new String(response.getData(), 0, response.getLength(), "US-ASCII");
			System.out.println(result);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
