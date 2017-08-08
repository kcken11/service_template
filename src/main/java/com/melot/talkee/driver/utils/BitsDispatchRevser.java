package com.melot.talkee.driver.utils;

public class BitsDispatchRevser {

	//TODO: get the index value from config file
	private static int STATIC_MIXBITS_INDEX = 111;
	private static String S_PRIVATE_KEY2 = "@kK1818$";

	public static String RevseDispatch(byte[] pSrcDestBits){
		if(null == pSrcDestBits || pSrcDestBits.length <= 8)
			return null;
		int nIndex = STATIC_MIXBITS_INDEX;
		byte[] dispBytes = new byte[8];
		
		int lenSrcDestBits = 0;
		int secs = (pSrcDestBits.length/20 + (pSrcDestBits.length%20 == 0? 0:1));
		int lenSec = 20;
		for(int i=0; i<secs; i++){
			if(i+1 == secs)
				lenSec = pSrcDestBits.length - i*20;
			int a = my_bits_disp_reverse(pSrcDestBits, 20*i, (lenSec - 8)*8, 
					dispBytes, 8*8, nIndex);
			lenSrcDestBits += a/8 - 8;//8 bytes for S_PRIVATE_KEY2
			String disp = new String(dispBytes);
			if(disp.compareTo(S_PRIVATE_KEY2) != 0)
				return null;
			if(i+1 <secs)
				nIndex = (pSrcDestBits[20*(i+1)-1] & 0xff);
		}
		
		byte[] retStrBytes = new byte[lenSrcDestBits];
		for(int i=0; i<secs; i++){
			for(int j=0; j<12; j++){
				if(j+i*12 >= lenSrcDestBits)
					break;
				retStrBytes[j+i*12] = pSrcDestBits[i*20+j];
			}
		}
		return new String(retStrBytes);
	}
	
	private static int my_bits_disp_reverse(byte[] pSrcDestBits, int nStart, int nSrcBitsLen,
			byte[] pDispBits, int nDispBitsLen, int nIndex)
	{
		int i;
		int j;
		if(null == pSrcDestBits || null == pDispBits || (nSrcBitsLen+nDispBitsLen)<2 ||
			(nSrcBitsLen+nDispBitsLen)>255)
			return -1;
		
		int[] pTemp=new int[nSrcBitsLen+nDispBitsLen];
		for(i=0; i<nSrcBitsLen+nDispBitsLen; i++)
		{
			pTemp[i]=(byte)i;
		}
		
		long n;
		//dispatch pDispBits into pSrcBis
		//using nIndex+(nIndex+(13,21,34,55,...))%nSrcBitsLen select insert point.
		long a=13L;
		long b=21L;
		int s=nDispBitsLen;
		byte v;
		while(nDispBitsLen > 0)
		{
			//use a
			n=(nIndex+(nIndex+a)%nSrcBitsLen)%nSrcBitsLen;
			for(i=nSrcBitsLen; i>n; i--)
			{
				pTemp[i]=pTemp[i-1];
			}
			pTemp[(int) n]=nSrcBitsLen;
			if(nDispBitsLen == 26)
				nDispBitsLen = 26;
			b=a+b;
			if(b > 0xffffffffL)
				b &= 0x00000000ffffffffL;
			a=b-a;
			if(a < 0)
				a &= 0x00000000ffffffffL;
			nSrcBitsLen++;
			nDispBitsLen--;
		}
		nDispBitsLen=s;
		nSrcBitsLen=nSrcBitsLen-nDispBitsLen;
		
		int curDispBits = 0;
		v=0;
		a=0;
		for(j=nSrcBitsLen; j<nDispBitsLen+nSrcBitsLen; j++)
		{
			for(i=0; i<nSrcBitsLen+nDispBitsLen; i++)
			{
				if(j==pTemp[i])
				{
					v|=(((pSrcDestBits[nStart + i/8]&0x0FF)>>(7-i%8))&1)<<(7-a);
					if(a%8==7)
					{
						pDispBits[curDispBits ++ ] = v;
						v=0;
						a=0;
					}
					else
						a++;
					break;
				}
			}
		}
		if(a!=0)
			pDispBits[curDispBits ++ ] = v;
	
		v=0;
		j=0;
		for(i=0; i<nSrcBitsLen+nDispBitsLen; i++)
		{
			if(pTemp[i]<nSrcBitsLen)
			{
				v|=((pSrcDestBits[nStart + i/8]>>(7-i%8))&1)<<(7-j%8);
				if(j%8==7)
				{
					pSrcDestBits[nStart + j/8] = v;
					v=0;
				}
				j++;
			}
		}
		if(j%8!=0)
			pSrcDestBits[nStart + j/8]=v;
		n=(nSrcBitsLen-1)/8+1;
		//change pSrcBits
		for(i=0; i<n; i+=2)
		{
			pSrcDestBits[nStart + i] ^= (byte)nIndex;
		}	
		
		return nSrcBitsLen+nDispBitsLen;
	}
}
