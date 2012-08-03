package com.opengldemo;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;

public class OpenGLDemo1 extends Activity {
    /** Called when the activity is first created. */
	private GLSurfaceView myGsv;
	private MyRenderer myRenderer;
	private float[] vertexArray = new float[]{
			-1f,1f,0f,
			-1f,-1f,0f,
			1f,1f,0f,
			1f,-1f,0f,
	};
	FloatBuffer vertexBuffer = FloatBuffer.wrap(vertexArray);
	
	private float[] colorArray = new float[]{
			1f,0f,0f,1f,
			0f,1f,0f,1f,
			0f,0f,1f,1f,
			1f,0f,1f,1f};
			private FloatBuffer colorBuffer = FloatBuffer.wrap(colorArray);

			
			
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        myGsv = new GLSurfaceView(this);
        myRenderer = new MyRenderer();
        myGsv.setRenderer(myRenderer);
        
        setContentView(myGsv);
        
    }
    
    
    
    class MyRenderer implements Renderer{

		private float rotate = 0;
		private float r =1;

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			//����͸������ģʽΪ�ٶ�����
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			//���������Ļʹ�õ���ɫ
			gl.glClearColor(0.2f, 0.3f, 0.4f, 0);
			//������ɫģʽ��GL10.GL_SMOOTH:ƽ����GL_FLAT:������
			gl.glShadeModel(GL10.GL_SMOOTH);
			//������Ȼ���
			gl.glClearDepthf(1.0f);
			//������Ȳ���
			gl.glEnable(GL10.GL_DEPTH_TEST);
			//������Ȳ���ģʽ
			gl.glDepthFunc(GL10.GL_LEQUAL);

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
			gl.glFrustumf(-(float) width / height, (float) width / height
			, -1, 1, 1, 10);
			//���ù۲��
			GLU.gluLookAt(gl, 0, 0, 2.8f, 0, 0, 0, 0, 1, 0);
			//���õ�ǰ����Ϊģ�͹۲����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// ����ģ�͹۲����
			gl.glLoadIdentity();

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);//�����ɫ�������Ȼ��棬Ȼ�����
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			
			gl.glTranslatef((float) (Math.sin(rotate)*r), (float) (Math.cos(rotate)*r), 0);
			rotate+=0.1;
			
			
			//���鿪��
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
			
			//���õ�һ��ɫ
 			
 			
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			
			
			
			//����ر�
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			

		}
    	
    }
}