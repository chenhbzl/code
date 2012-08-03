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
	// �������� ��ȡ��װ�ĳ���
	PopupWindow mPopupWindow;
	PackageManager packageManager;
	List<ApplicationInfo> appInfos;
	List<ApplicationInfo> userAppInfos;
	ProgressDialog pd;
	LayoutInflater inflater;
	TextView tv_asset_title;
	boolean isAllapp = true;
	// ʵ����һ������Ч���Ĳ����� , ��������������ĳ��view�����ϵ�
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
		pd.setMessage("��������Ӧ�ó���");
		getInstallApp();
		userAppInfos = new ArrayList<ApplicationInfo>();

		tv_asset_title = (TextView) this.findViewById(R.id.asset_title);

		tv_asset_title.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (isAllapp) {
					tv_asset_title.setText("�û�����");
					isAllapp = false;
					lv_appmanage.setAdapter(new AppListAdapter(userAppInfos));
				}else{
					isAllapp = true;
					tv_asset_title.setText("���г���");
					lv_appmanage
					.setAdapter(new AppListAdapter(appInfos));
				}

			}
		});

		lv_appmanage = (ListView) this.findViewById(R.id.lv_appmanage);
		// ����listview�Ĺ���״̬
		lv_appmanage.setOnScrollListener(new OnScrollListener() {

			// ������״̬�ı�Ļص�����
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				dismissPopUpwindow();
			}

			// listview���� ��ʱ�� ��ִ�еĻص�����
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
			}
		});
		lv_appmanage.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// �û����ĳ��item֮ǰ �ж� ��ǰ�������ǲ�����popwindow
				dismissPopUpwindow();
				// Ҫ֪��view��listview�е�λ��

				showPopWindow(view, position);
			}

		});
		
		// ��content menuע�� ĳ��view�ؼ� ��
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
			 Logger.i(TAG,"��ӵ�������  λ���� "  +info.position);
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
			 Logger.i(TAG,"�ӳ������б���ɾ��λ���� "+info.position );
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
				// ��ȡ�����еİ�װ�ĳ������Ϣ
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
	 * ����һ��popupwindow ��ָ����λ�� ����view ������һ���ؼ�Ϊ ��ʼλ��ȥ��ʾ popupwindow
	 */
	public void showPopWindow(View view, int position) {
		int[] arrayOfInt = new int[2];
		// ��ȡ�� ��ǰlistview����item �ڴ�������ʾ��λ��
		view.getLocationInWindow(arrayOfInt);
		// ��ȡ��popupwindow�Ĳ���
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
						"��ǰpopupwindow ��Ӧitem�� position"
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
						"android.intent.extra.SUBJECT", "f����");
				localIntent1 = localIntent1.putExtra(
						"android.intent.extra.TEXT", "�Ƽ���ʹ��һ�����  ����Ϊ"
								+ packname);
				localIntent1.createChooser(localIntent1, "����");
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
				// Toast.makeText(getApplicationContext(), "�޷�������Ӧ��", 1).show();
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

		// �� position ���ø� myView ����
		myview.setTag(position);		
		
		// ����popupwindow��ʾ������

		mPopupWindow = new PopupWindow(myview, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		
		mPopupWindow.update();
		int i = arrayOfInt[0] + 60;
		int j = arrayOfInt[1];
		// ����popupwindow�Ķ���Ч��
		mPopupWindow.setAnimationStyle(R.anim.popup_enter);
        Drawable localDrawable = getResources().getDrawable(R.drawable.local_popup_bg);
        
        // ********************�ǳ���Ҫ,Ҫ����popupwindow�ı��� 
        mPopupWindow.setBackgroundDrawable(localDrawable);
		Animation anim = AnimationUtils.loadAnimation(AppManagerActivity.this, R.anim.popup_enter);
		anim.setInterpolator(mInterpolator);
		anim.setDuration(1000);
		// ��һ������ ��popupwindow�����ص��ĸ�view������
		//
		mPopupWindow.showAtLocation(view, 51, i, j);
		myview.startAnimation(anim);
	}

	/*
	 * ��������ʾ
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
			// ��ȡappinfo��ͼ��
			Drawable icon = appinfo.loadIcon(packageManager);
			iv.setImageDrawable(icon);
			String appname = (String) appinfo.loadLabel(packageManager);
			tv.setText(appname);
			return view;
		}
	}

	public boolean filterApp(ApplicationInfo info) {
		// ϵͳ�������������ĳ���
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// �û���װ�ĳ��� �滻���� ϵͳ�ĳ���
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			// �û��Լ���װ��app
			return true;
		}
		return false;
	}

	
	
	
}
