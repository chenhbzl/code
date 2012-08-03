package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.util.Logger;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DragViewActivity extends Activity {
	protected static final String TAG = "DragViewActivity";
	ImageView iv;
	public static final int InitstartX= 160;
	public static final int InitStartY=300 ;
	SharedPreferences sp ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dragview);
		iv = (ImageView) this.findViewById(R.id.dragimage);
		sp= getSharedPreferences("config", Context.MODE_PRIVATE);
		iv.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Logger.i(TAG,"手指按下");
					
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int rx = (int) event.getRawX();
					int ry = (int) event.getRawY();
					int dx = rx - startX;
					int dy = ry - startY;
					
					v.layout(v.getLeft()+dx, v.getTop()+dy, v.getRight()+dx, v.getBottom()+dy); 
					startX = (int) event.getRawX();
					startY = (int )event.getRawY();
					Logger.i(TAG,"手指移动");
					break;
				case MotionEvent.ACTION_UP:
					int endx = v.getLeft();
					int endy = v.getTop();
					
			      	int finaldx =	endx - InitstartX;
			      	int finaldy =   endy - InitStartY;
					
			      	Editor editor=  sp.edit();
			      	editor.putInt("dx", finaldx);
			      	editor.putInt("dy", finaldy);
			      	editor.commit();
					Logger.i(TAG,"手指离开屏幕");
					break;
				}
				return true;
			}
		});
	}
}
