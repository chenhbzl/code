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
	// 顶点数组
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

	// 纹理数组
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

	// 图片尺寸压缩
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

		// 在OpenGL里面绘制文字
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

	class MyRenderer implements Renderer {

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
			// 载入纹理并且与整数进行关联;
			
			
			//雾化代码
			 float fogColor[]={0.5f,0.5f,0.5f,1.0f}; // 雾颜色为灰白色
			 int fogMode[]={
		                GL10.GL_EXP,
		                GL10.GL_EXP2,
		                GL10.GL_LINEAR
		        };
			 
			 gl.glClearColor(0.5f,0.5f,0.5f,1.0f); //雾化效果明显
			 gl.glFogx(GL10.GL_FOG_MODE, fogMode[2]); //设置雾模式
			 gl.glFogfv(GL10.GL_FOG_COLOR, fogColor,0); //雾颜色
			 //更好的外观，需求大的雾(反之为gl.glHint(GL10.GL_FOG_HINT, GL10.GL_FASTEST);)
			   gl.glHint(GL10.GL_FOG_HINT, GL10.GL_NICEST); 
			  //雾密度在模式为LINEAR下无效
//			   gl.glFogf(GL10.GL_FOG_DENSITY, 1f); 
			 gl.glFogf(GL10.GL_FOG_START, .5f); //开始位置
			 gl.glFogf(GL10.GL_FOG_END, .2f); //结束位置
			 gl.glEnable(GL10.GL_FOG); //启用雾

			
			
			image = initTex(gl, R.drawable.c);
			time = System.currentTimeMillis();

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

			fps++;
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println(fps);
				fps = 0;
				time = System.currentTimeMillis();
			}

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();

			// 数组开启
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		
			

			// 设置单一颜色
			// gl.glColor4f(0.3f, 0.8f, .6f, 1);
			// gl.glTranslatef(0, 0, x);//平移
			gl.glRotatef(r, x, 0, 0);// 旋转
			// gl.glScalef(x, 1, 0);//缩放

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

//			gl.glEnable(GL10.GL_TEXTURE_2D); // 启用2D纹理。
//			gl.glActiveTexture(image);// 激活纹理
//			gl.glBindTexture(GL10.GL_TEXTURE_2D, image); // 指明启用哪个纹理
//			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // 设置纹理坐标数组

			// 数组关闭
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