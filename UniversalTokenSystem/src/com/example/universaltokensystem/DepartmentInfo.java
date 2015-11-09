package com.example.universaltokensystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.universaltokensystem.CampusInfo.RestOperations;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DepartmentInfo extends Activity {

	String CampusId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_info);
		//TextView campusInfo = (TextView) findViewById(R.id.CampusInfo);
		Intent intent = getIntent();
		String campusList = "Welcome to " + intent.getExtras().getString("CampusInfo").toString() + " Campus";
		ArrayList<String> campusData = intent.getExtras().getStringArrayList("CampusData");
		CampusId = campusData.get(0);
		//campusInfo.setText("Campus ID:"+campusData.get(0)+" , Campus Name:"+campusData.get(1)+" , Campus Address:"+campusData.get(2));
		new RestOperations().execute();
	}
	

	public class RestOperations extends AsyncTask<Void, Void, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}
	
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
	
		}
	
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String restStudentURL = "http://cctoken.azurewebsites.net/api/department?CampusId="+CampusId;
			HttpGet httpGet = new HttpGet(restStudentURL);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}
	
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			final ArrayList<String> items = new ArrayList<String>();
			final Map<String, List<String>> campusJSONData = new MultivaluedHashMap<String, String>();
			final ListView listView = (ListView) findViewById(R.id.DepartmentList);
			try {
				JSONArray stList = new JSONArray(result);
				for (int i = 0; i < stList.length(); i++) {
					JSONObject json_data = stList.getJSONObject(i);
					String id = String.valueOf(json_data.getInt("dept_Id"));
					String dept_name = json_data.getString("dept_name");
					String room_no = json_data.getString("room_no");
					// String Campus = name;
					items.add(dept_name);
					campusJSONData.put(dept_name, Arrays.asList(id, dept_name, room_no));
	
					// items.add(json_data.getString("CampusName"));
					// items.add(json_data.getString("CampusAddress"));
				}
	
				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(DepartmentInfo.this,
						android.R.layout.simple_list_item_1, android.R.id.text1, items);
				listView.setAdapter(mArrayAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
	
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						String name = listView.getItemAtPosition(arg2).toString();
						/*Intent intentCampus = new Intent(DepartmentInfo.class);
						intentCampus.putExtra("CampusInfo", name);
						intentCampus.putStringArrayListExtra("CampusData",
								new ArrayList<String>(campusJSONData.get(name)));
						startActivity(intentCampus);*/
	
					}
				});
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
