package cipher;

import java.math.BigInteger;

/*
    SKIPJACK - 
        64bit Cookbook version, 80bit key.
 */
public class FastSkipJack {

	long word1, word2, word3, word4;
	int[] key;

	public FastSkipJack( int[] key ){
		this.key = key;
	}
	public long Encrypt( long message ){
		word1 = (message >>> 48);
		word2 = (message >> 32) & 0xFFFFL;
		word3 = (message >> 16) & 0xFFFFL;
		word4 = (message & 0xFFFFL);
		for( int step=0; step<32; ++step){
			round(step);
		}
		return word1 <<48 | word2 <<32 | word3 <<16 | word4;
	}
	public long Decrypt( long message ){
		word1 = (message >>> 48);
		word2 = (message >> 32) & 0xFFFFL;
		word3 = (message >> 16) & 0xFFFFL;
		word4 = (message & 0xFFFFL);
		for( int step=31; step>=0; --step){
			roundPrime(step);
		}
		return word1 <<48 | word2 <<32 | word3 <<16 | word4;
	}
	private void round( int step ){
		long gg;
		int g_1, g_2, g_3, g_4, g_5, g_6, cv0, cv1, cv2, cv3;

		int stepmod=step*4;
		cv0=key[(stepmod) % 10];
		cv1=key[(stepmod + 1) % 10];
		cv2=key[(stepmod + 2) % 10];
		cv3=key[(stepmod + 3) % 10];

		g_1 = (int)(word1 >>> 8);
		g_2 = (int)(word1 & 0xFF);     
		g_3 = (F[g_2 ^ cv0] ^ g_1);
		g_4 = (F[g_3 ^ cv1] ^ g_2);
		g_5 = (F[g_4 ^ cv2] ^ g_3);
		g_6 = (F[g_5 ^ cv3] ^ g_4);
		gg = ((long)g_5 << 8) | g_6;

		if(step<8||(step>15&&step<24)){//if its round A
			word1 = (gg ^ word4 ^ (step + 1));
		word4 = word3;
		word3 = word2;
		word2 = gg;
		}else{
			long old1 = word1;
			word1 = word4;
			word4 = word3;
			word3 = (old1 ^ word2 ^ (step + 1));
			word2 = gg; 
		}
	}
	private void roundPrime( int step ){
		long  gg;
		int g_1, g_2, g_3, g_4, g_5, g_6, cv0, cv1, cv2, cv3;

		int stepmod=step*4;
		cv0=key[(stepmod + 3) % 10];
		cv1=key[(stepmod + 2) % 10];
		cv2=key[(stepmod + 1) % 10];
		cv3=key[(stepmod) % 10];

		g_1 = (int) (word2 & 0xFF);
		g_2 = (int) (word2 >>> 8);     
		g_3 = (F[g_2 ^ cv0] ^ g_1);
		g_4 = (F[g_3 ^ cv1] ^ g_2);
		g_5 = (F[g_4 ^ cv2] ^ g_3);
		g_6 = (F[g_5 ^ cv3] ^ g_4);
		gg = ((long)g_6 << 8) | g_5;

		if(step<8||(step>15&&step<24)){//if its round A
			long old4 =word4;
		word4 = (word1 ^ word2 ^ (step+1));
		word1 = gg;
		word2 = word3;
		word3 = old4;
		}else{
			long old1 = word1;
			word1 = gg;
			word2 = (gg ^ word3 ^ (step + 1));
			word3 = word4;
			word4 = old1;
		}
	}

	/********* STATIC, SLOWER *********/

	public static long Encrypt( int[] key, long message ){
		long state = message;
		for( int step=0; step<32; ++step){
			state = round(step, key, state);
		}
		return state;        
	}      
	private static long round( int step, int[] key, long block){
		long w_1i, w_2i, w_3i, w_4i, w_2o;
		int g_1, g_2, g_3, g_4, g_5, g_6, cv0, cv1, cv2, cv3;

		w_1i = w_2i = w_3i = w_4i = w_2o  = 0;
		g_1 = g_2 = g_3 = g_4 = g_5 = g_6 = cv0 = cv1 = cv2 = cv3 = 0;

		int stepmod=step*4;
		cv0=key[(stepmod) % 10];
		cv1=key[(stepmod + 1) % 10];
		cv2=key[(stepmod + 2) % 10];
		cv3=key[(stepmod + 3) % 10];

		w_1i = (block >>> 48);
		w_2i = ((block >> 32) & 0xFFFFL);
		w_3i = ((block >> 16) & 0xFFFFL);
		w_4i = (block & 0xFFFFL);

		g_1 = (int) (w_1i >>> 8);
		g_2 = (int) (w_1i & 0xFF);     
		g_3 = (F[g_2 ^ cv0] ^ g_1);
		g_4 = (F[g_3 ^ cv1] ^ g_2);
		g_5 = (F[g_4 ^ cv2] ^ g_3);
		g_6 = (F[g_5 ^ cv3] ^ g_4);
		w_2o = ((long)g_5 << 8) | g_6;

		if(step<8||(step>15&&step<24)){//if its round A
			return (w_2o ^ w_4i ^ (step + 1)) << 48 | w_2o << 32 | w_2i << 16 | w_3i;
		}else{
			return w_4i << 48 | w_2o << 32 | (w_1i ^ w_2i ^ (step + 1)) << 16 | w_3i;
		}
	}

