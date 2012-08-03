package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.service.GPSInfoService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class LostProtectedSettingActivity extends Activity {
	CheckBox ck_isprotecting;
	TextView tv_reentry;
	TextView tv_change_instru;
	TextView tv_isprotecting;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostprotectedsetting);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		ck_isprotecting = (CheckBox) findViewById(R.id.ck_isprotecting);
		tv_reentry = (TextView) findViewById(R.id.tv_reentry_setup);
		tv_change_instru = (TextView) findViewById(R.id.tv_change_instru);
		tv_isprotecting = (TextView) findViewById(R.id.tv_isprotecting);
		//判断check应该显示的状态 
		if (sp.getBoolean("isprotecting", false)) {
			
			ck_isprotecting.setChecked(true);
			tv_isprotecting.setText("保护中");
		}

		// 2. 点击tv_reentry 从新执行设置向导
		tv_reentry.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LostProtectedSettingActivity.this,
						Setup1Config.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		// 3. tv_change_instru 更改发送位置的指令
		tv_change_instru.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LostProtectedSettingActivity.this,
						Setup3Config.class);
				startActivity(intent);
			}
		});
		// todo
		// 1. 点击ck_isprotecting 更改保护的状态

		ck_isprotecting.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (ck_isprotecting.isChecked()) {
					tv_isprotecting.setText("保护中");

				} else {
					tv_isprotecting.setText("保护已经停止");
					GPSInfoService.getInstance(LostProtectedSettingActivity.this).cancleLocationUpdates();
				}
				Editor editor = sp.edit();
				editor.putBoolean("isprotecting", ck_isprotecting.isChecked());
				editor.commit();
			}
		});
	}
}
