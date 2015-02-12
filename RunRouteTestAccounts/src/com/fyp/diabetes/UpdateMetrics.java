package com.fyp.diabetes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.androidgpsexample.R;
import com.fyp.library.UserFunctions;
import com.fyp.main.DashboardActivity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UpdateMetrics extends Activity implements OnClickListener
{
	EditText editWeight, editHeigth, editGlucose, editA1c, editBPsys, editBPdia;
	Button btnAdd,btnDelete,btnModify,btnView,btnViewAll,btnShowInfo,btnDashboard;
	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.metrics);

		// Enable permissions to post to db
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		
		editWeight = (EditText)findViewById(R.id.editWeight);
		editHeigth = (EditText)findViewById(R.id.editHeigth);
		editGlucose = (EditText)findViewById(R.id.editGlucose);
		editA1c = (EditText)findViewById(R.id.editA1c);
		editBPsys = (EditText)findViewById(R.id.editBPsys);
		editBPdia = (EditText)findViewById(R.id.editBPdia);

		btnAdd = (Button)findViewById(R.id.btnAdd);
		btnView = (Button)findViewById(R.id.btnView);
		btnViewAll = (Button)findViewById(R.id.btnViewAll);
		btnDashboard = (Button)findViewById(R.id.returnToDashboard);
		btnAdd.setOnClickListener(this);
		btnView.setOnClickListener(this);
		btnViewAll.setOnClickListener(this);
		btnDashboard.setOnClickListener(this);

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
	}

	@Override
	public void onClick(View view) {
		if(view == btnAdd) {
			Cursor c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = '"+userFunction.getUID(getApplicationContext())+"'", null);
			ArrayList<String> list = new ArrayList<String>();
			boolean errorCall = false;

			if(c.moveToLast()){
				list.add(c.getString(2));
				list.add(c.getString(3));
				list.add(c.getString(4));
				list.add(c.getString(5));
				list.add(c.getString(6));
				list.add(c.getString(7));
			}

			if(editWeight.getText().toString().trim().length()==0){
				errorCall = true;
				editWeight.setText(list.get(0).toString());
			} 
			if(editHeigth.getText().toString().trim().length()==0){
				errorCall = true;
				editHeigth.setText(list.get(1));
			}
			if(editGlucose.getText().toString().trim().length()==0){
				errorCall = true;
				editGlucose.setText(list.get(2));
			}
			if(editA1c.getText().toString().trim().length()==0){
				errorCall = true;
				editA1c.setText(list.get(3));
			}
			if(editBPsys.getText().toString().trim().length()==0){
				errorCall = true;
				editBPsys.setText(list.get(4));
			}
			if(editBPdia.getText().toString().trim().length()==0){
				errorCall = true;
				editBPdia.setText(list.get(5));
			}

			if (errorCall == true)
				showMessage("Warning!", "Empty Fields, using old values");

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date date = new Date();
			//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
			UserFunctions userFunction = new UserFunctions();
			
			userFunction.postMetrics(userFunction.getUID(getApplicationContext()), 
					editWeight.getText().toString(), editHeigth.getText().toString(), 
					editGlucose.getText().toString(), editA1c.getText().toString(), 
					editBPsys.getText().toString(), editBPdia.getText().toString());
			
			
			db.execSQL("INSERT INTO user_metrics (user_id, weight, height, glucose, hba1c, BPsys, BPdia, created_on) " +
					"VALUES('"+userFunction.getUID(getApplicationContext())+"','"+editWeight.getText()+"','"+editHeigth.getText()+
					"','"+editGlucose.getText()+"','"+editA1c.getText()+"','"+editBPsys.getText()+
					"','"+editBPdia.getText()+"','"+dateFormat.format(date).toString()+"');");

			showMessage("Success", "Record added");
			clearText();
			
				
		}

		// Delete command
		if(view == btnDelete)	{
			if(editWeight.getText().toString().trim().length()==0) {
				showMessage("Error", "Please enter Rollno");
				return;
			}
			Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editWeight.getText()+"'", null);
			if(c.moveToFirst())	{
				db.execSQL("DELETE FROM student WHERE rollno='"+editWeight.getText()+"'");
				showMessage("Success", "Record Deleted");
			}
			else {
				showMessage("Error", "Invalid Rollno");
			}
			clearText();
		}

		// Modify command
		if(view == btnModify)	{
			if(editWeight.getText().toString().trim().length()==0) {
				showMessage("Error", "Please enter Rollno");
				return;
			}
			Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+editWeight.getText()+"'", null);
			if(c.moveToFirst()) {
				db.execSQL("UPDATE student SET name='"+editWeight.getText()+"',marks='"+editWeight.getText()+
						"' WHERE rollno='"+editWeight.getText()+"'");
				showMessage("Success", "Record Modified");
			}
			else {
				showMessage("Error", "Invalid Rollno");
			}
			clearText();
		}

		// View most recent entry
		if(view == btnView) {
			Cursor c=db.rawQuery("SELECT * FROM user_metrics WHERE user_id = '"+userFunction.getUID(getApplicationContext())+"'", null);
			if(c.getCount()==0) {
				showMessage("Error", "No records found");
				return;
			}
			StringBuffer buffer = new StringBuffer();
			if(c.moveToLast()) {				
				buffer.append("Updated on: "+c.getString(8)+"\n");
				buffer.append("Weight: "+c.getString(2)+"\n");
				buffer.append("Height: "+c.getString(3)+"\n");
				buffer.append("Glucose: "+c.getString(4)+"\n");
				buffer.append("HbA1c: "+c.getString(5)+"\n");
				buffer.append("BP(sys/dia): "+c.getString(6)+"/"+c.getString(7)+"\n\n");				
			}
			showMessage("Last Entry", buffer.toString());

		}

		// View all
		if(view == btnViewAll) {
			Cursor c=db.rawQuery("SELECT * FROM user_metrics WHERE user_id = '"+userFunction.getUID(getApplicationContext())+"'", null);
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

		// 
		if(view==btnShowInfo) {
			showMessage("DB App","");
		}

		if(view==btnDashboard){
			Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
			startActivity(i);
			finish();		
		}
	}

	// Display message
	public void showMessage(String title,String message) {
		Builder builder=new Builder(this);
		builder.setCancelable(true);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.show();
	}

	// Clears textboxes
	public void clearText()	{
		editWeight.setText("");
		editHeigth.setText("");
		editA1c.setText("");
		editBPsys.setText("");
		editBPdia.setText("");
		editGlucose.setText("");
		editWeight.requestFocus();
	}
}