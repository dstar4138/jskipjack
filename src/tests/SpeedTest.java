package tests;

import cipher.FastSkipJack;
import cipher.SkipJack;

public class SpeedTest {

	/**
	 * Run Fast or Slow SkipJack encryption over a static plain-text 
	 * repeatedly for N number of times. N is specified by the user.
	 * The time it took to calculate N encryptions is divided by N and
	 * printed, the result is the Average time (in milliseconds it takes
	 * to encrypt a plain-text.
	 *  
	 * @param args The number of iterations, and whether the slow version
	 * 		should run.
	 */
	public static void main(String[] args) {
		int[] key = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		long plaintext = 0L;
		
		if (args.length < 1) {
			System.out.println("Usage: java SpeedTest [--slow] <num_iterations>");
			System.exit(1);
			
		} else if( args[0] == "--slow") { // RUN SLOW SKIPJACK
			int n = Integer.parseInt(args[1]);
		    long t1 = System.currentTimeMillis();
			for (int i = 0; i < n; ++i) {
				SkipJack.Encrypt(key, plaintext);
			}
		    long t2 = System.currentTimeMillis();
		    System.out.printf ("%f msec%n", (double)(t2 - t1)/(double)n);
		    
		} else { // RUN FAST SKIPJACK
			int n = Integer.parseInt(args[0]);
			
		    long t1 = System.currentTimeMillis();
			FastSkipJack cipher = new FastSkipJack(key);
			for (int i = 0; i < n; ++i) {
				cipher.Encrypt(plaintext);
			}
		    long t2 = System.currentTimeMillis();
		    
		    System.out.printf ("%f msec%n", (double)(t2 - t1)/(double)n);
		}
	}

}
