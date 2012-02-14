package tests;

import cipher.FastSkipJack;

public class SpeedTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: java SpeedTest <num_iterations>");
		} else {
			int n = Integer.parseInt(args[0]);
			int[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}; 
			FastSkipJack cipher = new FastSkipJack(key);
		    long t1 = System.currentTimeMillis();
			for (int i = 0; i < n; ++i) {
				cipher.Encrypt(0L);
			}
		    long t2 = System.currentTimeMillis();
		    System.out.printf ("%f msec%n", (double)(t2 - t1)/(double)n);
		}
	}

}
