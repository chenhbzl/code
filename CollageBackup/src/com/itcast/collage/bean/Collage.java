package com.itcast.collage.bean;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Rect;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itcast.collage.utils.Utils;

public class Collage {
	private ArrayList<Container> containers;
	private RelativeLayout rl;
	private String layoutPath;
	
	public String getLayoutPath() {
		return layoutPath;
	}
	public Collage(String path,Activity activity){
		layoutPath = path;
		//从XML模板文件里面获取数据实例化自身维护的containers
		containers = Utils.parseViewXml(path, activity);
		//大的rl
		rl = new RelativeLayout(activity);
		rl.setBackgroundColor(Color.WHITE);
		for (Container container : containers) {
			//初始化小的rl
			container.initRl(activity);
			//大的rl加入容器里面的小的rl
			rl.addView(container.getRl());
			
			//矩形对象
//			Rect outRect = new Rect();
//			container.getRl().getHitRect(outRect);
		}
		
	}
	
	public ArrayList<Container> getContaniners() {
		return containers;
	}

	public Container getContainerById(int id){
		Container container = null;
		for(Container c:containers){
			
			if(c.getId()==id){
				container = c;
			}
		}
		return container;
	}

	public RelativeLayout getRl() {
		return rl;
	}

	public void setRl(RelativeLayout rl) {
		this.rl = rl;
	}
	public void setAnimation(ArrayList<Container> currentContainers,Activity activity, RelativeLayout editCollagePhoto) {
			for(Container c:currentContainers){//遍历当前页面显示的容器
				Container con = null;
				
				 if(c.getId()<=this.containers.size()){
					 
					 con = this.getContainerById(c.getId());//取到与之相对应的新Collage中的容器
				 }else break;//如果新容器的数量大于当前的数量，超出数量的容器不加动画直接break
				
				 if(c.getIv()!=null){
					 
						Photo photo = new Photo(c.getPhoto().getPath(), (int) con.getWidth(),
								(int) con.getHeight());
				
						con.setPhoto(photo);
						ImageView iv = new ImageView(activity);
						iv.setImageBitmap(photo.getBitmap());
						
						//把imageView缩放居中后添加到con里面去
						Utils.setImageToCenter(iv,con,editCollagePhoto);
				 }
				 //平移动画     c.getX()-con.getX()计算的一个偏移量
				 //
					Animation translateAni = new TranslateAnimation(c.getX()-con.getX(), 0, c.getY()-con.getY(), 0);
					//缩放   1指的是图片的原始尺寸 0不显示
					//1(新容器尺寸)/x（老容器的尺寸） = con.getW()/c.getW();    x = c.getW()/con.getW()
					//scaleAni 四个参数 前两个X轴从某个比例到某比例 这里的比例等于1的时候是
					
					//1/x = con.getw/c.getw 
					Animation scaleAni = new ScaleAnimation(c.getWidth()/con.getWidth(), 1, c.getHeight()/con.getHeight(), 1);
					
					translateAni.setDuration(600);
					scaleAni.setDuration(600);
					
					AnimationSet as = new AnimationSet(false);
					 
					as.addAnimation(scaleAni);
					as.addAnimation(translateAni);
					 
					con.getRl().setAnimation(as);
					
	}
		
	}

}
