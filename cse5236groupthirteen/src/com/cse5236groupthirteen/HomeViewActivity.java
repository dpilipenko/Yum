package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.models.YumRestaurant;
import com.cse5236groupthirteen.models.YumRestaurantWithMyLocation;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This activity is responsible for listing all the restaurants to the user.
 * We use geolocation to get the user's current location and then query Parse
 * to return restaurants sorted by distance
 *
 */
public class HomeViewActivity extends YumViewActivity implements OnItemSelectedListener{

	// UI elements
	private ListView mListView;
	protected Dialog mSplashDialog;
	
	
	private ArrayAdapter<YumRestaurant> mListAdapter;
	private boolean mQuerying;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showSplashScreen();
		setContentView(R.layout.activity_home_view);
		
		// tests for location and network in a separate thread
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	    	  testLocationServices();
	    	  testNetworkConnection();
	      }
	    }, 0);
	    
	    // set init var
	    mQuerying = false;

	    // set up spinner
		Spinner searchDistance = (Spinner) findViewById(R.id.search_distance);
		searchDistance.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
							R.array.distance_radius, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchDistance.setAdapter(adapter);
		
		// set up restaurant list
		mListAdapter = new ArrayAdapter<YumRestaurant>(this, android.R.layout.simple_list_item_1);
		mListView = (ListView) findViewById(R.id.lstvw_homeView_restaurants);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				YumRestaurant r = mListAdapter.getItem(position);

				Intent intent = new Intent(HomeViewActivity.this,
						RestaurantViewActivity.class);
				// pass over restaurant information so it doesn't need to be parsed again
				intent.putExtra(YumRestaurant.R_UUID, r.getRestaurantId());
				intent.putExtra(YumRestaurant.R_NAME, r.getName());
				
				startActivity(intent);

			}

		});

	}
	
	/**
	 * This method tests the phones location services and displays an alert if it 
	 * can't access the location
	 */
	private void testLocationServices() {
		boolean test = YumHelper.testLocationServices(this);
		if (!test) {
			String msg = "Sorry, could not access your location.";
			YumHelper.displayAlert(this, msg);
		}
		
	}

	/**
	 * This method test the phones connection to the internet and to Parse, and
	 * displays an alert if it can't access the location
	 */
	private void testNetworkConnection() {
		
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    // check if have wifi or cell data
	    boolean haveData = false;
	    if (wifi.isConnected()) {
	        haveData = true;
	    } else if (mobile.isConnected()) {
	        haveData = true;
	    }
	    
	    // alerts if no available data
	    if (!haveData) {
	    	String msg = "Sorry, could not connect to internet";
	    	YumHelper.displayAlert(this, msg);
	    	return;
	    }

	    // tests Parse connection
		boolean haveParseConnection = YumHelper.testParseConnection(this);
		if (!haveParseConnection) {
			String msg = "Sorry, could not connect with our data at this time. " +
					"We apologize for the inconvinience.";
			YumHelper.displayAlert(this, msg);
			return;
		}
	}

	private void showSplashScreen() {
		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splash_screen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();

	}

	private void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (hasInternet()) {
			loadRestaurants();
		}
	}

	@Override
	public void onShake() {
		if (!mQuerying) {
			mListAdapter.clear();
			loadRestaurants();
		}

	}

	protected void showLoadingDialog() {
		if (mSplashDialog == null) {
			super.showLoadingDialog();
		}
		
	}

	protected void dismissLoadingDialog() {
		super.dismissLoadingDialog();
		removeSplashScreen();
	}

	/**
	 * This method loads restaurants from our back-end and populates the list 
	 */
	private void loadRestaurants() {	
		mQuerying = true;
		showLoadingDialog();
		
		// gets users current location
		final ParseGeoPoint myLocation = YumHelper.getLastBestLocationForParse(this);
		
		// query for 50 of the nearest restaurants 
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists(YumRestaurant.R_UUID);
		query.whereNear(YumRestaurant.R_GEOLOC, myLocation);
		query.setLimit(50);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				mQuerying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					
					// query successful
					mListAdapter.clear();
					for (ParseObject po : objects) {
						YumRestaurant r = new YumRestaurantWithMyLocation(po, myLocation);
						mListAdapter.add(r);
					}
					mListAdapter.notifyDataSetChanged();

				} else {
					
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					Toast.makeText(getApplicationContext(), errmsg,
							Toast.LENGTH_SHORT).show();
					Log.e("Yum", errmsg, e);
				}
				
			}
		});
	}
	
	/**
	 * This method loads restaurants within a kilometer radius
	 * @param radiusInKM
	 */
	private void loadRestaurants(int radiusInKM) {	
		mQuerying = true;
		showLoadingDialog();
		
		final ParseGeoPoint myLocation = YumHelper.getLastBestLocationForParse(this);
		
		// query for 50 restaurants within __ kilometer radius
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists(YumRestaurant.R_UUID);
		query.setLimit(50);
		query.whereWithinKilometers(YumRestaurant.R_GEOLOC, myLocation, radiusInKM);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				mQuerying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					
					// query successful
					mListAdapter.clear();
					for (ParseObject po : objects) {
						YumRestaurant r = new YumRestaurantWithMyLocation(po, myLocation);
						mListAdapter.add(r);
					}
					mListAdapter.notifyDataSetChanged();

				} else {
					
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					Toast.makeText(getApplicationContext(), errmsg,
							Toast.LENGTH_SHORT).show();
					Log.e("Yum", errmsg, e);
				}
				
			}
		});
	}


	/**
	 * This method should be called if the user changes the  radius they want
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		id = parent.getItemIdAtPosition(pos);
		
		int numer = (int)id;
		
		switch(numer) {
		case 0: // load all
			loadRestaurants();
			break;
		case 1: // load 5 km radius
			loadRestaurants(5);
			break;
		case 2: // load 10 km radius
			loadRestaurants(10);
			break;
		case 3: // load 20 km radius
			loadRestaurants(20);
			break;
		case 4: // load 50 km radius
			loadRestaurants(50);
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.getItemAtPosition(0);
		
	}

}
