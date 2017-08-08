package com.melot.talkee.driver.utils;

import java.util.Arrays;

public class BitsDispatch {
	//TODO: get the index value from config file
	private static int STATIC_MIXBITS_INDEX = 111;
	private static String S_PRIVATE_KEY2 = "@kK1818$";
	
	public static byte[] encodeUserNameAndPassword(byte[] namepassword, int len){
		if(namepassword == null)//|| len < 14
			return null;
		
		//12 chars for one section
		int secs = (len/12 + (len%12 == 0? 0:1));
		byte[] ppEncoded = new byte[secs * 20];//every sec needs 32 bytes
		int pEncodedLen = 0;
		int mixer = STATIC_MIXBITS_INDEX;
		byte[] p = ppEncoded;
		//every sec length : 12
		int lenSec = 12;
		for(int i=0; i<secs; i++){
	        //last sec length : len - 12 * i
			if(i+1 == secs)
				lenSec = len - i*12;
	        //copy namepassword to p (every section 12 to first 12/20)
			System.arraycopy(namepassword, i*12, p, i*20, lenSec);
 			int a = my_bist_disp(p, i*20, lenSec*8, S_PRIVATE_KEY2.getBytes(), 8*8, mixer);
			pEncodedLen += a/8;
			if(i+1 <secs)
				mixer = (p[(i+1)*20-1] & 0xff);
		}
		return Arrays.copyOfRange(p, 0, pEncodedLen);
	}

	private static int my_bist_disp(byte[] pSrcDestBits, int nstart, int nSrcBitsLen,
			  byte[] pDispBits, int nDispBitsLen,
			  int nIndex) {
		int i;
		if(null == pSrcDestBits || null == pDispBits || (nSrcBitsLen+nDispBitsLen) < 2 ||
				(nSrcBitsLen+nDispBitsLen) > 255) {
			return -1;
		}
		int[] pTemp = new int[nSrcBitsLen+nDispBitsLen];
		for(i = 0; i < nSrcBitsLen + nDispBitsLen; i++)
		{
			pTemp[i] = (byte)i;
		}
		long n = (nSrcBitsLen-1)/8+1;
		//change pSrcBits
		for(i=0; i<n; i+=2)
		{
			pSrcDestBits[nstart + i] ^= (byte)nIndex;
		}
		//dispatch pDispBits into pSrcBis
		//using nIndex+(nIndex+(13,21,34,55,...))%nSrcBitsLen select insert point.
		long a = 13l, b = 21l;
		int s = nDispBitsLen;
		byte v;
		while(nDispBitsLen > 0) {
			//use a
			n=(nIndex+(nIndex+a)%nSrcBitsLen)%nSrcBitsLen;
			for(i = nSrcBitsLen; i > n; i--)
			{
				pTemp[i] = pTemp[i-1];
			}
			pTemp[(int) n] = nSrcBitsLen;
			b = a + b;
			if(b > 0xffffffffL)
				b &= 0x00000000ffffffffL;
			a = b - a;
			if(a < 0)
				a &= 0x00000000ffffffffL;
			nSrcBitsLen++;
			nDispBitsLen--;
		}
		nDispBitsLen = s;
		nSrcBitsLen = nSrcBitsLen - nDispBitsLen;
		//TODO ?
		byte[] pFrom;
		for(i=0; i < nSrcBitsLen + nDispBitsLen; i++)
		{
			if(pTemp[i]>=nSrcBitsLen)
			{
				pFrom = pDispBits;
				pTemp[i] -= nSrcBitsLen;
				v = pFrom[pTemp[i]/8];
				v >>= (7-pTemp[i]%8);
				pTemp[i] = v;
			}
			else
			{
				pFrom = pSrcDestBits;
				v = pFrom[nstart + pTemp[i]/8];
				v >>= (7-pTemp[i]%8);
				pTemp[i] = v;
			}
		}
		pFrom = pSrcDestBits;
		v=0;
		//try
		int currentPFrom = nstart;
		for(i=0; i < nSrcBitsLen + nDispBitsLen; i++)
		{
			v|=(pTemp[i]&1)<<(7-i%8);
			if(i%8==7)
			{
				pFrom[currentPFrom ++]=v;
				v=0;
			}
		}
		if(i%8!=0) {
			pFrom[currentPFrom ++]=v;
		}
		return nSrcBitsLen+nDispBitsLen;
	}
	
}
