/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.fyp.library;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_LOGIN = "login";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";

	// Data table name
	private static final String TABLE_DATA = "data";

	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_TIME = "time";
	private static final String KEY_STEPS = "steps";



	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," 
				+ KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE,"
				+ KEY_UID + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
				+ "id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "user_id TEXT," 
				+ "distance TEXT,"
				+ "time TEXT,"
				+ "steps TEXT," 
				+ "type TEXT" 
				+ "created_at TEXT" + ")";
		db.execSQL(CREATE_DATA_TABLE);

	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}

	public void addData(String id, String distance, String time, String steps, String created_at) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_UID, id); // Name
		values.put(KEY_DISTANCE, distance); // Email
		values.put(KEY_TIME, time); // Email
		values.put(KEY_STEPS, steps); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At

		// Inserting Row
		db.insert(TABLE_DATA, null, values);
		db.close(); // Closing database connection
	}

	// Possible function to use in the future... not really sure what i started for...
	//	public void add(String id, String distance, String time, String steps, String created_at) {
	//		SQLiteDatabase db = this.getWritableDatabase();
	//		
	//		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//		Date date = new Date();
	//		//System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
	//		UserFunctions userFunction = new UserFunctions();
	//		db.execSQL("INSERT INTO data (user_id, weight, height, glucose, hba1c, BPsys, BPdia, created_on) " +
	//				"VALUES('"+userFunction.getUID(getApplicationContext())
	//				+"','"+editWeight.getText()
	//				+"','"+editHeigth.getText()
	//				+"','"+editGlucose.getText()
	//				+"','"+editA1c.getText()
	//				+"','"+editBPsys.getText()
	//				+"','"+editBPdia.getText()
	//				+"','"+dateFormat.format(date).toString()+"');");
	//
	//	
	//	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String,String> user = new HashMap<String,String>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
		}
		cursor.close();
		db.close();
		// return user
		return user;
	}

	public String getID(){
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		String userID = null; 
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			userID = cursor.getString(2);
		}
		cursor.close();
		db.close();
		return userID;
	}

	public String getName(){
		String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		String userID = null; 
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			userID = cursor.getString(1);
		}
		cursor.close();
		db.close();
		return userID;
	}

	/**
	 * Getting user login status
	 * return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}

	/**
	 * Re crate database
	 * Delete all tables and create them again
	 * */
	public void resetTables(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

}
