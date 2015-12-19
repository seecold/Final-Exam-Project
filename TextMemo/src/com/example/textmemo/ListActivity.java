package com.example.textmemo;

/**
 * �޸��� ����� ListView�� ǥ�����ִ� ��Ƽ��Ƽ��
 * �����ͺ��̽��� ����� �޸�  ListView�� ǥ���ϰ�
 * ���ο� �޸� ����� �� �ֵ��� �ϴ� ����� ��
 */

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListActivity extends Activity implements OnItemClickListener,
		OnClickListener {
	// �����ͺ��̽��� �����ϱ� ���� Handler ��ü�� ����
	DBHandler handler;

	// �����ͺ��̽��� ����� �޸� �����ϱ� ���� ArrayList ��ü�� ����
	ArrayList<MemoInfo> memoList;

	// �޸𸮽�Ʈ�� ǥ���� ListVew�� ����
	ListView view;

	// ����Ʈ �信 ����ڰ� ������ ���̾ƿ����� ǥ���ϱ� ���� ListAdapter�� ����
	// ����
	ListAdapter listAdapter;

	// ���� ��Ƽ��Ʈ�� �����ϱ� ���� requestCode�� ���
	private static final int EDIT_ACTIVITY = 1;

	// �� �޸� �ۼ��ϱ� ���� ��ư ��ü�� ���� ����
	Button newMemoBtn;
	Button newMemoBtn2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		// �� �޸� ��ư�� Ŭ������ �� �̺�Ʈ ������ ���
		newMemoBtn = (Button) findViewById(R.id.newMemoBtn);
		newMemoBtn2 = (Button) findViewById(R.id.newMemoBtn2);
		newMemoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), Subject.class);
				startActivity(intent);
			}
		});
		newMemoBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		// ���̹����� ��� ���� Handler ��ü ����
		handler = new DBHandler(this);
		// �޸� ����� �����ϴ� ArrayList ��ü ����
		memoList = new ArrayList<MemoInfo>();

		// ListView�� ����ڰ� ������ ���̾ƿ����� ǥ���� �� �ֵ���
		// ListAdapter ��ü�� �����Ͽ� ListView�� �����
		listAdapter = new ListAdapter(this, memoList);
		view = (ListView) findViewById(R.id.list);
		view.setAdapter(listAdapter);
		// ����Ʈ �������� Ŭ������ �� ������ ��ü�� ���� ��ü�� �����
		view.setOnItemClickListener(this);
		// ����Ʈ �並 2�� ���� ������ ���� �� ū�ٽ�Ʈ �޴��� ǥ�õǵ���
		// ����
		registerForContextMenu(view);

		Log.i("12-19", "���䰪�޳�?");

		// �����ͺ��̽��κ��� �޸� �о� ��Ͽ� ����ϰ� ǥ����
		displayMemoList();
	}

	protected void displayMemoList() {
		// handler ��ü�κ��� ��� �޸� �о� ��
		Cursor cursor = handler.selectAll();
		// �о� ���� �����Ͱ� ������ �Լ� ����
		if (cursor == null)
			return;
		// memoList�� ����� ����� ��� ������
		removeAllList();
		// Ŀ���� ���� �о� �� �����͸� memoList�� �߰���
		do {
			MemoInfo memo = new MemoInfo();
			memo.id = cursor.getInt(0);
			memo.writeDate = cursor.getLong(1);
			memo.memo = cursor.getString(2);
			memo.subject = cursor.getString(3);
			memo.wichx = cursor.getDouble(4);
			memo.wichy = cursor.getDouble(5);
			memo.subpos = cursor.getInt(6);

			memoList.add(memo);
		} while (cursor.moveToNext());
		// Ŀ���� �ݰ� listAdapter ��ü���� �˷� ���ŵ� ������
		// ȭ�鿡 ǥ�õǵ��� ��
		cursor.close();
		listAdapter.notifyDataSetChanged();
	}

	// memoList���� ��� ����� �����ϴ� �Լ�
	protected void removeAllList() {
		memoList.removeAll(memoList);
	}

	// �����Ͱ� �����Ǿ��� �� Ư�� ��ġ�� ����� �����ϰ�
	// ����� ������ �ϸ鿡 ǥ�õǵ��� �ϴ� �Լ�
	protected void removeList(int position) {
		MemoInfo memo = memoList.get(position);
		handler.delete(memo.id);
		memoList.remove(position);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);

		// ����Ʈ �������� �� 2�� ���� ������ ���� ��� ǥ�õǴ� ���ؽ�Ʈ
		// �޴��� �о�� ��ü�� ������
		getMenuInflater().inflate(R.menu.pop, menu);
		menu.setHeaderTitle(R.string.context_menu_title);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// ���ؽ�Ʈ �޴� �� Ư�� �޴��� ���õǾ��� ��
		// �޴� ������ �о� ��
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		if (info != null) {
			// �޴� ������ ������
			// ListView�� ��� ��ġ�� ����� ���õǾ����� ������
			int position = info.position;
			if (item.getItemId() == R.id.view_content) {
				// �޴��� ID�� view_content�̸�
				// ����Ʈ�� ����� �޸��� id, ����� position isCalled��
				// �����Ͽ� ����(������) ��Ƽ��Ƽ�� ������
				// �� �� ���� ����� �޾� ��Ͽ� �ݿ��ؾ� �ϹǷ�
				// startActivity�� �ƴ϶� startActivityForResult �Լ���
				// �̿���
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("id", memoList.get(position).id);
				intent.putExtra("position", position);
				intent.putExtra("isCalled", true);
				intent.putExtra("wichx", memoList.get(position).wichx);
				intent.putExtra("wichy", memoList.get(position).wichy);
				intent.putExtra("subject", memoList.get(position).subject);
				intent.putExtra("subpos", memoList.get(position).subpos);

				startActivityForResult(intent, EDIT_ACTIVITY);
			}
			// �޴��� ID�� remove_content(����)�̸� �ش� ����� ������
			else if (item.getItemId() == R.id.remove_content)
				removeList(position);
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		// ����Ʈ �������� Ŭ������ ��
		// ����Ʈ�� �����Ͽ� �޸� ID�� position, isCalled�� �����Ͽ�
		// ���� ��Ƽ��Ƽ�� ������
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", memoList.get(arg2).id);
		intent.putExtra("position", arg2);
		intent.putExtra("isCalled", true);
		startActivityForResult(intent, EDIT_ACTIVITY);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// ��Ƽ��Ƽ�� ����Ǹ� handler ��ü�� close �Լ��� �̿��Ͽ�
		// �����ͺ��̽��� ����
		handler.close();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int edit_id, edit_position;
		boolean added;

		super.onActivityResult(requestCode, resultCode, data);

		// ���� ��Ƽ��Ƽ�� ���� ���� ��� ������ ���� �� ���
		// requestCode�� EDIT_ACTIVITY�� ���� ���
		if (requestCode == EDIT_ACTIVITY) {
			// edit_id, edit_position isadded ���� ������ ���� �ݾ�
			edit_id = data.getIntExtra("edit_id", 0);
			edit_position = data.getIntExtra("edit_position", -1);
			added = data.getBooleanExtra("isadded", false);
			// ��ȿ�� edit_id�� edit_position�̸�
			// �޸� �о� memoList�� �ش� position�� ����� ����
			// �������� ������
			if (edit_id != 0 && edit_position >= 0) {
				MemoInfo edit_memo = handler.select(edit_id);
				memoList.set(edit_position, edit_memo);
				listAdapter.notifyDataSetChanged();
			} else if (added) {
				// �׷��� �ʰ� ����� �߰��Ǿ����� ��� ��ü�� ���� �о�
				// ȭ�鿡 ǥ����
				displayMemoList();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		// "�� ���" ��ư�� ���� ��� ����Ʈ ��ü�� �����Ͽ�
		// ���� ��Ƽ��Ƽ�� ������
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("id", 0);
		intent.putExtra("position", -1);
		intent.putExtra("isCalled", true);

		startActivityForResult(intent, EDIT_ACTIVITY);
	}

}
