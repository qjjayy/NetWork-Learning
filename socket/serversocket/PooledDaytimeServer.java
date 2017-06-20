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

public class PooledDaytimeServer {

	public final static int PORT = 1024;

	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(50); // 建立一个线程池，可以防止拒绝服务攻击，防止系统崩溃
		try (ServerSocket server = new ServerSocket()) {
			while (true) {
				try {
					// 这里不能使用try-with-resource，因为try-with-resource结束后会自动关闭，而此时socket并未使用结束
					Socket connection = server.accept();
					Callable<Void> task = new DayTimeTask(connection);
					pool.submit(task);
				} catch (IOException ex) {

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static class DayTimeTask implements Callable<Void> {

		private Socket connection;

		DayTimeTask(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() {
			try {
				Writer out = new OutputStreamWriter(connection.getOutputStream());
				Date now = new Date();
				out.write(now.toString() + "\r\n");
				out.flush();
			} catch (IOException ex) {
				System.err.println(ex);
			} finally {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

	}
}
