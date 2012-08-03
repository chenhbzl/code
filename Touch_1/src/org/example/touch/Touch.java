
package org.example.touch;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class Touch extends Activity implements OnTouchListener {
   private static final String TAG = "Touch";
   // These matrices will be used to move and zoom image
   Matrix matrix = new Matrix();
   Matrix savedMatrix = new Matrix();

   // 三种状态
   static final int NONE = 0;
   static final int DRAG = 1;
   static final int ZOOM = 2; 
   int mode = NONE;
   // Remember some things for zooming
   
   //开始点
   PointF start = new PointF();
   //中间点
   PointF mid = new PointF();
   //旧的两点间的距离
   float oldDist = 1f;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      
      
      ImageView view = (ImageView) findViewById(R.id.imageView);
      view.setOnTouchListener(this);
      
   }

   @Override
   public boolean onTouch(View v, MotionEvent event) {
	   
	   
	   
	   
	   //因为程序中只有一个空间添加了touch事件
      ImageView imageView = (ImageView) v;

      System.out.println(event.getX());
      switch (event.getAction()) {
      //单点按下的时候响应
      case MotionEvent.ACTION_DOWN:
    	  
    	  //第一次进来不起作用
    	  //第二次进入的时候是给savedMatrix幅值为matrix
         savedMatrix.set(matrix);
         //给起始点实例化
         start.set(event.getX(), event.getY());
         
         Log.d(TAG, "mode=DRAG");
         mode = DRAG;
         
         break;
         
         //多点按下的时候响应
      case MotionEvent.ACTION_POINTER_DOWN:
    	  //记录开始两点间的距离
         oldDist = spacing(event);
         Log.d(TAG, "oldDist=" + oldDist);
         if (oldDist > 10f) {
        	 
            savedMatrix.set(matrix);
            //获取两点之间的中心点坐标
            midPoint(mid, event);
            mode = ZOOM;
            Log.d(TAG, "mode=ZOOM");
         }
         break;
      case MotionEvent.ACTION_UP:
    	  
      case MotionEvent.ACTION_POINTER_UP:
         mode = NONE;
         Log.d(TAG, "mode=NONE");
         break;
         
         
         //移动的时候响应
      case MotionEvent.ACTION_MOVE:
    	  //判断是什么操作如果Drag执行
         if (mode == DRAG) {
            //图片的起始位置为上次操作的结束位置
        	 //让matrix里面的值等于savedMatrix；
        	 //第一次进来没有作用
        	
              matrix.set(savedMatrix);
            
            //！！！！！！！！！
            matrix.postTranslate(event.getX() - start.x,
                  event.getY() - start.y);
            
         }
         else if (mode == ZOOM) {
            float newDist = spacing(event);
            if (newDist > 10f) {
            	
               matrix.set(savedMatrix);
               float scale = newDist / oldDist;
               //！！！！！！！！！
               //前两个参数是X轴和y轴的缩放比 后两个是中心点
               matrix.postScale(scale, scale, mid.x, mid.y);
            }
         }
         break;
      }
      //真正改变图片位置的代码
      imageView.setImageMatrix(matrix);
      
      
      return true;  
   }


   /** 两点之间的距离 */
   private float spacing(MotionEvent event) {
	   //!!!!!!!event.getX(0)获取多点中第一点的X的坐标
      float x = event.getX(0) - event.getX(1);
      float y = event.getY(0) - event.getY(1);
      return FloatMath.sqrt(x * x + y * y);
   }

   /** 两点的中心点坐标 */
   private void midPoint(PointF point, MotionEvent event) {
      float x = event.getX(0) + event.getX(1);
      float y = event.getY(0) + event.getY(1);
      point.set(x / 2, y / 2);
   }
}
