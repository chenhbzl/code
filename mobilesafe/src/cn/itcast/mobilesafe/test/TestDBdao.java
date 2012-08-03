package cn.itcast.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.db.dao.BlackNumberDao;

public class TestDBdao extends AndroidTestCase{
	public void testAdd(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		
		dao.save("123456");
		
		
		for(int i = 10 ;i < 60 ;i++){
			dao.save("135123456"+i);
		}
	}
	
	public void testquery(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		
		assertEquals(true, dao.query("123456")) ;
		
		assertEquals(true, dao.query("1234567")) ;
	}
	
	public void testdelete(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		
		//dao.delete(1);
	}
	
	
	public void testfindAll(){
		BlackNumberDao dao = new BlackNumberDao(getContext());
		
		List<String>  numbers  = dao.findAll();
		
		for (String number: numbers ){
			System.out.println(number);
		}
		
	}
}
