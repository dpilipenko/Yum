package com.cse5236groupthirteen;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class RestaurantView extends Activity implements OnClickListener {

	private TextView restaurantName;
	private TextView restaurantStreetNumber;
	private TextView restaurantStreetName;
	private TextView restaurantPhone;
	private EditText setWaitTime;
	private TextView submittedWaitTimes=null;
	private ArrayList<String> waitTimeList = new ArrayList<String>();
	private Button inLineSubmit;
	private String waitTime;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_view);
		
		restaurantName = (TextView) findViewById(R.id.et_addRestaurant_restaurantName);
		restaurantStreetNumber = (TextView) findViewById(R.id.et_addRestaurant_streetnumber);
		restaurantStreetName = (TextView) findViewById(R.id.et_addRestaurant_streetname);
		restaurantPhone = (TextView) findViewById(R.id.et_addRestaurant_phonenumber);
		//submittedWaitTimes = (TextView) findViewById(R.id.waitTimeList);
		//setWaitTime = (EditText) findViewById(R.id.newWaitTime);
		inLineSubmit = (Button) findViewById(R.id.GotInLine);
		
	}
	
	@Override
	protected void onDestroy() {
		Log.v("RestaruantView", "CSE5236 - onDestroy is called.");
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		Log.v("RestaruantView", "CSE5236 - onStart is called.");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.v("RestaruantView", "CSE5236 - onStop is called.");
		super.onStop();
	}

	@Override
	protected void onPause() {
		Log.v("RestaruantView", "CSE5236 - onPause is called.");
		super.onPause();		
	}

	@Override
	protected void onResume() {
		Log.v("RestaruantView", "CSE5236 - onResume is called");
		super.onResume();
	}
	
	@Override
	public void onClick(View view) {
		
		if(setWaitTime == null) {
			//do nothing
		}
		else { //submit time to ArrayList
			waitTime = setWaitTime.toString();
			waitTimeList.add(waitTime);
		}
	}
	
	

}
