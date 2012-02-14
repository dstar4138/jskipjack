package cipher;

import java.math.BigInteger;
import java.io.*;

public class EncryptFile{

	private static BigInteger MASK = new BigInteger("FF", 16);
	
    public static void main( String[] args ) throws Exception{
    	if(args.length != 3){
    		System.out.println("Usage: java EncryptFile <key> <inputfile> <outputfile>");
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
		long block = 0;
    	int counter = 0;
	    try{
	    	while (true){
	    		block |= in.readUnsignedByte();
	    		counter++;
	    		if(counter==8){
	    			out.writeLong(SkipJack.Encrypt(key, block));
	    			counter=0; block=0;
	    		}else{
	    			block = block<<8;
	    		}
	    	}
	    }catch(EOFException e){
	    	// Add padding
	    	block |= 0x80;
	    	counter++;
	    	while (counter < 8) {
	    		block = block << 8;
	    		++counter;
	    	}
	    	out.writeLong(SkipJack.Encrypt(key, block));
	    }

	    in.close();
	    out.close();
    }
}
