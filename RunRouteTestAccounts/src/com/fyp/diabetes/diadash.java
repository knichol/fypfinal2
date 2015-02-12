package com.fyp.diabetes;

import com.example.androidgpsexample.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class diadash extends Activity {

	Button btnProf, btnUpd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		setContentView(R.layout.diahome);

		btnProf = (Button) findViewById(R.id.btnMetrics);
		btnUpd = (Button) findViewById(R.id.btnSetMet);

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
		
	}
	
}

