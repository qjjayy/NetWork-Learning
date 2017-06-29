package httpserver;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author qjj<br>
 *         一个功能较完备的服务器，但只接受GET请求
 */
public class JHTTP {
	private static final Logger logger = Logger.getLogger(JHTTP.class.getCanonicalName());

	private static final int NUM_THREADS = 50;
	private static final String INDEX_FILE = "index.html";

	private final File rootDirectory;
	private final int port;

	public JHTTP(File rootDirectory, int port) throws IOException {
		if (!rootDirectory.isDirectory()) {
			throw new IOException(rootDirectory + "does not exist as a directory");
		}
		this.rootDirectory = rootDirectory;
		this.port = port;
	}

	public void start() {
		ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
		try (ServerSocket server = new ServerSocket(port)) {
			logger.info("Accepting connection on port " + server.getLocalPort());
			logger.info("Document root: " + rootDirectory);

			while (true) {
				try {
					Socket connection = server.accept();
					Runnable r = new RequestProcessor(rootDirectory, INDEX_FILE, connection);
					pool.submit(r);
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

	public static void main(String[] args) {
		File docroot = new File("D:\\"); // 设置文档根目录
		int port = 1024;
		try {
			JHTTP webServer = new JHTTP(docroot, port);
			webServer.start();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Could not start server", e);
		}

	}
}
