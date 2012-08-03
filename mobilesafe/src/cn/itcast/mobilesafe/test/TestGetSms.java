package cn.itcast.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.util.SmsUtil;

public class TestGetSms extends AndroidTestCase{
	public void testGetSms(){
		
		 List<SmsInfo> infos = SmsUtil.getSmsInfos(getContext());
		
		 for(SmsInfo info : infos){
			 System.out.println(info.getAddress());
			 System.out.println(info.getBody());
		 }
	}
}
