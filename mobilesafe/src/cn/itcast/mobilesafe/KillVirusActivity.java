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
				 sb1.append("扫描完毕  共扫描 "+ i+ " 个程序");
				if(virusResult.size()>0){
					sb1.append("发现病毒 \n");
					   for(String packname : virusResult){
						   sb1.append("病毒名"+  packname);
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

		// 动画效果播放不出来 animationDrawable.start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		packagenames = new ArrayList<String>();
		virusResult = new ArrayList<String>();
		infos = new ArrayList<ApplicationInfo>();
		//播放扫描病毒的动画 
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
					
					//获取到当前手机里面所有的包名 
					infos = pm.getInstalledApplications(0);
					for(ApplicationInfo info : infos ){
						packagenames.add(info.packageName); 
					}
					
					//根据病毒库 比对当前系统里面的程序包 签名进行  杀毒 
					int count=0;
					// 杀毒引擎   快速的比对病毒 库  和病毒的签名 
					// 1.病毒库  2.杀毒引擎 
					StringBuilder sb = new StringBuilder();
					for(String packname : packagenames){
						
						
						sb.append("正在扫描 "+ packname);
						sb.append("\n");
						Message msg = new Message();
						msg.what = SCANNING;
						msg.obj  = sb;
						handler.sendMessage(msg);
						//检查当前的packname 和对应签名 是不是跟病毒库里面的信息一样 
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
		// 播放扫描病毒的动画
		animationDrawable.start();
		// todo
		// 获取病毒库信息, 对比当前程序的信息,完成病毒的扫描
	}
}
