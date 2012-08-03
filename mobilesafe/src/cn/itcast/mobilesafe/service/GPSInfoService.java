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
 * Ҫ���ǵ�̬�� ,ֻ�������һ��ʵ��.
 * ��ȡ�ֻ���gps��Ϣ 
 */
public class GPSInfoService {
	private Context context;
	private LocationManager manager;
	SharedPreferences sp ;
	//1. ˽�л����췽�� 
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
		//��ǰ����ֻ� ��֧�ֵĶ�λ��ʽ��ȡ���� 
		//�ж��ֶ�λ��ʽ gps network ,��վ, passive
		//���Ը��ݶ�λ������ ,��ȡ һ����õĶ�λ��ʽ 
		Criteria criteria = new Criteria();
		// ���ö�λ�ľ��� 
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); //��ȡ�����λ��
		criteria.setAltitudeRequired(false); // ������Ϣ
		criteria.setCostAllowed(true); //�����������
		criteria.setPowerRequirement(Criteria.POWER_LOW); //�͹���
		
		//��ȡһ������ϲ�ѯ������λ���ṩ�� 
		String provider  =manager.getBestProvider(criteria, true);
		
		// ע�� λ�øı�ļ����� 
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
	 * ��ȡ�ֻ������һ��λ�� 
	 * @return
	 */
	public String getLastPosition(){
		return sp.getString("lastlocation", "");
	}
	private class MyGPSLinster implements LocationListener{

		// �û�λ�øı��ʱ�� �Ļص����� 
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			//location 
			//��ȡ���û���γ�� 
			double latitude= location.getLatitude();
			double longitude = location.getLongitude();
			String locationstr = "jing du "+ longitude + " weidu  :"+latitude;
		    Editor 	editor =  sp.edit();
		    editor.putString("lastlocation", locationstr);
		    editor.commit();
		}
		// ״̬�ı� 
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
		//gps ,��
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		//�ر�
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}
}
