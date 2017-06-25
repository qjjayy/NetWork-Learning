package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class HTTPSClient {

	public static void main(String[] args) {
		int port = 1024;
		String host = "http://www.baidu.com";
		SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
		SSLSocket socket = null;
		try {
			socket = (SSLSocket) factory.createSocket(host, port);
			// 启动所有密码组
			String[] supported = socket.getSupportedCipherSuites();
			socket.setEnabledCipherSuites(supported);

			Writer out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			// https在GET行中需要完全URL
			out.write("GET http://" + host + "/ HTTP/1.1\r\n");
			out.write("Host: " + host + "\r\n");
			out.write("\r\n");
			out.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 读取首部
			String s;
			while (!(s = in.readLine()).equals("")) {
				System.out.println(s);
			}
			System.out.println();

			// 读取长度
			String contentLength = in.readLine();
			int length = Integer.MAX_VALUE;
			if (contentLength != null) {
				length = Integer.parseInt(contentLength.trim(), 16);
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
