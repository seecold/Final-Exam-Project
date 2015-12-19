package com.example.textmemo;

/*
 * 앱 실행시 처음에 표시되는 액티비티입니다.



 * 이 액티비티는 EditText를 이용하여 사용자의 입력을 받아 들이고 
 * SQLite 데이터베이스를 이용하여  데이터를 삽입하거나 수정합니다.
 * 
 *  각 버튼의 기능
 *  목록보기 :	목록을 표시하는 액티비티에 의해  실행된 경우 
 *  			현재 액티비티를 종료합니다. 이 때 새로운 내용이 
 *  			추가된 경우 호출한 엑티비티에 이를 알려 줍니다.
 *  
 *  새로 입력 :	입력된 내용을 지우고 새로운 데이터를 입력할 수 있도록
 *  			합니다.
 *  
 *  메모 등록 :	입력된 메모를 수정하거나 삽입합니다.
 *  			목록을 표시하는 엑티비티에 의해 수정할 레코드가 전달
 *  			되었으면 입력한 데이터를 데이터베이스에 수정하고 
 *  			종료합니다.
 *  			새로운 레코드를 삽입하는 경우(edit_id가 0임) 입력된
 *  			내용을 데이터베이스에 삽입하고 입력란을 깨끗이 지웁니다.
 *  
 *  통계보기 : 입력된 메모의 종류를 토대로  통계별로 보여줍니다.
 *  
 *  새로쓰기 : 글을 다시쓸수 있게해줍니다.
 */
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	// 데이터베이스 제어용 객체
	DBHandler handler;

	// 수정을 위한 edit_id, edit_position(목록에서의 항목 위치)
	// 지정을 위한 변수
	int edit_id = 0, edit_position = 0;

	// 목록표시 엑티비티에서 호출되었는지(isCalled),
	// 새로운 데이터가 삽입되었는지(isadded) 판단하기 위한 변수
	boolean isCalled = false, isadded = false;

	// 뷰 컨트를을 위한 객체용 변수
	Button resetBtn, submitBtn, listBtn;
	EditText memo;
	// 메모 내용을 저장하기 위한 객체용 변수
	MemoInfo memoinfo;
	ArrayAdapter<CharSequence> adspin;
	// 호출한 엑티비티로 실행결과를 들려주기 위한 인텐트 객체의 변수
	Intent result;
	Spinner spinner;

	static final LatLng SEOUL = new LatLng(37.56, 126.97);
	private GoogleMap map;
	Geocoder coder;
	 double wichx;
	double wichy;
	int subpos;
	String sLocationInfo = "";
	  List <Address> addresses;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 데이터베이스 헨들러 객체 생성
		handler = new DBHandler(this);
		coder = new Geocoder(this, Locale.KOREA);
		// 뷰 컨트롤 객체를 가져옴
		resetBtn = (Button) findViewById(R.id.resetBtn);
		resetBtn.setOnClickListener(this);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		listBtn = (Button) findViewById(R.id.listBtn);
		listBtn.setOnClickListener(this);
		memo = (EditText) findViewById(R.id.memo);
		// 목록 표시용 엑티비티에서 전달된 값을 읽어 옴
		Intent intent = getIntent();
		edit_id = intent.getIntExtra("id", 0);
		edit_position = intent.getIntExtra("position", -1);
		isCalled = intent.getBooleanExtra("isCalled", false);

		// 지도 위치값 저장값
		
		// 종류 pos값 저장값

		spinner = (Spinner) findViewById(R.id.spinner);

		spinner.setPrompt("일의 종류");

		adspin = ArrayAdapter.createFromResource(this, R.array.selected,
				android.R.layout.simple_spinner_item);

		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adspin);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				// 선택값 받는곳.

			}

			public void onNothingSelected(AdapterView<?> parent) {

			}

		});

		// 전달된 값 중 edit_id와 edit_position 값이 유효한 값이면
		// edit_id에 해당하는 자료를 검색하여 가져옴
		if (edit_id > 0 && edit_position >= 0) {
			memoinfo = handler.select(edit_id);
			memo.setText(memoinfo.memo);
		}

		MapFragment mapFragment = (MapFragment) getFragmentManager()

		.findFragmentById(R.id.map);

		map = mapFragment.getMap();

		// 현재 위치로 가는 버튼 표시

		map.setMyLocationEnabled(true);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));// 초기
																		// 위치...수정필요

		// 글쓰기할떄 현재위치 지도값.
		if (isCalled == false) {
			MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

				@Override
				public void gotLocation(Location location) {

					String msg = "lon: " + location.getLongitude()
							+ " -- lat: " + location.getLatitude();

					Toast.makeText(getApplicationContext(), msg,
							Toast.LENGTH_SHORT).show();

					wichx = location.getLongitude();
					wichy = location.getLatitude();
					updateLocation(location);
					drawMarker(location);
				

				}

			};

			MyLocation myLocation = new MyLocation();

			myLocation.getLocation(getApplicationContext(), locationResult);
			Log.i("12-19", "isCalled=false");
		} else {
			
			wichx = memoinfo.wichx;
			wichy = memoinfo.wichy;
			updateLocation2();
			// 쓴글 보기위한 저장위치 지도
			drawMarker2();
		
			Log.i("12-19", "isCalled=true");
			spinner.setSelection(memoinfo.subpos);

			
		}
		Log.i("12-19", "선택 값 :" + spinner.getSelectedItem().toString());

		Log.i("12-19", "선택 position :" + spinner.getSelectedItemPosition());

		Log.i("12-19", "oncre wichx :" + wichx);

		Log.i("12-19", "oncre wichy :" + wichy);

	
		 


		
		// 실행 결과 정보를 되돌리기 위한 인텐트 객체 생성
		result = new Intent();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		// 클릭된 뷰 컨트롤이 "새로 입력" 이면
		if (v == resetBtn) {
			resetMemo(); // 입력 화면 초기화
		} else if (v == submitBtn) { // 등록 버턴이 눌려진 경우

			// 내용이 입력되었는지 검사하고 내용이 입력되지 않았으면
			// 입력 안내 메시지 출력 후 함수 실행 종료
			if (!inputCheck()) {
				Toast.makeText(this, "내용을 입력하세요.", Toast.LENGTH_LONG).show();
				return;
			}

			// edit_id 가 0보다 크고 입력란의 내용과 원본과 내용이 다르면
			// 입력 내용으로 수정함
			if (edit_id > 0 && memo.getText().toString() != memoinfo.memo) {
				if (handler.update(edit_id, memo.getText().toString(), spinner
						.getSelectedItem().toString(), wichx, wichy, spinner
						.getSelectedItemPosition()) == 0)
					Toast.makeText(this, "수정할 수 없습니다.", Toast.LENGTH_LONG)
							.show();
				else {
					// 수정 후 결과 정보를 result 인텐트에 등록하고
					// setResult 함수를 이용하여 내용을 되돌린 후 종료
					Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_LONG).show();
					result.putExtra("edit_id", edit_id);
					result.putExtra("edit_position", edit_position);
					result.putExtra("isadded", isadded);
					setResult(RESULT_OK, result);
					finish();
				}
			} else {
				// 그렇지 않으면 입력된 내용을 데이터베이스에 삽입�c
				if (handler.insert(memo.getText().toString(), spinner
						.getSelectedItem().toString(), wichx, wichy, spinner
						.getSelectedItemPosition()) == 0)
					Toast.makeText(this, "등록할 수 없습니다.", Toast.LENGTH_LONG)
							.show();
				else {
					Toast.makeText(this, "등록되었습니다.", Toast.LENGTH_LONG).show();
					resetMemo();
					isadded = true;
				}
			}
		} else if (v == listBtn) { // 목록보기 버튼인 경우
			if (!isCalled) {
				// 인텐트를 이용하여 목록 표시용 엑티비티를 실행한 후
				// 종료
				Intent intent = new Intent(this, ListActivity.class);
				startActivity(intent);
				finish();
			} else {
				// 호출 경우 결과 정보를 초기화한 후(추가된 데이터가 있으면
				// isadded를 true로 설정) 되돌리고 종료함
				result.putExtra("edit_id", 0);
				result.putExtra("edit_position", -1);
				result.putExtra("isadded", isadded);
				setResult(RESULT_OK, result);
				finish();
				
			}
		}
	}

	// 내용이 입력되었는지 검사하여 true(입력됨), false(입력되지 않음)
	// 을 되돌림
	protected boolean inputCheck() {
		if (memo.getText().toString().length() == 0)
			return false;
		else
			return true;
	}

	// 입력 내용 초기화
	private void resetMemo() {
		edit_id = 0;
		memo.setText("");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// 뒤로 버튼을 누른 경우
		// 목록 표시용 액티비티에서 호출된 경우 실행 결과 정보를 초기화한 후
		// isadded를 true로 설정) 되돌리고 종료함
		// 그렇지 않으면 액티비티 종료함
		if (isCalled) {
			result.putExtra("edit_id", 0);
			result.putExtra("edit_position", -1);
			result.putExtra("isadded", isadded);
			setResult(RESULT_OK, result);
		}
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 엑티비티가 종료되면 handler 객체의 close 함수를 이용하여
		// 데이터베이스를 닫아 줌
		super.onDestroy();

		handler.close();
	}

	// 글쓰기시 현재위치 지도
	private void drawMarker(Location location) {

		// 기존 마커 지우기

		wichx = location.getLatitude();
		wichy = location.getLongitude();

		Log.i("12-19", String.valueOf(wichx));
		map.clear();

		LatLng currentPosition = new LatLng(wichx, wichy);

		// currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));

		map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

		// 마커 추가

		map.addMarker(new MarkerOptions()

				.position(currentPosition)

				.snippet(
						sLocationInfo)

				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

				.title("현재위치"));

	}
	
	   private void updateLocation(Location location) {
	 
	            try {
	                List <Address> addresses = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
	                if (addresses != null) {
	                    Address addr = addresses.get(0);
	                    for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
	                        String addLine = addr.getAddressLine(i);
	                        sLocationInfo += String.format("%s", addLine);
	                    }
	                }
	            } catch (IOException e) {
	                Toast.makeText(getBaseContext(), "찾지못함", Toast.LENGTH_SHORT)
	                        .show();
	            }
	 
	    }
	 

	

	// 쓴글 보기 지도
	private void drawMarker2() {

		// 기존 마커 지우기
		wichx = memoinfo.wichx;
		wichy = memoinfo.wichy;

		map.clear();

		LatLng currentPosition = new LatLng(memoinfo.wichx, memoinfo.wichy);

		Log.i("12-19", "memoinfo.wichx : " + String.valueOf(memoinfo.wichx));

		// currentPosition 위치로 카메라 중심을 옮기고 화면 줌을 조정한다. 줌범위는 2~21, 숫자클수록 확대

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 17));

		map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

		// 마커 추가

		map.addMarker(new MarkerOptions()

				.position(currentPosition)

				.snippet(sLocationInfo)

				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

				.title("현재위치"));

	}
	
	   private void updateLocation2() {
			 
           try {
               List <Address> addresses = coder.getFromLocation(memoinfo.wichx, memoinfo.wichy, 1);
               if (addresses != null) {
                   Address addr = addresses.get(0);
                   for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++) {
                       String addLine = addr.getAddressLine(i);
                       sLocationInfo += String.format("%s", addLine);
                   }
               }
           } catch (IOException e) {
               Toast.makeText(getBaseContext(), "찾지못함", Toast.LENGTH_SHORT)
                       .show();
           }

   }


}