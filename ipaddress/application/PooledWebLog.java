package application;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author qjj<br>
 *         处理Web服务器日志文件
 */
public class PooledWebLog {

	private final static int NUM_THREADS = 4;
	private static String fileName = "";

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
		Queue<LogEntry> results = new LinkedList<LogEntry>();

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
			for (String entry = in.readLine(); entry != null; entry = in.readLine()) {
				LookupTask task = new LookupTask(entry);
				Future<String> future = executor.submit(task);
				LogEntry result = new LogEntry(entry, future);
				results.add(result);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (LogEntry result : results) {
			try {
				System.out.println(result.future.get());
			} catch (InterruptedException | ExecutionException e) {
				System.out.println(result.original);
			}
		}

		executor.shutdown();
	}

	private static class LogEntry {
		String original;
		Future<String> future;

		LogEntry(String original, Future<String> future) {
			this.original = original;
			this.future = future;
		}
	}

}
