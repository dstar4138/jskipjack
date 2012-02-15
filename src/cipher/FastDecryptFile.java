package cipher;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.io.*;

/**
 * Decrypt a file using faster SkipJack. This is 
 * the non-static version of FastSkipJack. It 
 * takes the same parameters as DecryptFile, a 
 * key and the input and output file names.
 */
public class FastDecryptFile{

	private static BigInteger MASK = new BigInteger("FF", 16);

	public static void main( String[] args ) throws Exception{
		if(args.length != 3){
			System.out.println("Usage: java FastDecryptFile <key> <inputfile> <outputfile>");
			System.exit(1);
		}

		// Read in arguments.
		BigInteger k = new BigInteger(args[0],16);
		File input = new File (args[1]);
		File output = new File (args[2]);

		DataInputStream in =
				new DataInputStream
					(new FileInputStream (input));

		DataOutputStream out =
				new DataOutputStream
					(new FileOutputStream (output));

		// Break key into 10 bytes.
		int[] key = new int[10];
		for(int i=0,j=9; i<10; ++i,--j){
			key[j]= k.shiftRight(i*8).and(MASK).shortValue();
		}
		FastSkipJack cipher = new FastSkipJack(key);

		// Read in blocks from the file until there are no more.
		// Decrypt must catch the last block, so only write
		// when the next block is seen.
		long thisBlock = 0;
		long nextBlock = 0;
		try{
			thisBlock = in.readLong();
			while (true){
				nextBlock = in.readLong();
				out.writeLong(cipher.Decrypt(thisBlock));
				thisBlock = nextBlock;
			}
		}catch(EOFException e){
			// Remove padding
			thisBlock = cipher.Decrypt(thisBlock);
			int numDataBytes = 7 - (Long.numberOfTrailingZeros(thisBlock) / 8);
			byte[] finalbytes = ByteBuffer.allocate(8).putLong(thisBlock).array();
			out.write(finalbytes, 0, numDataBytes);
		}

		in.close();
		out.close();
	}
}
