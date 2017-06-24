package httpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj<br>
 *         无论接收什么请求，仅向客户端回传单个文件的定制服务器
 */
public class SingleFileHttpServer {

	private static final Logger logger = Logger.getLogger("SingleFileHttpServer");

	private final byte[] content;
	private final byte[] header;
	private final int port;
	private final String encoding;

	public SingleFileHttpServer(String content, String encoding, String mimeType, int port)
			throws UnsupportedEncodingException {
		this(content.getBytes(encoding), encoding, mimeType, port);
	}

	public SingleFileHttpServer(byte[] content, String encoding, String mimeType, int port) {
		this.content = content;
		this.port = port;
		this.encoding = encoding;
		String header = "HTTP/1.0 200 OK\r\n" + "Server: OneFile 2.0\r\n" + "Content-length: " + this.content.length
				+ "\r\n" + "Content-type: " + mimeType + "; charset=" + encoding + "\r\n\r\n";
		this.header = header.getBytes(Charset.forName("US-ASCII"));
	}

	public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(100);
		try (ServerSocket server = new ServerSocket(this.port)) {
			logger.info("Accepting connections on port" + server.getLocalPort());
			logger.info("Data to be sent:");
			logger.info(new String(this.content, encoding));

			while (true) {
				try {
					Socket connection = server.accept();
					pool.submit(new HttpHandler(connection));
				} catch (IOException e) {
					logger.log(Level.WARNING, "Exception accepting connection", e);
				} catch (RuntimeException e) {
					logger.log(Level.SEVERE, "Unexpected error", e);
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not start server", e);
		} catch (RuntimeException e) {
			logger.log(Level.SEVERE, "Could not start server " + e.getMessage(), e);
		}
	}

	private class HttpHandler implements Callable<Void> {

		private final Socket connection;

		HttpHandler(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() throws Exception {
			try {
				BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
				BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
				StringBuilder request = new StringBuilder(80);
				while (true) {
					int c = in.read();
					if (c == '\r' || c == '\n' || c == -1)
						break;
					request.append((char) c);
				}

				// 如果是HTTP/1.0或以后版本，则发送一个MIME首部
				if (request.toString().indexOf("HTTP/") != -1) {
					out.write(header);
				}
				out.write(content);
				out.flush();
			} catch (IOException e) {
				logger.log(Level.WARNING, "Error writing to client", e);
			} finally {
				connection.close();
			}
			return null;
		}

	}

	public static void main(String[] args) {
		int port = 1024;
		String encoding = "UTF-8";
		String fileName = "D:\\aa.txt";
		Path path = Paths.get(fileName);

		try {
			byte[] content = Files.readAllBytes(path);
			String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
			SingleFileHttpServer server = new SingleFileHttpServer(content, encoding, contentType, port);
			server.start();
		} catch (IOException e) {
			logger.severe(e.getMessage());
		}
	}
}
