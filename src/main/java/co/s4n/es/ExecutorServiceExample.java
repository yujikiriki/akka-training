package co.s4n.es;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorServiceExample {
	private static final int NTHREDS = 10;

	public static void main(String[] args) throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(NTHREDS);
		for (int i = 0; i < 500; i++) {
			Runnable worker = new MyRunnable(10000000L + i);
			executor.execute(worker);
		}
		executor.shutdown();
		executor.awaitTermination(60, TimeUnit.SECONDS);
		System.out.println("Finished all threads");
	}
}
