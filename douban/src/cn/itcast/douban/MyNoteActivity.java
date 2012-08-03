package cn.itcast.douban;

import java.util.List;

import com.google.gdata.data.douban.NoteEntry;
import com.google.gdata.data.douban.NoteFeed;
import com.google.gdata.util.ServiceException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyNoteActivity extends BaseActivity {

	protected static final int DELETE_SUCCESS = 30;
	protected static final int DELETE_ERROR =  31;
	private static final String TAG = "MyNoteActivity";
	ListView lv;
	RelativeLayout rl;
	LayoutInflater inflater;
	static int maxResult = 10;
	static int startIndex = 1;
	NoteAdapter noteAdapter;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DELETE_SUCCESS:
				startIndex = 1;
				total = 0;
				noteAdapter = null;
				fillData();
				break;
			case DELETE_ERROR:
				Toast.makeText(getApplicationContext(), "ɾ������ʧ��", 1).show();
				break;
			}
			
		}
		
	};
	// ��¼listview������Ŀ�ĸ���
	int total;
	boolean isfillingdata = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		startIndex = 1;
		total = 0;
		setContentView(R.layout.note);
		inflater = LayoutInflater.from(this);
		//��preferncesetting �����ȡ���õ�maxResult ��ֵ 
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String strnumber = sp.getString("load_note_number", "10");
		maxResult = Integer.parseInt(strnumber);
		setupView();

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.note_menu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting, menu);
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_setting:
			Log.i(TAG,"����setting����");
			Intent intent = new Intent(this,SettingActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// AdapterContextMenuInfo
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final int position = (int) info.id;

		switch (item.getItemId()) {
		case R.id.menu_edit_note:
			//���� noteentry�Ķ���
			   DoubanApplication doubanApp = (DoubanApplication) getApplication();
			   //��noteentry�Ķ��� ���õ��� application�ļ����� 
			   doubanApp.entry = (NoteEntry) lv.getItemAtPosition(position);
			//����༭��־�Ľ���.
			Intent editIntent = new Intent(this,EidtNoteActivity.class);
			startActivityForResult(editIntent, 0);

			//��Ҫ�༭����־��title �� ����ȡ����
			// myservice.updateNote();
			break;
		case R.id.menu_new_note:
			Intent newnoteintent = new Intent(this,NewNoteActivity.class);
			startActivityForResult(newnoteintent, 0);
			break;
		case R.id.menu_delete_note:
			// ��ȡ�ռ�����Ӧ��noteentry

			new Thread(){
				@Override
				public void run() {
					NoteEntry ne = (NoteEntry) lv.getItemAtPosition(position); 
					try {
						myService.deleteNote(ne);
						Message msg = new Message();
						msg.what = DELETE_SUCCESS;
						handler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = DELETE_ERROR;
						handler.sendMessage(msg);
					} 
				}
			}.start();
			break;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		startIndex = 1;
		noteAdapter = null;
		
		
	}

	private void setupView() {
		lv = (ListView) findViewById(R.id.lv_note);
		registerForContextMenu(lv);
		// ע��listview�Ĺ����¼�
		lv.setOnScrollListener(new OnScrollListener() {

			// ��listview�Ĺ���״̬�����ı��ʱ�����
			// view ����������ǵ�ǰ��listview
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					// �жϵ�ǰ�ɼ������һ��item �ǲ��� listview����ʾ�����һ��item
					// ��ȡ��ǰlistview���һ���ɼ���Ŀ�� λ��
					int position = view.getLastVisiblePosition();
					if ((position + 1) == total) {
						if (isfillingdata) {
							return;
						} else {
							startIndex += maxResult;
							fillData();
						}
					}
					break;
				}
			}

			// �ڹ�����ʱ��
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		rl = (RelativeLayout) findViewById(R.id.rl_note_loading);
	}

	@Override
	protected void onStart() {
		super.onStart();
		fillData();
	}

	/**
	 * ����ռ�����
	 */
	private void fillData() {
		new AsyncTask<Void, Void, NoteFeed>() {

			@Override
			protected void onPreExecute() {
				isfillingdata = true;
				super.onPreExecute();
				AnimationSet set = new AnimationSet(false);
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(1000);
				AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
				aa.setDuration(1000);
				set.addAnimation(aa);
				set.addAnimation(sa);
				rl.setAnimation(set);
				rl.setVisibility(View.VISIBLE);
			}

			@Override
			protected void onPostExecute(NoteFeed feeds) {
				isfillingdata = false;
				super.onPostExecute(feeds);
				AnimationSet set = new AnimationSet(false);
				ScaleAnimation sa = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f);
				sa.setDuration(1000);
				AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
				aa.setDuration(1000);
				set.addAnimation(aa);
				set.addAnimation(sa);
				rl.setAnimation(set);
				rl.setVisibility(View.INVISIBLE);
				if (feeds != null) {
					if (noteAdapter == null) {
						// ��ʼ�� listview������������
						noteAdapter = new NoteAdapter(feeds);
						lv.setAdapter(noteAdapter);
					} else {
						noteAdapter.addMoreNote(feeds);
						// ֪ͨ ���������� �ռ����ݷ����˸ı�
						noteAdapter.notifyDataSetChanged();
					}
				} else {
					Toast.makeText(getApplicationContext(), "��ȡ�ռ�ʧ��", 1).show();
				}
			}

			@Override
			protected NoteFeed doInBackground(Void... params) {
				try {
					String uid = myService.getAuthorizedUser().getUid();
					// ��ȡ�ӵ�һ���ռǿ�ʼ��10����Ŀ���ռ�
					NoteFeed feeds = myService.getUserNotes(uid, startIndex,
							maxResult);
					// ���µ�ǰlistview����Ԫ�ص���Ŀ
					total += maxResult;
					return feeds;

				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute();

	}

	// ���������ռ���Ŀ������������
	public class NoteAdapter extends BaseAdapter {
		List<NoteEntry> notes;

		/**
		 * ���»�ȡ��noteEntry����Ϣ �ӵ� ��ǰ��listview�������������ļ�������
		 * 
		 * @param feeds
		 */
		public void addMoreNote(NoteFeed feeds) {
			for (NoteEntry note : feeds.getEntries()) {
				notes.add(note);
			}
		}

		public NoteAdapter(NoteFeed feeds) {
			// �ռǼ��ϵĳ�ʼ��
			notes = feeds.getEntries();
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return notes.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return notes.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = inflater.inflate(R.layout.me_item, null);
			} else {
				view = convertView;
			}
			TextView tv = (TextView) view.findViewById(R.id.tv_me_acitvity);
			tv.setText(notes.get(position).getTitle().getPlainText());

			return view;
		}

	}
}
