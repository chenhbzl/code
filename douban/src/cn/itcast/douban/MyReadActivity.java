package cn.itcast.douban;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itcast.douban.task.LoadImageAsynTask;
import cn.itcast.douban.task.LoadImageAsynTask.ImageTaskCallback;

import com.google.gdata.data.douban.Attribute;
import com.google.gdata.data.douban.CollectionEntry;
import com.google.gdata.data.douban.CollectionFeed;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.ServiceException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyReadActivity extends BaseActivity {
	public static final String TAG = "MyReadActivity";
	ListView lv_subject;
	RelativeLayout rl;
	LayoutInflater infalter;
	Map<String,SoftReference<Bitmap>> map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.subject);
		map = new HashMap<String, SoftReference<Bitmap>>();
		infalter = LayoutInflater.from(this);
		setupView();
	}

	private void setupView() {
		lv_subject = (ListView) this.findViewById(R.id.lv_subject);
		rl = (RelativeLayout) this.findViewById(R.id.rl_subject_loading);
		
		// 注册listview的点击时间 
		lv_subject.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CollectionEntry ce = (CollectionEntry) lv_subject.getItemAtPosition(position);
				List<Attribute> attributes = ce.getSubjectEntry().getAttributes();
				for(Attribute attribute: attributes){
					if("isbn13".equals(attribute.getName())){
						Intent intent = new Intent(MyReadActivity.this,BookDetailActivity.class);
						intent.putExtra("isbn", attribute.getContent());
						startActivity(intent);
					}
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// 下载数据的操作
		fillData();
	}

	private void fillData() {
		new AsyncTask<Void, Void, CollectionFeed>() {
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
				aa.setDuration(2000);
				rl.setAnimation(aa);
				rl.setVisibility(View.VISIBLE);
			}

			@Override
			protected void onPostExecute(CollectionFeed result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
				aa.setDuration(2000);
				rl.setAnimation(aa);
				rl.setVisibility(View.INVISIBLE);
				if(result==null){
					Toast.makeText(MyReadActivity.this, "获取数据失败", 1).show();
				}else{
					lv_subject.setAdapter(new MyReadAdapter(result));
				}
				
			}

			@Override
			protected CollectionFeed doInBackground(Void... params) {
				try {
					String uid = myService.getAuthorizedUser().getUid();
					CollectionFeed feeds = myService.getUserCollections(uid, "book", "", null, 1, 30);
					return feeds;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} 
			}
		}.execute();

	}

	public class MyReadAdapter extends BaseAdapter{
		private List<CollectionEntry> entrys;

		public MyReadAdapter(CollectionFeed feeds) {
			entrys = feeds.getEntries();
		}

		public int getCount() {
			return entrys.size();
		}

		public Object getItem(int position) {
			return entrys.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view = infalter.inflate(R.layout.myread_item, null);
			final ImageView iv =  (ImageView) view.findViewById(R.id.book_img);
			RatingBar rb = (RatingBar) view.findViewById(R.id.ratingbar);
			TextView tv_tilte = (TextView) view.findViewById(R.id.book_title);
			TextView tv_description = (TextView) view.findViewById(R.id.book_description);
			CollectionEntry ce = entrys.get(position);
			String  title = ce.getTitle().getPlainText();
			tv_tilte.setText(title);
			List<Attribute> attributes  = ce.getSubjectEntry().getAttributes();
			StringBuilder sb = new StringBuilder();
			for(Attribute attribute: attributes){
				if("author".equals(attribute.getName())){
					String author = attribute.getContent();
					sb.append(author);
					sb.append("/");
				}else if ("price".equals(attribute.getName())){
					String author = attribute.getContent();
					sb.append(author);
					sb.append("/");
				}else if ("publisher".equals(attribute.getName())){
					String author = attribute.getContent();
					sb.append(author);
					sb.append("/");
				}else if ("pubdate".equals(attribute.getName())){
					String author = attribute.getContent();
					sb.append(author);
				}
			}
			String description = sb.toString();
			tv_description.setText(description);
			Rating rt = ce.getSubjectEntry().getRating();
			if(rt!=null){
				rb.setMax(10);
				float f = rt.getAverage();
				rb.setRating(f);
			}else{
				rb.setVisibility(View.INVISIBLE);
			}
			
			String iconurl = ce.getSubjectEntry().getLink("image", null).getHref();
			int start = iconurl.lastIndexOf("/");
			int end = iconurl.length();
			final String iconname = iconurl.substring(start, end);
			File file = new File(Environment.getExternalStorageDirectory(),iconname);
//			if(file.exists()){
//				iv.setImageURI(Uri.fromFile(file));
//				Log.i(TAG,"使用sd卡图片");
//			}
			if(map!=null && map.get(iconname)!=null){
				iv.setImageBitmap(map.get(iconname).get());
				Log.i(TAG,"使用内存缓存");
			}
			else{
			new LoadImageAsynTask(new ImageTaskCallback() {
				// 图片获取之后
				public void onImageLoaded(Bitmap bitmap) {
					// TODO Auto-generated method stub
					if(bitmap!=null){
					iv.setImageBitmap(bitmap);
//					// 需要把图片存到sd卡上 
//					if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
//						try {
//							File file = new File(Environment.getExternalStorageDirectory(),iconname);
//							FileOutputStream fos = new FileOutputStream(file);
//							bitmap.compress(CompressFormat.JPEG, 100, fos);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
					//软引用类型的bitmap 给存放到内存中
					map.put(iconname, new SoftReference<Bitmap>(bitmap));
					}else{
						iv.setImageResource(R.drawable.book);
					}
				}
				
				public void beforeImageLoaded() {
					// TODO Auto-generated method stub
					iv.setImageResource(R.drawable.book);
				}
			}).execute(iconurl);
			}
			return view;
		}
		
	}
}
