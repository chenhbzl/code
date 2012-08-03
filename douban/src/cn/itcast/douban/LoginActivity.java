package cn.itcast.douban;

import java.util.ArrayList;

import cn.itcast.douban.util.NetUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LoginActivity extends Activity {
	protected static final int NOT_NEED_CAPTCHA = 9;
	protected static final int NEED_CAPTCHA = 10;
	protected static final int CHECK_CAPTCHA_ERROR = 11;
	protected static final int LOGIN_SUCCESS = 12;
	protected static final int LGOIN_ERROR = 13;
	protected static final int DOWNLOAD_CAPTCHA_FINISH= 14;
	protected static final int DOWNLOAD_CAPTCHA_ERROR= 15;
	
	EditText et_email_login;
	EditText et_pwd_login;
	EditText et_captcha_login;
	ImageView iv_captcha_login;
	LinearLayout ll;
	String captcha;
	ProgressDialog pd ;
	SharedPreferences sp;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			pd.dismiss();
			switch (msg.what) {
			case NOT_NEED_CAPTCHA:
			
				break;
				//����֤���Ӧ�Ĳ�����Ϊ�ɼ� 
			case NEED_CAPTCHA:
				ll.setVisibility(View.VISIBLE);
				pd.setMessage("����������֤��");
				pd.show();
				new Thread(new LoadImageTask()).start();
				break;
			case CHECK_CAPTCHA_ERROR:
				Toast.makeText(LoginActivity.this, "�����֤��ʧ��", 1).show();
				break;
				
			case LOGIN_SUCCESS:
				finish();
				break;
			case LGOIN_ERROR:
				Toast.makeText(LoginActivity.this, "��½ʧ��", 1).show();
				break;
			case DOWNLOAD_CAPTCHA_FINISH:
				Bitmap bitmap =  (Bitmap) msg.obj;
				iv_captcha_login.setImageBitmap(bitmap);
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setupView();
		checkNeedCaptcha();

	}

	/**
	 * �ж��Ƿ���Ҫ��֤��
	 */
	private void checkNeedCaptcha() {
		pd.setMessage("����Ƿ���Ҫ��֤��");
		pd.show();
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				try {
					captcha = NetUtil.getCaptcha();
					if (captcha != null) {
						Message msg = new Message();
						msg.what= NEED_CAPTCHA;
						handler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what= NOT_NEED_CAPTCHA;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what= CHECK_CAPTCHA_ERROR;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	private void setupView() {
		// TODO Auto-generated method stub
		pd = new ProgressDialog(this);
		et_email_login = (EditText) findViewById(R.id.et_email_login);
		et_pwd_login = (EditText) findViewById(R.id.et_pwd_login);
		et_captcha_login = (EditText) findViewById(R.id.et_captcha_login);
		iv_captcha_login = (ImageView)findViewById(R.id.iv_captcha_login);
		ll = (LinearLayout) this.findViewById(R.id.ll_login);
	}

	public void btnUserLogin(View view) {
		final String email = et_email_login.getText().toString();
		final String pwd = et_pwd_login.getText().toString();
		final String captcha_solution = et_captcha_login.getText().toString();
		
		if ("".equals(email) || "".equals(pwd)) {
			Toast.makeText(this, "�û����������벻��Ϊ��", 1).show();
			return;
		}
		//�������Ҫ������֤�� 
		if(captcha==null){
			pd.setMessage("���ڵ�½������");
			pd.show();
			new Thread(){
				@Override
				public void run() {
					try {
						 ArrayList<String>  tokens = NetUtil.getAccess(email, pwd,"","",false);
	
						 if(tokens!=null){
							 Editor editor =  sp.edit();
							 editor.putString("accesstoken", tokens.get(0));
							 editor.putString("tokensecret", tokens.get(1));
							 editor.commit();
						 }
						 Message msg =  new Message();
						 msg.what = LOGIN_SUCCESS;
						 handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						 Message msg =  new Message();
						 msg.what = LGOIN_ERROR;
						 handler.sendMessage(msg);
					}
				}
			}.start();
		}
		else {
			pd.setMessage("���ڵ�½������");
			pd.show();
			new Thread(){
				@Override
				public void run() {
					try {
						 ArrayList<String>  tokens = NetUtil.getAccess(email, pwd,captcha_solution,captcha,true);
						 if(tokens!=null){
							 Editor editor =  sp.edit();
							 editor.putString("accesstoken", tokens.get(0));
							 editor.putString("tokensecret", tokens.get(1));
							 editor.commit();
						 }
						 Message msg =  new Message();
						 msg.what = LOGIN_SUCCESS;
						 handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						 Message msg =  new Message();
						 msg.what = LGOIN_ERROR;
						 handler.sendMessage(msg);
					}
				}
			}.start();
		}
	}

	public void btnUserCancle(View view) {
		finish();
	}
	
	// ִ��������֤��Ĳ���
	public class LoadImageTask implements Runnable{

		public void run() {
			// ����http://www.douban.com/misc/captcha?id=MWMeChQ5m0BGRglpbojqcwug&size=s
			String path = "http://www.douban.com/misc/captcha?id="+captcha +"&size=s";
			try {
				Bitmap bitmap = NetUtil.getBitmapImage(path);
				Message msg = new Message();
				msg.what= DOWNLOAD_CAPTCHA_FINISH;
				msg.obj = bitmap;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what= DOWNLOAD_CAPTCHA_ERROR;
				handler.sendMessage(msg);
			}
		}
	}
	
}
