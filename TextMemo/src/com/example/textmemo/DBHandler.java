package com.example.textmemo;

/*
 * �����ͺ��̽� ����� Ŭ����
 * �� Ŭ������ DBHelper�� ���� ��Ƽ��Ƽ���� ��û�� ������ �˻�, ����,
 * ���� ���� �����ϴ� ����� ��
 */
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class DBHandler {
	DBHelper helper; // DBHelper ��ü�� ����
	SQLiteDatabase db; // ��� ������ SQLiteDatabase ��ü�� ����
	Context context; // ��Ƽ��Ƽ ��ü�� ���� ����

	// �����ͺ��̽��� �����ϴ� �Լ�
	public void openDB() {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	// �����ͺ��̽��� �ݴ� �Լ�
	public void closeDB() {
		if (helper != null && db != null) {
			helper.close();
			helper = null;
			db = null;
		}
	}

	// DBHandler ������ �Լ� ��Ƽ��Ƽ ��ü�� ������ �����ϰ�
	// �����ͺ��̽��� �����ϴ� �Լ�
	public DBHandler(Context context) {
		this.context = context;
		openDB();
	}

	// Ŭ���������� DBHandler ��ü�� �����ϴ� �Լ�
	public static DBHandler open(Context context) throws SQLException {
		DBHandler handler = new DBHandler(context);
		return handler;
	}

	// DBHandler ��ü�� �ݴ� �Լ�
	// �����ͺ��̽��� �ݴ� ����� ��
	public void close() {
		closeDB();
	}

	// ��Ƽ��Ƽ���� ���޵� �޸��� ������ �����ͺ��̽��� �����ϴ� �Լ�
	public long insert(String memo, String subject, double wichx, double wichy,
			int subpos) {
		ContentValues val = new ContentValues();
		// ���� ��¥�� õ���� 1�� ������ ��ȯ�Ͽ� writedate�� �����ϰ�
		// ���޵� �޸��� ���� content�� �����Ͽ� ������
		Calendar cal = Calendar.getInstance();
		val.put("writedate", cal.getTimeInMillis());
		val.put("content", memo);
		val.put("subject", subject);
		val.put("wichx", wichx);
		val.put("wichy", wichy);
		val.put("subpos", subpos);

		return db.insert("memo", null, val);
	}

	// ���޹��� id�� memo�� �̿��Ͽ� �����ͺ��̽��� �ش� �ڷḦ �����ϴ�
	// �Լ�
	public long update(int id, String memo, String subject, double wichx,
			double wichy, int subpos) {
		ContentValues val = new ContentValues();
		Calendar cal = Calendar.getInstance();
		val.put("writedate", cal.getTimeInMillis());
		val.put("content", memo);
		val.put("subject", subject);
		val.put("wichx", wichx);
		val.put("wichy", wichy);
		val.put("subpos", subpos);

		return db.update("memo", val, "id = ?",
				new String[] { String.valueOf(id) });
	}

	// �����ͺ��̽��� ����� ��� �޸� �о� �� Ŀ���� �ǵ��� ��
	public Cursor selectAll() throws SQLiteException {
		// �����ͺ��̽��κ��� ��� �����͸� �˻���
		Cursor cursor = db.query("memo", new String[] { "id", "writedate",
				"content", "subject", "wichx", "wichy", "subpos" }, null, null,
				null, null, null);
		// Ŀ���� null(����)�̸� null ���� �ǵ���
		if (cursor == null)
			return null;

		// Ŀ���� ���� �˻��� ���ڵ� ���� 0�̸�
		// Ŀ���� �ݰ� null���� �ǵ���
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		// �˻��� �����Ͱ� ������ ������ ���� ��ġ�� ó�� ���ڵ�� �ű�
		// �� Ŀ���� �ǵ���
		cursor.moveToFirst();
		return cursor;
	}
	
	// �����ͺ��̽��� ����� ��� �޸� �о� �� Ŀ���� �ǵ��� ��,  ���
	public Cursor selectAll2() throws SQLiteException {
		// �ߺ��Ȱ����� üũ��.
		Cursor cursor= db.rawQuery("select subject, count(*) from memo group by subject", null);

		// Ŀ���� null(����)�̸� null ���� �ǵ���
		if (cursor == null)
			return null;

		// Ŀ���� ���� �˻��� ���ڵ� ���� 0�̸�
		// Ŀ���� �ݰ� null���� �ǵ���
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		// �˻��� �����Ͱ� ������ ������ ���� ��ġ�� ó�� ���ڵ�� �ű�
		// �� Ŀ���� �ǵ���
		cursor.moveToFirst();
		return cursor;
	}
	


	
	

	// �ش� ID�� ���� �ڷḦ �˻��Ͽ� �ǵ��� �ִ� �Խ�
	public MemoInfo select(int id) {
		// �ش� ID�� �ڷ� �˻�
		Cursor cursor = db.query("memo", new String[] { "id", "writedate",
				"content", "subject", "wichx", "wichy", "subpos" }, "id = ?",
				new String[] { Integer.toString(id) }, null, null, null);
		if (cursor == null)
			return null;
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		// �˻��� �����Ͱ� ������ ���� ��ġ�� ó������ �ñ� ��
		// �����͸� �о� memoInfo ��ü�� ������ �� �ǵ�����
		cursor.moveToFirst();
		MemoInfo memo = new MemoInfo();
		memo.id = cursor.getInt(0);
		memo.writeDate = cursor.getLong(1);
		memo.memo = cursor.getString(2);
		memo.subject = cursor.getString(3);
		memo.wichx = cursor.getDouble(4);
		memo.wichy = cursor.getDouble(5);
		memo.subpos = cursor.getInt(6);
		return memo;
	}
	
	// �ش� ID�� ���� �ڷḦ �˻��Ͽ� �ǵ��� �ִ� �Խ�
	public MemoInfo select2(int id) {
		// �ش� ID�� �ڷ� �˻�
		Cursor cursor = db.query("memo", new String[] { "id","subject"}, "id = ?",
				new String[] { Integer.toString(id) }, null, null, null);
		if (cursor == null)
			return null;
		if (cursor.getCount() == 0) {
			cursor.close();
			return null;
		}

		// �˻��� �����Ͱ� ������ ���� ��ġ�� ó������ �ñ� ��
		// �����͸� �о� memoInfo ��ü�� ������ �� �ǵ�����
		cursor.moveToFirst();
		MemoInfo memo = new MemoInfo();
		memo.id = cursor.getInt(0);
		memo.writeDate = cursor.getLong(1);
		memo.memo = cursor.getString(2);
		memo.subject = cursor.getString(3);
		memo.wichx = cursor.getDouble(4);
		memo.wichy = cursor.getDouble(5);
		memo.subpos = cursor.getInt(6);
		return memo;
	}


	// �ش� ID�� �ڷḦ �����ϴ� �Լ�
	public long delete(int id) {
		return db.delete("memo", "id = ?", new String[] { String.valueOf(id) });
	}
}
