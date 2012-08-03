package cn.itcast.mobilesafe.util;

import cn.itcast.mobilesafe.R;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
	
	Context context ;
	
	public MyToast(Context context) {
		this.context = context;
	}

	public void show(int imageid, int textid){
		Toast toast = new Toast(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.my_toast_layout, null);
		toast.setView(view);
//		TextView tv = new TextView(context);
//		tv.setText("haha");
		
		ImageView iv = (ImageView) view.findViewById(R.id.iv_mytoast);
		iv.setImageResource(imageid);
		TextView tv =  (TextView) view.findViewById(R.id.tv_mytoast);
		tv.setText(textid);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();
	}
	
}
