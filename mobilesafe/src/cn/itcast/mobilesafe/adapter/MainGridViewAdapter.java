package cn.itcast.mobilesafe.adapter;


import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.util.Logger;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// 完成gridview 数据到界面的适配 
public class MainGridViewAdapter extends BaseAdapter {
	private static final String TAG = "MainGridViewAdapter";
	private String[] names = {"手机防盗","通讯卫士","软件管理","任务管理","上网管理","手机杀毒","系统优化","高级工具","设置中心"};
	private int[] icons = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	private Context context;
	LayoutInflater infalter;
	
	public MainGridViewAdapter(Context context) {
		this.context = context;
		//方法1 通过系统的service 获取到 试图填充器 
		//infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//方法2 通过layoutinflater的静态方法获取到 视图填充器
		infalter = LayoutInflater.from(context);
	}

	// 返回gridview里面有多少个条目 
	public int getCount() {
		// TODO Auto-generated method stub
		return names.length;
	}

	//返回某个position对应的条目 
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//返回某个position对应的id
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//返回某个位置对应的视图 
	//问题: getview被调用了多少次?  根据 一个item占用的空间 和 当前的 gridview的大小决定  
	// listview getView   根据 一个item占用的空间 和 当前的 listview的大小决定   
	// listview / item 大小  + 1 ;
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.i(TAG,"GETVIEW "+ position);
		//把一个布局文件转换成视图
		//获取view
		
		
		View view = infalter.inflate(R.layout.mainactivity_item, null);
	    ImageView iv =	(ImageView) view.findViewById(R.id.main_gv_iv);
	    TextView  tv = (TextView) view.findViewById(R.id.main_gv_tv);
		//设置每一个item的名字和图标 
	    iv.setImageResource(icons[position]);
	    //if (position=0 , sharedpreference 里面存放的有string )
	    // tv.setText("mp3播放");
	    
		tv.setText(names[position]);
		// TODO Auto-generated method stub
		return view;
	}
}
