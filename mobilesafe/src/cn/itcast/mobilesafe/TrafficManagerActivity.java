package cn.itcast.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.itcast.mobilesafe.util.TaskInfo;
import cn.itcast.mobilesafe.util.TrafficDataUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrafficManagerActivity extends Activity {
	protected static final int UPDATA_INFO = 110;
	TextView tv_mobile_traffic;
	TextView tv_wifi_traffic;
	ListView lv;
	PackageManager pm;
	TrafficAdapter trafficAdapter;
	//书uo有程序的集合
	List<ResolveInfo> appInfos;
	//产生了流量的程序的集合
	List<ResolveInfo> trafficAppInfos;
	LayoutInflater inflater;
	
	
	Timer timer ;
	TimerTask task;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_INFO:
				// 更新listview  
				trafficAdapter.notifyDataSetChanged();
				break;
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		trafficAdapter  = new TrafficAdapter();
		task = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = UPDATA_INFO;
				handler.sendMessage(msg);
			}
		};
		
		timer = new Timer();
		//定期任务 
		timer.schedule(task, 1000, 2000);
		
		inflater = LayoutInflater.from(this);
		
		//初始化 产生流量的程序集合 
		trafficAppInfos  = new ArrayList<ResolveInfo>();
		
		setContentView(R.layout.traffic_stats);
		pm = getPackageManager();
		tv_mobile_traffic = (TextView) this.findViewById(R.id.tv_mobile_traffic);
		tv_wifi_traffic = (TextView) this.findViewById(R.id.tv_wifi_traffic);
		lv = (ListView) this.findViewById(R.id.traffic_content);
		
		long moblietotalrx = TrafficStats.getMobileRxBytes();
		long moblietotaltx = TrafficStats.getMobileTxBytes();
		
		long mobiletotaldata = moblietotalrx+moblietotaltx;
		String strmobiledata = TrafficDataUtil.getDataString(mobiletotaldata);
		tv_mobile_traffic.setText(strmobiledata);
		
		
		long wifitotalrx = TrafficStats.getTotalRxBytes();
		long wifitotaltx = TrafficStats.getTotalTxBytes();
		
		long wifitotaldata = wifitotalrx+wifitotaltx;
		String strwifidata = TrafficDataUtil.getDataString(wifitotaldata);
		tv_wifi_traffic.setText(strwifidata);

		lv.setAdapter(trafficAdapter);
		// 指定一个意图 ,是可以启动某个程序的意图 
		Intent intent  = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		appInfos = pm.queryIntentActivities(intent, 0);
		// 获取到 包名 appInfos.get(0).activityInfo.packageName
		for(ResolveInfo appinfo : appInfos){
			int uid =	appinfo.activityInfo.applicationInfo.uid; 
			if(	TrafficStats.getUidRxBytes(uid)==-1 && TrafficStats.getUidTxBytes(uid)==-1 ){
				continue;
			}else{
				trafficAppInfos.add(appinfo);
			}
		}
	}
	
	
	public class TrafficAdapter extends BaseAdapter{

		public int getCount() {
			// TODO Auto-generated method stub
			return trafficAppInfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return trafficAppInfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
		   View view =	inflater.inflate(R.layout.traffic_item, null);
		   ImageView ivicon =	 (ImageView) view.findViewById(R.id.iv_traffic_icon);
		   TextView tvname =   (TextView) view.findViewById(R.id.tv_traffic_name);
		   TextView tvrx =  (TextView) view.findViewById(R.id.tv_traffic_rxtraffic);
		   TextView tvtx = (TextView) view.findViewById(R.id.tv_traffic_txtraffic);
		   String packname = trafficAppInfos.get(position).activityInfo.packageName;
		   TaskInfo taskInfo = new TaskInfo(TrafficManagerActivity.this);
		   Drawable icon =  taskInfo.getAppIcon(packname);
		   String appname =  taskInfo.getAppName(packname);
		   if(icon!=null){
			   ivicon.setBackgroundDrawable(icon);
		   }else{
			   ivicon.setImageResource(R.drawable.icon);
		   }
		   tvname.setText(appname);
		   int uid = trafficAppInfos.get(position).activityInfo.applicationInfo.uid;
		   long rxtotal = TrafficStats.getUidRxBytes(uid);
		   long txtotal = TrafficStats.getUidTxBytes(uid);

		   String strrxdata = TrafficDataUtil.getDataString(rxtotal);
		   String strtxdata = TrafficDataUtil.getDataString(txtotal);
		   tvrx.setText(strrxdata);
		   tvtx.setText(strtxdata);
		   return view;
		}
	}
}
