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
		//��XMLģ���ļ������ȡ����ʵ��������ά����containers
		containers = Utils.parseViewXml(path, activity);
		//���rl
		rl = new RelativeLayout(activity);
		rl.setBackgroundColor(Color.WHITE);
		for (Container container : containers) {
			//��ʼ��С��rl
			container.initRl(activity);
			//���rl�������������С��rl
			rl.addView(container.getRl());
			
			//���ζ���
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
			for(Container c:currentContainers){//������ǰҳ����ʾ������
				Container con = null;
				
				 if(c.getId()<=this.containers.size()){
					 
					 con = this.getContainerById(c.getId());//ȡ����֮���Ӧ����Collage�е�����
				 }else break;//������������������ڵ�ǰ�������������������������Ӷ���ֱ��break
				
				 if(c.getIv()!=null){
					 
						Photo photo = new Photo(c.getPhoto().getPath(), (int) con.getWidth(),
								(int) con.getHeight());
				
						con.setPhoto(photo);
						ImageView iv = new ImageView(activity);
						iv.setImageBitmap(photo.getBitmap());
						
						//��imageView���ž��к���ӵ�con����ȥ
						Utils.setImageToCenter(iv,con,editCollagePhoto);
				 }
				 //ƽ�ƶ���     c.getX()-con.getX()�����һ��ƫ����
				 //
					Animation translateAni = new TranslateAnimation(c.getX()-con.getX(), 0, c.getY()-con.getY(), 0);
					//����   1ָ����ͼƬ��ԭʼ�ߴ� 0����ʾ
					//1(�������ߴ�)/x���������ĳߴ磩 = con.getW()/c.getW();    x = c.getW()/con.getW()
					//scaleAni �ĸ����� ǰ����X���ĳ��������ĳ���� ����ı�������1��ʱ����
					
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
