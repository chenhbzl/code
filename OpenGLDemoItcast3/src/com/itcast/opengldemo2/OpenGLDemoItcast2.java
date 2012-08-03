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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

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


	// ��������
	private float[] texArray = new float[] { 
			0,0, 
			0,1, 
			
			0.25f,0,
			0.25f,1,
			
			0.5f,0,
			0.5f,1,
			
			0.75f,0,
			0.75f,1,
			
			
			1f,0, 
			1f,1 };

	private FloatBuffer texBuffer = FloatBuffer.wrap(texArray);
	

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
	private float a = 2.325f;
	private float b = 4.65f;
	private float c = 6.975f;
	private float d = 9.3f;
	private float z = 1;

 
private float length = 160;
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
			
			
//			//������
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
			 gl.glFogf(GL10.GL_FOG_START, 0.5f); //��ʼλ��
			 gl.glFogf(GL10.GL_FOG_END, -0.5f); //����λ��
			 gl.glEnable(GL10.GL_FOG); //������

			
			
			image = initTex(gl, R.drawable.a);
			image2 = initTex(gl, R.drawable.c);

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
		
			// ��������
		 float[] vertexArray = new float[] {
					-4.65f,2.77f,0f,
					-4.65f,-2.77f,0f,
					
					-2.325f-a*s,2.77f,0-z*s,
					-2.325f-a*s,-2.77f,0-z*s,
					
					0-b*s,2.77f,0,
					0-b*s,-2.77f,0,
					
					2.325f-c*s,2.77f,0-z*s,
					2.325f-c*s,-2.77f,0-z*s,
					
					4.65f-d*s,2.77f,0f,
					4.65f-d*s,-2.77f,0f,
			};
		 	FloatBuffer vertexBuffer = FloatBuffer.wrap(vertexArray);

			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
//			gl.glColor4f(0.3f, 0.8f, .0f, 1);

			gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
			gl.glActiveTexture(image);// ��������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, image); // ָ�������ĸ�����
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������

			
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 10);
 
		 

			
			
			
			
			
			
			float[] vertexArray2 = new float[] {
			 
					4.65f-d*s,2.77f,0f,
					4.65f-d*s,-2.77f,0f,
					
					4.65f-c*s,2.77f,z*s-z,
					4.65f-c*s,-2.77f,z*s-z,
					
					4.65f-b*s,2.77f,0f,
					4.65f-b*s,-2.77f,0f,
					
					4.65f-a*s,2.77f,z*s-z,
					4.65f-a*s,-2.77f,z*s-z,
					
					4.65f,2.77f,0f,
					4.65f,-2.77f,0f,
			};
		 	FloatBuffer vertexBuffer2 = FloatBuffer.wrap(vertexArray2);
			
		 	gl.glEnable(GL10.GL_TEXTURE_2D); // ����2D����
			gl.glActiveTexture(image2);// ��������
			gl.glBindTexture(GL10.GL_TEXTURE_2D, image2); // ָ�������ĸ�����
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // ����������������
			
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer2);
			
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 10);
			
			

			

			
			
			
			
			
			
			// ����ر�
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		}

	}
private float beginX;
private float s;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			beginX = event.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			if ((event.getX()-beginX)/length <0 && (event.getX()-beginX)/length>-1) {
				s = -(event.getX()-beginX)/length;
			}
			
			break;
		case MotionEvent.ACTION_UP:
			if (s>0.5) {
				int temp = image;
				image = image2;
				image2 = temp;
				s = 0;
			}else{
				s = 0;
			}
	break;
		}
		return true;
	}
}