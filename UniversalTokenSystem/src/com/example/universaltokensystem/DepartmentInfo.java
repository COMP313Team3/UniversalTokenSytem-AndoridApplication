package com.example.universaltokensystem;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DepartmentInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.department_info);
		TextView campusInfo = (TextView) findViewById(R.id.CampusInfo);
		Intent intent = getIntent();
		String campusList = "Welcome to " + intent.getExtras().getString("CampusInfo").toString() + " Campus";
		ArrayList<String> campusData = intent.getExtras().getStringArrayList("CampusData");
		campusInfo.setText("Campus ID:"+campusData.get(0)+" , Campus Name:"+campusData.get(1)+" , Campus Address:"+campusData.get(2));
	}
}
