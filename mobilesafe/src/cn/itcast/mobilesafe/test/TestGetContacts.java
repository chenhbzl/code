package cn.itcast.mobilesafe.test;

import java.util.List;

import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.service.ContactInfoService;
import android.test.AndroidTestCase;

public class TestGetContacts extends AndroidTestCase {
	public void testGetinfo(){
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo>  contacts  = service.getContacts();
		System.out.println(contacts.get(0).getName());
	}
}
