package com.example.universaltokensystem;

import java.util.ArrayList;

<<<<<<< HEAD
=======
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
>>>>>>> 5fc3a79c57cc38a70dfd85fc8cc39e7bc0fd4d26
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
=======
import android.widget.EditText;
>>>>>>> 5fc3a79c57cc38a70dfd85fc8cc39e7bc0fd4d26
import android.widget.TextView;

public class CurrentToken extends Activity {

	String StudentId;
	String DeptName;
	String RoomNo;
<<<<<<< HEAD
=======
	String result;
	String Dept_id;
>>>>>>> 5fc3a79c57cc38a70dfd85fc8cc39e7bc0fd4d26
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_token);
		
		TextView txt_tokenId = (TextView) findViewById(R.id.txtTokenId);
		TextView txt_stId = (TextView) findViewById(R.id.txtStudentId);
		TextView txt_deptName = (TextView) findViewById(R.id.txtDeptName);
		TextView txt_roomno = (TextView) findViewById(R.id.txtroomno);
<<<<<<< HEAD
		String tokenInfo = getIntent().getExtras().getString("TokenInfo");
		txt_tokenId.setText(tokenInfo);
		Log.d("ToeknID", tokenInfo);
		ArrayList<String> deptInfo = getIntent().getExtras().getStringArrayList("DepartmentData");
		StudentId = deptInfo.get(3);
		Log.d("Student ID", StudentId);
		txt_stId.setText(StudentId);
		DeptName = deptInfo.get(1);
		txt_deptName.setText(DeptName);
		Log.d("DeptName", DeptName);
		RoomNo = deptInfo.get(2);
		txt_roomno.setText(RoomNo);
		Log.d("Roomno", RoomNo);
=======
		TextView txt_estimatetime = (TextView) findViewById(R.id.txtEstimateTime);
		String tokenInfo = getIntent().getExtras().getString("TokenInfo");
		txt_tokenId.setText(tokenInfo);
		ArrayList<String> deptInfo = getIntent().getExtras().getStringArrayList("DepartmentData");
		StudentId = deptInfo.get(3);
		txt_stId.setText(StudentId);
		DeptName = deptInfo.get(1);
		txt_deptName.setText(DeptName);
		RoomNo = deptInfo.get(2);
		txt_roomno.setText(RoomNo);
		Dept_id = deptInfo.get(0);
		HttpClient httpClient = new DefaultHttpClient();
		String restStudentURL = "http://tokensys.azurewebsites.net/api/Tokens/RetrieveTokensCountByDept?dept_id="+Dept_id;
		HttpGet httpGet = new HttpGet(restStudentURL);
		try {
			HttpResponse response = httpClient.execute(httpGet);				
			result = EntityUtils.toString(response.getEntity());
			String data = "Srinivas";
			Log.d("Result", data);
			Log.d("Result", result);
			txt_estimatetime.setText(result);
		} catch (Exception e) {
			return;
		}
		
>>>>>>> 5fc3a79c57cc38a70dfd85fc8cc39e7bc0fd4d26
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.current_token, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
