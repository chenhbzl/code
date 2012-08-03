package cn.itcast.mobilesafe;

import android.R.anim;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup2Config extends Activity {
	EditText et_number ;
	EditText et_message;
	SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2config);
		et_number = (EditText) findViewById(R.id.et_safe_number);
		et_message = (EditText) findViewById(R.id.safe_message_et);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String number = sp.getString("number", "");
		String message = sp.getString("message", "");
		et_number.setText(number);
		et_message.setText(message);
	}

	public void setup2configpre(View view) {
		Intent intent = new Intent(this, Setup1Config.class);
		startActivity(intent);
		finish();
	}

	public void selectContacts(View view) {
		Intent intent = new Intent(this, SelectContactActivity.class);
		// startActivity(intent);

		// 激活一个activity, 当目标activity结束的时候 , 可以返回一个状态码,还有一个值
		startActivityForResult(intent, 0);

	}

	public void setup2confignext(View view) {
		String number = sp.getString("number", null);
		String message= sp.getString("message", null);
		if(number==null || message == null){
			Toast.makeText(this, "请填写号码和短信内容", 1).show();
		}else{
			Intent intent = new Intent(this,Setup3Config.class);
			startActivity(intent);
			finish();
			//overridePendingTransition(android.R.anim.accelerate_interpolator, android.R.anim.accelerate_interpolator);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			String number = data.getStringExtra("number");
			et_number.setText(number);
		}
	}
	
	public void save(View view) {
		String number = et_number.getText().toString();
		String message = et_message.getText().toString();
		if("".equals(message)||"".equals(number)){
			Toast.makeText(this, "号码或者短信不能为空", 1).show();
		}
		Editor editor = sp.edit();
		editor.putString("number", number);
		editor.putString("message", message);
		editor.commit();
	}

}
