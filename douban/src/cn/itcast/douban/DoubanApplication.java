package cn.itcast.douban;


import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.douban.NoteEntry;

import android.app.Application;
/**
 * access token 5e2dd8b9b2e3eb271a898a2d18abc175
token  secret d1affd23502dad2a
 * @author Administrator
 *
 */
public class DoubanApplication extends Application {
 	public NoteEntry entry;
	// ����(app)�����ʼ��֮ǰ������ 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		String apiKey = "0acd20946740ceb0206761bd35c26c10";
		String secret = "b7feacef6b845643";

		// ���ö���api���ɶ����service
		DoubanService myService = new DoubanService("�ҵ�С����", apiKey,
				secret);
		myService.setAccessToken("5e2dd8b9b2e3eb271a898a2d18abc175", "d1affd23502dad2a");
		MyCrashHandler handler = MyCrashHandler.getInstance();
		handler.init(getApplicationContext(),myService);
		Thread.setDefaultUncaughtExceptionHandler(handler);
		
		
		
	}
}
