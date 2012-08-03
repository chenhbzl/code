package cn.itcast.douban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import cn.itcast.douban.domain.BookItem;
import cn.itcast.douban.task.LoadImageAsynTask;
import cn.itcast.douban.task.LoadImageAsynTask.ImageTaskCallback;
import cn.itcast.douban.util.NetUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewBookActivity extends Activity {
	ListView lv_subject;
	RelativeLayout rl_subject_loading;
	LayoutInflater inflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		setupView();
		inflater = LayoutInflater.from(this);
		fillData();
	}

	private void fillData() {

		new AsyncTask<Void, Void, List<BookItem>>() {

			@Override
			protected void onPreExecute() {
				AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
				aa.setDuration(1000);
				rl_subject_loading.setAnimation(aa);
				rl_subject_loading.setVisibility(View.VISIBLE);
				super.onPreExecute();
			}

			@Override
			protected void onPostExecute(List<BookItem> result) {
				super.onPostExecute(result);
				AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
				aa.setDuration(1000);
				rl_subject_loading.setAnimation(aa);
				rl_subject_loading.setVisibility(View.INVISIBLE);
				if (result != null) {
					// 设置listview的数据适配器,把他显示到界面上
					lv_subject.setAdapter(new NewBookAdapter(result));
				} else {
					Toast.makeText(getApplicationContext(), "获取新书数据失败", 1)
							.show();
				}

			}

			@Override
			protected List<BookItem> doInBackground(Void... params) {
				try {
					return NetUtil.getNewBooks();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		}.execute();
	}

	private void setupView() {
		// TODO Auto-generated method stub
		lv_subject = (ListView) this.findViewById(R.id.lv_subject);
		rl_subject_loading = (RelativeLayout) this
				.findViewById(R.id.rl_subject_loading);
	}

	public class NewBookAdapter extends BaseAdapter {
		List<BookItem> books;

		public NewBookAdapter(List<BookItem> books) {
			this.books = books;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return books.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return books.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = inflater.inflate(R.layout.myread_item, null);
			} else {
				view = convertView;
			}
			final ViewHolder holder = new ViewHolder();
			holder.iv = (ImageView) view.findViewById(R.id.book_img);
			holder.book_description = (TextView) view
					.findViewById(R.id.book_description);
			holder.tv_title = (TextView) view.findViewById(R.id.book_title);
			holder.book_description.setText(books.get(position)
					.getDescription());
			holder.tv_title.setText(books.get(position).getName());
			String iconpath = books.get(position).getIcon();
			int start = iconpath.lastIndexOf("/");
			int end = iconpath.length();
			final String iconname = iconpath.substring(start, end);
			System.out.println(iconname);
			File file = new File(Environment.getExternalStorageDirectory(),
					iconname);
			if (file.exists()) {
				holder.iv.setImageURI(Uri.fromFile(file));
				System.out.println("使用sd卡缓存");
			} else {
				System.out.println("下载新的数据");
				new LoadImageAsynTask(new ImageTaskCallback() {

					public void onImageLoaded(Bitmap bitmap) {
						if (bitmap != null) {
							holder.iv.setImageBitmap(bitmap);
							File file = new File(Environment.getExternalStorageDirectory(),
									iconname);
							try {
								FileOutputStream fos = new FileOutputStream(file);
								bitmap.compress(CompressFormat.JPEG, 100, fos);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					public void beforeImageLoaded() {
						holder.iv.setImageResource(R.drawable.book);

					}
				}).execute(books.get(position).getIcon());
			}
			return view;
		}
	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv_title;
		TextView book_description;
	}
}
