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
 * 自定义的 异常处理类 , 实现了 UncaughtExceptionHandler接口 
 * @author Administrator
 *
 */
public class MyCrashHandler implements UncaughtExceptionHandler {
	// 需求是 整个应用程序 只有一个 MyCrash-Handler 
	private static MyCrashHandler myCrashHandler ;
	private Context context;
	private DoubanService service;
	private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	
	//1.私有化构造方法
	
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
		System.out.println("程序挂掉了 ");
		// 1.获取当前程序的版本号. 版本的id
		String versioninfo = getVersionInfo();
		
		// 2.获取手机的硬件信息.
		String mobileInfo  = getMobileInfo();
		
		// 3.把错误的堆栈信息 获取出来 
		String errorinfo = getErrorInfo(arg1);
		
		
//		// 4.把所有的信息 还有信息对应的时间 提交到服务器 
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
	
		//干掉当前的程序 
		android.os.Process.killProcess(android.os.Process.myPid());
		
		
	}

	/**获取
	 * 错误的信息 
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
	 * 返回手机的硬件信息 
	 * @return
	 */
	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		//通过反射获取系统的硬件信息 
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for(Field field: fields){
				//暴力反射 ,获取私有的信息 
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
			return "版本号未知";
		}
	}

}
