package cn.itcast.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Setup1Config extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup1config);
	}

	public void setup1confignext(View view){
		Intent intent = new Intent(this,Setup2Config.class);
		startActivity(intent);
		finish();
		
		//实现activity切换的动画效果 
		overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	}
}
