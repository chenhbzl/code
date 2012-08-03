package cn.itcast.douban;

import java.io.IOException;

import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.douban.NoteEntry;
import com.google.gdata.util.ServiceException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewNoteActivity extends BaseActivity {
	EditText et_new_note_title;
	EditText et_new_note_content;
	RadioButton rb1, rb2, rb3;
	String privacy;
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_note);
		pd = new ProgressDialog(this);
		pd.setMessage("正在发表日记");
		setupView();

	}

	private void setupView() {
		et_new_note_title = (EditText) this
				.findViewById(R.id.et_new_note_title);
		et_new_note_content = (EditText) this
				.findViewById(R.id.et_new_note_content);
		rb1 = (RadioButton) this.findViewById(R.id.radio0);
		rb2 = (RadioButton) this.findViewById(R.id.radio1);
		rb3 = (RadioButton) this.findViewById(R.id.radio2);

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void save(View view) {
		final String title = et_new_note_title.getText().toString();
		final String content = et_new_note_content.getText().toString();

		if (rb1.isChecked()) {
			privacy = "private";
		} else if (rb2.isChecked()) {
			privacy = "public";
		} else if (rb3.isChecked()) {
			privacy = "friend";
		}
		if ("".equals(title) || "".equals(content)) {
			Toast.makeText(this, "标题或者内容不能为空", 1).show();
		} else {
			new AsyncTask<Void, Void, Boolean>() {
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
					pd.show();
				}

				@Override
				protected void onPostExecute(Boolean result) {
					// TODO Auto-generated method stub
					pd.dismiss();
					if (result) {
						finish();
					} else {
						Toast.makeText(getApplicationContext(), "发表日志失败", 1).show();
					}
					super.onPostExecute(result);
				}

				@Override
				protected Boolean doInBackground(Void... params) {
					try {
						myService
								.createNote(new PlainTextConstruct(title),
										new PlainTextConstruct(content),
										privacy, "yes");
						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}

				}
			}.execute();
		}

	}

	public void cancle(View view) {
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
