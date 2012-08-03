package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.util.Logger;
import cn.itcast.mobilesafe.util.MD5Encoder;
import android.R.anim;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LostProtectedActivity extends Activity {
	private static final String TAG = "LostProtectedActivity";
	SharedPreferences sp;
	LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//1 . 判断用户是否已经设置 密码 
		inflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE); 

		String password = sp.getString("password", null);
		if(password!=null){
			Logger.i(TAG, "用户设置了密码,弹出界面用户密码输入");	
			showNormalEntryDialog();
		}else{
			Logger.i(TAG, "用户没有设置密码");
			showFirstEntryDialog();
		}
	}
	/**
	 * 设置密码后进入手机防盗界面 
	 */
	private void showNormalEntryDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog d  = builder.create();
		View view = inflater.inflate(R.layout.normal_entry_dialog, null);
		//不允许用户通过 后退键  取消对话框 
		d.setCancelable(false);
		d.setTitle("请输入密码 ");
		//RelativeLayout.LayoutParams
		//FrameLayout.LayoutParams
		d.setView(view);
		d.show();
		final EditText normal_et = (EditText) view.findViewById(R.id.normal_password_et);

		Button confirm_bt = (Button) view.findViewById(R.id.secondConfirmbt);
		Button cancle_bt = (Button) view.findViewById(R.id.secondCanclebt);
		confirm_bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String password  = normal_et.getText().toString();
				String endcodepwd = null;
				try {
					endcodepwd = MD5Encoder.getMD5code(password);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String realpwd = sp.getString("password", null);
				if (endcodepwd!=null && realpwd!=null  &&endcodepwd.equals(realpwd)){
					Logger.i(TAG,"密码正确,进入lostprotectedactivity");
					d.dismiss();
					//setContentView(R.layout.lostprotect);
					LoadMainUI();
				}else{
					Toast.makeText(getApplicationContext(), "密码不正确", 1).show();
				}
			}
		});
		cancle_bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
				finish();
			}
		});
		
	}
	/**
	 * 第一次进入手机防盗界面
	 */
	private void showFirstEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog d  = builder.create();
		d.setTitle("输入密码");
		View view = inflater.inflate(R.layout.first_entry_dialog, null);
		final EditText first_pwd_et=  (EditText) view.findViewById(R.id.first_password_et);
		final EditText second_pwd_et = (EditText) view.findViewById(R.id.second_password_et);
		//d.addContentView(view, new LayoutParams(90, 100));
		d.setView(view);
		
		//不允许用户通过 后退键  取消对话框 
		d.setCancelable(false);
		d.show();
		
		Button confirm_bt =  (Button) view.findViewById(R.id.lostProtectedConfirmbt);
		Button cancle_bt = (Button) view.findViewById(R.id.lostProtectedCanclebt);
		
		confirm_bt.setOnClickListener(new OnClickListener() {
			String endcodedpwd;
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String first_pwd = first_pwd_et.getText().toString();
				String second_pwd = second_pwd_et.getText().toString();
				
				
				try {
					endcodedpwd = MD5Encoder.getMD5code(first_pwd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if("".equals(first_pwd)|| "".equals(second_pwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(first_pwd.equals(second_pwd)){
				    Editor editer =	sp.edit();
				    editer.putString("password", endcodedpwd);
				    editer.commit();
				    d.dismiss();
					//setContentView(R.layout.lostprotect);
				    LoadMainUI();
				}else{
					Toast.makeText(getApplicationContext(), "两次密码输入不一样", 1).show();
				}
			}
		});
		cancle_bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.dismiss();
				finish();
			}
		});
		
		
	}
	/*
	 * 加载主界面,如果用户没有做过设置向导,进入设置向导界面
	 * 如果用户已经设置向导, 就直接进入到配置页面  
	 */
	private void LoadMainUI(){
		boolean setupalready =  sp.getBoolean("setupalready", false);
		if(setupalready){
			Logger.i(TAG,"加载详细信息页面");
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
			finish();
		}else{
			Logger.i(TAG,"进入设置向导");
			Intent intent = new Intent(this,Setup1Config.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		
		
	}
}
