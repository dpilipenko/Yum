package com.cse5236groupthirteen.dev;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UpdateRestaurantGeoLocationsActivity extends Activity implements
		OnClickListener {

	private TextView txtvw_log;
	private TextView txtvw_currentLocation;
	private Button btn_start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_restaurant_geo_locations);
		Parse.initialize(this, ParseHelper.APPLICATION_ID,
				ParseHelper.CLIENT_KEY);

		txtvw_log = (TextView) findViewById(R.id.txtvw_dev_updategeolocations_log);
		txtvw_currentLocation = (TextView) findViewById(R.id.txtvw_dev_updategeolocations_currlocation);
		btn_start = (Button) findViewById(R.id.btn_dev_updategeolocations_start);

		btn_start.setOnClickListener(this);

		displayCurrentLocation();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_dev_updategeolocations_start:
			doUpdate();
			break;
		}

	}

	protected void l(String message) {
		txtvw_log.append(message);
	}

	protected void ln(String message) {
		l("\n" + message);
	}

	private void displayCurrentLocation() {
		Location myLocation = YumHelper.getLastBestLocation(this);
		double lat = myLocation.getLatitude();
		double lon = myLocation.getLongitude();
		String msg = "Your last known location:\n";
		msg = "(" + lat + "," + lon + ")";
		txtvw_currentLocation.setText(msg);
		Log.v("Yum", "SET CURRENT LOCATION");
	}

	private void doUpdate() {

		// start
		ln("Starting update");

		// download all restaurants
		ln("Getting restaurants");
		ArrayList<Restaurant> restaurants = getRestaurants();
		ln("Gotten restaurants");

		// for each restaurant address, get location coordinates
		ArrayList<ParseGeoPoint> locations = new ArrayList<ParseGeoPoint>();
		int count = restaurants.size();
		ln("Beginning coordinate fetch for " + count + " rests");
		for (int i = 0; i < count; i++) {
			Restaurant r = restaurants.get(i);
			ln("Getting geo-point for {" + i + "}");
			ParseGeoPoint gp = getParseGeoPointFromRestaurant(r);
			ln("Gotten geo-point for {" + i + "}");
			locations.add(i, gp);
		}
		ln("Finished coordinate fetch");

		// for each restaurant, add and write coordinates
		for (int i = 0; i < count; i++) {
			Restaurant r = restaurants.get(i);
			ParseGeoPoint gp = locations.get(i);
			r.setGeoPoint(gp);
			ParseObject po = r.toParseObject();
			ln("Saving geo-point for {" + i + "}");
			try {
				po.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ln("Saved geo-point for {" + i + "}");
		}

		// finish
		ln("Finished update");
	}

	private ParseGeoPoint getParseGeoPointFromRestaurant(Restaurant r) {

		return YumHelper.getParseGeoPointFromRestaurantFullAddress(this,
				r.getFullAddress());

	}

	private ArrayList<Restaurant> getRestaurants() {

		ParseQuery q = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		q.whereExists("objectId");
		List<ParseObject> pos = new ArrayList<ParseObject>();
		ln("Fetching restaurants");
		try {
			pos = q.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ln("Fetched restaurants");
		ArrayList<Restaurant> rs = new ArrayList<Restaurant>();
		for (ParseObject po : pos) {
			rs.add(new Restaurant(po));
		}
		return rs;

	}

}
