package com.example.textmemo;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Subject extends Activity {
	// 데이터베이스 제어용 객체
	DBHandler handler;

	// 데이터베이스에 저장된 메모를 관리하기 위한 ArrayList 객체용 변수
	ArrayList<MemoInfo> memoList;
	StatsAdapter listAdapter;
	// 통계리스트를 표시할 ListVew용 변수
	ListView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.stat);
		// 데이터베이스 헨들러 객체 생성
		handler = new DBHandler(this);
		memoList = new ArrayList<MemoInfo>();
		listAdapter = new StatsAdapter(this, memoList);
		view = (ListView) findViewById(R.id.list);
		view.setAdapter(listAdapter);
		// 리스트 아이템을 클릭했을 때 리스너 객체를 현재 객체로 등록함
		displayMemoList();
		
	}
	protected void displayMemoList() {
		// handler 객체로부터 모든 메모를 읽어 옴
		Cursor cursor = handler.selectAll2();
		// 읽어 들인 데이터가 없으면 함수 종료
		if (cursor == null)
			return;
		// memoList에 저장된 목록을 모두 삭제함
		removeAllList();
		// 커서를 통해 읽어 온 데이터를 memoList에 추가함
		do {
			MemoInfo memo = new MemoInfo();
			memo.memo = cursor.getString(0);
			memo.subject = cursor.getString(1);

			memoList.add(memo);
		} while (cursor.moveToNext());
		// 커서를 닫고 listAdapter 객체에게 알려 갱신된 정보가
		// 화면에 표시되도록 함
		cursor.close();
		listAdapter.notifyDataSetChanged();
	}
	
	protected void removeAllList() {
		memoList.removeAll(memoList);
	}
}
