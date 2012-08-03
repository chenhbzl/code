package cn.itcast.mobilesafe;

import java.util.List;

import cn.itcast.mobilesafe.adapter.BlackNumberListAdapter;
import cn.itcast.mobilesafe.db.dao.BlackNumberDao;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CallSmsSafeActivity extends Activity{
	ListView lv_call_sms_safe; 
	BlackNumberDao blackNumberDao;
	BlackNumberListAdapter blackNumberListAdapter;
	List<String> blacknumbers;
	AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.callsmssafe);
		blackNumberDao = new BlackNumberDao(this);
		blacknumbers = blackNumberDao.findAll();
		blackNumberListAdapter = new BlackNumberListAdapter(this,blacknumbers);
		lv_call_sms_safe = (ListView) this.findViewById(R.id.lv_call_sms_safe);
		lv_call_sms_safe.setAdapter(blackNumberListAdapter);
		lv_call_sms_safe.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String number = blacknumbers.get(position);
				blackNumberDao.delete(number);
				blacknumbers = blackNumberDao.findAll();
				blackNumberListAdapter.setBlackNumbers(blacknumbers);
//				blackNumberListAdapter = new BlackNumberListAdapter(CallSmsSafeActivity.this,blacknumbers);
//				lv_call_sms_safe.setAdapter(blackNumberListAdapter);
				blackNumberListAdapter.notifyDataSetChanged();
				return false;
			}	
		});
	}
	
	
	/*
	 * ��Ӻ����� 
	 */
	public void addBlackNumber(View view){
		AlertDialog.Builder builder = new Builder(this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.layout(10, 10, 90, 90);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final EditText et = new EditText(this);
		et.setHint("����������");
		linearLayout.addView(et);
		Button bt = new Button(this);
		bt.setText("��Ӻ�������Ϣ");
		linearLayout.addView(bt);
		bt.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String blacknumber = et.getText().toString();
				blackNumberDao.save(blacknumber);
				//��ȡ���µ����� 
				blacknumbers = blackNumberDao.findAll();
				//����listview��������,����Դ�����˸ı� 
				blackNumberListAdapter.notifyDataSetChanged();
				blackNumberListAdapter.setBlackNumbers(blacknumbers);
//				blackNumberListAdapter = new BlackNumberListAdapter(CallSmsSafeActivity.this,blacknumbers);
//				lv_call_sms_safe.setAdapter(blackNumberListAdapter);
				dialog.dismiss();
			}
		});
		builder.setView(linearLayout);
		
		dialog = builder.create();
		dialog.show();
	}
	
	
}
