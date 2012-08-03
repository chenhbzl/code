package com.itcast.jpct;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

public class HelloWorld extends Activity {
    /** Called when the activity is first created. */
	GLSurfaceView myGSV;
	MyRenderer myR ;
	FrameBuffer fb;
	World world;
	Object3D cube;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        myGSV = new GLSurfaceView(this);
        myR = new MyRenderer();
        myGSV.setRenderer(myR);
        setContentView(myGSV);
        
    }
    class MyRenderer implements Renderer{

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int w, int h) {
			// TODO Auto-generated method stub
			fb = new FrameBuffer(gl, w, h);
			world = new World();
			
			world.setAmbientLight(20, 20, 20);
			
			Resources res = getResources();
			Texture man = new Texture(res.openRawResource(R.raw.manpic));

			Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.icon)), 64, 64));
			TextureManager.getInstance().addTexture("texture", texture);
			TextureManager.getInstance().addTexture("man", man);

			
//			cube = Primitives.getCylinder(10);//
			try {
				cube = Loader.loadMD2(getResources().getAssets().open("tris.md2"),1f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//通过md2文件生成Object3D对象

// 			cube.calcTextureWrapSpherical();
 			cube.setTexture("man");
			
			
			Light sun = new Light(world);//通过当前的world对象获取Light对象
			sun.setIntensity(250, 250, 250);
			SimpleVector sv = new SimpleVector();
			sv.set(cube.getTransformedCenter());
			sv.y -= 100;
			sv.z -= 100;
			sun.setPosition(sv);//设置光源的位置

			
			//将3dObject添加到World中
			world.addObject(cube);
			//设置视角
			Camera cam = world.getCamera();
			cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
			cam.lookAt(cube.getTransformedCenter());
			//性能优化
			MemoryHelper.compact();

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			
			cube.rotateY(0.1f);
//			cube.translate(0.1f, 0, 0);
			fb.clear(RGBColor.BLACK);//清楚上一帧内容
			world.renderScene(fb);//渲染场景
			world.draw(fb);//将world中的内容画到FrameBuffer里面
			fb.display();//将FrameBuffer里面的内容显示出来

		}
    	
    }
}