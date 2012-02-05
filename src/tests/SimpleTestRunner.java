package tests;
import cipher.SkipJack; 

public class SimpleTestRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long message = Long.valueOf(args[0], 16);
		short[] key = new short[10];
		for (int i = 0; i < 10; ++i) {
			key[i] = Short.valueOf(args[1].substring(i, i + 2));
		}
		long ctext = SkipJack.Encrypt(key, message);
		//System.out.printf("%016x", ctext);
		
	}

}
