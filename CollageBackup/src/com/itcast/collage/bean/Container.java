package com.itcast.collage.bean;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Container implements OnTouchListener {
	private Photo photo;
	private int id;
	private int x;
	private int y;
	private float width;
	private float height;
	private RelativeLayout rl;
	private ImageView iv;
	
	public void Clear(){
		this.iv = null;
	}
	public ImageView getIv() {
		return iv;
	}

	public void setIv(ImageView iv) {
		this.iv = iv;
		this.rl.removeAllViews();
		this.rl.addView(iv);
		iv.setOnTouchListener(this);
		
	}

	public RelativeLayout getRl() {
		return this.rl;
	}

	public Container(){}
	 
	 
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
		
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	public void initRl(Activity activity) {
		rl= new RelativeLayout(activity);
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				(int)width, (int)height);
		rllp.leftMargin = x;
		rllp.topMargin = y;
		rl.setLayoutParams(rllp);
		rl.setBackgroundColor(Color.BLACK);
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
 
 
	
}
