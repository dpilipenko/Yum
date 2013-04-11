package com.cse5236groupthirteen;

import java.util.Stack;

import com.cse5236groupthirteen.protectedactivities.LoginActivity;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.parse.Parse;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * This class serves as a base class for our other Activities.
 * This class is responsible for initialize Parse, sensing shaking motions, 
 * as well as displaying the Loading screens
 *
 */
public class YumViewActivity extends Activity implements SensorEventListener {

	// for loading bar
	private Stack<ProgressDialog> mProgressBarsStack;
	
	// for shake detection
	final private int mShakeThreshold = 15;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometerSensor;
	private boolean mSensorInitialized;
	private float mLastXAxisReading;
	private float mLastYAxisReading;
	private float mLastZAxisReading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// for using Parse
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// for tracking loading bars
		mProgressBarsStack = new Stack<ProgressDialog>();
		
		// for detecting Shakes
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		mSensorInitialized = false;
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem menuDevPage = menu.add("Login Page");
		Intent intentDevPage = new Intent(this, LoginActivity.class);
		menuDevPage.setIntent(intentDevPage);
		
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	    mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// do nothing
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float xAxis = event.values[0];
        float yAxis = event.values[1];
        float zAxis = event.values[2];
		
        if (!mSensorInitialized) {
        	mLastXAxisReading = xAxis;
        	mLastYAxisReading = yAxis;
        	mLastZAxisReading = zAxis;
        	mSensorInitialized = true;
        } else {
        	final float Noise = 2.0f; 
        	float deltaX = Math.abs(mLastXAxisReading - xAxis);
        	float deltaY = Math.abs(mLastYAxisReading - yAxis);
        	float deltaZ = Math.abs(mLastZAxisReading - zAxis);
        	if (deltaX < Noise)
        		deltaX = 0.0f;
        	if (deltaY < Noise)
        		deltaY = 0.0f;
        	if (deltaZ < Noise)
        		deltaZ = 0.0f;
        	mLastXAxisReading = xAxis;
        	mLastYAxisReading = yAxis;
        	mLastZAxisReading = zAxis;
        	
        	boolean xShake = (deltaX > mShakeThreshold);
        	boolean yShake = (deltaY > mShakeThreshold);
        	boolean zShake = (deltaZ > mShakeThreshold);
        	
        	if (xShake || yShake || zShake) {
        		
        		onShake();
        		
        	}
        }
		
	}
	
	/**
	 * This is a callback method we created.
	 * This method will be called when the accelerometer reads
	 * a reading greater than the shake threshold
	 */
	protected void onShake() {
		// do nothing for now
	}
	
	protected void showLoadingDialog() {
		ProgressDialog pd = ProgressDialog.show(this, "", "Loading...");
		mProgressBarsStack.add(pd);
	}
	
	protected void dismissLoadingDialog() {
		if (mProgressBarsStack.size() == 0) {
			return;
		}
		ProgressDialog pd = mProgressBarsStack.pop();
		pd.dismiss();
	}
	
}
