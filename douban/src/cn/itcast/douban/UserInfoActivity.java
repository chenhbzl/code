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
			Log.i(TAG,"��accesstoken");
		}else {
			Log.i(TAG,"û����֤���򵽵�½����");
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.userinfo);
		setupView();
		
		
		
	}
	
	
	
	/**
	 * �������ݵĲ��� 
	 */
	private void fillData() {
		new AsyncTask<Void, Void, UserEntry>() {

			
			
			// ������ui�߳��� ,�ڵ��� doInbackground����֮ǰִ��,
			// ����������ui�ؼ��Ĳ���
			@Override
			protected void onPreExecute() {
				//��ʾ�û�������������
				super.onPreExecute();
				rl.setVisibility(View.VISIBLE);
			}

			// ���������߳���,ui�߳���, ��doInbackgroundִ����Ϻ�ִ��
			@Override
			protected void onPostExecute(UserEntry ue) {
				// TODO Auto-generated method stub
				super.onPostExecute(ue);
				//��������������ʾ
				rl.setVisibility(View.INVISIBLE);
				// ��ȡ�û������� 
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

			//��̨���еķ��� ,�����ڷ�ui�߳� ����ִ�к�ʱ�Ĳ��� 
			@Override
			protected UserEntry doInBackground(Void... params) {
				try {
					//��ȡ��½�û�������ʵ��
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
