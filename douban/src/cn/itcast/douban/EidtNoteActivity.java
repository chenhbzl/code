package cn.itcast.douban;

import java.io.IOException;
import java.net.MalformedURLException;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.douban.NoteEntry;
import com.google.gdata.util.ServiceException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class EidtNoteActivity extends BaseActivity {
	EditText et_new_note_title;
	EditText et_new_note_content;
	NoteEntry noteEntry;
	ProgressDialog pd ;
	RadioButton rb1,rb2,rb3;
	String privacy;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_note);
		pd = new ProgressDialog(this);
		// 获取noteentry的信息
		DoubanApplication doubanApp =  (DoubanApplication) getApplication();
		noteEntry = doubanApp.entry;
		setupView();
	}

	private void setupView() {
		et_new_note_title = (EditText) this.findViewById(R.id.et_new_note_title);
		et_new_note_content = (EditText) this.findViewById(R.id.et_new_note_content);
		rb1 = (RadioButton) this.findViewById(R.id.radio0);
		rb2 = (RadioButton) this.findViewById(R.id.radio1);
		rb3 = (RadioButton) this.findViewById(R.id.radio2);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//把要编辑的日记信息 回显到界面上 ,标题, 日记的内容 
		String title = noteEntry.getTitle().getPlainText();
		String content = noteEntry.getTextContent().getContent().getPlainText();
		
		et_new_note_title.setText(title);
		et_new_note_content.setText(content);
		
	}
	
	public void save(View view){
		final String title = et_new_note_title.getText().toString();
		final String content = et_new_note_content.getText().toString();

		if (rb1.isChecked()) {
			privacy = "private";
		} else if (rb2.isChecked()) {
			privacy = "public";
		} else if (rb3.isChecked()) {
			privacy = "friend";
		}
		new AsyncTask<Void, Void, Boolean>() {
			
			@Override
			protected void onPreExecute() {

				super.onPreExecute();
				pd.setMessage("正在更新日记");
				pd.show();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				pd.dismiss();
				if(result){
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "日记更新失败", 1).show();
				}
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					myService.updateNote(noteEntry, new PlainTextConstruct(title), new PlainTextConstruct(content), privacy, "yes");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} 
			}
		}.execute();
	}

	public void cancle(View view){
		String title = et_new_note_title.getText().toString();
		String content = et_new_note_content.getText().toString();
		if ("".equals(title) && "".equals(content)) {
			finish();
		} else {
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("警告");
			builder.setMessage("有未保存的数据,是否确定退出?");
			builder.setPositiveButton("确定", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					finish();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.create().show();
			
		}
	}
}
