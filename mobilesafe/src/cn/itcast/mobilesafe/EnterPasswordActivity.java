package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.inter.ILockAddressService;
import cn.itcast.mobilesafe.service.LockAppService;
import cn.itcast.mobilesafe.util.Logger;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class EnterPasswordActivity extends Activity {
	private static final String TAG = "EnterPasswordActivity";
	AlertDialog alertDialog;
	SharedPreferences sp;
	LayoutInflater inflater;
	EditText et_password ;
	ILockAddressService iLockService;
	MyConn myConn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
		setContentView(R.layout.entry_password);
		sp = getSharedPreferences("config", Context.MODE_APPEND);
		myConn = new MyConn();
		final String pwd = sp.getString("lock_app_pwd", "");
		et_password = (EditText) this.findViewById(R.id.et_entry_password);
		
		//绑定 程序锁的服务 
		Intent service = new Intent(this,LockAppService.class);
		bindService(service, myConn, Context.BIND_AUTO_CREATE);
		
		
//		if ("".equals(pwd)) {
//			// 设置程序锁密码
//			AlertDialog.Builder builder = new Builder(this);
//			View view = inflater.inflate(R.layout.first_entry_dialog, null);
//			final EditText first_pwd_et=  (EditText) view.findViewById(R.id.first_password_et);
//			final EditText second_pwd_et = (EditText) view.findViewById(R.id.second_password_et);
//			//d.addContentView(view, new LayoutParams(90, 100));
//			builder.setView(view);
//			
//			//不允许用户通过 后退键  取消对话框 
//			builder.setCancelable(false);
//			alertDialog = builder.create();
//			alertDialog.show();
//			
//			Button confirm_bt =  (Button) view.findViewById(R.id.lostProtectedConfirmbt);
//			Button cancle_bt = (Button) view.findViewById(R.id.lostProtectedCanclebt);
//			confirm_bt.setOnClickListener(new OnClickListener() {
//				
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					String fisrt_pwd = first_pwd_et.getText().toString();
//					String second_pwd = second_pwd_et.getText().toString();
//					if("".equals(fisrt_pwd)||"".equals(second_pwd)){
//						Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
//					}else if(fisrt_pwd.equals(second_pwd)){
//						Editor editor = sp.edit();
//						editor.putString("lock_app_pwd", fisrt_pwd);
//						editor.commit();
//						if (alertDialog != null) {
//							alertDialog.dismiss();
//						}
//					}else{
//						Toast.makeText(getApplicationContext(), "两次密码不相同", 1).show();
//					}
//
//				}
//			});
//			
//			cancle_bt.setOnClickListener(new OnClickListener() {
//				
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (alertDialog != null) {
//						alertDialog.dismiss();
//					}
//				}
//			});
//			
//		} else {
//			// 输入密码 进行判断
//			AlertDialog.Builder builder = new Builder(this);
//			View view = inflater.inflate(R.layout.normal_entry_dialog, null);
//			final EditText et = (EditText) view
//					.findViewById(R.id.normal_password_et);
//			Button bt_confirm = (Button) this
//					.findViewById(R.id.secondConfirmbt);
//			Button bt_cancle = (Button) this.findViewById(R.id.secondCanclebt);
//			bt_confirm.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					String userpwd = et.getText().toString();
//					if (userpwd.equals(pwd)) {
//						if (alertDialog != null) {
//							alertDialog.dismiss();
//						}
//						finish();
//					}
//				}
//			});
//			bt_cancle.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if (alertDialog != null) {
//						alertDialog.dismiss();
//					}
//				}
//			});
//
//			builder.setView(view);
//			alertDialog = builder.create();
//			alertDialog.show();
//		}

	}

	// 屏蔽用户按后退键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void entry(View view){
		String password = et_password.getText().toString();	
		String pwd = sp.getString("lock_app_pwd", "");
		Intent intent = getIntent();

		
		if (password.equals(pwd)){
			if(intent!=null){
				String packname =  intent.getStringExtra("packagename");
				Logger.i(TAG,packname);
				iLockService.stopLockAppFlag(packname);
			}
			finish();
		}
	}
	public class MyConn implements ServiceConnection{

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			iLockService = (ILockAddressService)service;
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			iLockService = null;
		}
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		unbindService(myConn);
		super.onDestroy();
	}

}
