package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FastUDPDiscardServer extends UDPServer {

	public final static int DEFAULT_PORT = 9;

	public FastUDPDiscardServer() {
		super(DEFAULT_PORT);
	}

	@Override
	public void respond(DatagramSocket socket, DatagramPacket request) throws IOException {

	}

	public static void main(String[] args) {
		FastUDPDiscardServer server = new FastUDPDiscardServer();
		Thread t = new Thread(server);
		t.start();
	}

}
