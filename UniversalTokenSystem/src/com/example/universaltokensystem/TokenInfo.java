package com.example.universaltokensystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.core.MultivaluedHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.universaltokensystem.DepartmentInfo.RestOperations;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TokenInfo extends Activity {

	private String DepartmentName;
	private ArrayList<String> DepartmentData;
	private ArrayList<String> CampusData;
	private String Issue;

	public String getIssue() {
		return Issue;
	}

	public void setIssue(String issue) {
		Issue = issue;
	}

	public ArrayList<String> getCampusData() {
		return CampusData;
	}

	public void setCampusData(ArrayList<String> campusData) {
		CampusData = campusData;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

	public ArrayList<String> getDepartmentData() {
		return DepartmentData;
	}

	public void setDepartmentData(ArrayList<String> departmentData) {
		DepartmentData = departmentData;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.token);
		Intent intent = getIntent();
		String departmentName = intent.getExtras().getString("DepartmentName");
		ArrayList<String> departmentData = intent.getExtras().getStringArrayList("DepartmentData");
		ArrayList<String> campusData = intent.getExtras().getStringArrayList("CampusData");

		setDepartmentName(departmentName);
		setDepartmentData(departmentData);
		setCampusData(campusData);

		List<String> displayCampusValues = getDepartmentData();

		TextView studentidtext = (TextView) findViewById(R.id.txtStudentId);
		studentidtext.setText(displayCampusValues.get(3));
		TextView departmenttext = (TextView) findViewById(R.id.TextView03);
		departmenttext.setText(displayCampusValues.get(1));
		
		new getCount().execute();
		
		Button generateToken = (Button) findViewById(R.id.btnLogin);
		generateToken.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText issueText = (EditText) findViewById(R.id.editText1);
				String issue = issueText.getText().toString().trim();
				if (TextUtils.isEmpty(issue)) {
					Toast.makeText(TokenInfo.this, "Please enter issue", Toast.LENGTH_SHORT).show();
				} else {
					setIssue(issue);
					new RestPostOperations().execute();
				}
			}
		});
	}

	// get the token id for generating the value.
	String generateTokenID() {
		String departmentName = getDepartmentName();
		departmentName.substring(0, 3);
		return null;
	}
	
	public class getCount extends AsyncTask<Void, Void, String> {
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
			String restStudentURL = "http://tokensys.azurewebsites.net//api/tokens/RetrieveTokensForStudent?studentid="+2;
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
			
			TextView estimateTime = (TextView) findViewById(R.id.txtEstimateTime);
			int num = 5;
			int time = 0;
			time = Integer.parseInt(result);
			String totalTime = time*num + "Minutes";
			estimateTime.setText(totalTime);
		}
	}
	
	public class RestPostOperations extends AsyncTask<Void, Void, String> {
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
			String result;
			ArrayList<String> Department = getDepartmentData();
			ArrayList<String> Campus = getCampusData();
			HttpClient httpClient = new DefaultHttpClient();
			String restTokenUrl = "http://tokensys.azurewebsites.net/api/Tokens/createToken";
			HttpPost httpPost = new HttpPost(restTokenUrl);
			try {

				JSONObject json = new JSONObject();
				json.put("student_id", Campus.get(4));
				json.put("dept_Id", Department.get(0));
				json.put("issue",  getIssue());
				HttpEntity e = new StringEntity(json.toString());
				httpPost.setEntity(e);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");
				HttpResponse response = httpClient.execute(httpPost);				
				result = EntityUtils.toString(response.getEntity());
				
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result!=null) {
				JSONObject jObject;
				try {
					jObject = new JSONObject(result);
					String token_Id;
					String st_id;
					token_Id = jObject.getString("tokenid");
					st_id = jObject.getString("student_id");
					
					Intent TokenNotificationintent = new Intent(TokenInfo.this, TokenNotificationService.class);
					TokenNotificationintent.putExtra("tokenId", token_Id);
					TokenNotificationintent.putExtra("studentId", st_id);
					startService(TokenNotificationintent);
						
					Intent intent = new Intent(TokenInfo.this, CurrentToken.class);
					intent.putStringArrayListExtra("DepartmentData",
							new ArrayList<String>(getDepartmentData()));
					intent.putExtra("TokenInfo", token_Id);
					intent.putExtra("stInfo", st_id);
					startActivity(intent);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} else {
				Toast.makeText(TokenInfo.this, "Unable to create token", Toast.LENGTH_SHORT).show();
			}

		}

		protected void showCustomTokenDialog() {

			// TODO Auto-generated method stub
			final Dialog dialog = new Dialog(TokenInfo.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.token_gen);

			Button generateToken = (Button) dialog.findViewById(R.id.btnLogin);
			generateToken.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					dialog.dismiss();
				}
			});

			dialog.show();
		}
	}

	public class TokenNotificationService extends Service {
		
		  String tokenId;
		  Integer studentId; 
		
		  String tokenStatus="";
		  Integer tokenWaitTime;
		  boolean FirstNotification;
		  boolean FinalNotification;
		
		   @Override
		   public IBinder onBind(Intent arg0) {
		      return null;
		   }
		   
		   @Override
		   public void onCreate() {
			   super.onCreate();
			   
		   };
		   
		   @Override
		   public int onStartCommand(Intent intent, int flags, int startId) {
			  
			   tokenId = getIntent().getExtras().getString("tokenId").toString();
			   studentId = Integer.parseInt(getIntent().getExtras().getString("studentId").toString());
			  
			  try{
				   while(isTokenActive())
				   {
					   if((tokenWaitTime >10 && !FirstNotification))
					   {
						   NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						   PendingIntent p=PendingIntent.getActivity(this, 0,new Intent(this,TokenNotificationActivity.class), 0);
						   NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
						   builder.setContentTitle("Centennial Token Application");
						   builder.setContentText("Your token wait time");
						   
						   Notification n= builder.build();
						   n.vibrate=new long[]{150,300,150,600};
						   n.flags=Notification.FLAG_AUTO_CANCEL;
						   nm.notify(R.drawable.ic_launcher,n);
						   
						   FirstNotification= true; 
					   }
					   if(tokenWaitTime <=10 && !FinalNotification)
					   {
						   NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
						   PendingIntent p=PendingIntent.getActivity(this, 0,new Intent(this,TokenNotificationActivity.class), 0);
						   NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
						   builder.setContentTitle("Centennial Token Application");
						   builder.setContentText("Your token wait time");
						   
						   Notification n= builder.build();
						   n.vibrate=new long[]{150,300,150,600};
						   n.flags=Notification.FLAG_AUTO_CANCEL;
						   nm.notify(R.drawable.ic_launcher,n);
						   
						   FinalNotification= true; 
					   }
					   Thread.sleep(300000); //5 mins
				   }				   
				   if(tokenStatus == "InActive")
				   {
					   stopSelf();
				   }
			   }
			   catch(Exception Ex)
			   {
				   
			   }
		       return START_STICKY;
		   }
		   
		   private boolean isTokenActive() {			
				GetTokenInfo(); 		   
				return tokenStatus == "Active";
		}

		private void GetTokenInfo() {
			
			try {
				String TokenInfo = new TokenInfoService().execute(tokenId).get();
				JSONArray stList = new JSONArray(TokenInfo);
				if(stList.length() == 1)
				{
				  JSONObject json_data = stList.getJSONObject(0);
				  tokenWaitTime = json_data.getInt("approximateWaitTimeinMins");
				  tokenStatus= json_data.getString("status");
				}
			} 
		    catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		   public void onDestroy() {
		      super.onDestroy();
		   }
		}
	
	public class TokenInfoService extends AsyncTask<String, Void, String> {
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
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			String tokenID = params[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String restTokenURL = "http://tokensys.azurewebsites.net/api/Tokens/RetrieveTokenById?token_Id=" + tokenID;
			HttpGet httpGet = new HttpGet(restTokenURL);
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
			super.onPostExecute(result);
		}
	}

	
	
}
