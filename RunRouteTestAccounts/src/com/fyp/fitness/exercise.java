package com.fyp.fitness;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidgpsexample.R;
import com.fyp.graphs.XYChartBuilderF;
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


		// Free Exercise Button
		btnFree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), MainActivity.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

		// Distance Exercise button
		btnDist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				distDialog();
			}
		});

		// Timed Exercise button
		btnTimed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				timedDialog();
			}
		});
		
		// Timed Exercise button
		btnGraph.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent dia = new Intent(getApplicationContext(), XYChartBuilderF.class);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
			}
		});

	}

	// Distance dialog pop-up
	protected void distDialog() {
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(exercise.this);
		View promptView = layoutInflater.inflate(R.layout.dist_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(exercise.this);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView.findViewById(R.id.exerciseDist);
		//final float dist = Float.parseFloat(editText.toString());

		// setup a dialog window
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("Start", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				final float dist = Float.parseFloat(editText.getText().toString());
				Intent dia = new Intent(getApplicationContext(), exdist.class);
				dia.putExtra("dist", dist);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
				finish();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

	// Timed dialog pop-up
	protected void timedDialog() {
		// get prompts.xml view
		LayoutInflater layoutInflater = LayoutInflater.from(exercise.this);
		View promptView = layoutInflater.inflate(R.layout.timed_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(exercise.this);
		alertDialogBuilder.setView(promptView);

		final EditText editText = (EditText) promptView.findViewById(R.id.exerciseTimed);
		//final float dist = Float.parseFloat(editText.toString());

		// setup a dialog window
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("Start", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				final int t = Integer.parseInt(editText.getText().toString());
				Intent dia = new Intent(getApplicationContext(), extimed.class);
				dia.putExtra("time", t);
				dia.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(dia);
				finish();
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});

		// create an alert dialog
		AlertDialog alert = alertDialogBuilder.create();
		alert.show();
	}

}
