package com.example.hzl15104305;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	
	//����
	private static String TableName = "cities";
	//���ݿ���
	private static String DBName = "test.db";
	//���ݿ�汾��
	private static int DBVersion = 1;
	private Context context;
	//���ݿ�ʵ��
	private SQLiteDatabase database;
	//�����Լ���ʵ��
	public static DBHelper dbHelper;
	//�������ݿ�����
	private String createDBSql =
	        "create table cities(id INTEGER PRIMARY KEY AUTOINCREMENT," +
	                "cityname TEXT NOT NULL);";
	
	public DBHelper(Context context){
	    super(context, DBName, null, DBVersion);
	    this.context = context;
	}

	//DBHepler����ģʽ����ʡ��Դ����ֹ���ʳ�ͻ
	public static synchronized DBHelper getInstance(Context context){
	    if(dbHelper == null){
	        dbHelper = new DBHelper(context);
	    }
	    return dbHelper;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(createDBSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	//�������ݣ�ʹ��ContentValues��ʽ����
	public void insertData(String keys[], String values[]){
	    ContentValues contentValues = new ContentValues();
	    for(int i = 0; i<keys.length; i++){
	        contentValues.put(keys[i], values[i]);
	    }
	    database = getWritableDatabase();
	    database.insert(TableName, null, contentValues);
	}
	
	//ͨ��idɾ������
	public void deleteDataById(int id) {
	    String[] args = {String.valueOf(id)};
	    //������Ҫ��д�����ݿ�
	    database = getWritableDatabase();
	    database.delete(TableName, "id=?", args);
	}
	
	//��ѯ��������
	public List<Map<String, Object>> queryAllCities(){
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    //������Ҫ�ɶ������ݿ�
	    database = getReadableDatabase();
	    Cursor cursor = database.query(TableName, null, null, null, null, null, null, null);
	    while(cursor.moveToNext()){
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("id", cursor.getInt(cursor.getColumnIndex("id")));
	        map.put("cityname", cursor.getString(cursor.getColumnIndex("cityname")));
	        list.add(map);
	    }
	    return list;
	}

}
