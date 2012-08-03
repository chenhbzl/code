package com.itcast.collage.adapter;

import java.util.ArrayList;

import com.itcast.collage.R;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CollagePhotoAdapter extends BaseAdapter {
	
	private Activity activity;
	private ArrayList<Bitmap> mSource;
	private ArrayList<Integer> selectedPos;
	
	
	public CollagePhotoAdapter(Activity activity, ArrayList<Bitmap> resource, ArrayList<Integer> selectedPos){
		this.activity = activity;
		this.mSource = resource;
		this.selectedPos = selectedPos;
	}
	
	@Override
	public int getCount() {
		return this.mSource.size();
	}
	

	@Override
	public Object getItem(int paramInt) {
		return this.mSource.get(paramInt);
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.i("ivan","----------------------in----positon = "+position);
		ViewTag myTag;
		
		if(convertView == null){
			
			convertView = this.activity.getLayoutInflater().inflate(R.layout.item_for_photolist, null);
			myTag = new ViewTag();
			myTag.photoIV = (ImageView) convertView.findViewById(R.id.edit_collage_item_photo);
			myTag.selectedIcon = (ImageView) convertView.findViewById(R.id.edit_collage_item_frame);
			//把myTag对象加载到了convertView里面
			convertView.setTag(myTag);
		}else{
			Log.i("lgb","convertView !=null");
			myTag = (ViewTag) convertView.getTag();
		}
		
		myTag.photoIV.setImageBitmap(this.mSource.get(position));
		myTag.photoIV.setBackgroundColor(Color.WHITE);
		//填充满
		myTag.photoIV.setScaleType(ScaleType.FIT_XY);
		myTag.selectedIcon.setVisibility(View.INVISIBLE);
		//判断如果此position对应的图片所对应的角标在selectedPos里面 让边框显示出来
		if(this.selectedPos.contains((int)(this.getItemId(position)))){
			myTag.selectedIcon.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	
	public static class ViewTag{
		public ImageView photoIV;
		public ImageView selectedIcon;
	}
}