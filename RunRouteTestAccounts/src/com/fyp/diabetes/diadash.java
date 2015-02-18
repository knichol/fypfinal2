package com.fyp.diabetes;

import com.example.androidgpsexample.R;
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

public class diadash extends Activity {

	Button btnProf, btnUpd, btnHist;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.diahome);

		btnProf = (Button) findViewById(R.id.btnMetrics);
		btnUpd = (Button) findViewById(R.id.btnSetMet);
		btnHist = (Button) findViewById(R.id.btnHist);

		db = openOrCreateDatabase("MetricsDB", Context.MODE_PRIVATE, null);
				
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
				Cursor c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = " +
						"'"+userFunction.getUID(getApplicationContext())+"'", null);
				if(c.getCount()==0) {
					showMessage("Error", "No records found");
					return;
				}
				StringBuffer buffer = new StringBuffer();
				while(c.moveToNext()) {
					buffer.append("Updated on: "+c.getString(8)+"\n");
					buffer.append("Weight: "+c.getString(2)+"\n");
					buffer.append("Height: "+c.getString(3)+"\n");
					buffer.append("Glucose: "+c.getString(4)+"\n");
					buffer.append("HbA1c: "+c.getString(5)+"\n");
					buffer.append("BP(sys/dia): "+c.getString(6)+"/"+c.getString(7)+"\n\n");				
				}
				showMessage("All Entries", buffer.toString());
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

