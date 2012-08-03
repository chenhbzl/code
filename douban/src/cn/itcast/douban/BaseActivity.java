package cn.itcast.douban;

import com.google.gdata.client.douban.DoubanService;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class BaseActivity extends Activity {
	SharedPreferences sp;
	DoubanService myService;
	ImageButton ib;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

	}
	
	
	
	/**
	 * 用户可见
	 */
	@Override
	protected void onStart() {
		super.onStart();
		ib = (ImageButton) this.findViewById(R.id.back_button);
		String apiKey = "0acd20946740ceb0206761bd35c26c10";
		String secret = "b7feacef6b845643";
		// 利用豆瓣api生成豆瓣的service
		myService = new DoubanService("我的小豆豆", apiKey,
				secret);
		String accesstoken = sp.getString("accesstoken", "");
		String  tokensecret = sp.getString("tokensecret", "");
		myService.setAccessToken(accesstoken, tokensecret);
		
		if(ib==null){
			
		}else{
			ib.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {	
					System.out.println("imagebutton clicked");
					finish();
				}
			});
		}
	}




	public boolean isAuthrized(){
		String accesstoken = sp.getString("accesstoken", "");
		String  tokensecret = sp.getString("tokensecret", "");
		if("".equals(accesstoken)||"".equals(tokensecret)){
			return false;
		}else{
			return true;
		}
	}

}
