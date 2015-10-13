package com.example.universaltokensystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        Button next = (Button) findViewById(R.id.button1);
        stuid = (EditText) findViewById(R.id.editText1);

        
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	v=view;
            	 new HttpAsyncTask().execute("http://cctoken.azurewebsites.net/api/students/");
                /*Intent myIntent = new Intent(view.getContext(), CampusInfo.class);
                startActivityForResult(myIntent, 0);*/
            }

        });
    }
    
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert input stream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
 
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;   
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
           // etResponse.setText(result);
           // JSONObject json;
            JSONArray array=null;
            int flag=0;
            String student_id=stuid.getText().toString();
            try {
				//json = new JSONObject(result);
			    array = new JSONArray(result);			           
                for (int i = 0; i < array.length(); i++) {
                JSONObject row = array.getJSONObject(i);
                String stu_id=row.getString("StudentID");
                if( student_id.equals(stu_id)){
                	
                	flag=1;
                	break;
                }               
            }
            
            if(flag==1)
            {
            	Intent myIntent = new Intent(v.getContext(), CampusInfo.class);
                startActivityForResult(myIntent, 0);
            }
            else{
            	 Toast.makeText(getBaseContext(), "Invalid Student ID", Toast.LENGTH_LONG).show();
            }
            
            } catch (JSONException e) {
				// TODO Auto-generated catch block
            	Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}           
       }
    }

}