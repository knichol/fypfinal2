package com.fyp.fitness;

import com.example.androidgpsexample.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class fitdash extends Activity{

	Button btnExer, btnGoals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.fithome);

		btnExer = (Button) findViewById(R.id.exercise);
		btnGoals = (Button) findViewById(R.id.goals);

		// Exercise Dash Link Button
		btnExer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), exercise.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);		
			}
		});
	
	
	
	
	}
}
