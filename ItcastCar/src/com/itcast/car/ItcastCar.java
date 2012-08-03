package com.itcast.car;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class ItcastCar extends Activity implements OnTouchListener {
	/** Called when the activity is first created. */

	private GLSurfaceView myGSF;
	private MyRenderer myRenderer;
	private float[] bgVertexArray = new float[] { -5.71f, 3.8f, 0f, -5.71f,
			-3.8f, 0f, 5.71f, 3.8f, 0f, 5.71f, -3.8f, 0f };
	FloatBuffer bgVertexBuffer = FloatBuffer.wrap(bgVertexArray);

	private float[] logoVertexArray = new float[] { -0.5f, 0.5f, 0f, -0.5f,
			-0.5f, 0f, 0.5f, 0.5f, 0f, 0.5f, -0.5f, 0f };
	FloatBuffer logoVertexBuffer = FloatBuffer.wrap(logoVertexArray);

	private float[] itemVertexArray = new float[] { -0.5f, 0.5f, 0f, -0.5f,
			-0.5f, 0f, 0.5f, 0.5f, 0f, 0.5f, -0.5f, 0f };
	FloatBuffer itemVertexBuffer = FloatBuffer.wrap(itemVertexArray);
	
	
	
	private float[] testVertexArray = new float[] { 
			-2f, 2f, 0f, 
			-2f,-2f, 0f, 
			2f, 2f, 0f,
			2f, -2f, 0f };
	FloatBuffer testVertexBuffer = FloatBuffer.wrap(testVertexArray);

	private float[] texArray = new float[] { 0, 0, 0, 1, 1, 0, 1, 1 };
	private Buffer texBuffer = FloatBuffer.wrap(texArray);

	private float s = 42.75f;
	private Bitmap bitmap;
	private MyPicture background, logoFrame, logo;
	private float logoScale = 0.8f;
	private float logoS = -0.01f;
	private List<MyPicture> items;
	private boolean animationEnd = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏

		myGSF = new GLSurfaceView(this);
		myGSF.setOnTouchListener(this);
		myRenderer = new MyRenderer();
		myGSF.setRenderer(myRenderer);

		setContentView(myGSF);

	}

	class MyRenderer implements Renderer {

		private float angle;
		private float r = 5;
		private boolean drawItemRotateEnd = true;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			// 声明透视修正模式为速度优先
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// 声明清除屏幕使用的颜色
			gl.glClearColor(0, 0, 0, 0);
			// 设置着色模式（GL10.GL_SMOOTH:平滑；GL_FLAT:单调）
			gl.glShadeModel(GL10.GL_SMOOTH);
			// 设置深度缓存
			gl.glClearDepthf(1.0f);
			// 开启深度测试
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// 设置深度测试模式
			gl.glDepthFunc(GL10.GL_LEQUAL);

			background = new MyPicture(initTex(gl, R.drawable.bg), 0, 0, 0);
			logoFrame = new MyPicture(initTex(gl, R.drawable.a7), 0, 0, 0);
			logo = new MyPicture(initTex(gl, R.drawable.log), 0, 0, 0);

			MyPicture item1 = new MyPicture(initTex(gl, R.drawable.a1), 0, 0, 0);
			MyPicture item2 = new MyPicture(initTex(gl, R.drawable.a2), 0, 0, 0);
			MyPicture item3 = new MyPicture(initTex(gl, R.drawable.a3), 0, 0, 0);
			MyPicture item4 = new MyPicture(initTex(gl, R.drawable.a4), 0, 0, 0);
			MyPicture item5 = new MyPicture(initTex(gl, R.drawable.a5), 0, 0, 0);
			MyPicture item6 = new MyPicture(initTex(gl, R.drawable.a6), 0, 0, 0);
			items = new ArrayList<MyPicture>();
			items.add(item1);
			items.add(item2);
			items.add(item3);
			items.add(item4);
			items.add(item5);
			items.add(item6);

		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			// 设置OpenGL场景大小为屏幕大小
			gl.glViewport(0, 0, width, height);
			// 设置当前矩阵为投影矩阵
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// 重置当前矩阵
			gl.glLoadIdentity();
			// 设置视口大小
			gl.glFrustumf(-(float) width / height, (float) width / height, -1,
					1, 1, 10);
			// 设置观察点
			GLU.gluLookAt(gl, 0, 0, 2.8f, 0, 0, 0, 0, 1, 0);
			// 设置当前矩阵为模型观察矩阵
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// 重置模型观察矩阵
			gl.glLoadIdentity();

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);

			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			
			
			drawBackGround(gl);

			drawLogo(gl);

			drawItems(gl);
			
			
		
			
			

			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}

		private void drawItems(GL10 gl) {
			// TODO Auto-generated method stub
			
			
			if(r <= 0 && drawItemRotateEnd ){
				
				drawItemRotateEnd = false;
			}else if(!drawItemRotateEnd){
				
				if (r <= 1) {
					r+= 0.05f;
				}else{
					animationEnd = true;
				}
			}else{
				r -= 0.03f;
				angle+=0.3f;
			}
			for (int i = 0; i < items.size(); i++) {
				MyPicture temp = items.get(i);
	 
				gl.glLoadIdentity();//起重置操作的作用
				// 设置背景透明
				
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
				gl.glEnable(GL10.GL_BLEND);
				gl.glDisable(GL10.GL_DEPTH_TEST);
				temp.setX((float)Math.cos(angle + i*Math.PI/3)*r);
				temp.setY((float)Math.sin(angle + i*Math.PI/3)*r);	
				gl.glTranslatef(temp.getX(), temp.getY(), 0);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, itemVertexBuffer);
				gl.glEnable(GL10.GL_TEXTURE_2D); // 启用2D纹理。
				gl.glActiveTexture(temp.getPic());// 激活纹理
				gl.glBindTexture(GL10.GL_TEXTURE_2D, temp.getPic()); // 指明启用哪个纹理
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // 设置纹理坐标数组
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
		}

		private void drawLogo(GL10 gl) {
			// TODO Auto-generated method stub
			gl.glLoadIdentity();
			// 设置背景透明
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			gl.glEnable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_DEPTH_TEST);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, logoVertexBuffer);
			gl.glEnable(GL10.GL_TEXTURE_2D); // 启用2D纹理。
			gl.glActiveTexture(logoFrame.getPic());// 激活纹理
			gl.glBindTexture(GL10.GL_TEXTURE_2D, logoFrame.getPic()); // 指明启用哪个纹理
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // 设置纹理坐标数组
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

			gl.glLoadIdentity();
			// 设置背景透明
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			gl.glEnable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_DEPTH_TEST);
			if (logoScale <= 0.4f) {
				logoS = -logoS;
			} else if (logoScale > 0.8f) {
				logoS = -logoS;
			}
			if(!animationEnd){
				logoScale += logoS;
			}
			 
			gl.glScalef(logoScale, logoScale, logoScale);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, logoVertexBuffer);
			gl.glEnable(GL10.GL_TEXTURE_2D); // 启用2D纹理。
			gl.glActiveTexture(logo.getPic());// 激活纹理
			gl.glBindTexture(GL10.GL_TEXTURE_2D, logo.getPic()); // 指明启用哪个纹理
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // 设置纹理坐标数组
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		}

		private void drawBackGround(GL10 gl) {
			// TODO Auto-generated method stub

			float temp = background.getX();
			temp += 0.01f;

			if (temp >= 0.5f) {
				temp = 0;
			}

			background.setX(temp);

			gl.glLoadIdentity();
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bgVertexBuffer);
			gl.glTranslatef(temp, 0, 0);
			gl.glEnable(GL10.GL_TEXTURE_2D); // 启用2D纹理。
			gl.glActiveTexture(background.getPic());// 激活纹理
			gl.glBindTexture(GL10.GL_TEXTURE_2D, background.getPic()); // 指明启用哪个纹理
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // 设置纹理坐标数组

			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}

		private int initTex(GL10 gl, int imgId) {
			bitmap = BitmapFactory.decodeResource(getResources(), imgId);
			bitmap = formatBitmap(bitmap, 512, 512);// 把图片压缩成2的次方不然纹理贴图不成功
			IntBuffer buffer = IntBuffer.allocate(1);
			// 创建纹理
			gl.glGenTextures(1, buffer);
			int tex1 = buffer.get();
			// 设置要使用的纹理
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex1);
			// 为纹理设置线性滤波
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);
			// 生成纹理
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
			return tex1;
		}

		private Bitmap formatBitmap(Bitmap bmp, int w, int h) {
			int imgw = bmp.getWidth();
			int imgh = bmp.getHeight();
			float scale_w = (float) w / imgw;
			float scale_h = (float) h / imgh;
			Matrix m = new Matrix();
			m.setScale(scale_w, scale_h);
			Bitmap newBitmap = Bitmap.createBitmap(bmp, 0, 0, imgw, imgh, m,
					true);
			bmp = newBitmap;
			return bmp;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			for (int i = 0; i < items.size(); i++) {
				MyPicture temp = items.get(i);
				float x = (4.65f+temp.getX()-0.5f)*s;
				float y = (2.77f+temp.getY()-0.5f)*s;
				
				
				float w = 1*s;
				float h = 1*s;
				
				Log.i("ivan"," x =" + event.getX()+ " y = "+ event.getY());
	 			Rect r = new Rect((int)x, (int)y, (int)(x+w), (int)(y+h));
	 			
	 			if (r.contains((int)event.getX(), (int)event.getY())) {
	 				
	 				Log.i("ivan"," contains ");
				}else{
					
				}
			}
			break;

		case MotionEvent.ACTION_MOVE:
			
			break;
		case MotionEvent.ACTION_UP:
	
	break;
		}
		return true;
	}
}