package com.fyp.diabetes;

import com.kninc.hlt.R;
import com.fyp.library.UserFunctions;

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
import android.widget.TextView;

public class prof extends Activity {

	TextView weight, height, glucose, A1c, BPsys, BPdia, updated, welcome;
	Button btnUpd, btnBack;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar and set layout
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.curr_metrics);

		// Gets users name from DB and trims to just show first name
		String s =  userFunction.getName(getApplicationContext());
		String ns = s.replaceAll(" .*", "");

		// Sets textview to show welcome message plus users first name
		welcome = (TextView)findViewById(R.id.WelcCurr);
		welcome.setText("Welcome " + ns);

		// Finding TextViews and Buttons from XML
		weight = (TextView)findViewById(R.id.updateWeight);
		height = (TextView)findViewById(R.id.editHeight);
		glucose = (TextView)findViewById(R.id.currGluc);
		A1c = (TextView)findViewById(R.id.currA1c);
		BPsys = (TextView)findViewById(R.id.currBPsys);
		BPdia = (TextView)findViewById(R.id.currBPdia);
		updated = (TextView)findViewById(R.id.currUpd);

		btnUpd = (Button)findViewById(R.id.btnCurrMetUpd);
		btnBack = (Button)findViewById(R.id.btnCurrMetBack);

		// Finding or creating the database is nonexistent 
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
				+ "created_on TEXT)");

		// Checking currently logged in users metrics
		Cursor c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);
		
		// If local db is null, force user to update metrics
		if(c.getCount()==0) {
			Intent dia = new Intent(getApplicationContext(), setmet.class);
			dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(dia);
			finish();
		}

		// Populating the textviews with the relevant db contents
		if(c.moveToLast()) {				
			height.setText(c.getString(3).toString());
			weight.setText(c.getString(2).toString());
			glucose.setText(c.getString(4).toString());
			A1c.setText(c.getString(5).toString());
			BPsys.setText(c.getString(6).toString());
			BPdia.setText(c.getString(7).toString());
			updated.setText(c.getString(10).toString());
		}

		// Update Metrics Button
		btnUpd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), setmet.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
				finish();
			}
		});

		// Back Button
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), diadash.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
				finish();
			}
		});

	}

	// Display message
	public void showMessage(String title,String message) {
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

}

