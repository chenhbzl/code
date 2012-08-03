package com.itcast.testrect;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TestRect extends Activity implements OnTouchListener {
    /** Called when the activity is first created. */
	ArrayList<RelativeLayout> rls = new ArrayList<RelativeLayout>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        
        RelativeLayout rl = new RelativeLayout(this);
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(100, 100);
        rl.setBackgroundColor(Color.RED);
        rls.add(rl);
        
        Rect r = new Rect();
        
        rl.getHitRect(r);
        
        int h =r.height();
        
         Log.i("ivan"," h = "+h);
        
        rl.setTag(0);
        ll.addView(rl,rllp);
        
        RelativeLayout rl1 = new RelativeLayout(this);
        RelativeLayout.LayoutParams rllp1 = new RelativeLayout.LayoutParams(100, 100);
        rl1.setBackgroundColor(Color.GREEN);
        
        ll.addView(rl1,rllp1);
        rl1.setTag(1);
        rls.add(rl1);
        RelativeLayout rl2 = new RelativeLayout(this);
        RelativeLayout.LayoutParams rllp2 = new RelativeLayout.LayoutParams(100, 100);
        rl2.setBackgroundColor(Color.YELLOW);
        
        ll.addView(rl2,rllp2);
        rls.add(rl2);
        rl2.setTag(2);
        RelativeLayout rl3 = new RelativeLayout(this);
        RelativeLayout.LayoutParams rllp3 = new RelativeLayout.LayoutParams(100, 100);
        rl3.setBackgroundColor(Color.BLUE);
        
        ll.addView(rl3,rllp3);
        rls.add(rl3);
        ll.setOnTouchListener(this);
        rl3.setTag(3);
        
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getY());
		
		for(RelativeLayout rl : rls){
			
			Rect r = new Rect();
			rl.getHitRect(r);
			
			
			  
	        
	        rl.getHitRect(r);
	        
	        int h =r.height();
	         Log.i("ivan"," onTouch h = "+h);
			if(r.contains((int)event.getX(), (int)event.getY())){
				System.out.println(rl.getTag());
			}
		}
		
		
		
		return true;
	}
}