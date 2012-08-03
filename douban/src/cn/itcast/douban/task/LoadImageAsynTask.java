package cn.itcast.douban.task;

import cn.itcast.douban.util.NetUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class LoadImageAsynTask extends AsyncTask<String, Void, Bitmap> {
	ImageTaskCallback iImageTaskCallback;
	
	
	// 创建构造方法  要求 在new LoadImageAsynTask的时候 一定要把实例化该接口的一个对象传递进来
	public LoadImageAsynTask(ImageTaskCallback iImageTaskCallback) {
		this.iImageTaskCallback = iImageTaskCallback;
	}

	public interface ImageTaskCallback {
		// 当图片下载完毕后的方法
		void onImageLoaded(Bitmap bitmap);
		// 在图片下载之前调用的方法 
		void beforeImageLoaded();
	}

	@Override
	protected void onPreExecute() {
		//设置imageview的图片为默认图片 
		super.onPreExecute();
		iImageTaskCallback.beforeImageLoaded();
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// 设置imageview的图片为下载完成后的图片
		super.onPostExecute(result);
		iImageTaskCallback.onImageLoaded(result);
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		String path = params[0];
		try {
			Bitmap bitmap = NetUtil.getBitmapImage(path);
			return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
