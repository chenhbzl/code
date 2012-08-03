package cn.itcast.mobilesafe.util;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import cn.itcast.mobilesafe.domain.SmsInfo;

public class SmsUtil {
	public static final String path = "content://sms/";
	public static List<SmsInfo> getSmsInfos(Context context){
		// ��ȡ�����������ṩ�ߵ�uri
		Uri uri = Uri.parse(path);
		ContentResolver  cr = context.getContentResolver(); 
		
		//_id , address , body ,date ,type 
		List<SmsInfo> smsinfos = new ArrayList<SmsInfo>();
		Cursor cursor = cr.query(uri, new String[]{"_id","address","body","date","type"}, 
				null, 
				null,			
				"date desc");
		
		while(cursor.moveToNext()){
			SmsInfo info = new SmsInfo();
			String address = cursor.getString(cursor.getColumnIndex("address"));
			info.setAddress(address);
			String body = cursor.getString(cursor.getColumnIndex("body"));
			info.setBody(body);
			String date = cursor.getString(cursor.getColumnIndex("date"));
			//�����ַ��������ڸ�ʽ���� 
			SimpleDateFormat dataFormater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date d = new Date(Long.parseLong(date));
			String strdate = dataFormater.format(d);
			info.setDate(strdate);
			String type = cursor.getString(cursor.getColumnIndex("type"));
			if("1".equals(type)){
				info.setType("����");
			}else if("2".equals(type)){
				info.setType("����");
			}
			smsinfos.add(info);
			
		}		
		return smsinfos;
	}
	
	/*
	 * ����ϵ�˵ļ�����Ϣ,��xml�ĸ�ʽ,д��һ��������� 
	 */
	public static void saveSms(List<SmsInfo> infos , OutputStream os) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		serializer.setOutput(os, "UTF-8");
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "smss");
		for (SmsInfo info : infos){
			serializer.startTag(null, "sms");
			serializer.startTag(null, "address");
			serializer.text(info.getAddress());
			serializer.endTag(null, "address");
			
			serializer.startTag(null, "body");
			serializer.text(info.getBody());
			serializer.endTag(null, "body");
			
			
			serializer.startTag(null, "date");
			serializer.text(info.getDate());
			serializer.endTag(null, "date");
			
			serializer.startTag(null, "type");
			serializer.text(info.getType());
			serializer.endTag(null, "type");
			serializer.endTag(null, "sms");
		}
		serializer.endTag(null, "smss");
		serializer.endDocument();
		
		//ע��  һ��Ҫflush��� ����� 
		os.flush();
		os.close();
	}
}
