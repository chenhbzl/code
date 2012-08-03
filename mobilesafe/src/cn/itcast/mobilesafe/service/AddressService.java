package cn.itcast.mobilesafe.service;
import java.io.File;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import cn.itcast.mobilesafe.util.DownLoadManager;

/*
 * �ṩ����, ��ѯĳ����������Ӧ�Ĺ����� 
 * 1. Ҫ�����ݿ� ���뵽������ 
 * assets Ŀ¼�²��ܴ�Ŵ���1M���ļ� 
 */
public class AddressService {
	/*
	 * �ж����ݿ� �Ƿ��Ѿ�����
	 */
	public boolean isDBExist() {
		File file = new File(Environment.getExternalStorageDirectory(),
				"address.db");
		return file.exists();
	}
	/**
	 * ��ʼ�����ݿ�
	 * 
	 * @throws Exception
	 */
	public void downLoadDB(String dburl, ProgressDialog pd) throws Exception {
		DownLoadManager.getDBFromServer(dburl, pd);
	}

	/**
	 * ��ѯ���ݿ� ���� number ����ѯ�ĵ绰����
	 */
	public String getAddress(String number) {
		String address = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/sdcard/address.db",
				null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = null;
		if (db.isOpen()) {
			// ��ѯ�����Ӧ�Ĺ�����
			// ������ֻ��� ��ȡ ǰ7λ
			// �ֻ���11λ �ֻ� 13X 15X 18x
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
			}else{ //�̶��绰����Ĳ�ѯ  
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
					return "ģ����";
				}else if(number.length()==7||number.length()==8){
					return "���غ��� ";
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
