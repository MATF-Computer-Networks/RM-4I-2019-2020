package p02_thread_pool;

import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

class ThreadPoolTest {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter base directory: ");
		String directory = in.nextLine();
		System.out.print("Enter keyword (e.g. volatile): ");
		String keyword = in.nextLine();
		in.close();

		ExecutorService pool = Executors.newCachedThreadPool();

		MatchCounter counter = new MatchCounter(Paths.get(directory), keyword, pool);
		Future<Integer> result = pool.submit(counter);

		try {
			System.out.println(result.get() + " matching files.");
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}

		pool.shutdown();

		int largestPoolSize = ((ThreadPoolExecutor) pool).getLargestPoolSize();
		System.out.println("largest pool size: " + largestPoolSize);
	}

}
