package cipher;

import java.math.BigInteger;
import java.io.*;

public class DecryptFile{

	private static BigInteger MASK = new BigInteger("FF", 16);
	
    public static void main( String[] args ) throws Exception{
    	if(args.length != 3){
    		System.out.println("Usage: java DecryptFile <key> <inputfile> <outputfile>");
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
		short[] key = new short[10];
		for(int i=0,j=9; i<10; ++i,--j){
			key[j]= k.shiftRight(i*8).and(MASK).shortValue();
		}
		
		// Read in blocks from the file until there are no more.
	    try{
	    	while (true){
	    		out.writeLong(SkipJack.Decrypt(key, in.readLong()));
	    	}
	    }catch(EOFException e){
	    	/* Because the input for this is supposed to be 64 bits, this should be the end.
	    	 * Discovery: DataInputStream does not alow moving backward.
	    	 * Must find way to remove padding.
	    	 */
	    }

	    in.close();
	    out.close();
    }
}
