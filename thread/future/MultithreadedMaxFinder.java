package future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 通过使用Future、Callable从线程返回信息
 * 
 * @author qjj
 *
 */
public class MultithreadedMaxFinder {

	public static int max(int[] data) throws InterruptedException, ExecutionException {
		if (data.length == 1) {
			return data[0];
		} else if (data.length == 0) {
			throw new IllegalArgumentException();
		}

		FindMaxTask task1 = new FindMaxTask(data, 0, data.length / 2);
		FindMaxTask task2 = new FindMaxTask(data, data.length / 2, data.length);

		ExecutorService service = Executors.newFixedThreadPool(2);

		Future<Integer> future1 = service.submit(task1);
		Future<Integer> future2 = service.submit(task2);

		// get()采用轮询的方式获取线程的返回值，如果返回值没有准备好，线程会被阻塞，直到准备就绪
		return Math.max(future1.get(), future2.get());
	}

	public static void main(String[] args) {
		int[] data = { 34, 64, 767, 23, 45, 89, 23, 6788, 26, 54, 90 };
		try {
			System.out.println(max(data));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

}
