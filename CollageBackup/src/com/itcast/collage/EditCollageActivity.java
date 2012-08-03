package com.itcast.collage;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.itcast.collage.adapter.CollagePhotoAdapter;
import com.itcast.collage.adapter.CollagePhotoAdapter.ViewTag;
import com.itcast.collage.bean.Collage;
import com.itcast.collage.bean.Container;
import com.itcast.collage.bean.Photo;
import com.itcast.collage.utils.Utils;

public class EditCollageActivity extends Activity implements OnClickListener,
		OnTouchListener {

	private RelativeLayout editCollageMain;
	private RelativeLayout editCollageToolbar;
	private RelativeLayout editCollageFuncButton;
	private RelativeLayout editAutoFillClear;
	private RelativeLayout editCollagePhoto;

	private LinearLayout photoOptionsLayout;// the portrait options bar in the right
	private RelativeLayout photoList;
	private RelativeLayout layoutList;

	private ImageView editPhotoBtn;
	private ImageView editLayoutBtn;
	private ImageView editExitBtn;
	private ImageView editDoneBtn;
	private ImageView editFrameBtn;
	private ImageView editAutoFillBtn;
	private ImageView editClearBtn;
	private ImageView hideLayoutListBtn;

	private ImageView layout3btn;
	private ImageView layout4btn;
	private ImageView layout5btn;
	private ImageView layout6btn;
	private ImageView layout7btn;

	private GridView editPhotoGridView;
	private Animation push_Right_Out;
	private Animation push_Left_In;

	private static final int PHOTO_FRAME_WIDTH = 725;
	private static final int PHOTO_FRAME_HEIGHT = 530;
	private static int PHOTOLIST =1;
	private static int LAYOUTLIST =2;
	private ArrayList<String> tmpPhotos = new ArrayList<String>();
	public ArrayList<Bitmap> tmpThumbnail = new ArrayList<Bitmap>();
	private Bundle infoPara;
	private String collageName;

	// 随机选择图片id
	private ArrayList<Integer> selectedPhotoPos = new ArrayList<Integer>();
	private ArrayList<Container> currentContainers;
	private Collage currentCollage;
	private ImageView hidePhotoListBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.edit_collage);
		// 获取图片路径的List
		getInfoFromIntent();
		// 通过图片路径的List获取缩略图
		getThumbnails(tmpPhotos);
		// 初始化界面控件
		initView();
		// 第一次进入的时候创建一个空的三张的Collage模板
		changeLayout(3);
		

	}

	private void initView() {
		editCollageMain = (RelativeLayout) findViewById(R.id.edit_collage_mainlayout);
		photoOptionsLayout = (LinearLayout) editCollageMain
				.findViewById(R.id.edit_collage_optionlayout);
		editCollageToolbar = (RelativeLayout) editCollageMain
				.findViewById(R.id.edit_collage_toolbarlayout);
		LayoutInflater loif = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		editCollageFuncButton = (RelativeLayout) loif.inflate(
				R.layout.edit_collage_optionslayout, null);
		editAutoFillClear = (RelativeLayout) editCollageMain
				.findViewById(R.id.edit_collage_autofill_clear_lo);

		photoOptionsLayout.addView(editCollageFuncButton);

		// initiate the frame photo button
		editFrameBtn = (ImageView) editAutoFillClear
				.findViewById(R.id.edit_collage_framebtn);
		editFrameBtn.setOnClickListener(this);
		// initiate the automatic fill photo button
		editAutoFillBtn = (ImageView) editAutoFillClear
				.findViewById(R.id.edit_collage_autofillbtn);
		editAutoFillBtn.setOnClickListener(this);
		// initiate the clear photo button
		editClearBtn = (ImageView) editAutoFillClear
				.findViewById(R.id.edit_collage_clearbtn);
		editClearBtn.setOnClickListener(this);

		// initiate the photo button
		editPhotoBtn = (ImageView) editCollageFuncButton
				.findViewById(R.id.edit_collage_photobtn);
		editPhotoBtn.setOnClickListener(this);
		// initiate the layout button
		editLayoutBtn = (ImageView) editCollageFuncButton
				.findViewById(R.id.edit_collage_layoutbtn);
		editLayoutBtn.setOnClickListener(this);
		// initiate the exit button
		editExitBtn = (ImageView) editCollageToolbar
				.findViewById(R.id.edit_collage_exitbtn);
		editExitBtn.setOnClickListener(this);
		// initiate the done button
		editDoneBtn = (ImageView) editCollageToolbar
				.findViewById(R.id.edit_collage_donebtn);
		editDoneBtn.setOnClickListener(this);

		push_Left_In = new TranslateAnimation(200, 0, 0, 0);
		push_Left_In.setDuration(800);
		push_Right_Out = new TranslateAnimation(0, 200, 0, 0);
		push_Right_Out.setDuration(800);


		editCollagePhoto = (RelativeLayout) editCollageMain
				.findViewById(R.id.edit_collage_photolayout);

 		editCollagePhoto.setOnTouchListener(editCollagePhotoOnTouchL);

		photoList = (RelativeLayout) loif.inflate(
				R.layout.edit_collage_photolist, null);

		layoutList = (RelativeLayout) loif.inflate(R.layout.edit_layoutlist,
				null);
	}

	private void getThumbnails(ArrayList<String> tmpPhotos2) {
		for (String path : tmpPhotos2) {
			Bitmap thumbBit = Utils.getThumPathByPicPath(path);
			tmpThumbnail.add(thumbBit);
		}
	}

	@SuppressWarnings("unchecked")
	void getInfoFromIntent() {
		infoPara = getIntent().getExtras();
		tmpPhotos = (ArrayList<String>) infoPara.get("paths");
		collageName = infoPara.getString("collageName");
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.edit_collage_layoutbtn:// layout按钮

			hideLayoutListBtn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layouthidebtn);

			layout3btn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layout3btn);
			layout3btn.setOnClickListener(this);

			layout4btn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layout4btn);
			layout4btn.setOnClickListener(this);

			layout5btn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layout5btn);
			layout5btn.setOnClickListener(this);

			layout6btn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layout6btn);
			layout6btn.setOnClickListener(this);

			layout7btn = (ImageView) layoutList
					.findViewById(R.id.edit_collage_layout7btn);
			layout7btn.setOnClickListener(this);

			hideLayoutListBtn.setOnClickListener(this);

			visibleOptions(LAYOUTLIST);

			break;
		case R.id.edit_collage_clearbtn:
			for (Container c : currentContainers) {
				c.getRl().removeAllViews();
				
				c.Clear();
			}
			break;
		case R.id.edit_collage_donebtn:
			
			
			Utils.savePic(collageName, currentCollage);
			
			
			Toast.makeText(this, "successed!", Toast.LENGTH_SHORT).show();
			break;
		case R.id.edit_collage_exitbtn:
			this.finish();
			break;
		case R.id.edit_collage_autofillbtn:

			putImage();

			break;
		case R.id.edit_collage_photobtn:

			editPhotoGridView = (GridView) photoList
					.findViewById(R.id.edit_collage_photo_gridview);
			//tmpThumbnail存放上个界面传递过来的图片
			BaseAdapter adapter = new CollagePhotoAdapter(this, tmpThumbnail,
					selectedPhotoPos);
			hidePhotoListBtn = (ImageView)photoList.findViewById(R.id.edit_collage_photohidebtn);
			hidePhotoListBtn.setOnClickListener(this);
			editPhotoGridView.setAdapter(adapter);
			
			editPhotoGridView.setOnTouchListener(this);
			visibleOptions(PHOTOLIST);
			break;
		case R.id.edit_collage_layouthidebtn:// 隐藏侧边栏
			hiddenOptions();
			break;
		case R.id.edit_collage_photohidebtn:// 隐藏侧边栏
			hiddenOptions();
			break;
		case R.id.edit_collage_framebtn:

			break;
		case R.id.edit_collage_layout3btn:
			changeLayout(3);
			break;
		case R.id.edit_collage_layout4btn:
			changeLayout(4);
			break;
		case R.id.edit_collage_layout5btn:
			changeLayout(5);
			break;
		case R.id.edit_collage_layout6btn:
			changeLayout(6);
			break;
		case R.id.edit_collage_layout7btn:
			changeLayout(7);
			break;
		}

	}

	private void changeLayout(int i) {
		editCollagePhoto.removeAllViews();//首先删除掉所有的View
		String path = null;
		if (currentCollage != null) { //判断之前是否有板式
			//获取到当前板式的路径
			path = currentCollage.getLayoutPath();
			
			while (currentCollage.getLayoutPath().equals(path)) {		//随机获取一个板式，但是不能重复
				path = Utils.getRandomLayout(i);						//
			}
			
			
		} else {
			path = Utils.getRandomLayout(i);
		}

		Collage myCollage = new Collage(path, this);
		
		RelativeLayout rl = myCollage.getRl();

		if (currentCollage != null) {
			myCollage.setAnimation(currentContainers, this, editCollagePhoto); //换板式的时候添加动画
		}

		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				PHOTO_FRAME_WIDTH, PHOTO_FRAME_HEIGHT);
		editCollagePhoto.addView(rl, rllp);
		
		currentCollage = myCollage;				//记录当前的显示内容
		currentContainers = myCollage.getContaniners();
		
		
	}

	private void putImage() {
		//循环的次数小于图片的数量 同时小于当前的容器的数量
		for (int i = 0; i < tmpPhotos.size() && i < currentContainers.size(); i++) {
			int photoPos = Utils.calculateRandomImagePosition(selectedPhotoPos,
					tmpPhotos.size());
			final Container container = currentContainers.get(i);
			String path = tmpPhotos.get(photoPos);
			Photo photo = new Photo(path, (int) container.getWidth(),
					(int) container.getHeight());
			container.setPhoto(photo);
			
			ImageView iv = new ImageView(this);
			iv.setImageBitmap(photo.getBitmap());
			
			Utils.setImageToCenter(iv, container, editCollagePhoto);//

		}
	
	}

	int beginDragEventX = 0, beginDragEventY = 0, beginDragX = 0,
			beginDragY = 0;
	private String mDragViewPath;
	private int TITLE_HIGHT = 25;

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//!!!!getX()获取到的是相对于此view的 而getRawX（）获取到的是相对于屏幕的
		int x = (int) event.getX();
		int y = (int) event.getY();
		int rawX = (int) event.getRawX();
		int rawY = (int) event.getRawY();

		// 从侧边栏托图片进入到collage

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
				//pointToPosition 用所点击的坐标xy 获取到GridView的position
				int position = editPhotoGridView.pointToPosition(x, y);
				
				int firstPosition = editPhotoGridView.getFirstVisiblePosition();
				if (position != -1) {
					beginDragEventX = (int) event.getX();
					beginDragEventY = (int) event.getY();
					
					
					View tempView = editPhotoGridView.getChildAt(position
							- firstPosition);
					ImageView iv = new ImageView(this);
					
					tempView.setDrawingCacheEnabled(true);
					
					iv.setImageBitmap(tempView.getDrawingCache());
					
					RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(
							-2, -2);
					beginDragY = lllp.topMargin = tempView.getTop()
							+ photoOptionsLayout.getTop()
							+ editPhotoGridView.getTop();
					
					beginDragX = lllp.leftMargin = tempView.getLeft()+ photoOptionsLayout.getLeft();
					
					
					editCollageMain.addView(iv, lllp);
					
					

					mDragViewPath = this.tmpPhotos.get(position);
					mDragView = iv;
					
				}
			break;

		case MotionEvent.ACTION_MOVE:
			if (mDragView != null) {

				RelativeLayout.LayoutParams lllp = (android.widget.RelativeLayout.LayoutParams) mDragView
						.getLayoutParams();
				//注意消化    event.getY() - beginDragEventY 得到一个偏移量 X同理
				lllp.topMargin = (int) (beginDragY + event.getY() - beginDragEventY);
				lllp.leftMargin = (int) (beginDragX + event.getX() - beginDragEventX);

				mDragView.setLayoutParams(lllp);
				//！！！！！！！点击事件不交给系统处理
//				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			if(mDragView == null){
				break;
			}
			for (Container c : currentContainers) {
				RelativeLayout rl = c.getRl();

				
				
//				改变界面的代码不是代码刚执行完就会生效！！！！！！！！
//				RelativeLayout relativeL= new RelativeLayout(this);
//				
//				RelativeLayout.LayoutParams rllp1 = new RelativeLayout.LayoutParams(400, 500);
//				rllp1.leftMargin =10;
//				
//				editCollageMain.addView(relativeL,rllp1);
//				
//				
//				
//				Rect rect1 = new Rect();
//				//给rect幅值
//				relativeL.getHitRect(rect1);
				
				
				
				
				
				
				//矩形
				Rect rect = new Rect();
				//给rect幅值
				rl.getHitRect(rect);
				
				if (rect.contains(rawX - editCollagePhoto.getLeft(), rawY
						- editCollagePhoto.getTop() - TITLE_HIGHT)) {
					
					// 清除掉容器中的图片
					rl.removeAllViews();
					
					Photo photo = new Photo(mDragViewPath, (int) c.getWidth(),
							(int) c.getHeight());
					
					c.setPhoto(photo);
					
					if (c.getIv() == null) {
						ImageView iv = new ImageView(this);
						c.setIv(iv);
					}
					//让图片以最小边为基准填出满容器（rl）并且居中显示
						Utils.setImageToCenter(c.getIv(), c, editCollagePhoto);
				}
			}
			//删除掉拖动的ImageView
			if (mDragView != null) {
				editCollageMain.removeView(mDragView);
				mDragView = null;
				
			}
			break;
		}

		return super.onTouchEvent(event);
	}

	private ImageView mDragView;
	private Container mClickContainer;
	
	
	
	//处理Collage中的位置变换
	private OnTouchListener editCollagePhotoOnTouchL = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			Log.i("ivan","onTouch");
			int rawX = (int) event.getRawX();
			int rawY = (int) event.getRawY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
