package cn.itcast.douban;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class MainTabActivity extends TabActivity {
	TabHost mTabHost;
	LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintab);
		inflater = LayoutInflater.from(this);
		mTabHost = getTabHost();
		TabSpec  meTab  = mTabHost.newTabSpec("me");
		//Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);
		meTab.setIndicator(perpareIndicator(R.string.me_tab, R.drawable.tab_main_nav_me_selector));
		Intent intent = new Intent(this,MeActivity.class);
		meTab.setContent(intent);
		mTabHost.addTab(meTab);
		TabSpec  newBookTab  = mTabHost.newTabSpec("newbook");
		newBookTab.setIndicator(perpareIndicator(R.string.newbook_tab, R.drawable.tab_main_nav_book));
		Intent newBookIntent = new Intent(this,NewBookActivity.class);
		newBookTab.setContent(newBookIntent);
		mTabHost.addTab(newBookTab);
		
		TabSpec bestReviewTab  =mTabHost.newTabSpec("bestReviewTab");
		bestReviewTab.setIndicator(perpareIndicator(R.string.best_review, R.drawable.detail_comment_selected));
		Intent bestReviewIntent = new Intent(this,BestReviewActivity.class);
		bestReviewTab.setContent(bestReviewIntent);
		mTabHost.addTab(bestReviewTab);
		
		
		TabSpec aboutTab = mTabHost.newTabSpec("about");
		aboutTab.setIndicator(perpareIndicator(R.string.about, R.drawable.tab_main_nav_more));
		Intent aboutIntent = new Intent(this,AboutActivity.class);
		aboutTab.setContent(aboutIntent);
		mTabHost.addTab(aboutTab);
	}

	/*
	 * 准备tabwidget的view对象 
	 */
	private View perpareIndicator(int title , int icon){
		View view = inflater.inflate(R.layout.tab_main_nav, null);
		ImageView  ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		ivIcon.setImageResource(icon);
		tvTitle.setText(title);
		return view;
	}


	
	
	
	
}
