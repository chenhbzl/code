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
		//1 . �ж��û��Ƿ��Ѿ����� ���� 
		inflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE); 

		String password = sp.getString("password", null);
		if(password!=null){
			Logger.i(TAG, "�û�����������,���������û���������");	
			showNormalEntryDialog();
		}else{
			Logger.i(TAG, "�û�û����������");
			showFirstEntryDialog();
		}
	}
	/**
	 * �������������ֻ��������� 
	 */
	private void showNormalEntryDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog d  = builder.create();
		View view = inflater.inflate(R.layout.normal_entry_dialog, null);
		//�������û�ͨ�� ���˼�  ȡ���Ի��� 
		d.setCancelable(false);
		d.setTitle("���������� ");
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
					Logger.i(TAG,"������ȷ,����lostprotectedactivity");
					d.dismiss();
					//setContentView(R.layout.lostprotect);
					LoadMainUI();
				}else{
					Toast.makeText(getApplicationContext(), "���벻��ȷ", 1).show();
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
	 * ��һ�ν����ֻ���������
	 */
	private void showFirstEntryDialog() {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog d  = builder.create();
		d.setTitle("��������");
		View view = inflater.inflate(R.layout.first_entry_dialog, null);
		final EditText first_pwd_et=  (EditText) view.findViewById(R.id.first_password_et);
		final EditText second_pwd_et = (EditText) view.findViewById(R.id.second_password_et);
		//d.addContentView(view, new LayoutParams(90, 100));
		d.setView(view);
		
		//�������û�ͨ�� ���˼�  ȡ���Ի��� 
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
					Toast.makeText(getApplicationContext(), "���벻��Ϊ��", 1).show();
				}else if(first_pwd.equals(second_pwd)){
				    Editor editer =	sp.edit();
				    editer.putString("password", endcodedpwd);
				    editer.commit();
				    d.dismiss();
					//setContentView(R.layout.lostprotect);
				    LoadMainUI();
				}else{
					Toast.makeText(getApplicationContext(), "�����������벻һ��", 1).show();
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
	 * ����������,����û�û������������,���������򵼽���
	 * ����û��Ѿ�������, ��ֱ�ӽ��뵽����ҳ��  
	 */
	private void LoadMainUI(){
		boolean setupalready =  sp.getBoolean("setupalready", false);
		if(setupalready){
			Logger.i(TAG,"������ϸ��Ϣҳ��");
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
			finish();
		}else{
			Logger.i(TAG,"����������");
			Intent intent = new Intent(this,Setup1Config.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		
		
	}
}
