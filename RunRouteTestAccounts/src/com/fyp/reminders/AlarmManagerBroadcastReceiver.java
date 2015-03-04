package com.fyp.reminders;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	final public static String ONE_TIME = "onetime";
	static int remindID;

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");
		//Acquire the lock
		wl.acquire();

		//You can do the processing here update the widget/remote views.
		Bundle extras = intent.getExtras();
		StringBuilder msgStr = new StringBuilder();

		if(extras != null) {
			remindID = extras.getInt("rID");
			long l = extras.getLong("timed");
			l = l/1000/60;
			msgStr.append("Mins timed for: "+String.valueOf(l)+" ");
		}
		
		Format formatter = new SimpleDateFormat("hh:mm:ss a");
		msgStr.append(formatter.format(new Date()));

		Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
		Log.d("Reminder", "received");


		Intent i = new Intent(context, DeleteReminder.class);
		i.putExtra("rID", remindID);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);

		//Release the lock
		wl.release();

	}
	
	// New code for reminders
	public void remindTimer(Context context, long ms, int rID) {
		Log.d("MS Passed", String.valueOf(ms));
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra("timed", ms);
		intent.putExtra("rID", rID);

		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, pi);
		Toast.makeText(context, "Alarm set in " + String.valueOf(ms) + " ms", 
				Toast.LENGTH_LONG).show();
	}

	// New code for repeated reminders
	public void repeatTimer(Context context, long ms, int rID, long rep) {
		Log.d("MS Passed", String.valueOf(ms));
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		intent.putExtra("timed", ms);
		intent.putExtra("rID", rID);
		intent.putExtra("rep", rep);

		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, rep, pi);
		Toast.makeText(context, "Repeat alarm set to repeat " + String.valueOf(rep) + " ms", 
				Toast.LENGTH_LONG).show();
	}

//	public void SetAlarm(Context context) {
//		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//		intent.putExtra(ONE_TIME, Boolean.FALSE);
//		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
//		//After after 30 seconds
//		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi); 
//	}
//
//	public void CancelAlarm(Context context) {
//		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		alarmManager.cancel(sender);
//	}
//
//	public void setOnetimeTimer(Context context) {
//		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//		intent.putExtra(ONE_TIME, Boolean.TRUE);
//		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
//		am.set(AlarmManager.RTC_WAKEUP, 5000, pi);
//	}

}
