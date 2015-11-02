package com.example.universaltokensystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class CampusInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_info);  
        TextView sID = (TextView)findViewById(R.id.StudentID);
		Intent intent = getIntent();
		String studentList = getIntent().getExtras().getString("StudentInfo").toString();
		try {
			JSONArray stList = new JSONArray(studentList);
			for(int i = 0; i<stList.length();i++){
				JSONObject stObj = stList.getJSONObject(0);
				String st_FName = stObj.getString("Firstname");
				String st_LName = stObj.getString("Lastname");
				String st_FullName = "Welcome to CCToken System: " +st_FName+" "+st_LName;
				sID.setText(st_FullName);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new RestOperations().execute();
    }   
    public class RestOperations extends AsyncTask<Void, Void, String> {
    	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
 	       InputStream in = entity.getContent();
 	         StringBuffer out = new StringBuffer();
 	         int n = 1;
 	         while (n>0) {
 	             byte[] b = new byte[4096];
 	             n =  in.read(b);
 	             if (n>0) out.append(new String(b, 0, n));
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
			String restStudentURL = "http://cctoken.azurewebsites.net/api/campuses/";
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
    		final ListView listView = (ListView) findViewById(R.id.CampusList);
    		try {
				JSONArray stList = new JSONArray(result);
				for(int i=0; i < stList.length() ; i++) {
				    JSONObject json_data = stList.getJSONObject(i);
				    /*int id=json_data.getInt("CampusId");
				    String name=json_data.getString("CampusName");
				    String address = json_data.getString("Address");
				    String Campus = name +  address;
				    items.add(Campus);*/
				    items.add(json_data.getString("CampusName"));
				}
				
				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(CampusInfo.this,android.R.layout.simple_list_item_1, android.R.id.text1, items);
				listView.setAdapter(mArrayAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						//String name = listView.getItemAtPosition(arg2).toString();
						//Toast.makeText(CampusInfo.this, name, Toast.LENGTH_LONG).show();
						Intent intent = new Intent(CampusInfo.this,CampusInfo.class);
						intent.putExtra("CampusInfo", items);
						startActivity(intent);
						
					}
				});
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    }		
}

