package cn.itcast.mobilesafe.service;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
/*
 * 要求是单态的 ,只允许存在一个实例.
 * 获取手机的gps信息 
 */
public class GPSInfoService {
	private Context context;
	private LocationManager manager;
	SharedPreferences sp ;
	//1. 私有化构造方法 
	private  GPSInfoService(Context context){	
		this.context= context;
		manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	private static GPSInfoService mGPSService;
	
	public synchronized static GPSInfoService getInstance(Context context){
		if(mGPSService==null)
			mGPSService = new GPSInfoService(context);
		return mGPSService;
	}
	
	public void registerLocationUpdates(){
		//当前你的手机 所支持的定位方式获取出来 
		//有多种定位方式 gps network ,基站, passive
		//可以根据定位的条件 ,获取 一个最好的定位方式 
		Criteria criteria = new Criteria();
		// 设置定位的精度 
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); //获取大体的位置
		criteria.setAltitudeRequired(false); // 海拔信息
		criteria.setCostAllowed(true); //允许产生费用
		criteria.setPowerRequirement(Criteria.POWER_LOW); //低功耗
		
		//获取一个最符合查询条件的位置提供者 
		String provider  =manager.getBestProvider(criteria, true);
		
		// 注册 位置改变的监听器 
		manager.requestLocationUpdates(provider, 60000, 0, getLinster());
		
	}
	
	
	public void cancleLocationUpdates(){
		manager.removeUpdates(getLinster());
		
	}
	private static MyGPSLinster myGPSLinser;
	
	private MyGPSLinster getLinster(){
		if(myGPSLinser==null)
			myGPSLinser = new MyGPSLinster();
		return myGPSLinser;
	}
	
	/**
	 * 获取手机的最后一次位置 
	 * @return
	 */
	public String getLastPosition(){
		return sp.getString("lastlocation", "");
	}
	private class MyGPSLinster implements LocationListener{

		// 用户位置改变的时候 的回调方法 
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//location 
			//获取到用户的纬度 
			double latitude= location.getLatitude();
			double longitude = location.getLongitude();
			String locationstr = "jing du "+ longitude + " weidu  :"+latitude;
		    Editor 	editor =  sp.edit();
		    editor.putString("lastlocation", locationstr);
		    editor.commit();
		}
		// 状态改变 
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
		//gps ,打开
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		//关闭
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}
}
