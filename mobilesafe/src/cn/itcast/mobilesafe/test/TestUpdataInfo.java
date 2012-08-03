package cn.itcast.mobilesafe.test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.itcast.mobilesafe.domain.UpdataInfo;
import cn.itcast.mobilesafe.util.UpdataInfoParser;
import android.test.AndroidTestCase;

public class TestUpdataInfo extends AndroidTestCase {
	
	public void testGetUpdataInfo() throws Exception{
		String path = "http://192.168.1.247:8080/updata.xml";
		UpdataInfo info;

			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection(); 
			InputStream is =	conn.getInputStream(); 
			info = UpdataInfoParser.getUpdataInfo(is);


		     assertEquals("3.0", info.getVersion());

	}

}
