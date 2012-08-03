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
import android.widget.RelativeLayout.LayoutParams;

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
		mAdapter = new MyAdapter(mArray);
		mGridView.setAdapter(mAdapter);
		
		mGridView.setOnTouchListener(this);
		mGridView.setNumColumns(5);
		rl.addView(mGridView);

	}

	class MyAdapter extends ArrayAdapter<Map<String, Object>> {

		MyAdapter(ArrayList<Map<String, Object>> mArray) {
			//
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

			// 将当前拖动的图片在GridView中隐藏
			if (from == position) {
				imageView.setVisibility(View.INVISIBLE);
			} else if (View.INVISIBLE == imageView.getVisibility()) {
				imageView.setVisibility(View.VISIBLE);
			}

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

		int x = (int) event.getX();
		int y = (int) event.getY();
		int position = mGridView.pointToPosition(x, y);
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//如果xy坐标没有点击到图片的时候position=-1；
			if (position == -1) {
				break;
			}
			from = position;
			
			//获取到点击的View
			View tempView = mGridView.getChildAt(position
					- mGridView.getFirstVisiblePosition());
			
			
			
			if (tempView == null) {
				break;
			}
			tempView.setDrawingCacheEnabled(true);
			
			mDragViewX = x - tempView.getLeft();
			mDragViewY = y - tempView.getTop();
			
			//tempView.getTop()获取到tempView自身的位置，因为按下的时候应该显示的位置和他自身的位置相同
			//tempView.getLeft() 同理
			
			startDragging(tempView.getDrawingCache(), tempView.getTop(),
					tempView.getLeft());
			
			tempView.setVisibility(View.INVISIBLE);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("ivan", "1 end");
			if (from == -1) {
				break;
			}
			if (mDragView != null) {
				to = position;
				Log.i("ivan", "to = ======" + to);
				RelativeLayout.LayoutParams rllp = (LayoutParams) mDragView.getLayoutParams();
				rllp.leftMargin = (int) (x - mDragViewX);
				rllp.topMargin = (int) (y - mDragViewY);
				
				
				mDragView.setLayoutParams(rllp);
				//做动画的时候不能再次改变
				if (from != to && to != -1 ) {

					aniFrom = from;
					aniTo = to;
					
					Map<String, Object> temp = mAdapter.getItem(from);
					mAdapter.remove(temp);
 					mAdapter.insert(temp, to);
					doingAni = true;
					from = to;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			View end = mGridView.getChildAt(from
					- mGridView.getFirstVisiblePosition());
			if (end != null) {
				end.setVisibility(View.VISIBLE);
			}
			rl.removeView(mDragView);
			break;
		}
		return true;
	}

	private void startDragging(Bitmap tempBitmap, int top, int left) {
		ImageView iv = new ImageView(getApplication());
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-2,
				-2);
		rllp.leftMargin = left;
		rllp.topMargin = top;
		iv.setImageBitmap(tempBitmap);
		mDragView = iv;
		
		rl.addView(mDragView, rllp);
		//两种写法一样
//		mDragView.setLayoutParams(rllp);
//		rl.addView(mDragView);

	}

}
