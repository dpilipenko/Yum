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

import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class HomeViewActivity extends YumViewActivity {

	private boolean querying;
	private ListView listView;
	private ArrayAdapter<Restaurant> listAdapter;
	protected Dialog splashDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_view);

		showSplashScreen();

		// set global variables
		querying = false;
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

}