	public static long Decrypt( int[] key, long message ){
		long state = message;
		for( int step=31; step>=0; --step){
			state=roundPrime(step,key,state);
		}
		return state;
	}
	private static long roundPrime( int step, int[] key, long block){
		long w_1i, w_2i, w_3i, w_4i, w_2o;
		int g_1, g_2, g_3, g_4, g_5, g_6, cv0, cv1, cv2, cv3;

		w_1i = w_2i = w_3i = w_4i = w_2o  = 0;
		g_1 = g_2 = g_3 = g_4 = g_5 = g_6 = cv0 = cv1 = cv2 = cv3 = 0;

		int stepmod=step*4;
		cv0=key[(stepmod + 3) % 10];
		cv1=key[(stepmod + 2) % 10];
		cv2=key[(stepmod + 1) % 10];
		cv3=key[(stepmod) % 10];

		w_1i = (block >>> 48);
		w_2i = ((block >> 32) & 0xFFFFL);
		w_3i = ((block >> 16) & 0xFFFFL);
		w_4i = (block & 0xFFFFL);

		g_1 = (int) (w_2i & 0xFF);
		g_2 = (int) (w_2i >>> 8);     
		g_3 = (F[g_2 ^ cv0] ^ g_1);
		g_4 = (F[g_3 ^ cv1] ^ g_2);
		g_5 = (F[g_4 ^ cv2] ^ g_3);
		g_6 = (F[g_5 ^ cv3] ^ g_4);
		w_2o = ((long)g_6 << 8) | g_5;

		if(step<8||(step>15&&step<24)){//if its round A
			return w_2o << 48 | w_3i << 32 | w_4i << 16 | (w_1i ^ w_2i ^ (step+1));
		}else{
			return w_2o << 48 | (w_2o ^ w_3i ^ (step + 1)) << 32 | w_4i << 16 | w_1i;
		}
	}

	// F-Table
	public static final int[] F = new int[]{
		0xa3,0xd7,0x09,0x83,0xf8,0x48,0xf6,0xf4,0xb3,0x21,0x15,0x78,0x99,0xb1,0xaf,0xf9,
		0xe7,0x2d,0x4d,0x8a,0xce,0x4c,0xca,0x2e,0x52,0x95,0xd9,0x1e,0x4e,0x38,0x44,0x28,
		0x0a,0xdf,0x02,0xa0,0x17,0xf1,0x60,0x68,0x12,0xb7,0x7a,0xc3,0xe9,0xfa,0x3d,0x53,
		0x96,0x84,0x6b,0xba,0xf2,0x63,0x9a,0x19,0x7c,0xae,0xe5,0xf5,0xf7,0x16,0x6a,0xa2,
		0x39,0xb6,0x7b,0x0f,0xc1,0x93,0x81,0x1b,0xee,0xb4,0x1a,0xea,0xd0,0x91,0x2f,0xb8,
		0x55,0xb9,0xda,0x85,0x3f,0x41,0xbf,0xe0,0x5a,0x58,0x80,0x5f,0x66,0x0b,0xd8,0x90,
		0x35,0xd5,0xc0,0xa7,0x33,0x06,0x65,0x69,0x45,0x00,0x94,0x56,0x6d,0x98,0x9b,0x76,
		0x97,0xfc,0xb2,0xc2,0xb0,0xfe,0xdb,0x20,0xe1,0xeb,0xd6,0xe4,0xdd,0x47,0x4a,0x1d,
		0x42,0xed,0x9e,0x6e,0x49,0x3c,0xcd,0x43,0x27,0xd2,0x07,0xd4,0xde,0xc7,0x67,0x18,
		0x89,0xcb,0x30,0x1f,0x8d,0xc6,0x8f,0xaa,0xc8,0x74,0xdc,0xc9,0x5d,0x5c,0x31,0xa4,
		0x70,0x88,0x61,0x2c,0x9f,0x0d,0x2b,0x87,0x50,0x82,0x54,0x64,0x26,0x7d,0x03,0x40,
		0x34,0x4b,0x1c,0x73,0xd1,0xc4,0xfd,0x3b,0xcc,0xfb,0x7f,0xab,0xe6,0x3e,0x5b,0xa5,
		0xad,0x04,0x23,0x9c,0x14,0x51,0x22,0xf0,0x29,0x79,0x71,0x7e,0xff,0x8c,0x0e,0xe2,
		0x0c,0xef,0xbc,0x72,0x75,0x6f,0x37,0xa1,0xec,0xd3,0x8e,0x62,0x8b,0x86,0x10,0xe8,
		0x08,0x77,0x11,0xbe,0x92,0x4f,0x24,0xc5,0x32,0x36,0x9d,0xcf,0xf3,0xa6,0xbb,0xac,
		0x5e,0x6c,0xa9,0x13,0x57,0x25,0xb5,0xe3,0xbd,0xa8,0x3a,0x01,0x05,0x59,0x2a,0x46
	};



	public static void main( String[] args ){
		if(args.length != 2){
			System.out.println("Usage: java FastSkipJack <key> <plaintext>");
			System.exit(1);
		}
		BigInteger MASK = new BigInteger("FF", 16);
		BigInteger k = new BigInteger(args[0],16);
		long message = Long.valueOf(args[1], 16);

		int[] key = new int[10];
		for(int i=0,j=9; i<10; ++i,--j){
			key[j]= k.shiftRight(i*8).and(MASK).shortValue();
		}

		FastSkipJack fsj = new FastSkipJack(key);
		System.out.printf("%016x%n",fsj.Encrypt(message));
	}
}

