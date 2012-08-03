package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.adapter.MainGridViewAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";
	GridView maingv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mainactivity);
		maingv = (GridView) this.findViewById(R.id.gv_all);
		//给gridview设置数据适配器 
		maingv.setAdapter(new MainGridViewAdapter(this));
		maingv.setOnItemClickListener(new MainItemClickListener());
	  
	}
	
	private class MainItemClickListener implements OnItemClickListener{
        /**
         * @param parent 代表当前的gridview 
         * @param 代表点击的item
         * @param 当前点击的item在适配中的位置 
         * @param id The row id of the item that was clicked.
         */
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			//Logger.i(TAG, "position"+ position);
			switch (position) {
			case 0:
				Intent intent = new Intent(MainActivity.this,LostProtectedActivity.class);
				startActivity(intent);
				break;
			case 1:
				Intent callsmssafeintent = new Intent(MainActivity.this,CallSmsSafeActivity.class);
				startActivity(callsmssafeintent);
				break;	
				
			case 2:
				Intent appintent = new Intent(MainActivity.this,AppManagerActivity.class);
				startActivity(appintent);
				break;	
				
			case 3:
				Intent taskmanagerintent = new Intent(MainActivity.this,TaskManagerActivity.class);
				startActivity(taskmanagerintent);
				break;		
				
				
			case 4:
				Intent trafficmanagerintent = new Intent(MainActivity.this,TrafficManagerActivity.class);
				startActivity(trafficmanagerintent);
				break;	
				
			case 5:
				Intent killvirusintent = new Intent(MainActivity.this,KillVirusActivity.class);
				startActivity(killvirusintent);
				break;		
			case 7:
				Intent atoolsintent = new Intent(MainActivity.this,AToolsActivity.class);
				startActivity(atoolsintent);
				break;
			}
		}
		
	}
	
}
