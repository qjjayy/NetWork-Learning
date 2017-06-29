package basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj<br>
 *         UDP服务器，往往不需要多线程，通常不会对一个客户端做太多工作，而且不会阻塞来等待另一端响应，因为UDP从来不会报告错误
 *
 */
public class DaytimeUDPServer {

	private static final int PORT = 1024;
	private static final Logger audit = Logger.getLogger("request");
	private static final Logger errors = Logger.getLogger("errors");

	public static void main(String[] args) {
		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			while (true) {
				DatagramPacket request = new DatagramPacket(new byte[1024], 1024);
				socket.receive(request);

				String daytime = new Date().toString();
				byte[] data = daytime.getBytes(Charset.forName("US-ASCII"));
				DatagramPacket response = new DatagramPacket(data, data.length, request.getAddress(),
						request.getPort());
				socket.send(response);
				audit.info(daytime + " " + request.getAddress());
			}
		} catch (IOException | RuntimeException e) {
			errors.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}