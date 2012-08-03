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
				//�Ի���֪ͨ�û��������� 
				showUpdataDialog();
				break;
			case GET_UNDATAINFO_ERROR:
				//�Ի���֪ͨ�û��������� 
				Toast.makeText(getApplicationContext(), "��ȡ������������Ϣʧ��", 1).show();
				LoginMain();
				break;	
			case DOWN_ERROR:
				//�Ի���֪ͨ�û��������� 
				Toast.makeText(getApplicationContext(), "�����°汾ʧ��", 1).show();
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
		//����һ������Ķ���Ч�� 
		RelativeLayout rl =  (RelativeLayout) findViewById(R.id.splash_rl);
		AlphaAnimation  aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(3000);
		rl.setAnimation(aa);
		
		//RotateAnimation
		// TranslateAnimation
		 
		 
		
		//��ȡpackagemanager��ʵ�� 
		packageManager = getPackageManager();
		versiontv = (TextView) findViewById(R.id.version);
	    try {
			versionname =getVersionName();
			versiontv.setText(versionname);
			//������ݿⲻ���� ִ���������ݵĲ��� 
			if(!addressService.isDBExist()){
				pd.setMessage("�����������ݿ�");
				pd.show();
				new Thread(new DownLoadDBTask()).start();
			}else{
				new Thread(new CheckVersionTask()).start();

			}
		} catch (Exception e) {
			Toast.makeText(this, "��ȡ�汾���쳣", 1).show();
			e.printStackTrace();
		}
	}
	
	//�����Ի���֪ͨ�û����³��� 
	protected void showUpdataDialog() {
		// TODO Auto-generated method stub 
		//1.����alertDialog��builder.  
		//2. Ҫ��builder��������, �Ի��������,��ʽ,��ť
		//3.ͨ��builder ����һ���Ի���
		//4.�Ի���show()����  
		AlertDialog.Builder builer = new Builder(this) ; 
		builer.setTitle("������");
		builer.setMessage(info.getDescription());
		builer.setPositiveButton("ȷ��", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// �ӷ����������� �µ�apk Ȼ��װ 
				Logger.i(TAG,"����apk,����");
				
				downLoadApk();
			}
		});
		builer.setNegativeButton("ȡ��", new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				LoginMain();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}


	protected void downLoadApk() {
		pd.setMessage("�������ظ���");
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

	
	//��װapk 
	protected void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		//data uri  tel:1234  context:// 
//		intent.setData(Uri.fromFile(file));
//		//type MIME ������ . ͼƬ image/jpg audio/MP3
//		intent.setType("application/vnd.android.package-archive");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	/*
	 * �������ݿ������   
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
	 * �ӷ�������ȡxml����  �ȶ԰汾�� 
	 */
	public class CheckVersionTask implements Runnable{

		public void run() {
			try {
				//����Դ�ļ���ȡ������ ��ַ 
				String path = getResources().getString(R.string.serverurl);
				// ��װ��url�Ķ��� 
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection(); 
				conn.setConnectTimeout(3000);
				InputStream is =	conn.getInputStream(); 
				 info =  UpdataInfoParser.getUpdataInfo(is);
				
				if(info.getVersion().equals(versionname)){
					Logger.i(TAG,"�汾����ͬ��������");
					LoginMain();
				}else{
					Logger.i(TAG,"�汾�Ų�ͬ ,��ʾ�û����� ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// ������ 
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				handler.sendMessage(msg);
				e.printStackTrace();
			} 
		}
		
	}
	/*
	 * ��ȡ��ǰ����İ汾�� 
	 */
	private String getVersionName() throws Exception{
		//"cn.itcast.mobliesafe"
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
	    return packInfo.versionName; 

	}
	private void LoginMain(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		//��������ǰ��activity 
		this.finish();
	}
}
