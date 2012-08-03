package cn.itcast.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import cn.itcast.mobilesafe.util.Logger;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAddressService extends Service {
	TelephonyManager telephonyManager;
	AddressService addressService;
	LayoutInflater inflater;
	WindowManager windowManager;
	BlackNumberDao blackNumberDao;
	SharedPreferences sp;
	public static final String TAG = "ShowAddressService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		addressService = new AddressService();
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		blackNumberDao = new BlackNumberDao(getApplicationContext());
		inflater = LayoutInflater.from(getApplicationContext());
		// ��ȡ��ϵͳ�ṩ�� �绰�ķ���
		telephonyManager = (TelephonyManager) getApplicationContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		windowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		telephonyManager.listen(new MyPhoneLinstener(),
				PhoneStateListener.LISTEN_CALL_STATE);

	}

	public class MyPhoneLinstener extends PhoneStateListener {
		View view = null;
		// ���绰�����״̬�ı��ʱ�����õĻص�����
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			Logger.i(TAG, incomingNumber);

			switch (state) {
			
			case TelephonyManager.CALL_STATE_RINGING:
				String address = addressService.getAddress(incomingNumber);
				/*
				 * ��ѯ���ݿ�,�ж� ��ǰ������� �Ƿ��Ǻ��������� 
				 */
				boolean isblacknumber = blackNumberDao.query(incomingNumber);
				if(isblacknumber){
					//�Ҷϵ绰 
					try {
						endCall();
						// ע��һ�����ݹ۲���,�۲�calllog�������Ϣ�仯 
						ContentResolver resolver = getApplicationContext().getContentResolver();
						resolver.registerContentObserver(CallLog.Calls.CONTENT_URI, 
								true, new CallLogChangeListener(new Handler(),incomingNumber));
						
						//deleteCallLog(incomingNumber);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				
				view = inflater.inflate(R.layout.show_address, null);
				TextView tv_address = (TextView) view
						.findViewById(R.id.tv_show_address);
				TextView tv_number = (TextView) view
						.findViewById(R.id.tv_show_number);
				tv_address.setText(address);
				//��ѯ�ú����Ӧ������ 
				String numbername = queryNumberName(incomingNumber);
				tv_number.setText(numbername);
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				 //��ȡƫ���� 
				int dx= sp.getInt("dx", 0);
				int dy = sp.getInt("dy", 0);
				System.out.println("dx =" + dx);
				System.out.println("dy =" + dx);
				params.x=dx+params.x ;
				params.y = dy+params.y;
				
				params.type = WindowManager.LayoutParams.TYPE_TOAST;
				// add window bad token
				windowManager.addView(view, params);
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (view != null) {
					windowManager.removeView(view);
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		telephonyManager.listen(null,PhoneStateListener.LISTEN_CALL_STATE);
		super.onDestroy();
	}

	
	/**
	 * ͨ�������ṩ�� ��ѯ��ǰ�ֻ���������Ӧ������
	 * @param incomingNumber
	 */
	public String queryNumberName(String incomingNumber) {

		Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+incomingNumber);
		ContentResolver  resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, 
				new String[]{"display_name"},
				null,
				null, 
				null);
		
		if(cursor.moveToFirst()){
		 String phonename =	cursor.getString(0);
		 cursor.close();
		 return phonename;
		}
		return incomingNumber;
	}

	public void deleteCallLog(String incomingNumber ) {
		// TODO Auto-generated method stub
		ContentResolver resolver = getApplicationContext().getContentResolver();
		Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI, 
				new String[]{"_id"}, 
				"number=?", 
				new String[]{incomingNumber}, 
				null);
		//�����ȡͨ���Ľ�� 
		if(cursor.moveToFirst()){
			resolver.delete(CallLog.Calls.CONTENT_URI, "_id="+cursor.getInt(0), null);
		}
	}

	/*
	 * �Ҷϵ绰 
	 */
	public void endCall() throws Exception{
		// TODO Auto-generated method stub
		Method method = Class.forName("android.os.ServiceManager")
				.getMethod("getService", String.class);
		IBinder binder = (IBinder) method.invoke(null,
				new Object[] { Context.TELEPHONY_SERVICE });
		ITelephony iTelephony = ITelephony.Stub.asInterface(binder);
		iTelephony.endCall();
		
	}
	/**
	 * ���ע��������ṩ�ߵĹ۲��� 
	 * @author Administrator
	 *
	 */
	public void unregisterContentObserver(CallLogChangeListener observer ){
		getApplicationContext().getContentResolver().unregisterContentObserver(observer);
	}

	public class CallLogChangeListener extends ContentObserver{
		private String incomingNumber;
		public CallLogChangeListener(Handler handler,String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
			// TODO Auto-generated constructor stub
		}

		/*
		 * �����ݷ����ı�� �����onchange
		 */
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			deleteCallLog(incomingNumber);
			//����� ��ǰ�����ݹ۲��� 
			unregisterContentObserver(this);
		}
	}
}
