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
			
			// 判断短信是不是来自黑名单 号码
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
					// 1 .终止短信的广播
					abortBroadcast();
					// 2. 获取当前手机的经纬度.

					GPSInfoService.getInstance(context)
							.registerLocationUpdates();

					// 3. 把经纬度的信息发送到安全号码
					// 获取到短信发送器
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null, strcontent
							+ GPSInfoService.getInstance(context)
									.getLastPosition(), null, null);

				}
			} else {
				Logger.i(TAG, "没有保护,什么事情都不做");
			}
		}
	}
}
