package tests;

import java.math.BigInteger;
import cipher.*;

public class SimpleTestRunner {

	/**
	 * @param args First one is plaintext, second is key.
	 */
	public static void main(String[] args) {
		BigInteger MASK = new BigInteger("FF", 16);
		
		long message = Long.valueOf(args[0], 16);
		BigInteger k = new BigInteger(args[1],16);
		
		short[] key = new short[10];
		for(int i=0,j=9; i<10; ++i,--j){
			key[j]= k.shiftRight(i*8).and(MASK).shortValue();
		}
		
		
		long t1 = System.currentTimeMillis();
		for(int i=0; i< 100000; i++){ SkipJack.Encrypt(key, message); }
		long t2 = System.currentTimeMillis();
		for(int i=0; i< 100000; i++){ FastSkipJack.Encrypt(key, message); }
		long t3 = System.currentTimeMillis();
		System.out.printf("Slow: Time=%d msecs%n", t2-t1);
		System.out.printf("Fast: Time=%d msecs%n", t3-t2);
	}
}