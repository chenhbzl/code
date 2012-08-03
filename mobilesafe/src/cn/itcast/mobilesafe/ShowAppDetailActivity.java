package cn.itcast.mobilesafe;

import android.app.Activity;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAppDetailActivity extends Activity {
	TextView tv_app_name;
	TextView tv_version;
	TextView tv_pack_name;
	TextView tv_app_promissions;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_app_detail);
		
		tv_app_name = (TextView) this.findViewById(R.id.tv_showapp_detail_appname);
		tv_pack_name = (TextView) this.findViewById(R.id.tv_showapp_detail_apppackname);
		tv_version = (TextView) this.findViewById(R.id.tv_showapp_detail_appversion);
		tv_app_promissions = (TextView) this.findViewById(R.id.tv_showapp_detail_apppromissions);
	
//		Intent intent = getIntent();
//		String packname= intent.getStringExtra("packname");
//		String appname = intent.getStringExtra("appname");
//		String appversion = intent.getStringExtra("appversion");
//		String[] apppermissions = intent.getStringArrayExtra("apppermissions");
		
		MobileSafeApp app = (MobileSafeApp)getApplication();
		RunningAppProcessInfo info = app.getInfo();
		Toast.makeText(this, info.processName, 1).show();
		
//		tv_app_name.setText(appname);
//		tv_pack_name.setText(packname);
//		tv_version.setText(appversion);
//		
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0 ; i< apppermissions.length;i++){
//			sb.append(apppermissions[i]);
//			sb.append("\n");
//		}
//		String strpermission = sb.toString();
//		tv_app_promissions.setText(strpermission);
	}
}
