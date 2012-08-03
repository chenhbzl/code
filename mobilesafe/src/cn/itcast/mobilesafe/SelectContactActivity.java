package cn.itcast.mobilesafe;

import cn.itcast.mobilesafe.adapter.ContactListAdapter;
import cn.itcast.mobilesafe.domain.ContactInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectContactActivity extends Activity {
	ListView lv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactlist);
		lv = (ListView) this.findViewById(R.id.contact_lv);
		lv.setAdapter(new ContactListAdapter(this));
		//给listview的每个item注册点击事件
		
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ContactInfo info = (ContactInfo) lv.getItemAtPosition(position);
				String number = info.getNumber();
				Intent intent = new Intent();
				intent.putExtra("number", number);
				
				//把intent里面的数据返回给调用的activity
				setResult(0, intent);
				finish();
			}
		});
	}

}
