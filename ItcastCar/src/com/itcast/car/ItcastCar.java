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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȥ����Ϣ��

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
			// ����͸������ģʽΪ�ٶ�����
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			// ���������Ļʹ�õ���ɫ
			gl.glClearColor(0, 0, 0, 0);
			// ������ɫģʽ��GL10.GL_SMOOTH:ƽ����GL_FLAT:������
			gl.glShadeModel(GL10.GL_SMOOTH);
			// ������Ȼ���
			gl.glClearDepthf(1.0f);
			// ������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			// ������Ȳ���ģʽ
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
			// ����OpenGL������СΪ��Ļ��С
			gl.glViewport(0, 0, width, height);
			// ���õ�ǰ����ΪͶӰ����
			gl.glMatrixMode(GL10.GL_PROJECTION);
			// ���õ�ǰ����
			gl.glLoadIdentity();
			// �����ӿڴ�С
			gl.glFrustumf(-(float) width / height, (float) width / height, -1,
					1, 1, 10);
			// ���ù۲��
			GLU.gluLookAt(gl, 0, 0, 2.8f, 0, 0, 0, 0, 1, 0);
			// ���õ�ǰ����Ϊģ�͹۲����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ����ģ�͹۲����
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
	 
				gl.glLoadIdentity();//�����ò���������
				// ���ñ���͸��
				
				gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
				gl.glEnable(GL10.GL_BLEND);
				gl.glDisable(GL10.GL_DEPTH_TEST);
				temp.setX((float)Math.cos(angle + i*Math.PI/3)*r);
				temp.setY((float)Math.sin(angle + i*Math.PI/3)*r);	
				gl.glTranslatef(temp.getX(), temp.getY(), 0);
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, itemVertexBuffer);
				gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
				gl.glActiveTexture(temp.getPic());// ��������
				gl.glBindTexture(GL10.GL_TEXTURE_2D, temp.getPic()); // ָ�������ĸ�����
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			}
		}

		private void drawLogo(GL10 gl) {
			// TODO Auto-generated method stub
			gl.glLoadIdentity();
			// ���ñ���͸��
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
			gl.glEnable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_DEPTH_TEST);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, logoVertexBuffer);
			gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
			gl.glActiveTexture(logoFrame.getPic());// ��������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, logoFrame.getPic()); // ָ�������ĸ�����
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

			gl.glLoadIdentity();
			// ���ñ���͸��
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
			gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
			gl.glActiveTexture(logo.getPic());// ��������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, logo.getPic()); // ָ�������ĸ�����
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������
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
			gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
			gl.glActiveTexture(background.getPic());// ��������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, background.getPic()); // ָ�������ĸ�����
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������

			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		}

		private int initTex(GL10 gl, int imgId) {
			bitmap = BitmapFactory.decodeResource(getResources(), imgId);
			bitmap = formatBitmap(bitmap, 512, 512);// ��ͼƬѹ����2�Ĵη���Ȼ������ͼ���ɹ�
			IntBuffer buffer = IntBuffer.allocate(1);
			// ��������
			gl.glGenTextures(1, buffer);
			int tex1 = buffer.get();
			// ����Ҫʹ�õ�����
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex1);
			// Ϊ�������������˲�
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
					GL10.GL_LINEAR);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
					GL10.GL_LINEAR);
			// ��������
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