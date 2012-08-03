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
			//声明透视修正模式为速度优先
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
			//声明清除屏幕使用的颜色
			gl.glClearColor(0.2f, 0.3f, 0.4f, 0);
			//设置着色模式（GL10.GL_SMOOTH:平滑；GL_FLAT:单调）
			gl.glShadeModel(GL10.GL_SMOOTH);
			//设置深度缓存
			gl.glClearDepthf(1.0f);
			//开启深度测试
			gl.glEnable(GL10.GL_DEPTH_TEST);
			//设置深度测试模式
			gl.glDepthFunc(GL10.GL_LEQUAL);

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
			gl.glFrustumf(-(float) width / height, (float) width / height
			, -1, 1, 1, 10);
			//设置观察点
			GLU.gluLookAt(gl, 0, 0, 2.8f, 0, 0, 0, 0, 1, 0);
			//设置当前矩阵为模型观察矩阵
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			// 重置模型观察矩阵
			gl.glLoadIdentity();

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);//清除颜色缓存和深度缓存，然后调用
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			
			gl.glTranslatef((float) (Math.sin(rotate)*r), (float) (Math.cos(rotate)*r), 0);
			rotate+=0.1;
			
			
			//数组开启
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			
			
			//设置单一颜色
 			
 			
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			
			
			
			//数组关闭
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
			

		}
    	
    }
}