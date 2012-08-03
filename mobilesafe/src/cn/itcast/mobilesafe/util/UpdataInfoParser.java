package cn.itcast.mobilesafe.util;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import cn.itcast.mobilesafe.domain.UpdataInfo;

public class UpdataInfoParser {
	/*
	 * 解析服务器返回的xml文件 
	 */
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{
		XmlPullParser  parser = Xml.newPullParser();  
		//设置解析的数据源 
		parser.setInput(is, "utf-8");
		
		int type = parser.getEventType();
		UpdataInfo info = new UpdataInfo();
		
		while(type != XmlPullParser.END_DOCUMENT ){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					info.setVersion(parser.nextText());
				}else if ("url".equals(parser.getName())){
					info.setUrl(parser.nextText());
				}else if ("description".equals(parser.getName())){
					info.setDescription(parser.nextText());
				}
				break;
			}
			type = parser.next();
		}
		return info;
	}
}
