package cn.itcast.mobilesafe.service;

import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.TaskManagerActivity;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.MemoryInfo;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	TimerTask task;
	ActivityManager am;
	Timer timer;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		timer = new Timer();
		// TODO Auto-generated method stub
		task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_layout); 
				am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
				MemoryInfo outInfo =	new ActivityManager.MemoryInfo();
				am.getMemoryInfo(outInfo);
				long availMemorySize = outInfo.availMem;
				String strMemorySize = availMemorySize/1024/1024 + "MB";
				rv.setTextViewText(R.id.tv_widget,"剩余系统内存为 "+ strMemorySize );
				Intent intent = new Intent(getApplicationContext(), TaskManagerActivity.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
				
				
				rv.setOnClickPendingIntent(R.id.tv_widget, pendingIntent);
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
				appWidgetManager.updateAppWidget(new ComponentName(getApplicationContext().getPackageName(), "cn.itcast.mobilesafe.widget.TaskWidget"), rv);
				
			}
		};
		timer.schedule(task, 1000, 3000);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer = null;
		super.onDestroy();
	}

}
