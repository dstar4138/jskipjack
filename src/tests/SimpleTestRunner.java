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
		
		int ITTER = 50000;
		
		FastSkipJack skip0 = new FastSkipJack(key);
		System.out.printf("%016x ?= %016x ?= %016x%n", skip0.Encrypt(message), SkipJack.Encrypt(key, message), FastSkipJack.Encrypt(key, message));
		
		long t1 = System.currentTimeMillis();
		for(int i=0; i< ITTER; i++){ SkipJack.Encrypt(key, message); }
		
		long t2 = System.currentTimeMillis();
		for(int i=0; i< ITTER; i++){ FastSkipJack.Encrypt(key, message); }
		
		long t3 = System.currentTimeMillis();
		FastSkipJack skip = new FastSkipJack(key);
		for(int i=0; i< ITTER; i++){ skip.Encrypt(message); }
		
		long t4 = System.currentTimeMillis();
		System.out.printf("Slow: Time=%d msecs%n", t2-t1);
		System.out.printf("Fast: Time=%d msecs%n", t3-t2);
		System.out.printf("Faster: Time=%d msecs%n", t4-t3);
		
		long dc1 = SkipJack.Decrypt(key, SkipJack.Encrypt(key, message));
		long dc2 = FastSkipJack.Decrypt(key, FastSkipJack.Encrypt(key, message));
		FastSkipJack skip1 = new FastSkipJack(key);
		long dc3 = skip1.Decrypt(skip1.Encrypt(message));
		System.out.printf("SlowD: %016x ?= %016x%n", message, dc1);
		System.out.printf("FastD: %016x ?= %016x%n"  , message, dc2);
		System.out.printf("FasterD: %016x ?= %016x%n"  , message, dc3);
		
	}
}