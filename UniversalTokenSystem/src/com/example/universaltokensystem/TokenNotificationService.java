package com.example.universaltokensystem;


import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


public class TokenNotificationService extends IntentService{

	  public TokenNotificationService(String name) {
		super(name);
	}
	  
	  public TokenNotificationService() {
			super("TokenNotificationService");
		}

    	String tokenId;
	  Integer studentId; 
	
	  String tokenStatus="";
	  Integer tokenWaitTime;
	  boolean FirstNotification;
	  boolean FinalNotification;
	  
	  @Override
	  protected void onHandleIntent(Intent intent) {
	  
		  tokenId = intent.getExtras().getString("tokenId").toString();
		   studentId = Integer.parseInt(intent.getExtras().getString("studentId").toString());
		  
		  try{
			  GetTokenInfo();
			   while(tokenStatus.equals("Active"))
			   {
				   if(tokenWaitTime >10  && !FirstNotification)
				   {
					   NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					   PendingIntent p=PendingIntent.getActivity(this, 0,new Intent(this,TokenNotificationActivity.class), 0);
					   NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
					   builder.setContentTitle("Centennial Token Application");
					   builder.setContentText("Your token wait time " + ((tokenWaitTime > 60) ?  (tokenWaitTime/ 60) + " Hrs " + (tokenWaitTime % 60) + " Mins" : String.valueOf(tokenWaitTime)));
					   builder.setSmallIcon(R.drawable.ic_launcher);
					   
					   Notification n= builder.build();
					   //n.vibrate=new long[]{150,300,150,600};
					   n.flags=Notification.FLAG_AUTO_CANCEL;
					   
					   try
					   {
					     nm.notify(R.drawable.ic_launcher,n);
					   }
					   catch(Exception ex)
					   {
						   System.out.print(ex.toString());
					   }
					   
					   FirstNotification= true; 
				   }
				   if(tokenWaitTime <=10 && !FinalNotification)
				   {
					   NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					   PendingIntent p=PendingIntent.getActivity(this, 0,new Intent(this,TokenNotificationActivity.class), 0);
					   NotificationCompat.Builder builder= new NotificationCompat.Builder(this);
					   builder.setContentTitle("Universal Token System");
					   builder.setContentText("Your token wait time approx " + tokenWaitTime + " mins");
					   
					   Notification n= builder.build();
					   n.vibrate=new long[]{150,300,150,600};
					   n.flags=Notification.FLAG_AUTO_CANCEL;
					   nm.notify(R.drawable.collegelogo,n);
					   
					   FinalNotification= true; 
				   }
				   Thread.sleep(60000); //5 mins
				   GetTokenInfo();
			   }				   
			   if(tokenStatus.equals("InActive"))
			   {
				   stopSelf();
			   }
		   }
		   catch(Exception Ex)
		   {
			   String s= Ex.toString();
			   s += s;
		   }
	  }
	  
	

	private void GetTokenInfo() {
		
		try {
			String TokenInfo = new TokenInfoService().execute(tokenId).get();
			JSONObject object = new JSONObject(TokenInfo);
			tokenStatus = object.getString("status");
			tokenWaitTime = object.getInt("approximateWaitTimeinMins");
		} 
	    catch (Exception e) {
			e.printStackTrace();
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