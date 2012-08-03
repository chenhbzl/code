package cn.itcast.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.EnterPasswordActivity;
import cn.itcast.mobilesafe.db.dao.LockAppDao;
import cn.itcast.mobilesafe.domain.LockAppInfo;
import cn.itcast.mobilesafe.inter.ILockAddressService;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class LockAppService extends Service {
	protected static final String TAG = "LockAppService";
	public static boolean flagservicestart = true;
	ActivityManager activityManager;
	Intent intent;
	LockAppDao lockAppDao;
	List<String> blockapppacks;
	List<LockAppInfo> blockappinfos;
	
	
	public class LockAppBinder extends Binder implements ILockAddressService {

		public void stopLockAppFlag(String packname) {
			// TODO Auto-generated method stub
			stopLockService(packname);
		}

		public void startLockAppFlag(String packname) {
			// TODO Auto-generated method stub
			startLockService(packname);
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new LockAppBinder();
	}

	@Override
	public void onCreate() {
		lockAppDao = new LockAppDao(getApplicationContext());
		blockapppacks = lockAppDao.findAll();
		blockappinfos = new ArrayList<LockAppInfo>();
		fillData(blockapppacks);
		
		
		// TODO Auto-generated method stub
		// �õ�activity�Ĺ�����
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		intent = new Intent(this, EnterPasswordActivity.class);
		// ���µ�����ջ�д��� activity��ʵ��
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		new Thread() {
			@Override
			public void run() {
				// ���Ź�, ��ͣ�Ĳ鿴��ǰactivity����ջ��ջ��
				while (true) {
					// ���Ȼ�ȡ�������������ջ, get(0) ��ȡ������ջջ����activity
					String packname = activityManager.getRunningTasks(1).get(0).topActivity
							.getPackageName();
					
					for (LockAppInfo apppackinfo : blockappinfos) {

						if (packname.equals(apppackinfo.getPackname())) {
							if (!apppackinfo.isFlagcanstart()) {
								// �����Ի���,�����µ�activity ���ǵ�ǰҪ������activity
								intent.putExtra("packagename", packname);
								startActivity(intent);
							}
						}
					}
				}
			}
		}.start();
		super.onCreate();
	}

	private void fillData(List<String> blockapppacks2) {
		// TODO Auto-generated method stub
		for(String blockapp :blockapppacks2 ){
			LockAppInfo info = new LockAppInfo();
			info.setPackname(blockapp);
			info.setFlagcanstart(false);
			blockappinfos.add(info);
		}
	}

	public void stopLockService(String packname) {
		//flagservicestart = false;
		for(LockAppInfo info : blockappinfos){
			if(info.getPackname().equals(packname)){
				info.setFlagcanstart(true);
			}
		}
	}
	public void startLockService(String packname) {
	//	flagservicestart = true;
		for(LockAppInfo info : blockappinfos){
			if(info.getPackname().equals(packname)){
				info.setFlagcanstart(false);
			}
		}
	}
}
