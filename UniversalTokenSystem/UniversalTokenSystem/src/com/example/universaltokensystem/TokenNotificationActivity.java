package com.example.universaltokensystem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.NotificationManager;
import android.content.Context;

public class TokenNotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_token_notification);
		
		NotificationManager nm= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(R.drawable.ic_launcher);
	}
}
