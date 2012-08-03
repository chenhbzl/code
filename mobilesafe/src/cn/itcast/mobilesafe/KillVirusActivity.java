package cn.itcast.mobilesafe;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.domain.VirusBean;
import cn.itcast.mobilesafe.util.TaskInfo;
import cn.itcast.mobilesafe.util.VirusInfo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class KillVirusActivity extends Activity {
	protected static final int SCANNING = 120;
	protected static final int SCANNING_FINISH =121;
	ImageView iv;
	AnimationDrawable animationDrawable;
	List<VirusBean> virusbeans;
	PackageManager pm;
	List<String> packagenames;
	List<String> virusResult;
	TextView tv_killvirus_info;
	ScrollView sv;
	List<ApplicationInfo> infos;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case SCANNING:
			   StringBuilder sb =	(StringBuilder) msg.obj; 
			   tv_killvirus_info.setText(sb.toString());
			   sv.scrollBy(0, 25);
				break;
			case SCANNING_FINISH:
				 int i =  (Integer) msg.obj;
				StringBuilder sb1  = new StringBuilder();
				 sb1.append("ɨ�����  ��ɨ�� "+ i+ " ������");
				if(virusResult.size()>0){
					sb1.append("���ֲ��� \n");
					   for(String packname : virusResult){
						   sb1.append("������"+  packname);
						   sb1.append("\n");
					   }
				   }
				   tv_killvirus_info.setText(sb1.toString());
				   animationDrawable.stop();
				   
		
				   
				break;
			}
			
			
			
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kill_virus);
		
		tv_killvirus_info = (TextView) this.findViewById(R.id.tv_killvirus_info);
		sv = (ScrollView) this.findViewById(R.id.sv_killvirus);
		
		
		pm = getPackageManager();
		iv = (ImageView) this.findViewById(R.id.iv_killvirus_am);
		
		iv.setBackgroundResource(R.drawable.scanning);
		animationDrawable = (AnimationDrawable) iv.getBackground();

		// ����Ч�����Ų����� animationDrawable.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		packagenames = new ArrayList<String>();
		virusResult = new ArrayList<String>();
		infos = new ArrayList<ApplicationInfo>();
		//����ɨ�財���Ķ��� 
		animationDrawable.start();
		new Thread(){
			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.247:8080/virus.xml");
					HttpURLConnection conn =  (HttpURLConnection) url.openConnection(); 
					InputStream is = conn.getInputStream();
					virusbeans =	VirusInfo.getVirusInfos(is);
					
					TaskInfo taskInfo = new TaskInfo(KillVirusActivity.this);
					
					//��ȡ����ǰ�ֻ��������еİ��� 
					infos = pm.getInstalledApplications(0);
					for(ApplicationInfo info : infos ){
						packagenames.add(info.packageName); 
					}
					
					//���ݲ����� �ȶԵ�ǰϵͳ����ĳ���� ǩ������  ɱ�� 
					int count=0;
					// ɱ������   ���ٵıȶԲ��� ��  �Ͳ�����ǩ�� 
					// 1.������  2.ɱ������ 
					StringBuilder sb = new StringBuilder();
					for(String packname : packagenames){
						
						
						sb.append("����ɨ�� "+ packname);
						sb.append("\n");
						Message msg = new Message();
						msg.what = SCANNING;
						msg.obj  = sb;
						handler.sendMessage(msg);
						//��鵱ǰ��packname �Ͷ�Ӧǩ�� �ǲ��Ǹ��������������Ϣһ�� 
						for(VirusBean virusbean : virusbeans){
							
							if(packname.equals(virusbean.getPackname())&&
									taskInfo.getAppSignature(packname).equals(virusbean.getSignature()))
							{
								virusResult.add(packname);
							}
						}
						
						count ++;
					}
					Message msg = new Message();
					msg.what = SCANNING_FINISH;
					msg.obj  = count;
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}

		}.start();
		return super.onTouchEvent(event);
	}
	public void killVirus(View view) {
		// ����ɨ�財���Ķ���
		animationDrawable.start();
		// todo
		// ��ȡ��������Ϣ, �Աȵ�ǰ�������Ϣ,��ɲ�����ɨ��
	}
}
