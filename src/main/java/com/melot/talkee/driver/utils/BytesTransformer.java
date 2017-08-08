package com.melot.talkee.driver.utils;

public class BytesTransformer {
	
	
	//TODO: get the map from config file
	private static byte eto32_table1[]=("AB56DE3C8L2WF4UVM7JRSGPQYZTXK9HN").getBytes();
	private static byte eto32_table2[]=("2WF4JZ7XKTC8LSGHUDEPQYVM9R63NAB5").getBytes();
	
	public static int EBT_FOR_SIGN = 1;
	public static int EBT_FOR_USERPASSWORD = 2;
	
	private byte eto32_table[] = null;
	public BytesTransformer(int ts){
		if(ts == EBT_FOR_SIGN)
			eto32_table = eto32_table1;
		else 
			eto32_table = eto32_table2;
	}

	public boolean encode(byte[] in, int ilen, 
			byte[] out, int olen)
	{
		if(in == null || out == null)
			return false;
		int s=0;
		int j=0;
		int buf[]={0,0};
		for(int i=0; i<ilen; i++)
		{
			buf[0]=in[i];
			buf[1]=i+1<ilen?in[i+1]:0;
			buf[0] <<= s; buf[0] &= 0xff; buf[0]>>=s;	//clear before s 
			if(s>=3)
			{
				buf[0]=(byte)((((buf[0]&0x0FF)<<(s-3))|((buf[1]&0x0FF)>>(11-s))) & 0xff);
				out[j++]=eto32_table[buf[0]];
				s=s-3;
			}
			else
			{
				out[j++]=eto32_table[buf[0]>>(8-s-5)];
				s+=5;
				i--;	//still consume this byte
			}
			if(j>=olen)
				break;
		}
		return true;
	}
	
	public boolean decode(byte[] in , int ilen, int sb, 
			byte[] out, int olen)
	{
		//sb --> 0-4
		int left=0;//left --> 0-7
		int m;
		
		if(sb>=5)
			return false;
		int i=0;
		int j=0;
		while(i<ilen)
		{		
			for(m=0; m<32; m++) 
			{	
				if(in[i]==eto32_table[m]) 
					break;
			}
			if(m==32)
				return false;
			while(sb<5)
			{
				if((m&((int)1<<(4-sb))) != 0)
					out[j]|=((int)1<<(7-left));
				left++;
				if(left==8)
				{
					left=0;
					j++;
					if(j>=olen)
						return true;//false
				}
				sb++;
			}
			i++;
			sb=0;
		}
		return true;
	}
	
}
