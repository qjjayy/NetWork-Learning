package eho;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import application.UDPServer;

public class UDPEchoServer extends UDPServer {

	public final static int DEFAULT_PORT = 1024;

	public UDPEchoServer() {
		super(DEFAULT_PORT);
	}

	@Override
	public void respond(DatagramSocket socket, DatagramPacket request) throws IOException {
		DatagramPacket outgoing = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),
				request.getPort());
		socket.send(outgoing);
	}

	public static void main(String[] args) {
		UDPEchoServer server = new UDPEchoServer();
		Thread t = new Thread(server);
		t.start();
	}

}
