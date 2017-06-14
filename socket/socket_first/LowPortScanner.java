package socket_first;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author qjj <br>
 *	检测本地有多少端口正在被使用
 */
public class LowPortScanner {

	public static void main(String[] args) {
		String host ="localhost";
		for (int i = 1; i < 1024; i++) {
			try (Socket s = new Socket(host, i)) {
				System.out.println("There is a server on port " + i + " of " + host);
			} catch (UnknownHostException e) {
				System.err.println(e);
			} catch (IOException e) {
				// 这个端口不是一个服务器
				System.err.println("There is no server on port" + i + " of " + host);
			}
		}
	}

}
