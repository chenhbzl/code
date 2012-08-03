package com.itcast.selector;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TestSelector extends Activity {
    /** Called when the activity is first created. */
	ArrayList<RelativeLayout>rls = new ArrayList<RelativeLayout>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LinearLayout ll  = (LinearLayout) findViewById(R.id.ll);
        Button b = new Button(this);
        b.setWidth(130);
//         b.setBackgroundResource(R.drawable.collage_createbtn_selector);
        b.setText("aaa");
        
        LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(130, 50);
        b.setLayoutParams(lllp);
        ll.addView(b);
        
        
        
        

    }
    
}