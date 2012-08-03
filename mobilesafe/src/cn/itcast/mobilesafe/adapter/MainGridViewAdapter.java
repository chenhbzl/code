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

// ���gridview ���ݵ���������� 
public class MainGridViewAdapter extends BaseAdapter {
	private static final String TAG = "MainGridViewAdapter";
	private String[] names = {"�ֻ�����","ͨѶ��ʿ","�������","�������","��������","�ֻ�ɱ��","ϵͳ�Ż�","�߼�����","��������"};
	private int[] icons = {R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings};
	private Context context;
	LayoutInflater infalter;
	
	public MainGridViewAdapter(Context context) {
		this.context = context;
		//����1 ͨ��ϵͳ��service ��ȡ�� ��ͼ����� 
		//infalter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//����2 ͨ��layoutinflater�ľ�̬������ȡ�� ��ͼ�����
		infalter = LayoutInflater.from(context);
	}

	// ����gridview�����ж��ٸ���Ŀ 
	public int getCount() {
		// TODO Auto-generated method stub
		return names.length;
	}

	//����ĳ��position��Ӧ����Ŀ 
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//����ĳ��position��Ӧ��id
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	//����ĳ��λ�ö�Ӧ����ͼ 
	//����: getview�������˶��ٴ�?  ���� һ��itemռ�õĿռ� �� ��ǰ�� gridview�Ĵ�С����  
	// listview getView   ���� һ��itemռ�õĿռ� �� ��ǰ�� listview�Ĵ�С����   
	// listview / item ��С  + 1 ;
	public View getView(int position, View convertView, ViewGroup parent) {
		Logger.i(TAG,"GETVIEW "+ position);
		//��һ�������ļ�ת������ͼ
		//��ȡview
		
		
		View view = infalter.inflate(R.layout.mainactivity_item, null);
	    ImageView iv =	(ImageView) view.findViewById(R.id.main_gv_iv);
	    TextView  tv = (TextView) view.findViewById(R.id.main_gv_tv);
		//����ÿһ��item�����ֺ�ͼ�� 
	    iv.setImageResource(icons[position]);
	    //if (position=0 , sharedpreference �����ŵ���string )
	    // tv.setText("mp3����");
	    
		tv.setText(names[position]);
		// TODO Auto-generated method stub
		return view;
	}
}
