package cn.itcast.mobilesafe.receiver;

import cn.itcast.mobilesafe.LostProtectedActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.sax.StartElementListener;

public class CallPhoneRecevier extends BroadcastReceiver {
	SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String number = getResultData();
      System.out.println(number);
      sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
      String ipnumber =  sp.getString("ipnumber", "");
      
      String newnumber = ipnumber+number;
      setResultData(newnumber);
      if ("20182018".equals(number)){
    	 // abortBroadcast();
    	  //outgoingcall 
    	  //把拨打电话的结果集置为空
    	  setResultData(null);
    	  Intent lostintent = new Intent(context,LostProtectedActivity.class);
    	  lostintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	  context.startActivity(lostintent);
      }
	}
}
