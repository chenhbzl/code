package cn.itcast.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import cn.itcast.mobilesafe.domain.ContactInfo;

/**
 * 获取联系人信息
 * @author Administrator
 *
 */
public class ContactInfoService {
	private Context context;
	
	public ContactInfoService(Context context) {
		this.context = context;
	}

	/*
	 * 获取联系人信息
	 * 
	 */
	   public List<ContactInfo> getContacts(){
		 List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		   //获取系统的contentresolver
		 ContentResolver resolver = context.getContentResolver();
		
		 Cursor cur =  resolver.query(ContactsContract.Contacts.CONTENT_URI, 
				   null, 
				   null, 
				   null, 
				   null);
		//循环遍历结果集 
		 while(cur.moveToNext()){
			 ContactInfo contact = new ContactInfo();
			 //1. 获取到联系人名字  
			 
			 //name字段对应的表中第几项
			 int nameFiledIndex = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			 String name = cur.getString(nameFiledIndex);
			 contact.setName(name);
			 
			 //2. 获取到联系人对应的id
			 int idIndex =  cur.getColumnIndex(ContactsContract.Contacts._ID);
			 String id = cur.getString(idIndex);
			 //3. 获取到联系人的号码 
			 Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					 null, 
					 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +id, 
					 null, 
					 null);
			 
			 while(phone.moveToNext()){
				int numberindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			    String number =	phone.getString(numberindex);
			    
			    //获取电话的类型 
			    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
			    String type = phone.getString(typeindex);
			    contact.setNumber(number);
			 }
			 phone.close();
			 contacts.add(contact);
		 }
		 cur.close();
         return contacts;
	}
}
