package com.fyp.reminders;

import java.util.ArrayList;
import com.example.androidgpsexample.R;
import com.fyp.diabetes.diadash;
import com.fyp.library.UserFunctions;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Reminder extends Activity {

	Button btnAdd, btnBack;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.remind_view);

		final ArrayList<UserRecord> users = new ArrayList<UserRecord>();
		UserRecord user;

		btnAdd = (Button)findViewById(R.id.btnLinkNewReminder);
		btnBack = (Button)findViewById(R.id.btnCurrRemindBack);

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

		Cursor c = db.rawQuery("SELECT * FROM user_reminder WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		while(c.moveToNext()) {
			user = new UserRecord(c.getString(2), c.getString(3) +" on " + c.getString(4), c.getString(6));
			users.add(user);
		}

		final ListView listView = (ListView) findViewById(R.id.ListViewReminder);
		listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, users));

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AddReminder.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), diadash.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

				AlertDialog.Builder alert = new AlertDialog.Builder(Reminder.this);

				alert.setMessage("Do you want delete this Reminder?");
				alert.setPositiveButton("Yes please!", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Code to remove from list
						String uid = users.get(position).id;
						ListAdapter la = listView.getAdapter();
						((ArrayAdapter<UserRecord>)la).notifyDataSetChanged(); 
						Cursor c = db.rawQuery("SELECT * FROM user_reminder", null);
						while(c.moveToNext())	{
							String id = c.getString(6);
							int rID = Integer.parseInt(c.getString(6));
							if(uid.contentEquals(id)){
								db.execSQL("DELETE FROM user_reminder WHERE remindID = '"+uid+"'");
								users.remove(position);

								Intent intent = new Intent(getApplicationContext(), 
										AlarmManagerBroadcastReceiver.class);
								intent.setData(Uri.parse("custom://" + rID));
								intent.setAction(String.valueOf(rID));
								
								PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 
										rID, intent, 0);
								AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
								alarmManager.cancel(pendingIntent);

								Log.d("Success", "Record Deleted "+String.valueOf(uid));
							}
						}
					}
				});

				alert.setNegativeButton("No thanks!", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				alert.show();
				return true;
			}
		});
	}

	public class UserItemAdapter extends ArrayAdapter<UserRecord> {
		private ArrayList<UserRecord> users;

		public UserItemAdapter(Context context, int textViewResourceId, ArrayList<UserRecord> users) {
			super(context, textViewResourceId, users);
			this.users = users;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.remind_listitem, null);
			}

			UserRecord user = users.get(position);
			if (user != null) {
				TextView message = (TextView) v.findViewById(R.id.remindMsg);
				TextView datetime = (TextView) v.findViewById(R.id.remindDT);

				if (message != null) {
					message.setText("MESSAGE: " + user.message);
				}

				if(datetime != null) {
					datetime.setText("TIME/DATE: " + user.datetime );
				}
			}
			return v;
		}

	}

	public class UserRecord {

		public String message;
		public String datetime;
		public String id;

		public UserRecord(String message, String datetime, String id) {
			this.message = message;
			this.datetime = datetime;
			this.id = id;
		}
	}
}