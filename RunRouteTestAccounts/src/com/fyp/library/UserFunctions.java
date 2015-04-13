package com.fyp.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


import android.content.Context;
import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;

	private static String loginURL = "http://danu6.it.nuigalway.ie/dbknp/test2/public_html/android_login_api/";
	private static String registerURL = "http://danu6.it.nuigalway.ie/dbknp/test2/public_html/android_login_api/";
	private static String postURL = "http://danu6.it.nuigalway.ie/dbknp/test2/public_html/android_login_api/";

	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String postnew_tag = "postnew";
	private static String postgoal_tag = "postgoal";	
	private static String postremind_tag = "postremind";	
	private static String postrun_tag = "postnew";	
	
	private static String postmetrics_tag = "postmet";

	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		Log.e("JSON loginUser", email + password);
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		Log.e("JSON loginUser", json.toString());
		return json;
		
	}

	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	public JSONObject postNew(String id, String type, String distance, String time, String steps){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postnew_tag));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("distance", distance));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("steps", steps));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(postURL, params);
		// return json
		return json;
	}

	public JSONObject postMetrics(String id, String weight, String height, String glucose, String a1c,
			String BPsys, String BPdia, String sex, String year){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postmetrics_tag));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("weight", weight));
		params.add(new BasicNameValuePair("height", height));
		params.add(new BasicNameValuePair("glucose", glucose));
		params.add(new BasicNameValuePair("a1c", a1c));
		params.add(new BasicNameValuePair("BPsys", BPsys));
		params.add(new BasicNameValuePair("BPdia", BPdia));
		params.add(new BasicNameValuePair("sex", sex));	
		params.add(new BasicNameValuePair("birth_year", year));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(postURL, params);
		// return json
		return json;
	}
	
	public JSONObject postGoal(String id, String desc, String type, String value, String comp_by,
			String comp){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postgoal_tag));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("desc", desc));
		params.add(new BasicNameValuePair("type", type));
		params.add(new BasicNameValuePair("value", value));
		params.add(new BasicNameValuePair("comp_by", comp_by));
		params.add(new BasicNameValuePair("comp", comp));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(postURL, params);
		// return json
		return json;
	}

	public JSONObject postReminder(String id, String msg, String time, String date){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postremind_tag));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("message", msg));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("date", date));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(postURL, params);
		// return json
		return json;
	}
	
	/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}

	// get user id
	public String getUID(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		String id = null;
		if(count > 0){
			id = db.getID();
		}
		return id;
	}

	// get user name
	public String getName(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		String id = null;
		if(count > 0){
			id = db.getName();
		}
		return id;
	}

	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}

}
