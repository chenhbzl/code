package cn.itcast.douban;
import java.io.IOException;

import cn.itcast.douban.task.LoadImageAsynTask;
import cn.itcast.douban.task.LoadImageAsynTask.ImageTaskCallback;

import com.google.gdata.data.TextContent;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.util.ServiceException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Path.FillType;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UserInfoActivity extends BaseActivity{

	private static final String TAG = "UserInfoActivity";
	ImageView iv_userinfo_icon;
	TextView tv_userinfo_title;
	TextView tv_userinfo_detail;
	TextView tv_userinfo_address;
	RelativeLayout rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		if(isAuthrized()){
			Log.i(TAG,"有accesstoken");
		}else {
			Log.i(TAG,"没有认证定向到登陆界面");
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.userinfo);
		setupView();
		
		
		
	}
	
	
	
	/**
	 * 下载数据的操作 
	 */
	private void fillData() {
		new AsyncTask<Void, Void, UserEntry>() {

			
			
			// 运行在ui线程中 ,在调用 doInbackground方法之前执行,
			// 可以做操作ui控件的操作
			@Override
			protected void onPreExecute() {
				//提示用户正在下载数据
				super.onPreExecute();
				rl.setVisibility(View.VISIBLE);
			}

			// 运行在主线程中,ui线程中, 在doInbackground执行完毕后执行
			@Override
			protected void onPostExecute(UserEntry ue) {
				// TODO Auto-generated method stub
				super.onPostExecute(ue);
				//隐藏下载数据提示
				rl.setVisibility(View.INVISIBLE);
				// 获取用户的名字 
				String title = ue.getTitle().getPlainText();
				tv_userinfo_title.setText(title);
				String address = ue.getLocation();
				tv_userinfo_address.setText(address);
				String detail =((TextContent) ue.getContent()).getContent().getPlainText();
				tv_userinfo_detail.setText(detail);
				String iconpath = ue.getLink("icon", null).getHref();
				new LoadImageAsynTask(new ImageTaskCallback() {
					
					public void onImageLoaded(Bitmap bitmap) {
						if(bitmap!=null){
							iv_userinfo_icon.setImageBitmap(bitmap);
						}else{
							iv_userinfo_icon.setImageResource(R.drawable.ic_launcher);
						}
					}
					
					public void beforeImageLoaded() {
						iv_userinfo_icon.setImageResource(R.drawable.ic_launcher);
					}
				}).execute(iconpath);
				
			}

			//后台运行的方法 ,运行在非ui线程 可以执行耗时的操作 
			@Override
			protected UserEntry doInBackground(Void... params) {
				try {
					//获取登陆用户的数据实体
					UserEntry ue =	myService.getAuthorizedUser();
					return ue;
				} catch (Exception e) {
					e.printStackTrace();
				} 
				return null;
			}
		}.execute();
		
	}

	private void setupView() {
		iv_userinfo_icon= 	(ImageView) this.findViewById(R.id.iv_userinfo_icon);
		tv_userinfo_title=(TextView) this.findViewById(R.id.tv_userinfo_title);
		tv_userinfo_detail=(TextView) this.findViewById(R.id.tv_userinfo_detail);
		tv_userinfo_address=(TextView) this.findViewById(R.id.tv_userinfo_address);
		rl = (RelativeLayout) this.findViewById(R.id.rl_loading);
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		fillData();
	}
	
}