//				
//				
//				
//				for (Container c : currentContainers) {
//					RelativeLayout rl = c.getRl();
//					//保证容器里面有图片才进行操作
//					if (c.getIv()!= null) {
//						Rect rect = new Rect();
//						//获取一个矩形
//						rl.getHitRect(rect);
//						//判断一个坐标xy是不是在这个矩形里面
//						if (rect.contains(
//								rawX - editCollagePhoto.getLeft(), rawY
//										- editCollagePhoto.getTop()
//										- TITLE_HIGHT)) {
//							mClickContainer = c;
//						}
//						
//					}
//				}
				
				
				
				
				
				
//
//				for (Container c : currentContainers) {
//					RelativeLayout rl = c.getRl();
//					//保证容器里面有图片才进行操作
//					if (c.getIv()!= null) {
//						Rect rect = new Rect();
//						//获取一个矩形
//						rl.getHitRect(rect);
//						//判断一个坐标xy是不是在这个矩形里面
//						if (rect.contains(
//								rawX - editCollagePhoto.getLeft(), rawY
//										- editCollagePhoto.getTop()
//										- TITLE_HIGHT)) {
//							// 清除掉容器中的图片
//							rl.removeAllViews();
//							mDragViewPath = c.getPhoto().getPath();
//							c.Clear();
//							Bitmap thumbBit = Utils
//									.getThumPathByPicPath(mDragViewPath);
//
//							ImageView iv = new ImageView(
//									EditCollageActivity.this);
//							iv.setImageBitmap(thumbBit);
//							RelativeLayout.LayoutParams lllp = new RelativeLayout.LayoutParams(
//									-2, -2);
//							lllp.leftMargin = rawX - thumbBit.getWidth()
//									/ 2;
//							lllp.topMargin = rawY - thumbBit.getHeight()
//									/ 2;
//
//							editCollageMain.addView(iv, lllp);
//							mDragView = iv;
//						}
//					}

