package com.itcast.opengldemo2;

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

public class OpenGLDemoItcast2 extends Activity implements OnTouchListener {
	/** Called when the activity is first created. */
	private GLSurfaceView myGsv;
	private MyRenderer myRenderer;
	private int image;
	private int image2;
	private Bitmap bitmap;
	private float x = 0;
	private float y = 0;
	private float r = 0;
	private long time;
	private int fps = 0;
	private float s = 42;//320/9.3f;
	List<MyPicture> pictures;
	// ��������
	private float[] texArray = new float[] { 0, 0, 0, 1, 1f, 0, 1f, 1 };

	private FloatBuffer texBuffer = FloatBuffer.wrap(texArray);

	float[] vertexArray2 = new float[] { 
			-1.5f, 1f, 0f,
			-1.5f, -1f, 0f, 
			1.5f,1f, 0f,
			1.5f, -1f, 0f, };
	FloatBuffer vertexBuffer2 = FloatBuffer.wrap(vertexArray2);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȥ����Ϣ��
		myGsv = new GLSurfaceView(this);
		myRenderer = new MyRenderer();
		myGsv.setRenderer(myRenderer);
		myGsv.setOnTouchListener(this);
		setContentView(myGsv);

	}

	// ͼƬ�ߴ�ѹ��
	private Bitmap formatBitmap(Bitmap bmp, int w, int h) {

		int imgw = bmp.getWidth();
		int imgh = bmp.getHeight();
		float scale_w = (float) w / imgw;
		float scale_h = (float) h / imgh;
		Matrix m = new Matrix();
		m.setScale(scale_w, scale_h);
		Bitmap newBitmap = Bitmap.createBitmap(bmp, 0, 0, imgw, imgh, m, true);
		bmp = newBitmap;
		return bmp;
	}

	private int initTex(GL10 gl, int imgId) {
		bitmap = BitmapFactory.decodeResource(getResources(), imgId);

		bitmap = formatBitmap(bitmap, 512, 512);

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


	class MyRenderer implements Renderer {

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
			// �������������������й���;

//			// //������
//			float fogColor[] = { 0.5f, 0.5f, 0.5f, 1.0f }; // ����ɫΪ�Ұ�ɫ
//			int fogMode[] = { GL10.GL_EXP, GL10.GL_EXP2, GL10.GL_LINEAR };
//
//			gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f); // ��Ч������
//			gl.glFogx(GL10.GL_FOG_MODE, fogMode[2]); // ������ģʽ
//			gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0); // ����ɫ
//			// ���õ���ۣ���������(��֮Ϊgl.glHint(GL10.GL_FOG_HINT, GL10.GL_FASTEST);)
//			gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST);
//			// ���ܶ���ģʽΪLINEAR����Ч
//			// gl.glFogf(GL10.GL_FOG_DENSITY, 1f);
//			gl.glFogf(GL10.GL_FOG_START, 0.5f); // ��ʼλ��
//			gl.glFogf(GL10.GL_FOG_END, -0.5f); // ����λ��
//			gl.glEnable(GL10.GL_FOG); // ������

			image = initTex(gl, R.drawable.a);
			image2 = initTex(gl, R.drawable.c);
			
			MyPicture myP1 = new MyPicture(initTex(gl, R.drawable.a), 0f, 0, 0f);
			
			MyPicture myP2 = new MyPicture(initTex(gl, R.drawable.c), -4.8f, 2, -2.8f);
			MyPicture myP3 = new MyPicture(initTex(gl, R.drawable.d), -6.8f, 0, -5.2f);
			MyPicture myP4 = new MyPicture(initTex(gl, R.drawable.b), -5.8f, -3, -1.5f);
			
			MyPicture myP5 = new MyPicture(initTex(gl, R.drawable.e), -4.8f, -1.5f, -2.8f);
			
			MyPicture myP6 = new MyPicture(initTex(gl, R.drawable.f), -3.8f, 3.2f, -1.2f);
			pictures = new ArrayList<MyPicture>();
			pictures.add(myP1);
//			pictures.add(myP2);
//			pictures.add(myP3);
//			pictures.add(myP4);
//			pictures.add(myP5);
//			pictures.add(myP6);
			
			
			
			

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
			gl.glLoadIdentity();

			// ���鿪��
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		 
			for (int i = 0; i < pictures.size(); i++) {
				MyPicture temp = pictures.get(i);
				
				float tempX = temp.getX();
				
				tempX += 0.01f;
				
				temp.setX(tempX);
				
				gl.glTranslatef(temp.getX(), temp.getY(), temp.getZ());
				
				gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
				gl.glActiveTexture(temp.getPic());// ��������
				gl.glBindTexture(GL10.GL_TEXTURE_2D, temp.getPic()); // ָ�������ĸ�����
				gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������
				
				gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer2);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
			}
			


			// ����ر�
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			MyPicture temp = pictures.get(0);
			float x = (4.65f+temp.getX()-1.5f)*s;
			float y = (2.77f+temp.getY()-1)*s;
			
			
			float w = 3*s;
			float h = 2*s;
			Log.i("ivan"," x =" + event.getX()+ " y = "+ event.getY());
 			Rect r = new Rect((int)x, (int)y, (int)(x+w), (int)(y+h));
 			
 			if (r.contains((int)event.getX(), (int)event.getY())) {
 				Log.i("ivan"," contains ");
			}else{
				Log.i("ivan"," no ");
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