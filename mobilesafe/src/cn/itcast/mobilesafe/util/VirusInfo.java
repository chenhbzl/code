package cn.itcast.mobilesafe.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import cn.itcast.mobilesafe.domain.VirusBean;

import android.util.Xml;

public class VirusInfo {
	public static List<VirusBean> getVirusInfos(InputStream is) throws Exception{
		XmlPullParser  parser =  	Xml.newPullParser();
		List<VirusBean> viruss = null;
		VirusBean virus = null;
		parser.setInput(is, "UTF-8");
	   int type = 	parser.getEventType();
		while(type!=XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("list".equals(parser.getName())){
					viruss = new ArrayList<VirusBean>();
				}else if("name".equals(parser.getName())){
					virus = new VirusBean();
					virus.setName(parser.nextText());
				}else if("packname".equals(parser.getName())){
					virus.setPackname(parser.nextText());
				}else if("description".equals(parser.getName())){
					virus.setDescription(parser.nextText());
				}
				else if("signature".equals(parser.getName())){
					virus.setSignature(parser.nextText());
				}
				break;
			case XmlPullParser.END_TAG:
				if("virus".equals(parser.getName())){
					viruss.add(virus);
					virus = null;
				}
			}			
			type = parser.next();
		}
		return viruss;
	}
}
