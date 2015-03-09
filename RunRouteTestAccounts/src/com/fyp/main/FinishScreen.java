package com.fyp.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.example.androidgpsexample.R;
import com.fyp.fitness.fitdash;
import com.fyp.library.UserFunctions;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class FinishScreen extends Activity {

	// Database push variables
	private static String distancePush = null;
	private static String timePush = null;
	private static String stepsPush = null;
	private static String type;

	TextView distView, timeView, stepsView, typeView;
	String fdist, fsteps, ftime;

	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_run);

		// Enable permissions to post to db
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		distView = (TextView) findViewById(R.id.finishDistance);
		timeView = (TextView) findViewById(R.id.finishTime);
		stepsView = (TextView) findViewById(R.id.finishSteps);
		typeView = (TextView) findViewById(R.id.finishType);

		float dist = 0;
		long runTime1 = System.currentTimeMillis();
		long runTime2 = 0;
		String dType = "m";

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			dist = extras.getFloat("distance");
			runTime2 = extras.getLong("time");
			type = extras.getString("type");
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

		fdist = String.format("%.2f", dist) + dType;
		ftime = time;
		fsteps = String.valueOf(steps);

		distView.setText(String.format("%.2f", dist) + dType);
		timeView.setText(time);
		stepsView.setText(String.valueOf(steps));
		typeView.setText(type);

		db = openOrCreateDatabase("android_api", Context.MODE_PRIVATE, null);

		// Return to run screen
		Button btn = (Button) findViewById(R.id.clickToRun);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

		// Push to databases
		Button btnPush = (Button) findViewById(R.id.clickToPush);
		btnPush.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View view) {
				String id = userFunction.getUID(getApplicationContext());

				// Update this here
				JSONObject json = userFunction.postNew(id, type.toString(), distancePush, timePush, stepsPush);

				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date = new Date();

				db.execSQL("INSERT INTO data (user_id, distance, time, steps, type, created_on) " +
						"VALUES('"+userFunction.getUID(getApplicationContext())+"','"
						+fdist+"','"+ftime+"','"+fsteps+"','"+type.toString()+"','"+dateFormat.format(date).toString()+"');");
				db.close();

				Intent i = new Intent(getApplicationContext(), fitdash.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});
	}
}
