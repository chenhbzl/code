package cn.itcast.mobilesafe.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.util.Logger;

public class BlackNumberListAdapter extends BaseAdapter {
	private static final String TAG = "BlackNumberListAdapter";
	private List<String> blacknumbers;
	private Context context;

	public void setBlackNumbers(List<String> blacknumbers){
		this.blacknumbers = blacknumbers;
	}
	
	public BlackNumberListAdapter(Context context, List<String> blacknumbers) {
		this.blacknumbers = blacknumbers;
		this.context = context;
	}

	public int getCount() {

		return blacknumbers.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return blacknumbers.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder ViewHolder = new ViewHolder();
		ViewHolder.tv= null;
		if (convertView == null) {
			Logger.i(TAG, "new textview ");
			ViewHolder.tv = new TextView(context);
		}else{
			ViewHolder.tv= (TextView)convertView;
			Logger.i(TAG, "use old textview ");
		}
		ViewHolder.tv.setText(blacknumbers.get(position));
		ViewHolder.tv.setBackgroundResource(R.drawable.focused_bg);
		return ViewHolder.tv;
	}
	static class ViewHolder{
		TextView tv;
	}

}
