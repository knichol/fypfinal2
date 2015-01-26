package com.example.androidgpsexample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.androidgpsexample.DatabaseHandler;
import com.example.androidgpsexample.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinishScreen extends Activity {
	
	private static String KEY_SUCCESS = "success";
	
	// JSON Response node names
	private static String KEY_ID = "id";
	private static String KEY_DISTANCE = "distance";
	private static String KEY_TIME = "time";
	private static String KEY_STEPS = "steps";
	private static String KEY_CREATED_AT = "created_at";
	
	// Database push variables
	private static String distancePush = null;
	private static String timePush = null;
	private static String stepsPush = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_run);
        
        // Enable permissions to post to db
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    
        float dist = 0;
        long runTime1 = System.currentTimeMillis();
        long runTime2 = 0;
        String dType = "m";
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
        	dist = extras.getFloat("distance");
        	runTime2 = extras.getLong("time");
        }
        distancePush = String.format("%.2f", dist);
        
        int steps = (int) (dist/0.762);
        stepsPush = Integer.toString(steps);
        
        // Convert to km if over 1000 metres
        if (dist > 1000){
        	dist = (float) (dist * 0.001); 
        	dType = "km";
        }
        
        // Getting time difference and formatting
        runTime1 = runTime1 - runTime2;         
        long second = (runTime1 / 1000) % 60;
        long minute = (runTime1 / (1000 * 60)) % 60;
        long hour = (runTime1 / (1000 * 60 * 60)) % 24;
        String time = String.format("%02d:%02d:%02d", hour, minute, second);
        timePush = time;
        // Setting the TextView to show distance and time
		TextView myTextView = (TextView) findViewById(R.id.distTextView);
		myTextView.setText("Total distance: " + String.format("%.2f", dist) + dType
				+ "\nTime: "+time + "\nSteps: "+steps);
        
		// Return to run screen
    	Button btn = (Button) findViewById(R.id.clickToRun);
    	btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
				startActivity(i);
				finish();
			}
		});
    	
    	
    	// Push to database
    	Button btnPush = (Button) findViewById(R.id.clickToPush);
    	btnPush.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				UserFunctions userFunction = new UserFunctions();
				// Get the users email to user as a key - emails are unique
				String id = userFunction.getUID(getApplicationContext());
				//String type = "PLACEHOLDER";
				// 
				JSONObject json = userFunction.postNew(id, distancePush, timePush, stepsPush);
				DatabaseHandler db = new DatabaseHandler(getApplicationContext());
				
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();
				
				db.addData(id, distancePush, timePush, stepsPush, dateFormat.format(date).toString());
				// check for login response
				try {
					if (json.getString(KEY_SUCCESS) != null) {
						//loginErrorMsg.setText("");
						String res = json.getString(KEY_SUCCESS); 
						if(Integer.parseInt(res) == 1){

							// user successfully registred
							// Store user details in SQLite Database
							db = new DatabaseHandler(getApplicationContext());
							JSONObject json_user = json.getJSONObject("user");
							KEY_CREATED_AT = dateFormat.format(date).toString();
							db.addData(json_user.getString(KEY_ID), json_user.getString(KEY_DISTANCE), 
									json.getString(KEY_TIME), json.getString(KEY_STEPS), 
									json_user.getString(KEY_CREATED_AT));						
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.w("JSON Failure", e);
				}
			}
		});

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
