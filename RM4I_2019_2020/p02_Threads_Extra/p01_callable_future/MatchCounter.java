package p01_callable_future;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

// Counts how many files contain given keyword
class MatchCounter implements Callable<Integer> {

	private Path directory;
	private String keyword;


	public MatchCounter(Path directory, String keyword) {
		this.directory = directory;
		this.keyword = keyword;
	}

	
	@Override
	public Integer call() {
		int count = 0;
		try {
			ArrayList<Future<Integer>> results = new ArrayList<Future<Integer>>();

			try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
				for (Path file : stream) {
					if (Files.isDirectory(file)) {
						MatchCounter counter = new MatchCounter(file, keyword);
						FutureTask<Integer> task = new FutureTask<Integer>(counter);
						results.add(task);
						Thread t = new Thread(task);
						t.start();
					} else if (this.search(file)) {
						count++;
					}
				}
			} catch (IOException | DirectoryIteratorException x) {
				System.err.println(x);
			}

			for (Future<Integer> result : results) {
				try {
					count += result.get();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		} catch (InterruptedException e) {

		}
		
		return count;
	}

	// Searches file for the given keyword
	public boolean search(Path file) {
		try (Scanner in = new Scanner(file)) {
			boolean found = false;
			while (!found && in.hasNextLine()) {
				String line = in.nextLine();
				if (line.contains(keyword))
					found = true;
			}
			return found;
		} catch (IOException e) {
			return false;
		}
	}
}
