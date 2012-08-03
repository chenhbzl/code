package cn.itcast.mobilesafe;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.itcast.mobilesafe.domain.UpdataInfo;
import cn.itcast.mobilesafe.service.AddressService;
import cn.itcast.mobilesafe.util.DownLoadManager;
import cn.itcast.mobilesafe.util.Logger;
import cn.itcast.mobilesafe.util.UpdataInfoParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity{
	public static final String TAG = "SplashActivity";
	public static final int UPDATA_CLIENT = 20;
	public static final int GET_UNDATAINFO_ERROR = 21;
	protected static final int DOWN_ERROR = 22;
	public static final int GET_DB_ERROR = 23;
	PackageManager packageManager;
	TextView versiontv;
	String versionname;
	UpdataInfo info;
	ProgressDialog pd;
	AddressService addressService;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				//对话框通知用户升级程序 
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//对话框通知用户升级程序 
				Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", 1).show();
				LoginMain();
				break;	
			case DOWN_ERROR:
				//对话框通知用户升级程序 
				Toast.makeText(getApplicationContext(), "下载新版本失败", 1).show();
				LoginMain();
				break;	
			case GET_DB_ERROR:
				pd.dismiss();
				new Thread(new CheckVersionTask()).start();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		pd = new  ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		addressService =  new AddressService();
		//增加一个淡入的动画效果 
		RelativeLayout rl =  (RelativeLayout) findViewById(R.id.splash_rl);
		AlphaAnimation  aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(3000);
		rl.setAnimation(aa);
		
		//RotateAnimation
		// TranslateAnimation
		 
		 
		
		//获取packagemanager的实例 
		packageManager = getPackageManager();
		versiontv = (TextView) findViewById(R.id.version);
	    try {
			versionname =getVersionName();
			versiontv.setText(versionname);
			//如果数据库不存在 执行下载数据的操作 
			if(!addressService.isDBExist()){
				pd.setMessage("正在下载数据库");
				pd.show();
				new Thread(new DownLoadDBTask()).start();
			}else{
				new Thread(new CheckVersionTask()).start();

			}
		} catch (Exception e) {
			Toast.makeText(this, "获取版本号异常", 1).show();
			e.printStackTrace();
		}
	}
	
	//弹出对话框通知用户更新程序 
	protected void showUpdataDialog() {
		// TODO Auto-generated method stub 
		//1.创建alertDialog的builder.  
		//2. 要给builder设置属性, 对话框的内容,样式,按钮
		//3.通过builder 创建一个对话框
		//4.对话框show()出来  
		AlertDialog.Builder builer = new Builder(this) ; 
		builer.setTitle("请升级");
		builer.setMessage(info.getDescription());
		builer.setPositiveButton("确定", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// 从服务器上下载 新的apk 然后安装 
				Logger.i(TAG,"下载apk,更新");
				
				downLoadApk();
			}
		});
		builer.setNegativeButton("取消", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}


	protected void downLoadApk() {
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread(){
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);
					sleep(3000);
					pd.dismiss();
					installApk(file);
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					handler.sendMessage(msg);
					e.printStackTrace();
				}
				
			}}.start();
		
	}

	
	//安装apk 
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		//data uri  tel:1234  context:// 
//		intent.setData(Uri.fromFile(file));
//		//type MIME 的类型 . 图片 image/jpg audio/MP3
//		intent.setType("application/vnd.android.package-archive");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	/*
	 * 下载数据库的任务   
	 */
	public class DownLoadDBTask implements Runnable{

		public void run() {

			try {
				addressService.downLoadDB("http://192.168.1.247:8080/address.db", pd);
				new Thread(new CheckVersionTask()).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Message msg = new Message();
				msg.what = GET_DB_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} 
			
		}
		
	}
	
	
	/*
	 * 从服务器获取xml解析  比对版本号 
	 */
	public class CheckVersionTask implements Runnable{

		public void run() {
			try {
				//从资源文件获取服务器 地址 
				String path = getResources().getString(R.string.serverurl);
				// 包装成url的对象 
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection(); 
				conn.setConnectTimeout(3000);
				InputStream is =	conn.getInputStream(); 
				 info =  UpdataInfoParser.getUpdataInfo(is);
				
				if(info.getVersion().equals(versionname)){
					Logger.i(TAG,"版本号相同无需升级");
					LoginMain();
				}else{
					Logger.i(TAG,"版本号不同 ,提示用户升级 ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// 待处理 
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} 
		}
		
	}
	/*
	 * 获取当前程序的版本号 
	 */
	private String getVersionName() throws Exception{
		//"cn.itcast.mobliesafe"
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
	    return packInfo.versionName; 

	}
	private void LoginMain(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//结束掉当前的activity 
		this.finish();
	}
}