//				}
				break;

			case MotionEvent.ACTION_MOVE:
				
				 
//				if (mDragView != null) {
//					RelativeLayout.LayoutParams lllp = (android.widget.RelativeLayout.LayoutParams) mDragView
//							.getLayoutParams();
//
//					lllp.leftMargin = rawX - mDragView.getWidth() / 2;
//					lllp.topMargin = rawY - mDragView.getHeight() / 2;
//
//					mDragView.setLayoutParams(lllp);
//					return true;
//				}

				break;
			case MotionEvent.ACTION_UP:
//				if (mDragView != null) {
//					editCollageMain.removeView(mDragView);
//					mDragView = null;
//				}else{
//					break;
//				}
//				
//				for (Container c : currentContainers) {
//					RelativeLayout rl = c.getRl();
//
//					Rect rect = new Rect();
//					rl.getHitRect(rect);
//					if (rect.contains(rawX - editCollagePhoto.getLeft(),
//							rawY - editCollagePhoto.getTop() - TITLE_HIGHT)) {
//						// 清除掉容器中的图片
//						rl.removeAllViews();
//						Photo photo = new Photo(mDragViewPath, (int) c
//								.getWidth(), (int) c.getHeight());
//						c.setPhoto(photo);
//						if (c.getIv() == null) {
//							ImageView iv = new ImageView(
//									EditCollageActivity.this);
//							c.setIv(iv);
//						}
//						Utils.setImageToCenter(c.getIv(), c,
//								editCollagePhoto);
//					}
//				}
				break;
			}//false和super的意义一样 为event事件处理完成之后将事件继续交给系统处理，true反之
			return true;
		}
	};
	
	
	// 隐藏侧边栏
	private void hiddenOptions() {
		photoOptionsLayout.startAnimation(push_Right_Out);
		push_Right_Out.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				photoOptionsLayout.removeAllViews();
				photoOptionsLayout.addView(editCollageFuncButton);
				photoOptionsLayout.startAnimation(push_Left_In);
			}
		});

	}
	//添加侧边栏
	private void visibleOptions(final int id){
		photoOptionsLayout.startAnimation(push_Right_Out);
		final LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 688);
		push_Right_Out.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				photoOptionsLayout.removeAllViews();
				if(id == PHOTOLIST){
					photoOptionsLayout.addView(photoList,lllp);
				}else if(id == LAYOUTLIST){
					photoOptionsLayout.addView(layoutList,lllp);
				}
				photoOptionsLayout.startAnimation(push_Left_In);
			}
		});
	}
}
