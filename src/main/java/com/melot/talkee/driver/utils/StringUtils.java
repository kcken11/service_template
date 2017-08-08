package com.melot.talkee.driver.utils;



/**
 * 字符串工具类。
 * 
 */
public abstract class StringUtils {

    private static String[][]htmlEncodeChars = {{"&","&amp;"},{"\"","&quot;"},{"\"","&quot;"},{"'","&#039;"},{"<","&lt;"},{">","&gt;"}};
    
    
	private StringUtils() {}

	/**
	 * 检查指定的字符串是否为空。
	 * <ul>
	 * <li>SysUtils.isEmpty(null) = true</li>
	 * <li>SysUtils.isEmpty("") = true</li>
	 * <li>SysUtils.isEmpty("   ") = true</li>
	 * <li>SysUtils.isEmpty("abc") = false</li>
	 * </ul>
	 * 
	 * @param value 待检查的字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(value.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查对象是否为数字型字符串,包含负数开头的。
	 */
	public static boolean isNumeric(Object obj) {
		if (obj == null) {
			return false;
		}
		char[] chars = obj.toString().toCharArray();
		int length = chars.length;
		if(length < 1){
			return false;
		}
		int i = 0;
		if(length > 1 && chars[0] == '-'){
			i = 1;
		}
		for (; i < length; i++) {
			if (!Character.isDigit(chars[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 检查指定的字符串列表是否不为空。
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 把通用字符编码的字符串转化为汉字编码。
	 */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isEmpty(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}

	public static String toUnderlineStyle(String name) {
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				if (i > 0) {
					newName.append("_");
				}
				newName.append(Character.toLowerCase(c));
			} else {
				newName.append(c);
			}
		}
		return newName.toString();
	}

	public static String convertString(byte[] data, int offset, int length) {
		if (data == null) {
			return null;
		} else {
			try {
				return new String(data, offset, length, "UTF-8");
			} catch (Exception e) {
				return null;
			}
		}
	}

	public static byte[] convertBytes(String data) {
		if (data == null) {
			return null;
		} else {
			try {
				return data.getBytes("UTF-8");
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static String toCamelName(String name){
		   StringBuilder result = new StringBuilder(); 
		   if (name == null || name.isEmpty()) { 
		   		return ""; 
		    } else if (!name.contains("_")) { 
		    	return name.substring(0, 1).toLowerCase() + name.substring(1); 
		    } 
		   String camels[] = name.split("_"); 
		    for (String camel :  camels) { 
		    	if (camel.isEmpty()) { 
		            continue; 
		        } 
		    	if (result.length() == 0) { 
		    		result.append(camel.toLowerCase()); 
		        } else { 
		        	result.append(camel.substring(0, 1).toUpperCase()); 
		            result.append(camel.substring(1).toLowerCase()); 
		        } 
		    } 
		    return result.toString(); 
	}
	
	public static String getFirstLowName(String name){
		return name.substring(0,1).toLowerCase() + name.substring(1);
	}

	
	public static String getFirstHighName(String name){
		return name.substring(0,1).toUpperCase()+ name.substring(1);
	}
	
	
	/**
	 * 替换掉字符串里的html六个特殊字符 
	 * 
	 */
	public static String htmlEncode(String sSource) {
	    String result = sSource;
	    for (String[] strings : htmlEncodeChars) {
	        result = result.replace(strings[0], strings[1]);
        }
	    return result;
	}
	
	/**
     * 替换掉字符串里的html六个特殊字符 
     * 
     */
    public static String htmlDecode(String sSource) {
        String result = sSource;
        for (String[] strings : htmlEncodeChars) {
            result = result.replace(strings[1], strings[0]);
        }
        return result;
    }
	
	
}
