package cn.itcast.douban;

import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.itcast.douban.domain.BookItem;

import net.htmlparser.jericho.Source;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

public class BookDetailActivity extends Activity {
	public static String path = "http://api.douban.com/book/subject/isbn/";
	ProgressDialog pd;
	BookItem item;
	TextView book_title,book_description,book_summary;
	RatingBar ratingbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		pd = new ProgressDialog(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.book_detail);
		setupView();
		String isbn = intent.getStringExtra("isbn");
		String bookpath = path + isbn + "?alt=json";
		fillData(bookpath);
	}
	private void setupView() {
		book_title = (TextView) this.findViewById(R.id.book_title);
		book_description = (TextView) this.findViewById(R.id.book_description);
		book_summary = (TextView) this.findViewById(R.id.book_summary);
		ratingbar = (RatingBar) this.findViewById(R.id.ratingbar);
		
	}
	private void fillData(String bookpath) {
		new AsyncTask<String, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pd.setMessage("正在获取数据");
				pd.show();
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				pd.dismiss();
				book_title.setText(item.getName());
				book_description.setText(item.getDescription());
				book_summary.setText(item.getSummary());
				ratingbar.setMax(10);
				ratingbar.setRating(item.getRating());
			}

			@Override
			protected Boolean doInBackground(String... params) {
				item = new BookItem();
				try {
					String bookpath = params[0];
					URL url = new URL(bookpath);
					URLConnection conn = url.openConnection();
					Source source = new Source(conn);
					String content = source.toString();
					JSONObject baseobj = new JSONObject(content);
					String summary = ((JSONObject)baseobj.get("summary")).get("$t").toString();
					item.setSummary(summary);
				    JSONArray array =	(JSONArray) baseobj.get("db:attribute");
				    StringBuilder sb = new StringBuilder();
					for(int i=0;i<array.length();i++){
						JSONObject attribute = (JSONObject) array.get(i);
						sb.append(attribute.get("@name").toString()+"="+attribute.get("$t").toString());
						sb.append("\n");
					}
					String description = sb.toString();
					item.setDescription(description);
				    String rating = ((JSONObject)(baseobj.get("gd:rating"))).get("@average").toString();
					item.setRating(Float.parseFloat(rating));
					String title = ((JSONObject)(baseobj.get("title"))).get("$t").toString();
					item.setName(title);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
		}.execute(bookpath);
	}
	
}
