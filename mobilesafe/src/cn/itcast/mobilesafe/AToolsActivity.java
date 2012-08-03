package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.service.BackSmsService;
import cn.itcast.mobilesafe.service.LockAppService;
import cn.itcast.mobilesafe.service.ShowAddressService;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AToolsActivity extends Activity {
	TextView tv_find_address;
	TextView tv_start_address_service;
	TextView tv_stop_address_service;
	TextView  tv_change_location;
	TextView  tv_auto_ipcall;
	EditText et_auto_ipnumber;
	TextView tv_back_sms;
	TextView tv_lock_app;
	ImageButton ib_atools;
	boolean isServiceStart = false;
	Intent service;
	SharedPreferences sp;
	NotificationManager notficationManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atools);
		// ��ȡϵͳnotification�ķ��� 
		notficationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_find_address = (TextView) this.findViewById(R.id.tv_find_address);
		ib_atools = (ImageButton) this.findViewById(R.id.ib_atools);
		tv_start_address_service = (TextView) this.findViewById(R.id.tv_start_address_service);
		tv_change_location = (TextView) this.findViewById(R.id.tv_change_location);
		tv_auto_ipcall = (TextView) this.findViewById(R.id.tv_auto_ipcall);
		tv_back_sms = (TextView) this.findViewById(R.id.tv_back_sms);
		tv_lock_app = (TextView) this.findViewById(R.id.tv_lock_app);
		tv_lock_app.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent service = new Intent(AToolsActivity.this,LockAppService.class);
				startService(service);
			}
		});
		tv_back_sms.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// ���� ����Ϣ 
				Intent service= new Intent(AToolsActivity.this,BackSmsService.class);
				startService(service);
			}
		});
		
		
		
		tv_auto_ipcall.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ͨ����������contentview�ķ��� ������ʾ�Ľ���
				//����Ҫ�����µ�activity 
				//�������� ip���ŵĽ��� 
				setContentView(R.layout.auto_ipcall);
				
				
			}
		});
		
		
		
		ib_atools.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isServiceStart){
				ib_atools.setImageResource(R.drawable.open);
				tv_start_address_service.setText("�����Ѿ�����");
				isServiceStart = true;
				service  = new Intent (AToolsActivity.this,ShowAddressService.class);
				startService(service);
				}
				else{
					ib_atools.setImageResource(R.drawable.close);
					isServiceStart = false;
					tv_start_address_service.setText("�����Ѿ��ر�");
					stopService(service);
				
				}
			}
		});
		
		
		
		//tv_stop_address_service = (TextView) this.findViewById(R.id.tv_stop_address_service);
		tv_find_address.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ��ʾһ���绰�����ز�ѯ�Ľ��� 
				Intent intent = new Intent (AToolsActivity.this,FindAddressActivity.class);
				startActivity(intent);
			}
		});
		tv_change_location.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AToolsActivity.this,DragViewActivity.class);
				startActivity(intent);
			}
		});
		

	}
	public void set_ipnumber(View view){
		et_auto_ipnumber =  (EditText) findViewById(R.id.et_auto_ipnumber);
	    String ipnumber =	et_auto_ipnumber.getText().toString();
	     Editor editor = sp.edit();
	     editor.putString("ipnumber", ipnumber);
	     editor.commit();
	     //Toast.makeText(this, "IP���� �������", 1).show();
	     //
	    // 1. ��ȡ��ϵͳ��notificationManager
	    
	    // 2. ʵ����һ��notification 
	     String tickerText = "IP���� �������";
	     long when = System.currentTimeMillis();
	     Notification notification = new Notification(R.drawable.notification, tickerText, when);
	   //  notification.flags= Notification.FLAG_NO_CLEAR
	     notification.sound = Uri.parse("/sdcard/haha.mp3"); 
	     
	     
	     
	     //3 .�����û����notification�Ķ��� 
	     // pendingIntent ���ڵ���ͼ 
	     Intent intent = new Intent(this,MainActivity.class);
	     
	     PendingIntent pendingIntent  = PendingIntent.getActivity(this, 0, intent, 0);
	     // ��һ�����ڵ���ͼ���ø� notification
	     notification.setLatestEventInfo(this, "�Զ�ip�����������", "ip����Ϊ"+ipnumber, pendingIntent);
	    
	     //4. �Ѷ����notification ���ݸ� notificationmanager 
	     notficationManager.notify(0, notification);
	     setContentView(R.layout.atools);
	}
}
