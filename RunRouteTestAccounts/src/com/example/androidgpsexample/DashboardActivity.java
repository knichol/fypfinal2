package com.example.androidgpsexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnLogout, btnRun, btnUpdate;
	TextView welcome;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		/**
		 * Dashboard Screen for the application
		 * */        
		// Check login status in database
		userFunctions = new UserFunctions();
		if (userFunctions.isUserLoggedIn(getApplicationContext())){
			setContentView(R.layout.dashboard);
			
			// Gets users name from DB and trims to just show first name
			String s =  userFunctions.getName(getApplicationContext()).toUpperCase();
			String ns = s.replaceAll(" .*", "");
			
			// Sets textview to show welcome message plus users name
			welcome = (TextView)findViewById(R.id.welcomeMsg);
			welcome.setText("WELCOME " + ns);
			
			btnLogout = (Button) findViewById(R.id.btnLogout);
			btnRun = (Button) findViewById(R.id.clickToRunAgain);
			btnUpdate = (Button) findViewById(R.id.clickToUpdate);

			btnLogout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					userFunctions.logoutUser(getApplicationContext());
					Intent login = new Intent(getApplicationContext(), LoginActivity.class);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
			});


			btnRun.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
				}
			});

			btnUpdate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), UpdateMetrics.class);
					startActivity(i);
					finish();
				}
			});

		} else{
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(), LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}       
	}
}