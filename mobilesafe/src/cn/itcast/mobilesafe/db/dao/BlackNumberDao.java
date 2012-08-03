package cn.itcast.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.itcast.mobilesafe.db.DBOpenHelper;


/*
 * �������ݿ��data access object
 */
public class BlackNumberDao {
	DBOpenHelper dbOpenHelper;
	Context context;

	public BlackNumberDao(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
	}
	
	/*
	 * ��� ���������� 
	 * params number : Ҫ��ӵĺ��������� 
	 */
	public void save(String number){
		SQLiteDatabase db =	dbOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("insert into blacknumber (number) values(?) ", new Object[]{number});
			db.close();
		}
	}
	/*
	 * ��ѯ�ĺ���������
	 */
	public boolean query(String number){
		long result=0;
		SQLiteDatabase db =	dbOpenHelper.getReadableDatabase();
		if(db.isOpen()){
		  Cursor cursor =	db.rawQuery("select count(*) from blacknumber where number=? ", new String[]{number});
			if(cursor.moveToFirst()){
				
				result = cursor.getLong(0);
			}
			cursor.close();
			db.close();
		}
		return result>0;
	}
	/*
	 * ɾ������������ 
	 */
	public void delete (String number){
		SQLiteDatabase db =	dbOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from blacknumber where number=?",new Object[]{number});
			db.close();
		}
	}
	
	/*
	 * ��ѯ���еļ�¼ 
	 */
	public List<String> findAll(){
		List<String> numbers = new ArrayList<String>();
		SQLiteDatabase db =	dbOpenHelper.getReadableDatabase();
		if(db.isOpen()){
		   Cursor cursor =	db.rawQuery("select * from blacknumber", null);
			while (cursor.moveToNext()){
				String number = cursor.getString(cursor.getColumnIndex("number"));
				numbers.add(number);
			}
			cursor.close();
			db.close();
		}
		return numbers;
	}
}
