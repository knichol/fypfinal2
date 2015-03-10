package com.fyp.fitgoals;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.androidgpsexample.R;
import com.fyp.library.UserFunctions;

public class AddGoal extends Activity {

	EditText goalDesc, goalValue;
	Button btnAdd, btnBack, btnType;

	SQLiteDatabase db;
	TimePicker timePicker;
	UserFunctions userFunction = new UserFunctions();

	AlertDialog levelDialog;
	String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar and set layout
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_add);

		// Enable permissions to post to db
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// Assigning buttons etc.
		goalDesc = (EditText)findViewById(R.id.addGoalDesc);		
		goalValue = (EditText)findViewById(R.id.addGoalValue);

		btnAdd = (Button)findViewById(R.id.btnAddNewGoal);
		btnBack = (Button)findViewById(R.id.btnAddNewGoalCancel);		
		btnType = (Button)findViewById(R.id.btnAddNewGoalType);

		// Creating db if nonexistent
		db = openOrCreateDatabase("GoalsDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS user_goals ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 			
				+ "user_id TEXT,"
				+ "goal_desc TEXT,"
				+ "type TEXT,"
				+ "value INTEGER,"
				+ "complete_by TEXT,"
				+ "completed INTEGER,"
				+ "gID INTEGER,"
				+ "created_on TEXT)");

		// Goal Type Button - Uses a switch to determine which type of goal the user is creating
		btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Strings to Show In Dialog with Radio Buttons
				CharSequence[] items = {" Exercise Time "," Distance "," Weight Loss "};

				// Previously in use, may use in future
				// CharSequence[] items = {" Exercise Time "," Distance "," Steps Taken "," Weight Loss "};

				// Creating and Building the Dialog 
				AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
				builder.setTitle("Select Goal Type");
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch(item) {
						case 0:
							type = "time";
							btnType.setText("Exercise Time");
							goalDesc.requestFocus();
							break;
						case 1:
							type = "dist";
							btnType.setText("Distance");
							goalDesc.requestFocus();
							break;
							// Previously used function, may use in future
							//case 2:
							//	type = "step";
							//	btnType.setText("Steps Taken");
							//	goalDesc.requestFocus();
							//	break;			
						case 2:
							type = "weight";
							btnType.setText("Weight Loss");
							goalDesc.requestFocus();
							break;
						}   
						levelDialog.dismiss();
					}
				});
				levelDialog = builder.create();
				levelDialog.show();
			}
		});

		// Add Goal Button - 
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Pulling info from the datepicker and putting it in a string
				DatePicker datePicker = (DatePicker) findViewById(R.id.addGoalDate);
				String day = String.valueOf(datePicker.getDayOfMonth());
				String month = String.valueOf(datePicker.getMonth() + 1);
				String year = String.valueOf(datePicker.getYear());
				String date = day+"/"+month+"/"+year;

				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

				// Checking if there is a currently active internet connection
				if(checkInternet(getApplicationContext())) {
					// Posting goal to online db 
					userFunction.postGoal(userFunction.getUID(getApplicationContext()), 
							goalDesc.getText().toString(), type, goalValue.getText().toString(),
							date, "No");
				}

				// Posting goal to local db
				db.execSQL("INSERT INTO user_goals (user_id, goal_desc, type, value, complete_by, completed, gID, created_on) " +
						"VALUES('"+userFunction.getUID(getApplicationContext())+"','"+goalDesc.getText()+
						"','"+type+"','"+goalValue.getText()+"','"+date+"','"+0+"','"+(int)Math.random()*1000+"','"+dateFormat.format(now).toString()+"');");

				// Returning to Goal list
				Intent i = new Intent(getApplicationContext(), Goal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}

		});

		// Back Button - Returns to Goal list
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), Goal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});
	}	

	// This function checks if there is an active internet connection
	public boolean checkInternet(Context context) {
		ConnectivityManager conn = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifi = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		// Check if wifi or mobile network is available or not. If any of them is
		// available or connected then it will return true, otherwise false;
		return wifi.isConnected() || mobile.isConnected();
	}
}
