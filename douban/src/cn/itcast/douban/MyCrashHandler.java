package cn.itcast.douban;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gdata.client.douban.DoubanService;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.util.ServiceException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.text.format.DateFormat;

/**
 * �Զ���� �쳣������ , ʵ���� UncaughtExceptionHandler�ӿ� 
 * @author Administrator
 *
 */
public class MyCrashHandler implements UncaughtExceptionHandler {
	// ������ ����Ӧ�ó��� ֻ��һ�� MyCrash-Handler 
	private static MyCrashHandler myCrashHandler ;
	private Context context;
	private DoubanService service;
	private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	//1.˽�л����췽��
	
	private MyCrashHandler(){
		
	}
	
	public static synchronized MyCrashHandler getInstance(){
		if(myCrashHandler!=null){
			return myCrashHandler;
		}else {
			myCrashHandler  = new MyCrashHandler();
			return myCrashHandler;
		}
	}
	public void init(Context context,DoubanService service){
		this.context = context;
		this.service = service;
	}
	

	public void uncaughtException(Thread arg0, Throwable arg1) {
		System.out.println("����ҵ��� ");
		// 1.��ȡ��ǰ����İ汾��. �汾��id
		String versioninfo = getVersionInfo();
		
		// 2.��ȡ�ֻ���Ӳ����Ϣ.
		String mobileInfo  = getMobileInfo();
		
		// 3.�Ѵ���Ķ�ջ��Ϣ ��ȡ���� 
		String errorinfo = getErrorInfo(arg1);
		
		
//		// 4.�����е���Ϣ ������Ϣ��Ӧ��ʱ�� �ύ�������� 
//		System.out.println(dataFormat.format(new Date()));
//		System.out.println("------------");	
//		System.out.println(versioninfo);
//		System.out.println("------------");
//		System.out.println(mobileInfo);
//		
//		System.out.println("------------");
//		
//		System.out.println(errorinfo);
		try {
			service.createNote(new PlainTextConstruct(dataFormat.format(new Date())), 
					new PlainTextConstruct(versioninfo+mobileInfo+errorinfo), "public", "yes");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		//�ɵ���ǰ�ĳ��� 
		android.os.Process.killProcess(android.os.Process.myPid());
		
		
	}

	/**��ȡ
	 * �������Ϣ 
	 * @param arg1
	 * @return
	 */
	private String getErrorInfo(Throwable arg1) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		arg1.printStackTrace(pw);
		pw.close();
		String error= writer.toString();
		return error;
	}

	/**
	 * �����ֻ���Ӳ����Ϣ 
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		//ͨ�������ȡϵͳ��Ӳ����Ϣ 
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for(Field field: fields){
				//�������� ,��ȡ˽�е���Ϣ 
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name+"="+value);
				sb.append("\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private String getVersionInfo(){
		try {
			PackageManager pm = context.getPackageManager();
			 PackageInfo info =pm.getPackageInfo(context.getPackageName(), 0);
			 return  info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "�汾��δ֪";
		}
	}

}
