package com.example.universaltokensystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

		// dept_name, Arrays.asList(id, dept_name, room_no, getStudentId())
		// campusJSONData.put(name, Arrays.asList(id, name, address,
		// getStudentId()));

		setDepartmentName(departmentName);
		setDepartmentData(departmentData);
		setCampusData(campusData);

		// campusInfo.setText("Campus ID:"+campusData.get(0)+" , Campus
		// Name:"+campusData.get(1)+" , Campus Address:"+campusData.get(2));

		List<String> displayCampusValues = getDepartmentData();
		// ArrayList<String> displayDepartmentValues =
		// getDepartmentData();*/

		TextView studentidtext = (TextView) findViewById(R.id.TextView02);
		studentidtext.setText(displayCampusValues.get(3));
		// studentidtext.setText("300777789");
		TextView departmenttext = (TextView) findViewById(R.id.TextView03);
		departmenttext.setText(displayCampusValues.get(1));
		TextView roomtext = (TextView) findViewById(R.id.TextView04);
		// departmenttext.setText("ICET");
		roomtext.setText(displayCampusValues.get(2));
		// roomtext.setText("Room 02");
		TextView campusName = (TextView) findViewById(R.id.textView05);
		campusName.setText(getCampusData().get(1));
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
			// Map<String, Object> params1;
			String text = null;

			ArrayList<String> Department = getDepartmentData();
			ArrayList<String> Campus = getCampusData();
			// TODO: The tokenid is auto generated and needs to be fetched by
			// the get call in
			// the screen 2
			// params1.put("tokenid", "ICET49");

			/*
			 * HttpClient httpClient = new DefaultHttpClient(); HttpContext
			 * localContext = new BasicHttpContext(); String restTokenUrl =
			 * "http://cctokens.azurewebsites.net/api/Tokens/createToken";
			 * HttpPost httpPost = new HttpPost(restTokenUrl); try { // Iterator
			 * iter = params1.entrySet().iterator(); JSONObject holder = new
			 * JSONObject(); holder.put("Id", 0); // holder.put("tokenid", "");
			 * holder.put("student_id", Campus.get(4)); holder.put("dept_Id",
			 * Department.get(0)); holder.put("createdTime", "");
			 * holder.put("closingTime", ""); holder.put("issue", getIssue());
			 * holder.put("status", ""); holder.put("Advisor_Id", "");
			 * holder.put("advisor_comments", "");
			 * 
			 * StringEntity se = new StringEntity(holder.toString());
			 * httpPost.setEntity(se); httpPost.setHeader("Accept",
			 * "application/json"); httpPost.setHeader("Content-type",
			 * "application/json");
			 * 
			 * HttpResponse response = httpClient.execute(httpPost,
			 * localContext); HttpEntity entity = response.getEntity(); text =
			 * getASCIIContentFromEntity(entity);
			 */

			Map<String, String> params1;
			// String text = null;

			params1 = new HashMap<String, String>();
			params1.put("Id", "3");
			params1.put("tokenid", "ICET-03");
			params1.put("student_id", Campus.get(4));
			params1.put("dept_Id", Department.get(0));
			params1.put("createdTime", "2015-11-12 12:30:00");
			params1.put("closingTime", "2015-11-12 01:30:00");
			params1.put("issue", getIssue());
			params1.put("status", "InActive");
			params1.put("Advisor_Id", "1");
			params1.put("advisor_comments", "fixed");

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String restTokenUrl = "http://cctokens.azurewebsites.net/api/Tokens/createToken";
			HttpPost httpPost = new HttpPost(restTokenUrl);
			try {
				Iterator iter = params1.entrySet().iterator();

				JSONObject holder = new JSONObject();

				while (iter.hasNext()) {
					Map.Entry pairs = (Map.Entry) iter.next();
					String key = (String) pairs.getKey();
					String value = params1.get(key);

					holder.put(key, value);
				}

				StringEntity se = new StringEntity(holder.toString());
				httpPost.setEntity(se);
				httpPost.setHeader("Accept", "application/json");
				httpPost.setHeader("Content-type", "application/json");

				HttpResponse response = httpClient.execute(httpPost, localContext);
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

			if (result.equals("[]")) {
				Toast.makeText(TokenInfo.this, "Not Inserted", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TokenInfo.this, "Inserted", Toast.LENGTH_SHORT).show();
				showCustomTokenDialog();

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

}
