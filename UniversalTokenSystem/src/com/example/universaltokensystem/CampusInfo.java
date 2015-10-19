package com.example.universaltokensystem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
    }   
}

