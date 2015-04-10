package com.fyp.reminders;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.kninc.hlt.R;
import com.fyp.diabetes.diadash;
import com.fyp.library.UserFunctions;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class DeleteReminder extends Activity{

	int rID;
	long rep = 0;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	private NotificationManager mNotificationManager;
	private int notificationID = 100;
	private int numMessages = 0;
	String msg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		super.onCreate(savedInstanceState);

		// Enable permissions to interact with db
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Bundle extras = getIntent().getExtras();

		if(extras != null) {
			rID = extras.getInt("rID");
			rep = extras.getLong("rep");
		}

		if(rep != 0) {
			db = openOrCreateDatabase("RemindersDB", Context.MODE_PRIVATE, null);
			
			Cursor c = db.rawQuery("SELECT * FROM user_reminder WHERE remindID = '"+rID+"'", null);
			if(c.moveToFirst())	{
				msg = c.getString(2);
				displayNotification(msg);
				Log.d("Repeat Reminder", String.valueOf(rID));
			}
			else {
				Log.d("Error", "Invalid Rollno "+ String.valueOf(rID));
			}

			Intent i = new Intent(getApplicationContext(), diadash.class);
			i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			finish();
		}
		
		if(rep == 0) {
			Log.d("Single Reminder", String.valueOf(rID));
			db = openOrCreateDatabase("RemindersDB", Context.MODE_PRIVATE, null);
			Log.d("rID to delete", String.valueOf(rID));

			Cursor c = db.rawQuery("SELECT * FROM user_reminder WHERE remindID = '"+rID+"'", null);
			if(c.moveToFirst())	{
				msg = c.getString(2);
				displayNotification(msg);
				db.execSQL("DELETE FROM user_reminder WHERE remindID = '"+rID+"'");
				Log.d("Success", "Record Deleted");
			}
			else {
				Log.d("Error", "Invalid Rollno "+ String.valueOf(rID));
			}

			Intent i = new Intent(getApplicationContext(), diadash.class);
			i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
			finish();
		}
	}

	protected void displayNotification(String msg) {
		Log.d("Remind notif", "from DeleteReminder");

		// Invoking the default notification service
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);	
		NumberFormat formatter = new DecimalFormat("#0.00");   
		mBuilder.setContentTitle("HLT Reminder!");
		mBuilder.setContentText("Your Message: " + msg);
		mBuilder.setTicker("HLT: " + msg);

		// CHANGE THESE ICONS
		mBuilder.setSmallIcon(R.drawable.notif_ic);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notif_ic);
		mBuilder.setLargeIcon(bm);

		mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);
		mBuilder.setLights(Color.WHITE, 1500, 1000);
		mBuilder.setAutoCancel(true);
		
		// Increase notification number every time a new notification arrives
		mBuilder.setNumber(++numMessages);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, diadash.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(diadash.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// notificationID allows you to update the notification later on
		mNotificationManager.notify(notificationID, mBuilder.build());
	}

}
