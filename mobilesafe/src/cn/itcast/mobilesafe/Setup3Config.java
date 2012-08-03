package cn.itcast.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Setup3Config extends Activity {
	SharedPreferences sp;
	EditText et_instruction;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.setup3config);
		et_instruction = (EditText) findViewById(R.id.et_safe_instruction);
		String instruction = sp.getString("instruction", "");
		et_instruction.setText(instruction);
	}

	public void finishSetting(View view){

		String instruction = et_instruction.getText().toString();
		if("".equals(instruction)){
			Toast.makeText(this, "内容不能为空", 1).show();
		}else{
			Editor editor = sp.edit();
			editor.putString("instruction", instruction);
			editor.putBoolean("setupalready", true);
			editor.commit();
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
}
