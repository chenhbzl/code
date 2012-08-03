package cn.itcast.mobilesafe.util;

import java.security.MessageDigest;

public class MD5Encoder {
	private static final char HEX_DIG[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
	};
	public static String getMD5code(String string) throws Exception{
		MessageDigest  md5 = MessageDigest.getInstance("MD5");
		byte[] resource  = string.getBytes();
		byte[] result = md5.digest(resource);
		return getHexString(result);
	}
	static String getHexString(byte[] b){
		StringBuilder sb = new StringBuilder();
		for(int i= 0; i<b.length;i++){
			 sb.append( HEX_DIG[b[i]& 0xf0 >>>4]);
			 sb.append( HEX_DIG[b[i]& 0x0f]);
		}
		return sb.toString();
	}
}
