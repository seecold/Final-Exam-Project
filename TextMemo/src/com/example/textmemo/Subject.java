package com.example.textmemo;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Subject extends Activity {
	// �����ͺ��̽� ����� ��ü
	DBHandler handler;

	// �����ͺ��̽��� ����� �޸� �����ϱ� ���� ArrayList ��ü�� ����
	ArrayList<MemoInfo> memoList;
	StatsAdapter listAdapter;
	// ��踮��Ʈ�� ǥ���� ListVew�� ����
	ListView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.stat);
		// �����ͺ��̽� ��鷯 ��ü ����
		handler = new DBHandler(this);
		memoList = new ArrayList<MemoInfo>();
		listAdapter = new StatsAdapter(this, memoList);
		view = (ListView) findViewById(R.id.list);
		view.setAdapter(listAdapter);
		// ����Ʈ �������� Ŭ������ �� ������ ��ü�� ���� ��ü�� �����
		displayMemoList();
		
	}
	protected void displayMemoList() {
		// handler ��ü�κ��� ��� �޸� �о� ��
		Cursor cursor = handler.selectAll2();
		// �о� ���� �����Ͱ� ������ �Լ� ����
		if (cursor == null)
			return;
		// memoList�� ����� ����� ��� ������
		removeAllList();
		// Ŀ���� ���� �о� �� �����͸� memoList�� �߰���
		do {
			MemoInfo memo = new MemoInfo();
			memo.memo = cursor.getString(0);
			memo.subject = cursor.getString(1);

			memoList.add(memo);
		} while (cursor.moveToNext());
		// Ŀ���� �ݰ� listAdapter ��ü���� �˷� ���ŵ� ������
		// ȭ�鿡 ǥ�õǵ��� ��
		cursor.close();
		listAdapter.notifyDataSetChanged();
	}
	
	protected void removeAllList() {
		memoList.removeAll(memoList);
	}
}
