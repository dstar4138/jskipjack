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
    	BigInteger k = new BigInteger(args[1],16);
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
	    		out.writeLong(SkipJack.Encrypt(key, in.readLong()));
	    	}
	    }catch(EOFException e){
	    	// There is a possibility that it didn't grab everything.
	    	long block=0;
	    	byte b=0;
	    	try{
	    		while(true){ b=in.readByte(); block=block<<8; block|=b; }
	    	}catch(EOFException e2){}
	    	out.writeLong(SkipJack.Encrypt(key, block));
	    }

	    in.close();
	    out.close();
    }
}
