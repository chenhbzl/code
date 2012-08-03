package com.itcast.collage.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.itcast.collage.bean.Collage;
import com.itcast.collage.bean.Container;

public class Utils implements OnTouchListener {

//	根据图片路径生成缩略图
	public static Bitmap getThumPathByPicPath(String picPath) {
		//!!!!!!!!!!!
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inSampleSize = 10;
		Bitmap thumbBit = BitmapFactory.decodeFile(picPath, opt);
		return thumbBit;
	}
//计算一个随机的图片路径集合的position
	public static int calculateRandomImagePosition(
			ArrayList<Integer> selectedImagePos, int totalCount) {
		int ranPos;
		while (true) {
			ranPos = (int) Math.rint(Math.random() * (totalCount - 1));
			if (selectedImagePos.contains(ranPos)) {
				continue;
			} else {
				selectedImagePos.add(ranPos);
				break;
			}
		}
		return ranPos;
	}

	//将图片压缩后加载到内存 图片占用内存较大，真正图片相关项目当中图片都是被压缩过的
 
	public static Bitmap scalePic(String path, int widthToScale,
			int heightToScale) {

		int height = heightToScale;
		int width = widthToScale;

		BitmapFactory.Options bfo = new BitmapFactory.Options();
		bfo.inJustDecodeBounds = false;
		int scalePer = 1;
		try {
			//根据文件大小计算缩放比例
			File file = new File(path);
			long size = file.length();
			while (true) {
				if (size > (1.0 * 1024 * 1024)) {
					size /= 2;
					scalePer += 1;
					continue;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//设置给bfo一个缩放的比例
		bfo.inSampleSize = scalePer;

		Bitmap bmp = BitmapFactory.decodeFile(path, bfo);
		
		
		
		
		
		
		int photoWidth = bmp.getWidth();
		int photoHeight = bmp.getHeight();
		System.out.println(photoWidth + " h " + photoHeight);
		float scaleW = 0f;
		float scaleH = 0f;

		scaleW = ((float) width) / photoWidth;
		scaleH = ((float) height) / photoHeight;

		if ((photoWidth > width) || (photoHeight > height)) {

			if (scaleW < scaleH) {
				scaleW = scaleH;
			} else {
				scaleH = scaleW;
			}
		}

		if ((photoWidth < width) && (photoHeight < height)) {
			if (scaleW > scaleH) {
				scaleH = scaleW;
			} else {
				scaleW = scaleH;
			}
		}

		Matrix scaleM = new Matrix();
		scaleM.postScale(scaleW, scaleH);

		Bitmap scaledBmp = Bitmap.createBitmap(bmp, 0, 0, photoWidth,
				photoHeight, scaleM, true);
		 bmp.recycle();
		return scaledBmp;
	}

	//获取随机板式
	public static String getRandomLayout(int i) {
		return "Templates/edit_collage_layout_" + i + "_"
				+ (new Random().nextInt(3) + 1) + ".xml";
	}

	public static Rect collageParentRect = new Rect();
	
	//通过矩阵变换以小边为准填充显示并居中
	public static void setImageToCenter(ImageView iv, Container container,
			RelativeLayout editCollagePhoto) {

		editCollagePhoto.getHitRect(collageParentRect);
	 
		int imgL = collageParentRect.left + container.getX();
		int imgT = collageParentRect.top + container.getY();
		int imgR = imgL + (int) container.getWidth();
		int imgB = imgT + (int) container.getHeight();
 
		Rect currentViewRect = new Rect(imgL, imgT, imgR, imgB);
		Bitmap photo = container.getPhoto().getBitmap();
		Matrix tmpMatrix = new Matrix();
		if (photo != null) {
			int rectW = currentViewRect.width();
			int rectH = currentViewRect.height();
			int photoW = photo.getWidth();
			int photoH = photo.getHeight();

			iv.setScaleType(ScaleType.MATRIX);
			int xOffset = 0;
			int yOffset = 0;
			xOffset = (rectW - photoW) / 2;
			yOffset = (rectH - photoH) / 2;

			tmpMatrix.postTranslate(xOffset, yOffset);
			iv.setImageMatrix(tmpMatrix);
		}

		iv.setImageBitmap(photo);
		
//		Animation ani = new ScaleAnimation(0, 1, 0, 1);
//		ani.setDuration(1000);
//		iv.setAnimation(ani);
 
		container.setIv(iv);
		iv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("ivan","image ontouch");
				  //因为程序中只有一个空间添加了touch事件
			      ImageView imageView = (ImageView) v;

			      System.out.println(event.getX());
			      switch (event.getAction()) {
			      //单点按下的时候响应
			      case MotionEvent.ACTION_DOWN:
			    	  
			    	  //第一次进来不起作用
			    	  //第二次进入的时候是给savedMatrix幅值为matrix
			         savedMatrix.set(matrix);
			         //给起始点实例化
			         start.set(event.getX(), event.getY());
			         
			         mode = DRAG;
			         
			         break;
			         //多点按下的时候响应
			      case MotionEvent.ACTION_POINTER_DOWN:
			    	  //记录开始两点间的距离
			         oldDist = spacing(event);
			         if (oldDist > 10f) {
			        	 
			            savedMatrix.set(matrix);
			            //获取两点之间的中心点坐标
			            midPoint(mid, event);
			            mode = ZOOM;
			         }
			         break;
			      case MotionEvent.ACTION_UP:
			    	  
			      case MotionEvent.ACTION_POINTER_UP:
			         mode = NONE;
			         break;
			         
			         
			         //移动的时候响应
			      case MotionEvent.ACTION_MOVE:
			    	  //判断是什么操作如果Drag执行
			         if (mode == DRAG) {
			            //图片的起始位置为上次操作的结束位置
			        	 //让matrix里面的值等于savedMatrix；
			        	 //第一次进来没有作用
			        	
			            matrix.set(savedMatrix);
			            //！！！！！！！！！
			            matrix.postTranslate(event.getX() - start.x,
			                  event.getY() - start.y);
			         }
			         else if (mode == ZOOM) {
			            float newDist = spacing(event);
			            if (newDist > 10f) {
			               matrix.set(savedMatrix);
			               float scale = newDist / oldDist;
			               //！！！！！！！！！
			               //前两个参数是X轴和y轴的缩放比 后两个是中心点
			               matrix.postScale(scale, scale, mid.x, mid.y);
			            }
			         }
			         break;
			      }
			      //真正改变图片位置的代码
			      imageView.setImageMatrix(matrix);
			      
			      
			      return true;  
			}
		});

	}
	
	
	
