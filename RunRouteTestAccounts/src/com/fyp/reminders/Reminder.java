package com.fyp.reminders;

import java.util.ArrayList;
import com.example.androidgpsexample.R;
import com.fyp.diabetes.diadash;
import com.fyp.library.UserFunctions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

		ArrayList<UserRecord> users = new ArrayList<UserRecord>();
		UserRecord user;

		btnAdd = (Button)findViewById(R.id.btnLinkNewReminder);
		btnBack = (Button)findViewById(R.id.btnCurrRemindBack);

		db = openOrCreateDatabase("RemindersDB", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM user_reminder WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		while(c.moveToNext()) {
			user = new UserRecord(c.getString(2), c.getString(3) +" on " + c.getString(4));
			users.add(user);
		}

		ListView listView = (ListView) findViewById(R.id.ListViewReminder);
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

		public UserRecord(String message, String datetime) {
			this.message = message;
			this.datetime = datetime;
		}
	}
}