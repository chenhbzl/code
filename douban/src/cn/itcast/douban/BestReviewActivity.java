package cn.itcast.douban;

import java.util.List;

import cn.itcast.douban.domain.Review;
import cn.itcast.douban.util.NetUtil;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BestReviewActivity extends Activity {
	protected static final int GET_REVIEW_SUCCESS = 100;
	protected static final int GET_REVIEW_ERROR = 101;
	ListView lv;
	RelativeLayout rl;
	int startIndex = 1;
	List<Review> reviews;
	LayoutInflater inflater;
	BestReviewAdapter bestReviewAdapter;
	boolean isLoading = false;

	private void addMoreReview(List<Review> reviews) {
		for (Review review : reviews) {
			this.reviews.add(review);
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isLoading = false;
			ScaleAnimation sa = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f);
			sa.setDuration(1000);
			rl.setAnimation(sa);
			rl.setVisibility(View.INVISIBLE);
			switch (msg.what) {
			case GET_REVIEW_SUCCESS:
				// 设置listview的数据适配器
				if (bestReviewAdapter != null) {
					bestReviewAdapter.notifyDataSetChanged();
				} else {
					bestReviewAdapter = new BestReviewAdapter();
					lv.setAdapter(bestReviewAdapter);
				}
				break;

			case GET_REVIEW_ERROR:
				Toast.makeText(getApplicationContext(), "获取评论失败", 1).show();
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		setUpView();

		fillData();

	}

	private void fillData() {
		isLoading = true;
		ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
		sa.setDuration(1000);
		rl.setAnimation(sa);
		rl.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				try {
					if (reviews != null && reviews.size() > 0) {
						// 把新获取到的数据 加到 reviews 的集合里面
						addMoreReview(NetUtil.getReviews(startIndex));
					} else {
						reviews = NetUtil.getReviews(startIndex);
					}
					Message msg = new Message();
					msg.what = GET_REVIEW_SUCCESS;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = GET_REVIEW_ERROR;
					handler.sendMessage(msg);
				}
			}
		}.start();

	}

	private void setUpView() {
		// 每次初始化界面的时候 重置 开始的位置
		startIndex = 1;
		inflater = LayoutInflater.from(this);
		lv = (ListView) this.findViewById(R.id.lv_subject);
		rl = (RelativeLayout) this.findViewById(R.id.rl_subject_loading);

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当滚动到最后一个条目,并且当前的状态是停止滚动的时候
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					// 最后一个可见条目的id+1 等于listview里面数据的总条目
					if (view.getLastVisiblePosition() + 1 == reviews.size()) {
						// 加载更多的数据
						if (isLoading) {
							return;
						} else {
							fillData();
							startIndex += 10;
						}

					}
					break;
				}

			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	public class BestReviewAdapter extends BaseAdapter {

		public int getCount() {
			// TODO Auto-generated method stub
			return reviews.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return reviews.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				view = inflater.inflate(R.layout.review_item, null);
			} else {
				view = convertView;
			}
			holder.tv_title = (TextView) view.findViewById(R.id.review_title);
			holder.tv_author = (TextView) view.findViewById(R.id.author_name);
			holder.rb = (RatingBar) view.findViewById(R.id.ratingbar);
			holder.tv_summary = (TextView) view
					.findViewById(R.id.review_summary);
			Review review = reviews.get(position);
			holder.tv_title.setText(review.getTitle());
			holder.tv_author.setText(review.getReviewauthor());
			holder.tv_summary.setText(review.getSummary());
			holder.rb.setMax(5);
			holder.rb.setRating(review.getRating());
			return view;
		}

	}

	static class ViewHolder {
		TextView tv_title;
		TextView tv_author;
		TextView tv_summary;
		RatingBar rb;
	}
}