	static Matrix matrix = new Matrix();
	   static Matrix savedMatrix = new Matrix();

	   // 三种状态
	   static final int NONE = 0;
	   static final int DRAG = 1;
	   static final int ZOOM = 2; 
	   static int mode = NONE;
	   // Remember some things for zooming
	   
	   //开始点
	  static PointF start = new PointF();
	   //中间点
	  static PointF mid = new PointF();
	   //旧的两点间的距离
	  static float oldDist = 1f;
	
	
	public static ArrayList<Container> parseViewXml(String filePath,Activity mActivity) {
		ArrayList<Container> containers = null;
		try {
			
			XMLHandler handler = new XMLHandler();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(mActivity.getAssets().open(filePath)));
			
			containers =  handler.getContainers();
		} catch (FactoryConfigurationError e) {
			// ignore this exception
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// ignore this exception
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// ignore this exception
			e.printStackTrace();
		}
		return containers;
	}
	public static void savePic(String collageName, Collage currentCollage) {
 
		RelativeLayout rl = currentCollage.getRl();
		
		rl.setDrawingCacheEnabled(true);
		
		Bitmap collageBitmap = rl.getDrawingCache();
		
		String savePath ="/sdcard/" + collageName + ".jpg";
		File file1 = new File(savePath);

		FileOutputStream bitmapWriter = null;
		try {
			bitmapWriter = new FileOutputStream(file1);
			
			if (collageBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					bitmapWriter)) {
				bitmapWriter.flush();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmapWriter != null) {
				try {
					bitmapWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	

	   /** 两点之间的距离 */
	   public static float spacing(MotionEvent event) {
		   //!!!!!!!event.getX(0)获取多点中第一点的X的坐标
	      float x = event.getX(0) - event.getX(1);
	      float y = event.getY(0) - event.getY(1);
	      return FloatMath.sqrt(x * x + y * y);
	   }

	   /** 两点的中心点坐标 */
	   public static void midPoint(PointF point, MotionEvent event) {
	      float x = event.getX(0) + event.getX(1);
	      float y = event.getY(0) + event.getY(1);
	      point.set(x / 2, y / 2);
	   }
}

