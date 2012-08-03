package cn.itcast.mobilesafe.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.util.SmsUtil;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class BackSmsService extends Service {
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// oncreate 
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		// 备份短信,存文件  
		
		
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 List<SmsInfo> smsInfos =	 SmsUtil.getSmsInfos(getApplicationContext());
				 File file = new File("/sdcard/backsms.xml");
				 try {
					FileOutputStream fos = new FileOutputStream(file);
					 SmsUtil.saveSms(smsInfos, fos);
					 Looper.prepare();
					 Toast.makeText(getApplicationContext(), "备份完成", 1).show();
					 Looper.loop();
				 } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				 super.run();
			}			
		}.start();
	}
}
