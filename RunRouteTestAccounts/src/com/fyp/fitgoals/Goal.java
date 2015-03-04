package com.fyp.fitgoals;

import java.util.ArrayList;
import com.example.androidgpsexample.R;
import com.fyp.fitness.fitdash;
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

public class Goal extends Activity {

	Button btnAdd, btnBack;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();
	String comp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_view);

		ArrayList<UserRecord> users = new ArrayList<UserRecord>();
		UserRecord user;

		btnAdd = (Button)findViewById(R.id.btnLinkAddNewGoal);
		btnBack = (Button)findViewById(R.id.btnLinkToFitHome);

		db = openOrCreateDatabase("GoalsDB", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM user_goals WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		while(c.moveToNext()) {
			if(c.getInt(6)==1)
				comp = "Yes";
			else if(c.getInt(6)!=1)
				comp = "No";
			
			if(c.getString(3).contains("time")){
				user = new UserRecord(c.getString(2), "Exercise for "+c.getString(4)+" minutes." , c.getString(5), comp);
				users.add(user);
			}
			
			else if(c.getString(3).contains("dist")){
				user = new UserRecord(c.getString(2), "Exercise for "+c.getString(4)+" metres." , c.getString(5), comp);
				users.add(user);
			}
			
			else if(c.getString(3).contains("step")){
				user = new UserRecord(c.getString(2), "Travel "+c.getString(4)+" steps." , c.getString(5), comp);
				users.add(user);
			}
			
			else if(c.getString(3).contains("weight")){
				user = new UserRecord(c.getString(2), "Lose "+c.getString(4)+" kilograms." , c.getString(5), comp);
				users.add(user);
			}	
		}

		ListView listView = (ListView) findViewById(R.id.ListViewGoal);
		listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, users));

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), AddGoal.class);
				i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(i);
				finish();
			}
		});

		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), fitdash.class);
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
				v = vi.inflate(R.layout.goal_listitem, null);
			}

			UserRecord user = users.get(position);
			if (user != null) {
				TextView message = (TextView) v.findViewById(R.id.GoalMsg);
				TextView typevalue = (TextView) v.findViewById(R.id.GoalType);
				TextView comp_by = (TextView) v.findViewById(R.id.complete_by);
				TextView comp = (TextView) v.findViewById(R.id.completed);

				if (message != null) {
					message.setText("Goal Message: " + user.message);
				}

				if(typevalue != null) {
					typevalue.setText("Aim: " + user.typevalue);
				}
				
				if(comp_by != null) {
					comp_by.setText("Complete By: " + user.comp_by);
				}
				
				if(comp != null) {
					comp.setText("Completed? " + user.comp);
				}
			}
			return v;
		}
	}

	public class UserRecord {

		public String message, typevalue, comp_by, comp;

		public UserRecord(String message, String tv, String comp_by, String comp) {
			this.message = message;
			this.typevalue = tv;
			this.comp_by = comp_by;
			this.comp = comp;
		}
	}
}