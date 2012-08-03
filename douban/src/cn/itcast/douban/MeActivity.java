package cn.itcast.douban;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MeActivity extends Activity {
	ListView lv;
	public static final String[] arrays = {"�Ҷ�","�ҿ�","����","����","�ҵ��ռ�","�ҵ�����","�ҵ�С��"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me);
		lv = (ListView) this.findViewById(R.id.lv_melist);
		Drawable divider = new ColorDrawable(Color.TRANSPARENT);
		lv.setDivider(divider);
		lv.setDividerHeight(8);
		lv.setAdapter(new ArrayAdapter<String>(this, R.layout.me_item, R.id.tv_me_acitvity, arrays));

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (position) {
				
				case 0:
					Intent myreadintent = new Intent(MeActivity.this,MyReadActivity.class);
					startActivity(myreadintent);
					break;
				case 4:
					Intent noteintent = new Intent(MeActivity.this,MyNoteActivity.class);
					startActivity(noteintent);
					break;
				
				case 5:
					Intent userInfointent = new Intent(MeActivity.this,UserInfoActivity.class);
					startActivity(userInfointent);
					break;


				}
			}
		});
	
	}
	/**
	 * �˳�Ӧ�ó������ʾ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		System.out.println(event.getKeyCode());
		if (event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("����");
			builder.setMessage("�Ƿ��˳�����");
			builder.setPositiveButton("ȷ��", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					finish();
					
				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			
			builder.create().show();
		}
		return super.onKeyDown(keyCode, event);
	}

}
