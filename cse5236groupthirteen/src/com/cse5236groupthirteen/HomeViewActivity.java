package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.utilities.RestaurantWithMyLocation;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
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

public class HomeViewActivity extends YumViewActivity implements OnItemSelectedListener{

	private boolean querying;
	private ListView listView;
	private ArrayAdapter<Restaurant> listAdapter;
	protected Dialog splashDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showSplashScreen();
		setContentView(R.layout.activity_home_view);
		
	    final Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	      @Override
	      public void run() {
	    	  testLocationServices();
	    	  testNetworkConnection();
	      }
	    }, 0);


		querying = false;

		Spinner searchDistance = (Spinner) findViewById(R.id.search_distance);
		searchDistance.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
							R.array.distance_radius, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchDistance.setAdapter(adapter);
		
		listView = (ListView) findViewById(R.id.lstvw_homeView_restaurants);
		listAdapter = new ArrayAdapter<Restaurant>(this,
				android.R.layout.simple_list_item_1);
		
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Restaurant r = listAdapter.getItem(position);

				Intent intent = new Intent(HomeViewActivity.this,
						RestaurantViewActivity.class);
				
				// pass over restaurant information so it doesn't need to be parsed again
				intent.putExtra(Restaurant.R_UUID, r.getRestaurantId());
				intent.putExtra(Restaurant.R_NAME, r.getName());
				
				startActivity(intent);

			}

		});

	}

	private void testLocationServices() {
		boolean test = YumHelper.testLocationServices(this);
		if (!test) {
			String msg = "Sorry, could not access your location.";
			YumHelper.displayAlert(this, msg);
		}
		
	}

	private void testNetworkConnection() {
		
		ConnectivityManager connec = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    boolean haveData = false;
	    if (wifi.isConnected()) {
	        haveData = true;
	    } else if (mobile.isConnected()) {
	        haveData = true;
	    }
	    
	    if (!haveData) {
	    	String msg = "Sorry, could not connect to internet";
	    	YumHelper.displayAlert(this, msg);
	    	return;
	    }

		boolean haveParseConnection = YumHelper.testParseConnection(this);
		if (!haveParseConnection) {
			String msg = "Sorry, could not connect with our data at this time. " +
					"We apologize for the inconvinience.";
			YumHelper.displayAlert(this, msg);
			return;
		}
	}

	private void showSplashScreen() {
		splashDialog = new Dialog(this, R.style.SplashScreen);
		splashDialog.setContentView(R.layout.splash_screen);
		splashDialog.setCancelable(false);
		splashDialog.show();

	}

	private void removeSplashScreen() {
		if (splashDialog != null) {
			splashDialog.dismiss();
			splashDialog = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadDataFromParse();
	}

	@Override
	public void onShake() {
		if (!querying) {
			listAdapter.clear();
			loadDataFromParse();
		}

	}

	protected void showLoadingDialog() {
		if (splashDialog == null) {
			super.showLoadingDialog();
		}
		
	}

	protected void dismissLoadingDialog() {
		super.dismissLoadingDialog();
		removeSplashScreen();
	}

	private void loadDataFromParse() {	
		querying = true;
		showLoadingDialog();
		
	
		final ParseGeoPoint myLocation = YumHelper.getLastBestLocationForParse(this);
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists(Restaurant.R_UUID);
		query.whereNear(Restaurant.R_GEOLOC, myLocation);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				querying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					
					// query successful
					listAdapter.clear();
					for (ParseObject po : objects) {
						Restaurant r = new RestaurantWithMyLocation(po, myLocation);
						listAdapter.add(r);
					}
					listAdapter.notifyDataSetChanged();

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
	
	private void loadDataFromParse(int radius) {	
		querying = true;
		showLoadingDialog();
		
	
		final ParseGeoPoint myLocation = YumHelper.getLastBestLocationForParse(this);
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists(Restaurant.R_UUID);
		//query.whereNear(Restaurant.R_GEOLOC, myLocation);
		query.whereWithinKilometers(Restaurant.R_GEOLOC, myLocation, radius);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				querying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					
					// query successful
					listAdapter.clear();
					for (ParseObject po : objects) {
						Restaurant r = new RestaurantWithMyLocation(po, myLocation);
						listAdapter.add(r);
					}
					listAdapter.notifyDataSetChanged();

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


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		id = parent.getItemIdAtPosition(pos);
		
		int numer = (int)id;
		
		switch(numer) {
		case 0: 
			break;
		case 1:
			loadDataFromParse(5);
			break;
		case 2:
			loadDataFromParse(10);
			break;
		case 3:
			loadDataFromParse(20);
			break;
		case 4:
			loadDataFromParse(50);
			break;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		parent.getItemAtPosition(0);
		
	}

}
