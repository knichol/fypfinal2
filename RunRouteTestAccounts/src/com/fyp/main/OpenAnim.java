package com.fyp.main;

import com.example.androidgpsexample.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OpenAnim extends Activity {

	Button btnReg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.openpage);

		// Creating 2 AnimationSets
		AnimationSet s = new AnimationSet(false);
		AnimationSet s2 = new AnimationSet(false);

		ImageView image1 = (ImageView)findViewById(R.id.imageAnim);
		TextView text1 = (TextView)findViewById(R.id.WelcCurr);

		// Animations
		Animation anim1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_zoom_out);
		Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_fade_out);
		Animation animText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation_fade_in);

		// Adding Animations to sets & setting durations
		s.addAnimation(anim1);
		s.addAnimation(anim2);
		s.setDuration(2500);
		s2.addAnimation(animText);
		s2.addAnimation(anim2);
		s2.setStartTime(2000);

		// Starting anim sets
		image1.startAnimation(s);
		text1.startAnimation(s2);

		// Timer to start next activity & change screens
		new CountDownTimer(3500, 3500) {
			@Override
			public void onTick(long millisUntilFinished) {}

			@Override
			public void onFinish() {
				// Starting main dashboard
				Intent start = new Intent(getApplicationContext(), DashboardActivity.class);
				start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(start);
				// Closing animation screen
				finish();
			}
		}.start();

	}
}

