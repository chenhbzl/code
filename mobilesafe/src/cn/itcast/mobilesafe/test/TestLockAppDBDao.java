package cn.itcast.mobilesafe.test;

import java.util.List;

import cn.itcast.mobilesafe.db.dao.LockAppDao;
import android.test.AndroidTestCase;

public class TestLockAppDBDao extends AndroidTestCase{
	
	
	public void testAddLockApp(){
		LockAppDao dao = new LockAppDao(getContext());
		dao.save("com.android.mms");
		dao.save("cn.itcast.xxx");
	}
	
	public void testDelete(){
		LockAppDao dao = new LockAppDao(getContext());
		dao.delete("cn.itcast.xxx");
	}
	public void testFindAll(){
		LockAppDao dao = new LockAppDao(getContext());
		List<String> packnames =  dao.findAll();
		for (String packname: packnames){
			System.out.println(packname);
		}
	}
	public void testFind(){
		LockAppDao dao = new LockAppDao(getContext());
		boolean result =dao.find("com.android.mms");
		assertEquals(true, result);
	}
}
