package com.beyondsoft.gridviewdemo;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GridViewDemo extends Activity implements OnTouchListener {

	private ArrayList<Map<String, Object>> mArray;
	private GridView mGridView;
	private RelativeLayout rl;
	private MyAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.main);

		// 要将main.xml中的LinearLayout改成RelativeLayout
		rl = (RelativeLayout) findViewById(R.id.rl);
		mGridView = new GridView(this);
		mArray = Utils.getData();
		mAdapter = new MyAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setNumColumns(5);
		mGridView.setOnTouchListener(this);
		rl.addView(mGridView);
	}

	class MyAdapter extends ArrayAdapter<Map<String, Object>> {

		MyAdapter() {
			super(GridViewDemo.this, mGridView.getId(), mArray);
		}

		public ArrayList<Map<String, Object>> getList() {
			return mArray;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = (View) inflater.inflate(R.layout.mygrid, parent, false);
			}
			ImageView imageView = (ImageView) row.findViewById(R.id.img);

			imageView.setImageResource(Integer.valueOf(mArray.get(position)
					.get("img").toString()));
			if (position == to) {
				imageView.setVisibility(View.INVISIBLE);
			} else {
				if (imageView.getVisibility() == View.INVISIBLE) {
					imageView.setVisibility(View.VISIBLE);
				}
			}
			// 给View添加animation
			Animation an = null;
			if (position > aniTo && position <= aniFrom) {
				if (position % 5 == 0) {
					an = new TranslateAnimation(255, 0, -85, 0);
				} else {
					an = new TranslateAnimation(-60, 0, 0, 0);
				}
			} else if (position < aniTo && position >= aniFrom) {
				if (position % 5 == 4) {
					an = new TranslateAnimation(-255, 0, 85, 0);
				} else {
					an = new TranslateAnimation(60, 0, 0, 0);
				}
			}

			if (an != null) {
				an.setDuration(300);
				an.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						doingAni = false;
					}
				});
				imageView.setAnimation(an);
			}
			return imageView;
		}
	}

	private ImageView mDragView;
	private float mDragViewX;
	private float mDragViewY;
	private int from = -1;
	private int to = -1;
	private int aniFrom;
	private int aniTo;
	private boolean doingAni = false;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		int y = (int) event.getY();
		// 获取点击图片的位置id
		int position = mGridView.pointToPosition(x, y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (position != -1) {

				from = position;
				View tempView = mGridView.getChildAt(position
						- mGridView.getFirstVisiblePosition());
				if (tempView != null) {

					startDragging(tempView);
					mDragViewX = x - tempView.getLeft();
					mDragViewY = y - tempView.getTop();

					tempView.setVisibility(View.INVISIBLE);

				}
			}
			break;
		case MotionEvent.ACTION_MOVE:

			if (position != -1) {
				to = position;
			}

			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					-2, -2);
			lp.topMargin = (int) (y - mDragViewY);
			lp.leftMargin = (int) (x - mDragViewX);
			mDragView.setLayoutParams(lp);

			if (from != -1 && to != -1 && from != to&& !doingAni) {
				
				doingAni = true;
				Map<String, Object> item = mAdapter.getItem(from);
				mAdapter.remove(item);
				mAdapter.insert(item, to);
				
				aniFrom = from;
				aniTo = to;
				
				from = to;
			}

			break;
		case MotionEvent.ACTION_UP:
			if (mDragView != null) {

				View endView = mGridView.getChildAt(from
						- mGridView.getFirstVisiblePosition());
				if (endView != null) {
					endView.setVisibility(View.VISIBLE);
				}

				rl.removeView(mDragView);
			}
			break;
		}
		return true;
	}

	private void startDragging(View tempView) {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(-2, -2);
		lp.topMargin = tempView.getTop();
		lp.leftMargin = tempView.getLeft();
		ImageView iv = new ImageView(getApplicationContext());
		tempView.setDrawingCacheEnabled(true);
		iv.setImageBitmap(tempView.getDrawingCache());
		mDragView = iv;

		rl.addView(mDragView, lp);

	}

}
