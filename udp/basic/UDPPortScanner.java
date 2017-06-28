package basic;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author qjj<br>
 *         扫描正在运行的UDP端口，注意UDP端口和TCP端口是截然不同的
 */
public class UDPPortScanner {

	public static void main(String[] args) {
		for (int port = 1024; port <= 65535; port++) {
			try {
				DatagramSocket server = new DatagramSocket(port);
				server.close();
			} catch (SocketException e) {
				System.out.println("There is a server on port " + port + ".");
			}
		}
	}

}
