package cn.itcast.douban.task;

import cn.itcast.douban.util.NetUtil;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class LoadImageAsynTask extends AsyncTask<String, Void, Bitmap> {
	ImageTaskCallback iImageTaskCallback;
	
	
	// �������췽��  Ҫ�� ��new LoadImageAsynTask��ʱ�� һ��Ҫ��ʵ�����ýӿڵ�һ�����󴫵ݽ���
	public LoadImageAsynTask(ImageTaskCallback iImageTaskCallback) {
		this.iImageTaskCallback = iImageTaskCallback;
	}

	public interface ImageTaskCallback {
		// ��ͼƬ������Ϻ�ķ���
		void onImageLoaded(Bitmap bitmap);
		// ��ͼƬ����֮ǰ���õķ��� 
		void beforeImageLoaded();
	}

	@Override
	protected void onPreExecute() {
		//����imageview��ͼƬΪĬ��ͼƬ 
		super.onPreExecute();
		iImageTaskCallback.beforeImageLoaded();
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// ����imageview��ͼƬΪ������ɺ��ͼƬ
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
