package tests;
import java.math.BigInteger;
import java.util.Arrays;

import cipher.SkipJack; 

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
	
		System.out.println("key="+Arrays.toString(key));
		System.out.printf("Ciphertext: %016x%n",SkipJack.Encrypt(key, message));
	}
}