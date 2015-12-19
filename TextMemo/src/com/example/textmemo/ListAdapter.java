package com.example.textmemo;

/*
 * �� Ŭ������ ����Ʈ�信 ������ ǥ���� �� ����ڰ� ������
 * ������� ǥ�õǵ��� �ϱ� ���� ����ڰ� ������ �����
 * Ŭ�����Դϴ�.
 * �� Ŭ������ ��Ƽ��Ʈ Ŭ�������� ��ü�� �����Ͽ� ����մϴ�.
 */

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// ����Ʈ�信 ǥ�õ� ������ �����ϱ� ���� Ŭ����
class ListInfo {
	TextView writeDate;
	TextView memo;
}

public class ListAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	ArrayList<MemoInfo> listData;
	ListInfo info;
	int month;
	// ����� Ŭ������ ������
	// ���޹��� context�κ��� Inflate ��ü�� ��� ����
	// ����Ʈ�� ǥ���� �����͸� ���޹޾� �����Ѵ�.
	public ListAdapter( Context context, ArrayList<MemoInfo> data ) {
		inflater = LayoutInflater.from( context );
		this.context = context;
		listData = data;
	}
	
	// ����� ������ �ǵ��� �ִ� �Լ�
	public int getCount() {
		return listData.size();
	}
	
	// �ش� ��ġ�� �����͸� �ǵ��� �ִ� �Լ�
	public MemoInfo getItem(int position) {
		return listData.get( position );
	}
	
	// �ش� ��ġ�� ������ ID�� �ǵ��� �ִ� �Լ�
	// ���⼭�� position ���� �״�� �ǵ�����
	public long getItemId(int position) {
		return position;
	}
	
	// �ش� ��ġ�� �� ��Ʈ���� �ǵ��� �ִ� �Լ�
	public View getView(int position, View convertview, ViewGroup parent) {
		// ���� View ��ü�� ��� ��
		View v = convertview;
		
		// ��¥�� ǥ���ϱ� ���� Calendar ��ü ����
		Calendar cal = Calendar.getInstance();
		month=cal.get(Calendar.MONTH)+1;
		if( v == null ) {
			// ���� ���� �䰡 �������� �ʾҴٸ�
			// ���� ����� ��ü�� �����ϰ�
			info = new ListInfo();
			
			// XML�κ��� �񏋿� ǥ���� ���̾ƿ� ��ü�� ������ ��
			v = inflater.inflate( R.layout.memo_list, null );

			// ������ ���̾ƿ� ��ü�κ��� ��¥ ����� �ؽ�Ʈ �� ��ü ������ ������
			info.writeDate = (TextView)v.findViewById( R.id.writedate );

			// ��¥��ü�� �̿��Ͽ� ������� �����Ͽ� ȭ�鿡 ǥ���� �� �ֵ���
			// �����Ͽ� �� �ؽ�Ʈ�� �信 �����Ѵ�.
			cal.setTimeInMillis( listData.get( position ).writeDate );
			info.writeDate.setText( cal.get( Calendar.YEAR ) + "�� " + month   + "�� " + cal.get( Calendar.DAY_OF_MONTH ) + "��" );

			// ���̾ƿ� ��ü�κ��� �޸� ������ �����ϱ� ���� �ؽ�Ʈ �� ��ü ������ ������  
			// �޸� ������ �����ϰ�
			info.memo = (TextView)v.findViewById( R.id.memostring );
			info.memo.setText( listData.get( position ).memo );
			// ���� �±׷� ������
			v.setTag( info );
		}
		else if( ((ListInfo)v.getTag()).memo.getText().toString() != listData.get( position ).memo ) {
			// ���� ��� ������ ���� �ױ׷� ������ ������ ��� ������ �޸� ������ �ٸ� ���
			// ���� ������ ������� �並 ���� ����� ������
			info = new ListInfo();
			v = inflater.inflate( R.layout.memo_list, null );
			info.writeDate = (TextView)v.findViewById( R.id.writedate );
			cal.setTimeInMillis( listData.get( position ).writeDate );
			info.writeDate.setText( cal.get( Calendar.YEAR ) + "�� " + month   + "�� " + cal.get( Calendar.DAY_OF_MONTH ) + "��" );
			info.memo = (TextView)v.findViewById( R.id.memostring );
			info.memo.setText( listData.get( position ).memo );
			v.setTag( info );
		}
		return v;
	}

	// ��� ������ �����ϴ� �Լ�
	public void setArrayList(ArrayList<MemoInfo> arrays) {
		listData = arrays;
	}
	
	// ��� ������ �ǵ��� �ִ� �Լ�
	public ArrayList<MemoInfo> getArrayList() {
		return listData;
	}
}
