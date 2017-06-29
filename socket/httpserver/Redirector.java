package httpserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj<br>
 *         实现重定向的定制服务器
 */
public class Redirector {
	private static final Logger logger = Logger.getLogger(new File("Redirector").getAbsolutePath());

	private final int port;
	private final String newSite;

	public Redirector(int port, String newSite) {
		this.port = port;
		this.newSite = newSite;
	}

	public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(100);
		try (ServerSocket server = new ServerSocket(port)) {
			while (true) {
				try {
					Socket connection = server.accept();
					pool.submit(new RedirectHandler(connection));
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

	private class RedirectHandler implements Callable<Void> {

		private final Socket connection;

		RedirectHandler(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() throws Exception {
			try {
				BufferedWriter out = new BufferedWriter( //
						new OutputStreamWriter(connection.getOutputStream(), "US-ASCII"));
				BufferedReader in = new BufferedReader( //
						new InputStreamReader(connection.getInputStream()));
				StringBuilder request = new StringBuilder(80);
				while (true) {
					int c = in.read();
					if (c == '\r' || c == '\n' || c == -1)
						break;
					request.append((char) c);
				}
				String get = request.toString();
				String[] pieces = get.split("\\w*");
				String theFile = pieces[1];

				// 如果是HTTP/1.0或以后版本，则发送一个MIME首部
				if (get.indexOf("HTTP") != -1) {
					out.write("HTTP/1.0 302 FOUND\r\n");
					Date now = new Date();
					out.write("Date: " + now + "\r\n");
					out.write("Server: Redirector 1.1\r\n");
					out.write("Location: " + newSite + theFile + "\r\n");
					out.write("Content-type: text/html\r\n\r\n");
					out.flush();
				}

				// 并不是所有浏览器都支持重定向，所以我们需要生成HTML指出文档转移到哪里
				out.write("<HTML><HEAD>Document moved</HEAD></HTML>");
				out.write("<BODY><H1>Document moved</H1>\r\n");
				out.write("The document " + theFile + "has moved to\r\n<A HREF=\"" + newSite + theFile + "\">" + newSite
						+ theFile + "</A>.\r\n Please update your bookmarks<P>");
				out.write("</BODY></HTML>\r\n");
				out.flush();
				logger.log(Level.INFO, "Redirected", connection.getRemoteSocketAddress());
			} catch (IOException e) {
				logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), e);
			} finally {
				try {
					connection.close();
				} catch (IOException e) {

				}
			}
			return null;
		}
	}

	public static void main(String[] args) {
		int port = 1024;
		String theSite = "http://www.baidu.com";
		Redirector redirector = new Redirector(port, theSite);
		redirector.start();
	}
}
