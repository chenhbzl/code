package cn.itcast.mobilesafe.widget;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.UpdateWidgetService;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class TaskWidget extends AppWidgetProvider {

	private static final String TAG = "TaskWidget";
	ActivityManager am;
	Intent service;

	@Override
	public void onEnabled(Context context) {
		service = new Intent(context, UpdateWidgetService.class);
		context.startService(service);
		super.onEnabled(context);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		System.out.println("hah");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		RemoteViews rv = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);
		am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMemorySize = outInfo.availMem;
		String strMemorySize = availMemorySize / 1024 / 1024 + "MB";
		rv.setTextViewText(R.id.tv_widget, "剩余系统内存为 " + strMemorySize);
		appWidgetManager.updateAppWidget(
				new ComponentName(context.getPackageName(),
						"cn.itcast.mobilesafe.widget.TaskWidget"), rv);
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	@Override
	public void onDisabled(Context context) {
		// TODO Auto-generated method stub
		super.onDisabled(context);
		if (service != null) {
			context.stopService(service);
		}
	}

}
