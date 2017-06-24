package serversocket;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author qjj <br>
 *         检测本地有多少端口正在被使用(速度更快)
 */
public class LocalPortScanner {

	public static void main(String[] args) {
		for (int port = 1; port <= 65535; port++) {
			try {
				// 如果这个端口上已经有服务器在运行，下一行就会失败，进入catch块
				@SuppressWarnings({ "unused", "resource" })
				ServerSocket server = new ServerSocket(port);
				System.out.println("This port " + port + " is not in use.");
			} catch (IOException e) {
				System.err.println("There is a server on port " + port + ".");
			}
		}
	}

}
