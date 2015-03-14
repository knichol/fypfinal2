package com.fyp.diabetes;

import java.util.ArrayList;
import com.fyp.diabetes.diadash;
import com.fyp.library.UserFunctions;
import com.kninc.hlt.R;

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

public class History extends Activity {

	Button btnAdd, btnBack;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hist_view);

		ArrayList<UserRecord> users = new ArrayList<UserRecord>();
		UserRecord user;

		// prob wont need buttons update/cancel?
		btnBack = (Button)findViewById(R.id.btnHistBack);
		btnAdd = (Button)findViewById(R.id.btnHistUpd);

		// Creating db if nonexistent
		db = openOrCreateDatabase("MetricsDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS user_metrics ("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 			
				+ "user_id TEXT,"
				+ "weight TEXT,"
				+ "height TEXT,"
				+ "glucose TEXT,"
				+ "hba1c TEXT,"
				+ "BPsys TEXT,"
				+ "BPdia TEXT,"
				+ "sex TEXT,"
				+ "birth_year TEXT,"
				+ "created_on TEXT)");

		Cursor c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		while(c.moveToNext()) {
			user = new UserRecord(c.getString(10), c.getString(2), c.getString(4), 
					c.getString(5), c.getString(6)+"/"+c.getString(7));
			users.add(user);
		}

		ListView listView = (ListView) findViewById(R.id.ListViewHist);
		listView.setAdapter(new UserItemAdapter(this, android.R.layout.simple_list_item_1, users));

		btnAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), setmet.class);
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
				v = vi.inflate(R.layout.hist_listitem, null);
			}

			UserRecord user = users.get(position);
			if (user != null) {
				TextView upd = (TextView) v.findViewById(R.id.histUpd);
				TextView weight = (TextView) v.findViewById(R.id.histWeight);
				TextView gluc = (TextView) v.findViewById(R.id.histGluc);
				TextView a1c = (TextView) v.findViewById(R.id.histA1c);
				TextView bp = (TextView) v.findViewById(R.id.histBP);

				if (upd != null) {
					upd.setText("UPDATED: " + user.updated);
				}

				if(weight != null) {
					weight.setText("WEIGHT: " + user.weight + " kg");
				}

				if(gluc != null) {
					gluc.setText("GLUCOSE: " + user.glucose + " mg/dL");
				}

				if(a1c != null) {
					a1c.setText("HBA1C: " + user.a1c + "% mmol/mol");
				}

				if(bp != null) {
					bp.setText("Blood Pressure: " + user.bp + " (sys/dia)");
				}
			}
			return v;
		}
	}

	public class UserRecord {

		public String updated;
		public String weight;
		public String glucose;
		public String a1c;
		public String bp;

		public UserRecord(String u, String w, String g, String a, String b) {
			this.updated = u;
			this.weight = w;
			this.glucose = g;
			this.a1c = a;
			this.bp = b;
		}
	}
}