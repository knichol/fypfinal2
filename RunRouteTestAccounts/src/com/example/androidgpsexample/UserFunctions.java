/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.androidgpsexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;

public class UserFunctions {
	
	private JSONParser jsonParser;
	
	private static String loginURL = "http://danu6.it.nuigalway.ie/dbknp/android_login_api/";
	private static String registerURL = "http://danu6.it.nuigalway.ie/dbknp/android_login_api/";
	private static String postURL = "http://danu6.it.nuigalway.ie/dbknp/test/public_html/android_login_api/";
	
	private static String login_tag = "login";
	private static String register_tag = "register";
	private static String postnew_tag = "postnew";
	
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
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		// Log.e("JSON", json.toString());
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
	
	public JSONObject postNew(String id, String distance, String time, String steps){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", postnew_tag));
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("distance", distance));
		params.add(new BasicNameValuePair("time", time));
		params.add(new BasicNameValuePair("steps", steps));
		
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(postURL, params);
		// return json
		return json;
	}
	
	public JSONObject postMetrics(String id, String weight, String height, String glucose, String a1c,
			String BPsys, String BPdia){
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
