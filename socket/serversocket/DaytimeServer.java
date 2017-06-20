package serversocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author qjj <br>
 *         服务器接收连接，并向客户端发送时间数据
 */
public class DaytimeServer {

	public final static int PORT = 1024;

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(PORT)) {
			while (true) {
				try (Socket connection = server.accept()) {
					Writer out = new OutputStreamWriter(connection.getOutputStream());
					Date now = new Date();
					out.write(now.toString() + "\r\n");
					out.flush();
					connection.close();
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

}
