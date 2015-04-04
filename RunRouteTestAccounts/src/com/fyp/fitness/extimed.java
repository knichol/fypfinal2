package com.fyp.fitness;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.kninc.hlt.R;
import com.fyp.main.DashboardActivity;
import com.fyp.main.FinishScreen;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class extimed extends FragmentActivity implements android.location.LocationListener,
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {

	private LocationManager lm;
	
	// Notification variables
	private NotificationManager mNotificationManager;
	private int notificationID = 100;
	private int numMessages = 0;

	// Timer variables
	Timer timer;
	TimerTask timerTask;

	private Location locationA = new Location("point A");
	private Location locationB = new Location("point B");
	private static float distance;
	private static float timerDist;

	// Users input distance
	private static int usertime;

	private boolean run = true;

	private int ctr;
	private double oldLat, oldLong, currLat, currLong;

	final long runTime = System.currentTimeMillis();

	// Map fragment
	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			usertime = extras.getInt("time");
		}

		Button btnLinkToHome = (Button) findViewById(R.id.click);
		Button btnFinish = (Button) findViewById(R.id.dist);

		// For Pausing/Resuming current run
		final Button pause = (Button) findViewById(R.id.pause);
		pause.setText("Pause");
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				if(run == false) {
					run = true;
					pause.setText("Pause");
				} else {
					run = false;
					pause.setText("Resume");
				} 
			}
		});

		// Checking if GPS enabled
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		}

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Request updates here
		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
				5000,		// 5 sec
				1, this); 	// 1 meter
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000,   	// 5 sec
				1, this);	// 1 meter

		// Set up the map fragment
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
		// Enable the current location "blue dot"
		mapFragment.getMap().setMyLocationEnabled(true);


		// Center map here
		CameraUpdate center = CameraUpdateFactory.newLatLng(
				new LatLng(53.2657006237, -7.5076599419));
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);

		mapFragment.getMap().moveCamera(center);
		mapFragment.getMap().animateCamera(zoom);

		startTimer();
		Log.i("Timer Started", "from launch");

		// Link to "Home" Screen
		btnLinkToHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				stoptimertask();
				numMessages = 0;
				run = false;
				Intent i = new Intent(getApplicationContext(),
						DashboardActivity.class);
				startActivity(i);
				finish();
			}
		});

		// Link to "Finish" Screen
		btnFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				stoptimertask();
				numMessages = 0;
				run = false;
				Intent i = new Intent(getApplicationContext(), FinishScreen.class);
				i.putExtra("distance", distance);
				i.putExtra("time", runTime);
				i.putExtra("type", "Timed");
				startActivity(i);
				finish();
			}
		});
	}

	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
				dialog.cancel();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public void timeReached() {
		stoptimertask();
		numMessages = 0;
		run = false;
		Intent i = new Intent(getApplicationContext(), FinishScreen.class);
		i.putExtra("distance", distance);
		i.putExtra("time", runTime);
		startActivity(i);
		finish();
	}

	@Override
	public void onLocationChanged(Location arg0) {

		final long runTime2 = System.currentTimeMillis();

		if (run == true && arg0 != null){	
			if (ctr < 1){
				mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(
						new LatLng(arg0.getLatitude(), arg0.getLongitude())));
				
				currLat = arg0.getLatitude();
				currLong = arg0.getLongitude();

				oldLat = currLat;
				oldLong = currLong;

				ctr += 1;

				long diff = runTime2 - runTime;

				diff = diff/(1000*60) % 60;

				// change this and below instances
				if(diff >= usertime){
					timeReached();
				}
			}

			else if (ctr == 1 && mapFragment.getMap().getMyLocation() != null){
				mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(
						new LatLng(arg0.getLatitude(), arg0.getLongitude())));
				
				currLat = mapFragment.getMap().getMyLocation().getLatitude();
				currLong = mapFragment.getMap().getMyLocation().getLongitude();

				oldLat = currLat;
				oldLong = currLong;

//				// Add marker to map here
//				mapFragment.getMap().addMarker(new MarkerOptions()
//				.position(new LatLng(currLat, currLong))
//				.title("Counter: "+(ctr)));;

				ctr += 1;

				long diff = runTime2 - runTime;

				diff = diff/(1000*60) % 60;

				// change this and below instances
				if(diff >= usertime){
					timeReached();
				}
			}

			else if (ctr >= 2 && mapFragment.getMap().getMyLocation() != null){
				mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLng(
						new LatLng(arg0.getLatitude(), arg0.getLongitude())));
				
				oldLat = currLat;
				oldLong = currLong;

				currLat = mapFragment.getMap().getMyLocation().getLatitude();
				currLong = mapFragment.getMap().getMyLocation().getLongitude();

