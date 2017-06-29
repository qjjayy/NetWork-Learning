package socket_first;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author qjj <br>
 *         获知随机端口
 */
public class RandomPort {

	public static void main(String[] args) {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(0);
			System.out.println("This server runs on port" + server.getLocalPort());
			System.out.println(server.toString());
		} catch (IOException e) {
			System.err.println(e);
		}

	}

}
