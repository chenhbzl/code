package cn.itcast.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.itcast.mobilesafe.db.LockAppDBOpenHelper;

public class LockAppDao {
	LockAppDBOpenHelper lockAppDBOpenHelper ;

	public LockAppDao(Context context) {
		lockAppDBOpenHelper = new LockAppDBOpenHelper(context);
	}
	
	/*save
	 * ����һ�� ���������¼ 
	 */
	public void save(String packname){
		SQLiteDatabase db =	lockAppDBOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("insert into lockapplist (appname) values(?)", 
					new Object[]{packname}
					);
			db.close();
		}
	}
	
	
	
	/* find
	 * ��ѯ�Ƿ��Ǽ�������
	 */
	public boolean find(String packname){
		SQLiteDatabase db =	lockAppDBOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select count(*) from lockapplist where appname=?", 
					new String[]{packname}
					);
			if(cursor.moveToFirst()){
				long result = cursor.getLong(0);
				if (result >0)
					return true;
			}
			cursor.close();
			db.close();
		}
		return false;
	}
	
	/* ��ѯ���еļ�������
	 * findAll
	 */
	public List<String> findAll(){
		List<String> packnames = new ArrayList<String>();
		SQLiteDatabase db =	lockAppDBOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor cursor = db.rawQuery("select * from lockapplist", null); 
			while(cursor.moveToNext()){
			   String packname =	cursor.getString(cursor.getColumnIndex("appname"));
			   packnames.add(packname);
			}
			cursor.close();
			db.close();
		}
		return packnames;
	}
	
	
	
	/*delete 
	 * ɾ��ĳ���������� 
	 */
	public void delete(String packname){
		SQLiteDatabase db =	lockAppDBOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.execSQL("delete from lockapplist where appname=?", new String[]{packname});
			db.close();
		}
	}
}
