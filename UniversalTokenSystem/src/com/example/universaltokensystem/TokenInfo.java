package com.example.universaltokensystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
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
	private String generatedToken;

	public String getGeneratedToken() {
		return generatedToken;
	}

	public void setGeneratedToken(String generatedToken) {
		this.generatedToken = generatedToken;
	}

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

		List<String> displayDeptVals = getDepartmentData();

		// studentidtext.setText("300777789");
		TextView studentidtext = (TextView) findViewById(R.id.TextView02);
		studentidtext.setText(displayDeptVals.get(3));

		// departmenttext.setText("ICET");
		TextView departmenttext = (TextView) findViewById(R.id.TextView03);
		departmenttext.setText(displayDeptVals.get(1));

		// roomtext.setText("Room 02");
		TextView roomtext = (TextView) findViewById(R.id.TextView04);
		roomtext.setText(displayDeptVals.get(2));

		// Campus Name
		TextView campusName = (TextView) findViewById(R.id.textView05);
		campusName.setText(getCampusData().get(1));

		// Listener to GET Call and POST Data
		Button generateToken = (Button) findViewById(R.id.btnLogin);
		generateToken.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText issueText = (EditText) findViewById(R.id.editText1);
				String issue = issueText.getText().toString().trim();
				if (TextUtils.isEmpty(issue)) {
					Toast.makeText(TokenInfo.this, "Please enter issue", Toast.LENGTH_SHORT).show();
				} else {

					// Setting the issue text
					setIssue(issue);
					new RestGetOperations().execute();
					new RestPostOperations().execute();
				}
			}
		});
	}

	// get the token id for generating the value.
	String generateTokenID(String tokenfromGET) {

		String departmentName = getDepartmentName();
		String tokenappend = departmentName;
		if ("".equals(tokenfromGET)) {

			tokenappend = tokenappend + "-" + 01;

		} else {
			String[] parts = tokenfromGET.split("-");
			int genLastTokenNumber = Integer.valueOf(parts[1]) + 1;
			tokenappend = tokenappend + "-" + String.valueOf(genLastTokenNumber);
		}

		return tokenappend;
	}

	/**
	 * 
	 * POST Operations for Token
	 * 
	 * @author Evlyn Maria
	 *
	 */
	public class RestPostOperations extends AsyncTask<Void, Void, String> {

		ArrayList<String> Department = getDepartmentData();
		ArrayList<String> Campus = getCampusData();

		String generatedTokenData = null;

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
			System.out.println("Department List Data Department Info::" + "::Department ID::" + Department.get(0)
					+ "::Department Name::" + Department.get(1) + "::Romm No::" + Department.get(2) + "::Student ID::"
					+ Department.get(3));
			System.out.println("Student List Data Campus Info::" + "::Campus ID::" + Campus.get(0) + "::Campus Name::"
					+ Campus.get(1) + "::Campus Address::" + Campus.get(2) + "::Student ID::" + Campus.get(3)
					+ "::Student Table ID::" + Campus.get(4));

			System.out.println("onPreExecute()" + Integer.valueOf(getCampusData().get(4)));
			System.out.println("onPreExecute()" + Integer.valueOf(getDepartmentData().get(0)));

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String GETCall = "http://cctokens.azurewebsites.net/api/Tokens/RetrieveTokensByDept?deptID="
					+ Integer.valueOf(Department.get(0));
			System.out.println("onPreExecute() GET Call: "
					+ "http://cctokens.azurewebsites.net/api/Tokens/RetrieveTokensByDept?deptID="
					+ Integer.valueOf(Department.get(0)));
			System.out.println("onPreExecute()" + Integer.valueOf(Department.get(0)));
			HttpGet httpGet = new HttpGet(GETCall);

			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				StatusLine statusLine = response.getStatusLine();

				// Check the Http Request for success
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					System.out.println("onPreExecute()" + out.toString());
					out.close();
				} else {
					// Closes the connection.
					System.out.println("HTTP1:" + statusLine.getReasonPhrase());
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {
				e.getLocalizedMessage();
			}

		}

		@Override
		protected String doInBackground(Void... params) {
			String text = null;
			Map<String, String> params1 = new HashMap<String, String>();
			params1.put("Id", "3");
			params1.put("tokenid", getGeneratedToken());
			params1.put("student_id", Campus.get(4));
			params1.put("dept_Id", Department.get(0));
			params1.put("createdTime", "2015-11-12 12:30:00");
			params1.put("closingTime", "2015-11-12 01:30:00");
			params1.put("issue", getIssue());
			System.out.println("doInBackground issue " + getIssue());
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

			TextView txtTokenName = (TextView) dialog.findViewById(R.id.txtTokenName);
			txtTokenName.setText(getGeneratedToken());

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

	/**
	 * 
	 * GET Operations for Token
	 * 
	 * @author Evlyn Maria
	 *
	 */
	public class RestGetOperations extends AsyncTask<Void, Void, String> {

		ArrayList<String> Department = getDepartmentData();
		ArrayList<String> Campus = getCampusData();

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

			String generatedTokenData = null;
			System.out.println("Department List Data Department Info::" + "::Department ID::" + Department.get(0)
					+ "::Department Name::" + Department.get(1) + "::Romm No::" + Department.get(2) + "::Student ID::"
					+ Department.get(3));
			System.out.println("Student List Data Campus Info::" + "::Campus ID::" + Campus.get(0) + "::Campus Name::"
					+ Campus.get(1) + "::Campus Address::" + Campus.get(2) + "::Student ID::" + Campus.get(3)
					+ "::Student Table ID::" + Campus.get(4));

			System.out.println("onPreExecute()" + Integer.valueOf(getCampusData().get(4)));
			System.out.println("onPreExecute()" + Integer.valueOf(getDepartmentData().get(0)));

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			String GETCall = "http://cctokens.azurewebsites.net/api/Tokens/RetrieveTokensByDept?deptID="
					+ Integer.valueOf(Department.get(0));
			System.out.println("onPreExecute() GET Call: "
					+ "http://cctokens.azurewebsites.net/api/Tokens/RetrieveTokensByDept?deptID="
					+ Integer.valueOf(Department.get(0)));
			System.out.println("onPreExecute()" + Integer.valueOf(Department.get(0)));
			HttpGet httpGet = new HttpGet(GETCall);

			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				generatedTokenData = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return generatedTokenData;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				JSONArray stList = new JSONArray(result);
				int lenOfJSONArray = stList.length();
				if (lenOfJSONArray == 0) {
					setGeneratedToken(generateTokenID(""));
				} else {
					JSONObject json_data = (lenOfJSONArray == 0 || lenOfJSONArray == 1)
							? stList.getJSONObject(lenOfJSONArray) : stList.getJSONObject(lenOfJSONArray - 1);
					String id = json_data.getString("tokenid");
					System.out.println("GET CALL" + id);
					setGeneratedToken(generateTokenID(id));
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
