package com.fyp.reminders;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.androidgpsexample.R;
import com.fyp.library.UserFunctions;

public class AddReminder extends Activity {

	EditText remindMsg;
	Button btnAdd, btnBack;
	SQLiteDatabase db;
	TimePicker timePicker;
	UserFunctions userFunction = new UserFunctions();
	AlertDialog levelDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.reminder_add);

		timePicker = (TimePicker) findViewById(R.id.remindSetTime);
		timePicker.setIs24HourView(true);

		// Enable permissions to post to db
		if (android.os.Build.VERSION.SDK_INT > 9)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		remindMsg = (EditText)findViewById(R.id.remindSetMsg);
		//		remindTime = (EditText)findViewById(R.id.remindSetTime);
		//		remindDate = (EditText)findViewById(R.id.remindSetDate);

		btnAdd = (Button)findViewById(R.id.btnRemindAdd);
		btnBack = (Button)findViewById(R.id.btnRemindAddCancel);

		db = openOrCreateDatabase("RemindersDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS user_reminder ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 			
				+ "user_id TEXT,"
				+ "message TEXT,"
				+ "time TEXT,"
				+ "date TEXT,"
				+ "ms_until INTEGER,"
				+ "remindID INTEGER,"
				+ "created_on TEXT)");

		// Update Metrics Button
		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Strings to Show In Dialog with Radio Buttons
				//AlertDialog levelDialog;
				CharSequence[] items = {" Remind Once "," Remind Daily "," Remind Weekly "};

				// Creating and Building the Dialog 
				AlertDialog.Builder builder = new AlertDialog.Builder(arg0.getContext());
				builder.setTitle("Repeat?");
				builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						switch(item) {
						case 0:
							// Never repeat
							onceReminder();
							break;
						case 1:
							// Repeat daily
							long repDaily = 1000 * 60 * 60 * 24;
							repReminder(repDaily);
							break;
						case 2:
							// Repeat weekly
							long repWeekly = 1000 * 60 * 60 * 24 * 7;
							repReminder(repWeekly);
							break;
						}   
						levelDialog.dismiss();
					}
				});
				levelDialog = builder.create();
				levelDialog.show();
			}
		});


		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), Reminder.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

	}	

	public void onceReminder() {
		int hourChk = timePicker.getCurrentHour();
		String minute = String.valueOf(timePicker.getCurrentMinute());
		String am_pm = (hourChk < 12) ? "AM" : "PM";
		if(hourChk > 12){
			hourChk -= 12;
		}
		String time = String.valueOf(hourChk)+":"+minute+" "+am_pm;

		DatePicker datePicker = (DatePicker) findViewById(R.id.remindSetDate);
		String day = String.valueOf(datePicker.getDayOfMonth());
		String month = String.valueOf(datePicker.getMonth() + 1);
		String year = String.valueOf(datePicker.getYear());
		String date = day+"/"+month+"/"+year;

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.YEAR, datePicker.getYear());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		if(am_pm == "PM"){
			cal.set(Calendar.HOUR, timePicker.getCurrentHour() - 12);
		}
		else
			cal.set(Calendar.HOUR, timePicker.getCurrentHour());

		Log.d("cal ms", String.valueOf(cal.getTimeInMillis()));

		Date now = new Date();
		long dateMS = cal.getTimeInMillis() - now.getTime();
		int remindID = (int) (Math.random() * 10000000);

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

		db.execSQL("INSERT INTO user_reminder (user_id, message, time, date, ms_until, remindID, created_on) " +
				"VALUES('"+userFunction.getUID(getApplicationContext())+"','"+remindMsg.getText()+
				"','"+time+"','"+date+"','"+dateMS+"','"+remindID+"','"+dateFormat.format(now).toString()+"');");

		Log.d("Mins", String.valueOf((dateMS/(1000*60))));
		Log.d("Hours", String.valueOf((dateMS/(1000*60*60))));
		Log.d("Days", String.valueOf((dateMS/(1000*60*60*24))));
		Log.d("Millisecs", String.valueOf(dateMS));

		// Need to bundle dateMS to AlarmManagerActivity
		Intent i = new Intent(getApplicationContext(), AlarmManagerActivity.class);
		i.putExtra("msUntil", dateMS);
		i.putExtra("remindID", remindID);		
		startActivity(i);
		finish();
	}

	public void repReminder(long rep) {
		int hourChk = timePicker.getCurrentHour();
		String minute = String.valueOf(timePicker.getCurrentMinute());
		String am_pm = (hourChk < 12) ? "AM" : "PM";
		if(hourChk > 12){
			hourChk -= 12;
		}
		String time = String.valueOf(hourChk)+":"+minute+" "+am_pm;

		DatePicker datePicker = (DatePicker) findViewById(R.id.remindSetDate);
		String day = String.valueOf(datePicker.getDayOfMonth());
		String month = String.valueOf(datePicker.getMonth() + 1);
		String year = String.valueOf(datePicker.getYear());
		String date = day+"/"+month+"/"+year;

		//Lets suppose you have a DatePicker instance called datePicker
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
		cal.set(Calendar.MONTH, datePicker.getMonth());
		cal.set(Calendar.YEAR, datePicker.getYear());
		cal.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		if(am_pm == "PM"){
			cal.set(Calendar.HOUR, timePicker.getCurrentHour() - 12);
		}
		else
			cal.set(Calendar.HOUR, timePicker.getCurrentHour());

		Log.d("cal ms", String.valueOf(cal.getTimeInMillis()));

		Date now = new Date();
		long dateMS = cal.getTimeInMillis() - now.getTime();
		int remindID = (int) (Math.random() * 10000000);

		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

		db.execSQL("INSERT INTO user_reminder (user_id, message, time, date, ms_until, remindID, created_on) " +
				"VALUES('"+userFunction.getUID(getApplicationContext())+"','"+remindMsg.getText()+
				"','"+time+"','"+date+"','"+dateMS+"','"+remindID+"','"+dateFormat.format(now).toString()+"');");

		Log.d("Mins", String.valueOf((dateMS/(1000*60))));
		Log.d("Hours", String.valueOf((dateMS/(1000*60*60))));
		Log.d("Days", String.valueOf((dateMS/(1000*60*60*24))));
		Log.d("Millisecs", String.valueOf(dateMS));
		Log.d("Rep", String.valueOf(rep));

		rep = 5000;
		// Need to bundle dateMS to AlarmManagerActivity
		Intent i = new Intent(getApplicationContext(), AlarmManagerActivity.class);
		i.putExtra("msUntil", dateMS);
		i.putExtra("remindID", remindID);
		i.putExtra("repeat", rep);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		finish();
	}
}
