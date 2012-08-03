package com.itcast.opengldemo2;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class OpenGLDemoItcast2 extends Activity implements OnTouchListener {
	/** Called when the activity is first created. */
	private GLSurfaceView myGsv;
	private MyRenderer myRenderer;
	private int image;
	private Bitmap bitmap;
	private float x = 0;
	private float y = 0;
	private float r = 0;
	private long time;
	private int fps = 0;
	// ��������
	private float[] vertexArray = new float[] {
			// FRONT
			-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
			0.5f,
			0.5f,
			0.5f,
			0.5f,
			0.5f,
			// BACK
			-0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, -0.5f,
			-0.5f,
			0.5f,
			0.5f,
			-0.5f,
			// LEFT
			-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f,
			-0.5f,
			0.5f,
			-0.5f,
			// RIGHT
			0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			0.5f,
			0.5f,
			// TOP
			-0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
			0.5f, -0.5f,
			// BOTTOM
			-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
			-0.5f, -0.5f,

	};

	// ��������
	private float[] texArray = new float[] { 0, 0, 0, 1, 1f, 0, 1f, 1 };

	private FloatBuffer texBuffer = FloatBuffer.wrap(texArray);
	FloatBuffer vertexBuffer = FloatBuffer.wrap(vertexArray);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		// ��OpenGL�����������
		// Bitmap b = Bitmap.createBitmap(156, 80, Bitmap.Config.ARGB_4444);
		//
		// Canvas c = new Canvas(b);
		// Paint p = new Paint();
		// p.setColor(Color.RED);
		// p.setTextSize(30);
		// c.drawText("Hello", 0, 30, p);
		//
		//
		//
		// bitmap = b;

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
			
			
			//������
			 float fogColor[]={0.5f,0.5f,0.5f,1.0f}; // ����ɫΪ�Ұ�ɫ
			 int fogMode[]={
		                GL10.GL_EXP,
		                GL10.GL_EXP2,
		                GL10.GL_LINEAR
		        };
			 
			 gl.glClearColor(0.5f,0.5f,0.5f,1.0f); //��Ч������
			 gl.glFogx(GL10.GL_FOG_MODE, fogMode[2]); //������ģʽ
			 gl.glFogfv(GL10.GL_FOG_COLOR, fogColor,0); //����ɫ
			 //���õ���ۣ���������(��֮Ϊgl.glHint(GL10.GL_FOG_HINT, GL10.GL_FASTEST);)
			   gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST); 
			  //���ܶ���ģʽΪLINEAR����Ч
//			   gl.glFogf(GL10.GL_FOG_DENSITY, 1f); 
			 gl.glFogf(GL10.GL_FOG_START, .5f); //��ʼλ��
			 gl.glFogf(GL10.GL_FOG_END, .2f); //����λ��
			 gl.glEnable(GL10.GL_FOG); //������

			
			
			image = initTex(gl, R.drawable.c);
			time = System.currentTimeMillis();

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

			fps++;
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println(fps);
				fps = 0;
				time = System.currentTimeMillis();
			}

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			// ���鿪��
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		
			

			// ���õ�һ��ɫ
			// gl.glColor4f(0.3f, 0.8f, .6f, 1);
			// gl.glTranslatef(0, 0, x);//ƽ��
			gl.glRotatef(r, x, 0, 0);// ��ת
			// gl.glScalef(x, 1, 0);//����

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glColor4f(0.3f, 0.8f, .0f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glColor4f(0.3f, 0.2f, .6f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 4, 4);
			gl.glColor4f(0.8f, 0.8f, .6f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 8, 4);
			gl.glColor4f(0.3f, 0.8f, .0f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 12, 4);
			gl.glColor4f(0.3f, 0.0f, .6f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 16, 4);
			gl.glColor4f(0.1f, 0.3f, .6f, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 20, 4);

//			gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
//			gl.glActiveTexture(image);// ��������
//			gl.glBindTexture(GL10.GL_TEXTURE_2D, image); // ָ�������ĸ�����
//			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������

			// ����ر�
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;

		case MotionEvent.ACTION_MOVE:
			x += 0.05f;
			y += 0.1f;
			r += 1;
			break;
		case MotionEvent.ACTION_UP:
	
	break;
		}
		return true;
	}
}