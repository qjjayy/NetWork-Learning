package socket_first;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * @author qjj <br>
 * Socket 信息  
 */
public class SocketInfo {

	public static void main(String[] args) {
		String host = "www.baidu.com";
		try (Socket socket = new Socket(host, 80)) {
			System.out.println("Connected to " + socket.getInetAddress()
			+ " on port " + socket.getPort()
			+ " from port " + socket.getLocalPort()
			+ " of " + socket.getLocalAddress());
			
			// SocketAddress类的主要用途是为暂时的socket连接信息（如IP地址和端口）提供一个方便的存储
			SocketAddress baidu = socket.getRemoteSocketAddress();
			// 不提供任何参数的Socket构造方法，不会建立连接
			try (Socket socket2 = new Socket()) {
				 socket2.connect(baidu);
				 // isConnected 表示是否从未连接过一个远程主机，isClosed 表示是否关闭socket（Socket根本没有打开过也返回false）
				 System.out.println(socket2.isConnected() && !socket2.isClosed());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
