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
 * ��ȡ��ϵ����Ϣ
 * @author Administrator
 *
 */
public class ContactInfoService {
	private Context context;
	
	public ContactInfoService(Context context) {
		this.context = context;
	}

	/*
	 * ��ȡ��ϵ����Ϣ
	 * 
	 */
	   public List<ContactInfo> getContacts(){
		 List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		   //��ȡϵͳ��contentresolver
		 ContentResolver resolver = context.getContentResolver();
		
		 Cursor cur =  resolver.query(ContactsContract.Contacts.CONTENT_URI, 
				   null, 
				   null, 
				   null, 
				   null);
		//ѭ����������� 
		 while(cur.moveToNext()){
			 ContactInfo contact = new ContactInfo();
			 //1. ��ȡ����ϵ������  
			 
			 //name�ֶζ�Ӧ�ı��еڼ���
			 int nameFiledIndex = cur.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			 String name = cur.getString(nameFiledIndex);
			 contact.setName(name);
			 
			 //2. ��ȡ����ϵ�˶�Ӧ��id
			 int idIndex =  cur.getColumnIndex(ContactsContract.Contacts._ID);
			 String id = cur.getString(idIndex);
			 //3. ��ȡ����ϵ�˵ĺ��� 
			 Cursor phone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					 null, 
					 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +id, 
					 null, 
					 null);
			 
			 while(phone.moveToNext()){
				int numberindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			    String number =	phone.getString(numberindex);
			    
			    //��ȡ�绰������ 
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
