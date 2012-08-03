package cn.itcast.mobilesafe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.itcast.mobilesafe.util.Logger;
import cn.itcast.mobilesafe.util.MyToast;
import cn.itcast.mobilesafe.util.TaskInfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TaskManagerActivity extends Activity {
	protected static final int GET_RUNNING_APP_FINISH = 90;
	protected static final String TAG = "TaskManagerActivity";
	TextView tv_task_manger_memory_info;
	ActivityManager am;
	ListView lv_task_manger;
	List<RunningAppProcessInfo> runningAppProcessInfos ;
	List<RunningAppProcessInfo> userRunningAppProcessInfos;
	ProgressDialog pd ;
	LayoutInflater inflater;
	TaskMangerListAdapter taskMangerListAdapter;
	HashMap<Integer, Boolean>  isselected;
	Handler hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_RUNNING_APP_FINISH:
				pd.dismiss();
				taskMangerListAdapter = new TaskMangerListAdapter();
				lv_task_manger.setAdapter(taskMangerListAdapter);
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.task_manager);
		isselected = new HashMap<Integer, Boolean>();
		pd = new ProgressDialog(this);
		pd.setMessage("正在获取程序列表");
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		MemoryInfo outInfo =	new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMemorySize = outInfo.availMem;
		String strMemorySize = availMemorySize/1024/1024 + "MB";
		tv_task_manger_memory_info = (TextView) this.findViewById(R.id.tv_task_manger_memory_info);
		tv_task_manger_memory_info.setText(strMemorySize);
		lv_task_manger = (ListView) this.findViewById(R.id.lv_task_manger);
		//设置listview item的点击事件 
		lv_task_manger.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Logger.i(TAG,"item 被点击了 位置是 "+ position);
				if(isselected.get(position)){
					isselected.put(position, false);
					//刷新界面  
					taskMangerListAdapter.notifyDataSetChanged();
				}else{
					isselected.put(position, true);
					taskMangerListAdapter.notifyDataSetChanged();
				}
				
			}
			
		});
		
		pd.show();
		new Thread(){
			@Override
			public void run() {
				initData();
	
				Message msg = new Message();
				msg.what = GET_RUNNING_APP_FINISH;
				hander.sendMessage(msg);
			}

		}.start();
		
		

		registerForContextMenu(lv_task_manger);
		
		//lv_task_manger.setAdapter(null);
		
		
//		boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//		setContentView(R.layout.task_manager);
//		
//		if(flag){
//			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
//		}
		
		
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.app_detail, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}


	
	
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info =	 (AdapterContextMenuInfo)item.getMenuInfo();
		int positon = info.position;
		RunningAppProcessInfo appinfo =userRunningAppProcessInfos.get(positon);
		// 程序名 版本号 包名 权限
		Intent intent = new Intent(this,ShowAppDetailActivity.class);
