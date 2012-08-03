package cn.itcast.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.db.dao.LockAppDao;
import cn.itcast.mobilesafe.util.Logger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AppManagerActivity extends Activity {
	protected static final int GET_APP_FINISHED = 80;
	protected static final String TAG = "AppManagerActivity";
	LockAppDao lockAppDao;
	ListView lv_appmanage;
	// 包管理器 获取安装的程序
	PopupWindow mPopupWindow;
	PackageManager packageManager;
	List<ApplicationInfo> appInfos;
	List<ApplicationInfo> userAppInfos;
	ProgressDialog pd;
	LayoutInflater inflater;
	TextView tv_asset_title;
	boolean isAllapp = true;
	// 实例化一个动画效果的插入器 , 插入器是作用在某个view对象上的
	Interpolator mInterpolator = new Interpolator() {

		public float getInterpolation(float input) {
			final float inner = (input * 1.55f) - 1.1f;
			return 1.2f - inner * inner;
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_APP_FINISHED:
				pd.dismiss();
				getUserApp();
				lv_appmanage.setAdapter(new AppListAdapter(appInfos));
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.applationinstall);
		lockAppDao = new LockAppDao(this);
		packageManager = getPackageManager();
		inflater = LayoutInflater.from(this);
		pd = new ProgressDialog(this);
		pd.setMessage("正在搜索应用程序");
		getInstallApp();
		userAppInfos = new ArrayList<ApplicationInfo>();

		tv_asset_title = (TextView) this.findViewById(R.id.asset_title);

		tv_asset_title.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isAllapp) {
					tv_asset_title.setText("用户程序");
					isAllapp = false;
					lv_appmanage.setAdapter(new AppListAdapter(userAppInfos));
				}else{
					isAllapp = true;
					tv_asset_title.setText("所有程序");
					lv_appmanage
					.setAdapter(new AppListAdapter(appInfos));
				}

			}
		});

		lv_appmanage = (ListView) this.findViewById(R.id.lv_appmanage);
		// 监听listview的滚动状态
		lv_appmanage.setOnScrollListener(new OnScrollListener() {

			// 当滚动状态改变的回调方法
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				dismissPopUpwindow();
			}

			// listview滚动 的时候 会执行的回调方法
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});
		lv_appmanage.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// 用户点击某个item之前 判断 当前界面上是不是有popwindow
				dismissPopUpwindow();
				// 要知道view在listview中的位置

				showPopWindow(view, position);
			}

		});
		
		// 把content menu注册 某个view控件 上
		registerForContextMenu(lv_appmanage);

	}

	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		 MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.context_menu, menu);
	}



	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		switch (item.getItemId()) {
		 case R.id.item1:
			 Logger.i(TAG,"添加到程序锁  位置是 "  +info.position);
			 if(isAllapp){
				 ApplicationInfo  appinfo = appInfos.get(info.position);
				 String packname = appinfo.packageName;
				 lockAppDao.save(packname);
			 }else{
				 ApplicationInfo  appinfo = userAppInfos.get(info.position);
				 String packname = appinfo.packageName;
				 lockAppDao.save(packname);
			 }
			 
			 break;
		 case R.id.item2:
			 Logger.i(TAG,"从程序锁列表中删除位置是 "+info.position );
			 if(isAllapp){
				 ApplicationInfo  appinfo = appInfos.get(info.position);
				 String packname = appinfo.packageName;
				 lockAppDao.delete(packname);
			 }
			 else{
				 ApplicationInfo  appinfo = userAppInfos.get(info.position);
				 String packname = appinfo.packageName;
				 lockAppDao.delete(packname);
			 }
			 break;
		 }
		
		
		return super.onContextItemSelected(item);
	}



	private void dismissPopUpwindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
	}

	private void getInstallApp() {
		pd.show();
		new Thread() {
			@Override
			public void run() {
				// 获取到所有的安装的程序的信息
				appInfos = packageManager
						.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
				Message msg = new Message();
				msg.what = GET_APP_FINISHED;
				handler.sendMessage(msg);
			}
		}.start();

	}
	
	private void getUserApp(){
		for (ApplicationInfo appinfo : appInfos) {
			if (filterApp(appinfo)) {
				userAppInfos.add(appinfo);
			}

		}
	}
	/*
	 * 创建一个popupwindow 在指定的位置 参数view 是以那一个控件为 初始位置去显示 popupwindow
	 */
	public void showPopWindow(View view, int position) {
		int[] arrayOfInt = new int[2];
		// 获取到 当前listview里面item 在窗体中显示的位置
		view.getLocationInWindow(arrayOfInt);
		// 获取到popupwindow的布局
		final View myview = inflater.inflate(R.layout.popup_item, null);

		ImageButton ib_share = (ImageButton) myview
				.findViewById(R.id.ib_popup_share);
		ImageButton ib_start = (ImageButton) myview
				.findViewById(R.id.ib_popup_start);
		ImageButton ib_uninstall = (ImageButton) myview
				.findViewById(R.id.ib_popup_uninstall);
		ib_share.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Logger.i(TAG,
						"当前popupwindow 对应item的 position"
								+ (Integer) myview.getTag());
				int posi = (Integer) myview.getTag();
				ApplicationInfo appinfo;
				if (isAllapp){
				appinfo = appInfos.get(posi);
				}else {
					appinfo= userAppInfos.get(posi);
				}
				String packname = appinfo.packageName;
				Intent localIntent1 = new Intent("android.intent.action.SEND");
				localIntent1 = localIntent1.setType("text/plain");
				localIntent1 = localIntent1.putExtra(
						"android.intent.extra.SUBJECT", "f分享");
				localIntent1 = localIntent1.putExtra(
						"android.intent.extra.TEXT", "推荐你使用一款软件  名字为"
								+ packname);
				localIntent1.createChooser(localIntent1, "分享");
				startActivity(localIntent1);

			}
		});
		ib_start.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				int posi = (Integer) myview.getTag();
				ApplicationInfo appinfo;
				if (isAllapp){
				appinfo = appInfos.get(posi);
				}else {
					appinfo= userAppInfos.get(posi);
				}
				String packname = appinfo.packageName;
				// Intent intent = new Intent();
				// intent= packageManager.getLaunchIntentForPackage(packname);
				// <action android:name="android.intent.action.MAIN" />
				// <category android:name="android.intent.category.LAUNCHER" />
				// Intent i = new Intent();
				// i.setAction("android.intent.action.MAIN");
				// i.addCategory("android.intent.category.LAUNCHER");
				//
				// List<ResolveInfo> lists =
				// packageManager.queryIntentActivities(i,
				// PackageManager.MATCH_DEFAULT_ONLY);
				// for(ResolveInfo list :lists){
				//
				// }
				try {
					PackageInfo packinfo = packageManager.getPackageInfo(
							packname, PackageManager.GET_ACTIVITIES);
					ActivityInfo[] activityinfos = packinfo.activities;
					ActivityInfo lunchactivity = activityinfos[0];
					Intent intent = new Intent();
					intent.setComponent(new ComponentName(packname,
							lunchactivity.name));
					startActivity(intent);

				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// if(intent==null){
				// Toast.makeText(getApplicationContext(), "无法启动改应用", 1).show();
				// return;
				// }
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// startActivity(intent);
			}
		});
		ib_uninstall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int posi = (Integer) myview.getTag();
				ApplicationInfo appinfo;
				if (isAllapp){
				appinfo = appInfos.get(posi);
				}else {
					appinfo= userAppInfos.get(posi);
				}
				String packname = appinfo.packageName;
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DELETE);
				Uri data = Uri.parse("package:" + packname);
				intent.setData(data);
				startActivity(intent);
			}
		});

		// 把 position 设置给 myView 对象
		myview.setTag(position);		
		
		// 设置popupwindow显示的内容

		mPopupWindow = new PopupWindow(myview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		
		mPopupWindow.update();
		int i = arrayOfInt[0] + 60;
		int j = arrayOfInt[1];
		// 设置popupwindow的动画效果
		mPopupWindow.setAnimationStyle(R.anim.popup_enter);
        Drawable localDrawable = getResources().getDrawable(R.drawable.local_popup_bg);
        
        // ********************非常重要,要设置popupwindow的背景 
        mPopupWindow.setBackgroundDrawable(localDrawable);
		Animation anim = AnimationUtils.loadAnimation(AppManagerActivity.this, R.anim.popup_enter);
		anim.setInterpolator(mInterpolator);
		anim.setDuration(1000);
		// 第一个参数 是popupwindow被挂载到哪个view对象上
		//
		mPopupWindow.showAtLocation(view, 51, i, j);
		myview.startAnimation(anim);
	}

	/*
	 * 绑定数据显示
	 */
	public class AppListAdapter extends BaseAdapter {
		private List<ApplicationInfo> myappInfos;

		public AppListAdapter(List<ApplicationInfo> appInfos) {
			myappInfos = appInfos;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return myappInfos.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return myappInfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = inflater.inflate(R.layout.applationinstall_item, null);
			} else {
				view = convertView;
			}
			ImageView iv = (ImageView) view.findViewById(R.id.lv_appicon);
			TextView tv = (TextView) view.findViewById(R.id.lv_appname);
			ApplicationInfo appinfo = myappInfos.get(position);
			// 获取appinfo的图标
			Drawable icon = appinfo.loadIcon(packageManager);
			iv.setImageDrawable(icon);
			String appname = (String) appinfo.loadLabel(packageManager);
			tv.setText(appname);
			return view;
		}
	}

	public boolean filterApp(ApplicationInfo info) {
		// 系统的软件被升级后的程序
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// 用户安装的程序 替换掉了 系统的程序
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			// 用户自己安装的app
			return true;
		}
		return false;
	}

	
	
	
}
