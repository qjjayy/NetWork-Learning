package serversocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj
 *	记录请求和错误的daytime服务器
 */
public class LoggingDaytimeServer {
	
	public final static int PORT = 1024;
	// 日志工具是线程安全的，所以将它们存储在共享的静态字段中很安全，这样就可以在多线程中共享
	private final static Logger auditLogger = Logger.getLogger("requests");
	private final static Logger errorLogger = Logger.getLogger("errors");
	
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(50);
		
		try (ServerSocket server = new ServerSocket(PORT)) {
			while (true) {
				try {
					Socket connection = server.accept();
					Callable<Void> task = new DaytimeTask(connection);
					pool.submit(task);
				} catch (IOException ex) {
					errorLogger.log(Level.SEVERE, "accept error", ex);
				} catch (RuntimeException ex) {
					errorLogger.log(Level.SEVERE, "unexpected error " + ex.getMessage(), ex);
				}
			}
		} catch (IOException ex) {
			errorLogger.log(Level.SEVERE, "Can't start server", ex);
		} catch (RuntimeException ex) {
			// 网络服务器强烈推荐的一种做法，可以防止由于一个请求进入计划外的代码路径而抛出一个IllegalArgumentException，
			// 从而导致整个服务器都崩溃
			errorLogger.log(Level.SEVERE, "Can't start server: " + ex.getMessage(), ex);
		}
		
	}

	private static class DaytimeTask implements Callable<Void> {

		private Socket connection;
		
		DaytimeTask(Socket connection) {
			this.connection = connection;
		}
		
		@Override
		public Void call() throws Exception {
			try {
				Date now = new Date();
				// 先写入日志记录以防万一客户断开连接
				auditLogger.info(now + " " + connection.getRemoteSocketAddress());
				Writer out = new OutputStreamWriter(connection.getOutputStream());
				out.write(now.toString() + "\r\n");
				out.flush();
			} catch (IOException ex) {
				// 客户端断开连接，忽略
				// 这里的异常，既不是代码的bug,也不是服务器错误
			} finally {
				try {
					connection.close();
				} catch (IOException ex) {
					// 忽略
				}
			}
			return null;
		}
		
	}
}