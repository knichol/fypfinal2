package com.fyp.fitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.androidgpsexample.R;
import com.fyp.diabetes.prof;
import com.fyp.diabetes.setmet;
import com.fyp.main.MainActivity;

public class exercise extends Activity {

	Button btnFree, btnTimed, btnDist, btnGraph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.exercise);

		btnFree = (Button) findViewById(R.id.exerFree);
		btnTimed = (Button) findViewById(R.id.exerTimed);
		btnDist = (Button) findViewById(R.id.exerDist);
		btnGraph = (Button) findViewById(R.id.exerGraph);

		
		// Profile Link Button
		btnFree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), MainActivity.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});
		
	}
}
