package cn.itcast.mobilesafe.adapter;

import java.util.List;

import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.service.ContactInfoService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 通过系统提供的content provider获取到手机联系人信息 
 * @author Administrator
 *
 */


public class ContactListAdapter extends BaseAdapter {
	private Context context;
	List<ContactInfo> contacts;
	LayoutInflater inflater;

	public ContactListAdapter(Context context) {
		this.context = context;
		ContactInfoService contactInfoService = new ContactInfoService(context);
		contacts = contactInfoService.getContacts();
		inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	//返回某个位置对应的item
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contacts.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.contact_list_item, null);
		TextView tv_name = (TextView) view.findViewById(R.id.contact_name);
		TextView tv_number = (TextView) view.findViewById(R.id.contact_number);
		
		tv_name.setText(contacts.get(position).getName());
		tv_number.setText(contacts.get(position).getNumber());
		return view;
	}

}
