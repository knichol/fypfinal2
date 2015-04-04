package com.fyp.main;

import com.kninc.hlt.R;
import com.fyp.diabetes.diadash;
import com.fyp.fitness.fitdash;
import com.fyp.library.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends Activity {
	UserFunctions userFunctions;
	Button btnLogout, btnDia, btnFit;
	TextView welcome;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

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
			btnDia = (Button) findViewById(R.id.diabetesDash);
			btnFit = (Button) findViewById(R.id.fitnessDash);

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

			btnDia.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), diadash.class);
					startActivity(i);
					//finish();
				}
			});

			btnFit.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(), fitdash.class);
					startActivity(i);
					//finish();
				}
			});

		} 
		else{
			// user is not logged in show login screen
			Intent login = new Intent(getApplicationContext(), LoginActivity.class);
			login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(login);
			// Closing dashboard screen
			finish();
		}       
	}
}