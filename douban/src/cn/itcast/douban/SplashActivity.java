package cn.itcast.douban;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends Activity {
	TextView tv_version;
	PackageManager pm;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splashactivity);
        
        pm = getPackageManager();
        tv_version =  (TextView) this.findViewById(R.id.tv_version);
        
        try {
        	 PackageInfo packinfo = pm.getPackageInfo(getPackageName(), 0);
        	 String version = packinfo.versionName;
        	 tv_version.setText(version);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        // ��splashactivity�����������Ƿ���� 
        if (isNetworkAvaiable()){

        }else{
        	AlertDialog.Builder builder = new Builder(this);
        	builder.setTitle(R.string.warning);
        	builder.setMessage(R.string.network_down);
        	builder.setPositiveButton("ȷ��", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//�����������ý���
					Intent intent = new Intent();
					//class ����һ��Ҫдȫ·�� 
					intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
					startActivity(intent);
					
				}
			});
        	builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {

				}
			});
        	builder.create().show();
        }
        new Thread(){

			@Override
			public void run() {
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Intent intent = new Intent(SplashActivity.this,MainTabActivity.class);
				startActivity(intent);
				finish();
			}        	
        }.start();
        
    }
    
    
    /*
     * �ж������Ƿ���� 
     */
    private boolean isNetworkAvaiable(){
    	ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo  info = cm.getActiveNetworkInfo();
    	// ����ֻ�������״̬  info.isRoaming()
    	return (info!=null && info.isConnected());
    }
}