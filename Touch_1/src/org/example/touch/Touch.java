
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

   // ����״̬
   static final int NONE = 0;
   static final int DRAG = 1;
   static final int ZOOM = 2; 
   int mode = NONE;
   // Remember some things for zooming
   
   //��ʼ��
   PointF start = new PointF();
   //�м��
   PointF mid = new PointF();
   //�ɵ������ľ���
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
	   
	   
	   
	   
	   //��Ϊ������ֻ��һ���ռ������touch�¼�
      ImageView imageView = (ImageView) v;

      System.out.println(event.getX());
      switch (event.getAction()) {
      //���㰴�µ�ʱ����Ӧ
      case MotionEvent.ACTION_DOWN:
    	  
    	  //��һ�ν�����������
    	  //�ڶ��ν����ʱ���Ǹ�savedMatrix��ֵΪmatrix
         savedMatrix.set(matrix);
         //����ʼ��ʵ����
         start.set(event.getX(), event.getY());
         
         Log.d(TAG, "mode=DRAG");
         mode = DRAG;
         
         break;
         
         //��㰴�µ�ʱ����Ӧ
      case MotionEvent.ACTION_POINTER_DOWN:
    	  //��¼��ʼ�����ľ���
         oldDist = spacing(event);
         Log.d(TAG, "oldDist=" + oldDist);
         if (oldDist > 10f) {
        	 
            savedMatrix.set(matrix);
            //��ȡ����֮������ĵ�����
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
         
         
         //�ƶ���ʱ����Ӧ
      case MotionEvent.ACTION_MOVE:
    	  //�ж���ʲô�������Dragִ��
         if (mode == DRAG) {
            //ͼƬ����ʼλ��Ϊ�ϴβ����Ľ���λ��
        	 //��matrix�����ֵ����savedMatrix��
        	 //��һ�ν���û������
        	
              matrix.set(savedMatrix);
            
            //������������������
            matrix.postTranslate(event.getX() - start.x,
                  event.getY() - start.y);
            
         }
         else if (mode == ZOOM) {
            float newDist = spacing(event);
            if (newDist > 10f) {
            	
               matrix.set(savedMatrix);
               float scale = newDist / oldDist;
               //������������������
               //ǰ����������X���y������ű� �����������ĵ�
               matrix.postScale(scale, scale, mid.x, mid.y);
            }
         }
         break;
      }
      //�����ı�ͼƬλ�õĴ���
      imageView.setImageMatrix(matrix);
      
      
      return true;  
   }


   /** ����֮��ľ��� */
   private float spacing(MotionEvent event) {
	   //!!!!!!!event.getX(0)��ȡ����е�һ���X������
      float x = event.getX(0) - event.getX(1);
      float y = event.getY(0) - event.getY(1);
      return FloatMath.sqrt(x * x + y * y);
   }

   /** ��������ĵ����� */
   private void midPoint(PointF point, MotionEvent event) {
      float x = event.getX(0) + event.getX(1);
      float y = event.getY(0) + event.getY(1);
      point.set(x / 2, y / 2);
   }
}
