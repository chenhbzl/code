package com.itcast.collage.bean;

import com.itcast.collage.utils.Utils;

import android.graphics.Bitmap;

public class Photo {
	private String path;
	private Bitmap bitmap;
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Photo(String path2, int i, int j) {
		super();
		this.path = path2;
 
		bitmap = Utils.scalePic(path2, i, j);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
	
}
