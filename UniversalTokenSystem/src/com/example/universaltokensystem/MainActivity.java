package com.example.universaltokensystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	View v;
	EditText stuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
        Button submit = (Button)findViewById(R.id.btnLogin);
        submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText StudentID = (EditText)findViewById(R.id.txtStudentID);
				String SId = StudentID.getText().toString().trim();	
				if(TextUtils.isEmpty(SId)){
					Toast.makeText(MainActivity.this,"Please enter a valid StudentID", Toast.LENGTH_SHORT).show();
				}else{
					new RestOperations().execute();
				}
			}
		});
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
    	final HttpClient httpClient = new DefaultHttpClient();
    	/*String content;
    	String error;*/
    	ProgressDialog progressDailog = new ProgressDialog(MainActivity.this);
    	//String Data;
    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		
    		progressDailog.setTitle("Please wait...");
    		progressDailog.show();
    	}
    	
    	@Override
    	protected String doInBackground(Void... params) {
    		// TODO Auto-generated method stub
    		HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			EditText StudentID = (EditText)findViewById(R.id.txtStudentID);
			String SId = StudentID.getText().toString().trim();	
			String data = null;
			String restStudentURL = "http://cctoken.azurewebsites.net/api/students/?StudentID="+SId;
	        HttpGet httpGet = new HttpGet(restStudentURL);
	        try {
	               HttpResponse response = httpClient.execute(httpGet, localContext);
	               HttpEntity entity = response.getEntity();
	               data = getASCIIContentFromEntity(entity);
	        } catch (Exception e) {
	          	 return e.getLocalizedMessage();
	        }
	        return data;
    	}

    	@Override
    	protected void onPostExecute(String result) {
    		// TODO Auto-generated method stub
    		super.onPostExecute(result);
    		if (result.equals("[]")) {
    			Toast.makeText(MainActivity.this,"Invalid StudentID", Toast.LENGTH_SHORT).show();
			}else{
				Intent intent = new Intent(MainActivity.this,CampusInfo.class);
				intent.putExtra("StudentInfo", result);
				startActivity(intent);
			}
    		((EditText)findViewById(R.id.txtStudentID)).setText("");
    		progressDailog.dismiss();
    	}
    }		

}