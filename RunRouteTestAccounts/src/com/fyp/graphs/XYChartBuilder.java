package com.fyp.graphs;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.androidgpsexample.R;
import com.fyp.library.UserFunctions;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class XYChartBuilder extends Activity {
	/** The main dataset that includes all the series that go into a chart. */
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

	/** The main renderer that includes all the renderers customizing a chart. */
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

	/** The most recently added series. */
	private XYSeries mCurrentSeries;

	/** The most recently created renderer, customizing the current series. */
	private XYSeriesRenderer mCurrentRenderer;

	/** Button for creating a new series of data. */
	private Button mNewSeries;

	/** The chart view that displays the data. */
	private GraphicalView mChartView;

	SQLiteDatabase db;
	UserFunctions userFunction = new UserFunctions();
	Cursor c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xy_chart);

		// set some properties on the main renderer
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.argb(255, 247, 234, 200));
		mRenderer.setMarginsColor(Color.argb(255, 247, 234, 200));
		mRenderer.setAxisTitleTextSize(25);
		mRenderer.setChartTitleTextSize(30);
		mRenderer.setLabelsTextSize(30);
		mRenderer.setLegendTextSize(30);
		mRenderer.setMargins(new int[] { 10, 30, 25, 20 });
		mRenderer.setZoomButtonsVisible(false);
		mRenderer.setPointSize(15);
		mRenderer.setShowGrid(true);

		// the button that handles the new series of data creation
		mNewSeries = (Button) findViewById(R.id.new_series);

		mNewSeries.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//repaint();
				series("Weight");
				getWeight();
			}
		});

	}

	public void series(String S){
		//String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
		String seriesTitle = S;

		// create a new series of data
		XYSeries series = new XYSeries(seriesTitle);
		mDataset.addSeries(series);
		mCurrentSeries = series;
		// create a new renderer for the new series
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		// set some renderer properties
		renderer.setPointStyle(PointStyle.CIRCLE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setDisplayChartValuesDistance(10);
		mCurrentRenderer = renderer;
		mChartView.repaint();
	}

	public void getWeight(){
		// Finding or creating the database is nonexistent 
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

		// Checking currently logged in users metrics
		c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		int i=1;
		while(c.moveToNext()) {
			addpts(i++,Double.parseDouble(c.getString(2)));
		}
	}
	
	public void getGlucose(){
		// Finding or creating the database is nonexistent 
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

		// Checking currently logged in users metrics
		c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		int i=1;
		while(c.moveToNext()) {
			addpts(i++,Double.parseDouble(c.getString(4)));
		}
	}
	
	public void getA1c(){
		// Finding or creating the database is nonexistent 
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

		// Checking currently logged in users metrics
		c = db.rawQuery("SELECT * FROM user_metrics WHERE user_id = "+
				"'"+userFunction.getUID(getApplicationContext())+"'", null);

		int i=1;
		while(c.moveToNext()) {
			addpts(i++,Double.parseDouble(c.getString(5)));
		}
	}
		
	public void addpts(double nX, double nY){
		double x = nX;
		double y = nY;

		// add a new data point to the current series
		mCurrentSeries.add(x, y);

		// repaint the chart such as the newly added point to be visible
		mChartView.repaint();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
			// enable the chart click events
			mRenderer.setClickEnabled(true);
			mRenderer.setSelectableBuffer(10);
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// handle the click event on the chart
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						//Toast.makeText(XYChartBuilder.this, "No chart element", Toast.LENGTH_SHORT).show();
					} else {
						// display information of the clicked point
						Toast.makeText(
								XYChartBuilder.this,
								"Chart element in series index " + seriesSelection.getSeriesIndex()
								+ " data point index " + seriesSelection.getPointIndex() + " was clicked"
								+ " closest point value X=" + seriesSelection.getXValue() + ", Y="
								+ seriesSelection.getValue(), Toast.LENGTH_SHORT).show();
					}
				}
			});
			layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			boolean enabled = mDataset.getSeriesCount() > 0;
		} else {
			mChartView.repaint();
		}
	}
}