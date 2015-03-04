package com.fyp.fitgoals;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_add);

		// Enable permissions to post to db
		if (android.os.Build.VERSION.SDK_INT > 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		goalDesc = (EditText)findViewById(R.id.addGoalDesc);		
		goalValue = (EditText)findViewById(R.id.addGoalValue);
		
		
		btnAdd = (Button)findViewById(R.id.btnAddNewGoal);
		btnBack = (Button)findViewById(R.id.btnAddNewGoalCancel);		
		btnType = (Button)findViewById(R.id.btnAddNewGoalType);

		db = openOrCreateDatabase("GoalsDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS user_goals ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 			
				+ "user_id TEXT,"
				+ "goal_desc TEXT,"
				+ "type TEXT,"
				+ "value INTEGER,"
				+ "complete_by TEXT,"
				+ "completed INTEGER,"
				+ "created_on TEXT)");

		// Goal Type Button
		btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Strings to Show In Dialog with Radio Buttons
				CharSequence[] items = {" Exercise Time "," Distance "," Steps Taken "," Weight Loss "};

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
						case 2:
							type = "step";
							btnType.setText("Steps Taken");
							goalDesc.requestFocus();
							break;			
						case 3:
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


		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DatePicker datePicker = (DatePicker) findViewById(R.id.addGoalDate);
				String day = String.valueOf(datePicker.getDayOfMonth());
				String month = String.valueOf(datePicker.getMonth() + 1);
				String year = String.valueOf(datePicker.getYear());
				String date = day+"/"+month+"/"+year;

				Date now = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				db.execSQL("INSERT INTO user_goals (user_id, goal_desc, type, value, complete_by, completed, created_on) " +
						"VALUES('"+userFunction.getUID(getApplicationContext())+"','"+goalDesc.getText()+
						"','"+type+"','"+goalValue.getText()+"','"+date+"','"+0+"','"+dateFormat.format(now).toString()+"');");
			
				Intent i = new Intent(getApplicationContext(), Goal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

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
}
