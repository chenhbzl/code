package cn.itcast.mobilesafe;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;


public class MobileSafeApp extends Application {

	RunningAppProcessInfo info;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public RunningAppProcessInfo getInfo() {
		return info;
	}

	public void setInfo(RunningAppProcessInfo info) {
		this.info = info;
	}
}
