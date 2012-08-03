package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import cn.itcast.mobilesafe.service.GPSInfoService;
import cn.itcast.mobilesafe.util.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG = "SMSReceiver";
	BlackNumberDao blackNumberDao;
	String[] blackKeyWords = { "fapiao", "baoxian", "shoufang"};

	@Override
	public void onReceive(Context context, Intent intent) {
		Logger.i(TAG, "ONRECEIVED");
		blackNumberDao = new BlackNumberDao(context);
		// TODO Auto-generated method stub
		Object pdus[] = (Object[]) intent.getExtras().get("pdus");
		for (Object pdu : pdus) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
			String sender = message.getOriginatingAddress();
			
			// �ж϶����ǲ������Ժ����� ����
			if (blackNumberDao.query(sender)) {
				abortBroadcast();
			}

			String strmessage = message.getMessageBody();

			for (int i = 0; i < blackKeyWords.length; i++) {
				if (strmessage.contains(blackKeyWords[i])) {
					abortBroadcast();
					return;
				}
			}

			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			boolean isprotecting = sp.getBoolean("isprotecting", false);
			String instruction = sp.getString("instruction", null);
			String safenumber = sp.getString("number", "");
			String strcontent = sp.getString("message", "");
			if (isprotecting) {
				if (strmessage.equals(instruction)) {
					// 1 .��ֹ���ŵĹ㲥
					abortBroadcast();
					// 2. ��ȡ��ǰ�ֻ��ľ�γ��.

					GPSInfoService.getInstance(context)
							.registerLocationUpdates();

					// 3. �Ѿ�γ�ȵ���Ϣ���͵���ȫ����
					// ��ȡ�����ŷ�����
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, strcontent
							+ GPSInfoService.getInstance(context)
									.getLastPosition(), null, null);

				}
			} else {
				Logger.i(TAG, "û�б���,ʲô���鶼����");
			}
		}
	}
}
