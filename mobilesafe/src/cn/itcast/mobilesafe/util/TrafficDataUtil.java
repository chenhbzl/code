package cn.itcast.mobilesafe.util;

import java.text.DecimalFormat;

public class TrafficDataUtil {
	public static String getDataString(long total){
		//###.00 
		DecimalFormat df = new DecimalFormat("###.00");
		if(total==-1){
			return "0";
		}else if(total <1024){
			return total+"byte";
		}else if(total <1024*1024){
			return  df.format(total/1024f) +"KB";
		}else if(total <1024*1024*1024){
			return  df.format(total/1024f/1024f) +"MB";
		}else if(total <1024*1024*1024*1024){
			return  df.format(total/1024f/1024f/1024f) +"GB";
		}
		return "³¬³öÍ³¼Æ·¶Î§";
	}
}
