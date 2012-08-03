package cn.itcast.mobilesafe.service;
import java.io.File;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import cn.itcast.mobilesafe.util.DownLoadManager;

/*
 * 提供方法, 查询某个号码所对应的归属地 
 * 1. 要把数据库 引入到程序里 
 * assets 目录下不能存放大于1M的文件 
 */
public class AddressService {
	/*
	 * 判断数据库 是否已经存在
	 */
	public boolean isDBExist() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"address.db");
		return file.exists();
	}
	/**
	 * 初始化数据库
	 * 
	 * @throws Exception
	 */
	public void downLoadDB(String dburl, ProgressDialog pd) throws Exception {
		DownLoadManager.getDBFromServer(dburl, pd);
	}

	/**
	 * 查询数据库 参数 number 被查询的电话号码
	 */
	public String getAddress(String number) {
		String address = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/address.db",
				null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = null;
		if (db.isOpen()) {
			// 查询号码对应的归属地
			// 如果是手机号 获取 前7位
			// 手机号11位 手机 13X 15X 18x
			if (number.matches("^1[358]\\d{9}$")) {
				String numberprefix = number.substring(0, 7);
				cursor = db.rawQuery(
						"select city from info where mobileprefix=? limit 1",
						new String[] { numberprefix });
				if (cursor.moveToFirst()) {
					address = cursor.getString(0);
					System.out.println(address);
				}
				cursor.close();
			}else{ //固定电话号码的查询  
				if(number.length()==10){
					String numberprefix = number.substring(0, 3);
					cursor = db.rawQuery(
							"select city from info where area=? limit 1" ,
							new String[] { numberprefix });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
				}else if(number.length()==11){
					String numberprefix = number.substring(0, 3);
					cursor = db.rawQuery(
							"select city from info where area=? limit 1" ,
							new String[] { numberprefix });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					String numberprefix4 = number.substring(0, 4);
					cursor = db.rawQuery(
							"select city from info where area=? limit 1" ,
							new String[] { numberprefix4 });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
					
				}else if(number.length()==12){

					String numberprefix = number.substring(0, 4);
					cursor = db.rawQuery(
							"select city from info where area=? limit 1" ,
							new String[] { numberprefix });
					if (cursor.moveToFirst()) {
						address = cursor.getString(0);
					}
					cursor.close();
				}else if(number.length()==4){
					return "模拟器";
				}else if(number.length()==7||number.length()==8){
					return "本地号码 ";
				}
			}
			db.close();
		}
		if (address != null) {
			return address;
		} else {
			return number;
		}
	}

}
