package com.example.universaltokensystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import android.view.ViewGroup;

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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CampusInfo extends Activity {

	private String studentId;
	private String studentTableId;
	private String Stu_Id;

	public String getStudentTableId() {
		return studentTableId;
	}

	public void setStudentTableId(String studentTableId) {
		this.studentTableId = studentTableId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campus_info);
		TextView sID = (TextView) findViewById(R.id.StudentID);
		String studentList = getIntent().getExtras().getString("StudentInfo").toString();
		
		
		// The actual student id : 300777789 
		setStudentId(getIntent().getExtras().getString("StudentID").toString());
		try {
			JSONArray stList = new JSONArray(studentList);
			for (int i = 0; i < stList.length(); i++) {
				JSONObject stObj = stList.getJSONObject(0);
				String st_FName = stObj.getString("Firstname");
				String st_LName = stObj.getString("Lastname");
				String st_FullName = "Welcome to CCToken System: " + st_FName + " " + st_LName;
				String st_ID = stObj.getString("Id");
				Stu_Id = stObj.getString("Id");
				sID.setText(st_FullName);
				
				//The table student Id column value
				setStudentTableId(st_ID);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}

		Intent TokenNotificationintent = new Intent(CampusInfo.this, TokenNotificationService.class);
		TokenNotificationintent.putExtra("studentId", Stu_Id);
		startService(TokenNotificationintent);
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
			String restStudentURL = "http://tokensys.azurewebsites.net/api/campuses/";
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
			final ListView listView = (ListView) findViewById(R.id.CampusList);
			try {
				JSONArray stList = new JSONArray(result);
				for (int i = 0; i < stList.length(); i++) {
					JSONObject json_data = stList.getJSONObject(i);
					String id = String.valueOf(json_data.getInt("CampusId"));
					String name = json_data.getString("CampusName");
					String address = json_data.getString("CampusAddress");
					// String Campus = name;
					items.add(name);
					campusJSONData.put(name, Arrays.asList(id, name, address, getStudentId(),getStudentTableId()));
					
					System.out.println("Student List Data Campus Info::"+ "::Campus ID::" +id+"::Campus Name::"+ name
							+"::Campus Address::"+ address+ "::Student ID::"+getStudentId()+"::Student Table ID::"+getStudentTableId());
					

					// items.add(json_data.getString("CampusName"));
					// items.add(json_data.getString("CampusAddress"));
				}

				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(CampusInfo.this,
						R.layout.mylist, R.id.Itemname, items){
				            @Override
				            public View getView(int position, View convertView, ViewGroup parent){
				                // Get the current item from ListView
				                View view = super.getView(position,convertView,parent);
				                if(position %2 == 1)
				                {
				                    // Set a background color for ListView reguar row/item
				                    view.setBackgroundColor(Color.parseColor("#b2b300"));
				                }
				                else
				                {
				                    // Set the background color for alternate row/item
				                    view.setBackgroundColor(Color.parseColor("#808000"));
				                }
				                return view;
				            }
				        };
				listView.setAdapter(mArrayAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						String name = listView.getItemAtPosition(arg2).toString();
						Intent intentCampus = new Intent(CampusInfo.this, DepartmentInfo.class);
						intentCampus.putExtra("CampusInfo", name);
						intentCampus.putStringArrayListExtra("CampusData",new ArrayList<String>(campusJSONData.get(name)));
						startActivity(intentCampus);

					}
				});
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
