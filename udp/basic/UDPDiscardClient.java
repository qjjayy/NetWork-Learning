package basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPDiscardClient {

	public final static int PORT = 1024;

	public static void main(String[] args) {
		String hostname = "localhost";

		try (DatagramSocket theSocket = new DatagramSocket()) {
			InetAddress server = InetAddress.getByName(hostname);
			BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String theLine = userInput.readLine();
				if (theLine.equals("."))
					break;
				byte[] data = theLine.getBytes();
				DatagramPacket theOutput = new DatagramPacket(data, data.length, server, PORT);
				theSocket.send(theOutput);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
