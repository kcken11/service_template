package com.melot.talkee.driver.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;

public class PasswordFunctions {
	
	public static String upd(String up){
		BytesTransformer bts = new BytesTransformer(BytesTransformer.EBT_FOR_USERPASSWORD);
		byte[] upb = up.getBytes();
		int decodedLen = upb.length*5 / 8;
		byte[] rs = new byte[decodedLen];
		if(bts.decode(upb, upb.length, 0, rs, decodedLen)){
			return BitsDispatchRevser.RevseDispatch(rs);
		}
		return null;
	}
	
	/**
	 * 对u=&p=加密成up
	 * @param up
	 * @return
	 */
	public static String upEncode(String up) {
		byte[] upb = BitsDispatch.encodeUserNameAndPassword(up.getBytes(), up.length());
		//byte[] upb = {18, 51, 10, -13, -88, 70, 12, 101, 2, -94, 77, 66, 70, -118, 118, -110, -40, 37, 120, 106, 5, -123, 96, 82, -62, 82, 24, -123, 79, 57, -102, -51, 80, 120, 67, 42, -47, -29};
		BytesTransformer bts = new BytesTransformer(BytesTransformer.EBT_FOR_USERPASSWORD);
		int encodeLen = upb.length * 8 / 5 + ((upb.length*8)%5 > 0 ? 1 : 0);
		byte[] rs = new byte[encodeLen];
		if (bts.encode(upb, upb.length, rs, encodeLen)) {
			return new String(rs);
		}
		return null;
	}
	
	/**
	 * 解密userid/password组合字符串,生成JsonObject
	 * @param input MTY3MTYxMTcxMTY3MTcxMTY5MTczMTcwMTY4MjAxMjE3MTY1MTczMTg3
	 * @return {"userId":2167698,"password":"3Yd58K"}
	 * @throws Exception
	 */
	public static JsonObject decryptUDPD(String input) throws Exception {
		JsonObject jsonObj = new JsonObject();
		StringBuffer output = new StringBuffer();
		String decodeStr = new String(new Base64().decode(input.getBytes()));
		int idx = 0;
		while(decodeStr.length()>0) {
			String s = decodeStr.substring(0, 3);
			int i = Integer.parseInt(s);
			if(idx%2==0) {
				output.append((char)(i-'u'));
			} else {
				output.append((char)(i-'p'));
			}
			decodeStr = decodeStr.substring(3, decodeStr.length());
			idx++;
		}
		String[] str = output.toString().split(":");
		if (StringUtils.isNotBlank(str[0])) {
			jsonObj.addProperty("userId", Integer.valueOf(str[0]));
		}
		if (StringUtils.isNotBlank(str[1])) {
			jsonObj.addProperty("password", str[1]);
		}
	    return jsonObj;
	}

	/**
	 * 对userid/password的JsonObject组合加密
	 * @param jsonObject {"userId":2167698,"password":"3Yd58K"}
	 * @return MTY3MTYxMTcxMTY3MTcxMTY5MTczMTcwMTY4MjAxMjE3MTY1MTczMTg3
	 * @throws Exception
	 */
	public static String encryptUDPD(JsonObject jsonObject) throws Exception {
		Integer userId = jsonObject.get("userId").getAsInt();
		String password = jsonObject.get("password").getAsString();
		String input = userId+":"+password;
		StringBuffer output = new StringBuffer();
		int idx = 0;
		char[] charArr = input.toCharArray();
		for (char c : charArr) {
			if(idx%2==0) {
				output.append(('u'+c));
			} else {
				output.append(('p'+c));
			}
			idx++;
		}
		return new String(new Base64().encode(output.toString().getBytes()));
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(decryptUDPD("MTY5MTYzMTY3MTcwMjIxMTY4MTcwMTYxMTY5MTY3"));

		String input = ":666666a";
		System.out.println(input);
        StringBuffer output = new StringBuffer();
        int idx = 0;
        char[] charArr = input.toCharArray();
        for (char c : charArr) {
            if(idx%2==0) {
                output.append(('u'+c));
            } else {
                output.append(('p'+c));
            }
            idx++;
        }
        System.out.println(output.toString());
        System.out.println(new String(new Base64().encode(output.toString().getBytes()))); ;
	}
}
