package com.itcast.opengldemo2;


public class MyPicture {
	private int bitmap;
	private float x;
	private float y;
	private float z;
	
	
	public MyPicture(){
		
	}
	
	public MyPicture(int bitmap, float x, float y, float z) {
		super();
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public int getPic() {
		return bitmap;
	}
	public void setBitmap(int bitmap) {
		this.bitmap = bitmap;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	
	
	
	
}