//		intent.putExtra("packname", appinfo.processName);
//		TaskInfo taskinfo = new TaskInfo(this);
//		String appname = taskinfo.getAppName(appinfo.processName);
//		intent.putExtra("appname", appname);
//		String appversion = taskinfo.getAppVersion(appinfo.processName);
//		intent.putExtra("appversion", appversion);
//		String[] apppermissions = taskinfo.getAppPremission(appinfo.processName);
//		intent.putExtra("apppermissions", apppermissions);
		// 把 appinfo 这个对象 传递给ShowAppDetailActivity 
		
		MobileSafeApp app = (MobileSafeApp)getApplication();
		app.setInfo(appinfo);
		
		startActivity(intent);
		
		return super.onContextItemSelected(item);
	}


	//当用户 界面可见的时候 会调用的方法 
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new Thread(){
			@Override
			public void run() {
				initData();
				Message msg = new Message();
				msg.what = GET_RUNNING_APP_FINISH;
				hander.sendMessage(msg);
			}
		}.start();
	}



	protected void initData() {
		// TODO Auto-generated method stub
		runningAppProcessInfos =am.getRunningAppProcesses();
		userRunningAppProcessInfos = new ArrayList<ActivityManager.RunningAppProcessInfo>();
		for(int i = 0; i< runningAppProcessInfos.size();i++){
			 if("system".equals(runningAppProcessInfos.get(i).processName)
			||"android.process.media".equals(runningAppProcessInfos.get(i).processName)
			||"android.process.acore".equals(runningAppProcessInfos.get(i).processName)
			){
				 continue;
				
			 }
			 userRunningAppProcessInfos.add(runningAppProcessInfos.get(i));
		}
		
		
		//初始化 checkbox的状态  
		for (int i= 0; i<userRunningAppProcessInfos.size();i++ ){
			isselected.put(i, false);
		}
	}

	public class TaskMangerListAdapter extends BaseAdapter{

		public int getCount() {
			// TODO Auto-generated method stub
			return userRunningAppProcessInfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return userRunningAppProcessInfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			TaskInfo taskInfo = new TaskInfo(TaskManagerActivity.this);
			if(convertView == null){
				view = inflater.inflate(R.layout.task_manger_item, null);
			}else{
				view = convertView;
			}
			// runningAppProcessInfos.get(position).processName 当前程序的包名 ,
			String packname = userRunningAppProcessInfos.get(position).processName;
			//根据当前程序包名 获取对应的内容信息 
		   TextView tv_appname =	(TextView) view.findViewById(R.id.tv_task_manger_item_appname);
		   TextView tv_pid = (TextView) view.findViewById(R.id.tv_task_manger_item_apppid);
		   TextView tv_appversion = (TextView) view.findViewById(R.id.tv_task_manger_item_appversion);
		   TextView tv_app_memory = (TextView) view.findViewById(R.id.tv_task_manger_item_memoryinfo);
		   ImageView iv_appicon =	(ImageView) view.findViewById(R.id.iv_task_manger_item_appicon);
		   CheckBox cb_check_item  =	(CheckBox) view.findViewById(R.id.cb_task_manger_item);
		   if( isselected.get(position)){
			  cb_check_item.setChecked(true);
		   }else{
			  cb_check_item.setChecked(false);
		   }
		   
		    String appname= taskInfo.getAppName(packname);
		    if(appname!=null){
		    	tv_appname.setText(appname);
		    }else{
		    	tv_appname.setText("");
		    }
		   
		    String apppid ="程序的进程号: "+ userRunningAppProcessInfos.get(position).pid;
		    tv_pid.setText(apppid);
		   
		    String appVersion= taskInfo.getAppVersion(packname);
		    if(appVersion!=null){
		    	tv_appversion.setText(appVersion);
		    }else{
		    	tv_appversion.setText("");
		    }
		   //获取某个进程占用的内存信息 
		    int[] pids = {userRunningAppProcessInfos.get(position).pid};
		    
		    android.os.Debug.MemoryInfo[] memoryInfos =  am.getProcessMemoryInfo(pids);
		    
		    int memorysize = memoryInfos[0].getTotalPrivateDirty();
		    
		    tv_app_memory.setText("内存占用为 "+ memorysize +" KB");
		    
		    Drawable appicon =   taskInfo.getAppIcon(packname);
		    if(appicon!=null){
		    	iv_appicon.setImageDrawable(appicon);
		    }else{
		    	iv_appicon.setImageResource(R.drawable.icon);
		    }
			// TODO Auto-generated method stub
//			TextView tv = new TextView(TaskManagerActivity.this);
//			tv.setText(runningAppProcessInfos.get(position).processName);
			return view;
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.task_manager_menu, menu);
	   //通过代码创建menu
		//menu.add(groupId, itemId, order, title)
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		MyToast myToast = new MyToast(TaskManagerActivity.this);
		switch (item.getItemId()) {
		case R.id.mu_kill_selected:
			killSelected();
			myToast.show(R.drawable.notification, R.string.kill_all_finish);
			break;
		case R.id.mu_kill_all:
			killAll();
			myToast.show(R.drawable.notification, R.string.kill_all_finish);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 杀死所有进程 
	 */
	private void killAll() {
		// TODO Auto-generated method stub
		for (int i = 0 ; i< userRunningAppProcessInfos.size();i++){
			am.restartPackage(userRunningAppProcessInfos.get(i).processName);
		}
		// 更新显示的界面 
		pd.show();
		new Thread(){
			@Override
			public void run() {
				initData();
				Message msg = new Message();
				msg.what = GET_RUNNING_APP_FINISH;
				hander.sendMessage(msg);
			}
		}.start();
	}

	/*
	 * 杀死选择的进程 
	 */
	private void killSelected() {
		// TODO Auto-generated method stub
		for (int i=0 ;i < isselected.size(); i++){
			if(isselected.get(i)){
				//终止一个activity 需要用到 activitymanager
				// restartpackage 可以杀死 与进程相关的服务, alarm , 所有与之相关的线程
				am.restartPackage(userRunningAppProcessInfos.get(i).processName);
			}
		}
		//更新listview的显示  
		pd.show();
		new Thread(){
			@Override
			public void run() {
				initData();
				Message msg = new Message();
				msg.what = GET_RUNNING_APP_FINISH;
				hander.sendMessage(msg);
			}
		}.start();
	}
}
