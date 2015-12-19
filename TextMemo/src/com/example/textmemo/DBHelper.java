package com.example.textmemo;

/*
 * DBHandler�� ���� �����ͺ��̽��� ������ �� �ֵ��� �����ִ� Ŭ����
 * �� Ŭ������ ���� ��� ���� �����ͺ��̽� ��ü�� ��� ������ �� ������
 * �����ͺ��̽��� ���ʸ� ���� �� ���̺��� �����ϰ� ���׷��̵带 �� �� ��
 * ���� �ϴ� �Լ�
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	// DBHelper �ļ��� �Լ�
	// ���� ���½� �����ͺ��̽��� ����
	public DBHelper( Context context) {
		super( context, "textmemo.db", null, 1);
	}
	
	// �����ͺ��̽��� ���� ������� �� ȣ��Ǵ� �Լ��� 
	// ���̺��� �����ϴ� ����� ������
	public void onCreate( SQLiteDatabase db ) {
		String table = "CREATE TABLE memo ( "
						+ "id INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ "writedate INTEGER, content TEXT,subject TEXT,wichx DOUBLE,wichy DOUBLE,subpos INTEGER)";
		db.execSQL(table);
	}
	
	// ���̺��� ������ ���׷��̵尡 �ʿ��� �� ȣ��Ǵ� �Լ���
	// ���� ���̺��� ������ �� ���� ����ų�, ���̺��� �����ϴ� ���
	// �� ��
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
		db.execSQL( "DROP TABLE IF EXISTS memo" );
		onCreate( db );
	}
}