//				// Add marker to map here
//				mapFragment.getMap().addMarker(new MarkerOptions()
//				.position(new LatLng(currLat, currLong))
//				.title("Counter: "+(ctr)));;

				ctr += 1;

				addLines();

				// Getting distance between points
				locationA.setLatitude(oldLat);
				locationA.setLongitude(oldLong);
				locationB.setLatitude(currLat);
				locationB.setLongitude(currLong);
				distance += locationA.distanceTo(locationB);
				timerDist = distance - timerDist;

				Toast toast = Toast.makeText(getApplicationContext(), "Curr dist: "+distance, 2);
				toast.show();

				long diff = runTime2 - runTime;

				diff = diff/(1000*60) % 60;

				// change this and below instances
				if(diff >= usertime){
					timeReached();
				}
			}
		}
	}

	public void startTimer() {
		// set a new Timer
		Log.i("New timer", "from startTimer");

		// stop and initialize the TimerTask's job
		stoptimertask();
		timer = new Timer();
		initializeTimerTask();

		// schedule the timer, after the first 30mins the TimerTask will run every 5mins
		timer.schedule(timerTask, 1800000, 1800000);
	}

	public void stoptimertask() {
		Log.i("Kill timer", "from stoptimertask");

		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;
		}
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			@Override
			public void run() {
				// If the distance b/w pts grtr than 50m, restart timer
				if (timerDist > 50) {
					stoptimertask();
					startTimer();
				}
				// Else, send notification
				else {
					Log.i("Sending notif", "from initializeTimerTask");
					stoptimertask();
					displayNotification();
					startTimer();
				}
			}
		};
	}

	protected void displayNotification() {
		Log.i("Display notif", "from displayNotification");

		// Invoking the default notification service
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(this);	
		NumberFormat formatter = new DecimalFormat("#0.00");   
		mBuilder.setContentTitle("HLT Movement Update");
		mBuilder.setContentText("You've moved: "+formatter.format(timerDist)+"m in the last while...");
		mBuilder.setTicker("You haven't moved much lately!");
		mBuilder.setSmallIcon(R.drawable.notif_ic);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.notif_ic);
		mBuilder.setLargeIcon(bm);
		mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
		mBuilder.setDefaults(Notification.DEFAULT_SOUND);

		// Increase notification number every time a new notification arrives
		mBuilder.setNumber(++numMessages);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, exdist.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(exdist.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);

		mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// notificationID allows you to update the notification later on
		mNotificationManager.notify(notificationID, mBuilder.build());
	}

	private void addLines() {
		googleMap = mapFragment.getMap();
		googleMap.addPolyline((new PolylineOptions())
				.add(new LatLng(oldLat, oldLong), new LatLng(currLat, currLong))
				.width(7)
				.color(Color.RED)
				.geodesic(true));
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Log.e("GPS", "provider disabled " + arg0);
	}

	@Override
	public void onProviderEnabled(String arg0) {
		Log.e("GPS", "provider enabled " + arg0);
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.e("GPS", "status changed to " + arg0 + " [" + arg1 + "]");
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {	
	}

	@Override
	public void onConnected(Bundle arg0) {
	}

	@Override
	public void onDisconnected() {
	}
}