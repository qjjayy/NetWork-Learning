package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj<br>
 *         基于UDP的服务器抽象工具类
 */
public abstract class UDPServer implements Runnable {

	private final int bufferSize; // 单位为字节
	private final int port;
	private final Logger logger = Logger.getLogger(UDPServer.class.getCanonicalName());
	private volatile boolean isShutDown = false;

	public UDPServer(int port, int bufferSize) {
		this.bufferSize = bufferSize;
		this.port = port;
	}

	public UDPServer(int port) {
		this(port, 8192);
	}

	@Override
	public void run() {
		byte[] buffer = new byte[bufferSize];
		try (DatagramSocket socket = new DatagramSocket(port)) {
			socket.setSoTimeout(10000); // 每10秒检查一次是否关闭
			while (true) {
				if (isShutDown)
					return;
				DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(incoming);
					this.respond(socket, incoming);
				} catch (SocketTimeoutException e) {
					if (isShutDown)
						return;
				} catch (IOException e) {
					logger.log(Level.WARNING, e.getMessage(), e);
				}
			}
		} catch (SocketException e) {
			logger.log(Level.SEVERE, "Could not bind to port: " + port, e);
		}
	}

	public abstract void respond(DatagramSocket socket, DatagramPacket request) throws IOException;

	public void shutDown() {
		this.isShutDown = true;
	}
}
