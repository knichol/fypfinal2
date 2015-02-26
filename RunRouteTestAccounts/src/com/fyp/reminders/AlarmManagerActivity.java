package com.fyp.reminders;

import com.example.androidgpsexample.R;
import com.fyp.diabetes.diadash;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class AlarmManagerActivity extends Activity {

	private AlarmManagerBroadcastReceiver alarm;

	Button btnRepeat, btnCancel, btnOne, btnRemind;
	Long timeMS, repeat;
	int remindID;
	boolean repchk = false;
	SQLiteDatabase db;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		alarm = new AlarmManagerBroadcastReceiver();

		// If coming from AddReminder
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			timeMS = extras.getLong("msUntil");
			remindID = extras.getInt("remindID");
			repeat = extras.getLong("repeat");
		}

		Log.e("Here", "getting here");


		// This code is causing big problems
		//		// Repeated reminders
		//		if (repeat != null){
		//			// do stuff
		//			Log.d("repeat", "not null");
		//			// Starting single reminders timer
		//			alarm.repeatTimer(getBaseContext(), timeMS, remindID, repeat);
		//			Log.d("Reminder", "sent");
		//
		//			// Sending back to diabetes dash
		//			Intent dia = new Intent(getApplicationContext(), diadash.class);
		//			dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		//			startActivity(dia);
		//			setContentView(R.layout.diahome);
		//		}

		// Starting single reminders timer
		alarm.remindTimer(getBaseContext(), timeMS, remindID);
		Log.d("Reminder", "sent");

		// Sending back to diabetes dash
		Intent dia = new Intent(getApplicationContext(), diadash.class);
		dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(dia);
		setContentView(R.layout.diahome);

		// Everything below this not being used
		//		btnRepeat= (Button) findViewById(R.id.btStart);
		//		btnCancel = (Button) findViewById(R.id.btCancel);
		//		btnOne = (Button) findViewById(R.id.btOneTime);
		//		
		//		btnRepeat.setOnClickListener(new View.OnClickListener() {
		//			@Override
		//			public void onClick(View arg0) {
		//				startRepeatingTimer(arg0);
		//			}
		//		});
		//		
		//		btnCancel.setOnClickListener(new View.OnClickListener() {
		//			@Override
		//			public void onClick(View arg0) {
		//				cancelRepeatingTimer(arg0);
		//			}
		//		});
		//		
		//		btnOne.setOnClickListener(new View.OnClickListener() {
		//			@Override
		//			public void onClick(View arg0) {
		//				onetimeTimer(arg0);
		//			}
		//		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	//	public void startRepeatingTimer(View view) {
	//		Context context = this.getApplicationContext();
	//		if(alarm != null) {
	//			alarm.SetAlarm(context);
	//		} else {
	//			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	//		}
	//	}
	//
	//	public void cancelRepeatingTimer(View view) {
	//		Context context = this.getApplicationContext();
	//		if(alarm != null) {
	//			alarm.CancelAlarm(context);
	//		} else {
	//			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	//		}
	//	}
	//
	//	public void onetimeTimer(View view) {
	//		Context context = this.getApplicationContext();
	//		if(alarm != null) {
	//			alarm.setOnetimeTimer(context);
	//		} else {
	//			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
	//		}
	//	}

	// Currently only using this, possibly not even being used
	public void remindTimer(View view) {
		Context context = this.getApplicationContext();
		if(alarm != null) {
			alarm.remindTimer(context, timeMS, remindID);
		} else {
			Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
		}
	}

}
