package com.fyp.reminders;

import com.kninc.hlt.R;
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
	long timeMS; 
	long repeat = 0;
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

		// Repeated reminders
		if (repeat != 0){
			// do stuff
			Log.d("Repeat Reminder", "Sent");
			// Starting single reminders timer
			alarm.repeatTimer(getBaseContext(), timeMS, remindID, repeat);
			Log.d("Repeat Reminder", String.valueOf(repeat));

			// Sending back to diabetes dash
			Intent dia = new Intent(getApplicationContext(), diadash.class);
			dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(dia);
			finish();
			//setContentView(R.layout.diahome);
		}

		else if (repeat == 0) {
			// Starting single reminders timer
			alarm.remindTimer(getBaseContext(), timeMS, remindID);
			Log.d("Single Reminder Timer", "sent");

			// Sending back to diabetes dash
			Intent dia = new Intent(getApplicationContext(), diadash.class);
			dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(dia);
			finish();
			//setContentView(R.layout.diahome);
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

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
