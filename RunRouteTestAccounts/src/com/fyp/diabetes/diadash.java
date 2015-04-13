package com.fyp.diabetes;

import com.fyp.graphs.XYChartBuilder;
import com.fyp.library.UserFunctions;
import com.fyp.main.DashboardActivity;
import com.fyp.reminders.Reminder;
import com.kninc.hlt.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class diadash extends Activity {

	Button btnProf, btnUpd, btnHist, btnRemind, btnGraph;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar and set layout
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diahome);

		btnProf = (Button) findViewById(R.id.btnMetrics);
		btnUpd = (Button) findViewById(R.id.btnSetMet);
		btnHist = (Button) findViewById(R.id.btnHist);
		btnRemind = (Button) findViewById(R.id.btnRemind);
		btnGraph = (Button) findViewById(R.id.btnFit);

		// Finding or creating the database is nonexistent 
		db = openOrCreateDatabase("MetricsDB", Context.MODE_PRIVATE, null);

		// Checks if current metrics db is empty, forces user entry if it is
		metricsNull(db);

		// Profile Link Button
		btnProf.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), prof.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

		// Update Metrics Button
		btnUpd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), setmet.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

		// Metrics History Button
		btnHist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), History.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

		// Reminders Button
		btnRemind.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), Reminder.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

		// Graphs Button
		btnGraph.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), XYChartBuilder.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

	}

	// Forcing back to bring to main dash - fixes reminder bug
	@Override
	public void onBackPressed() {
		Intent dia = new Intent(getApplicationContext(), DashboardActivity.class);
		dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(dia);
		finish();
	}

	// Display message
	public void showMessage(String title,String message) {
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	// Function basically creates the metrics table if nonexistent and start the update metrics
	// Activity, also notifies that activity that the db is empty
	public void metricsNull(SQLiteDatabase db) {
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

		// Checking currently logged in users metrics
		Cursor c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		// If query returns no results
		if(c.getCount()==0) {
			Intent dia = new Intent(getApplicationContext(), setmet.class);
			dia.putExtra("metNull", 1);
			dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(dia);
			finish();
		}
	}
}

