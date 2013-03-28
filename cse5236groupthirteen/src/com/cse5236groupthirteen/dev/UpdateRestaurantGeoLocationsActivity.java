package com.cse5236groupthirteen.dev;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.R.layout;
import com.cse5236groupthirteen.R.menu;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.google.android.maps.GeoPoint;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UpdateRestaurantGeoLocationsActivity extends Activity implements OnClickListener {

	private TextView txtvw_log;
	private TextView txtvw_currentLocation;
	private Button btn_start;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_restaurant_geo_locations);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		txtvw_log = (TextView)findViewById(R.id.txtvw_dev_updategeolocations_log);
		txtvw_currentLocation = (TextView)findViewById(R.id.txtvw_dev_updategeolocations_currlocation);
		btn_start = (Button)findViewById(R.id.btn_dev_updategeolocations_start);
		
		btn_start.setOnClickListener(this);
		
		displayCurrentLocation();
		
	}

	private void displayCurrentLocation() {
		Location myLocation = getLastBestLocation();
		double lat = myLocation.getLatitude();
		double lon = myLocation.getLongitude();
		String msg = "Your last known location:\n";
		msg = "("+lat+","+lon+")";
		txtvw_currentLocation.setText(msg);
		Log.v("Yum", "SET CURRENT LOCATION");
	}

	private Location getLastBestLocation() {
		LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    Location locationGPS = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }

	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	        return locationGPS;
	    }
	    else{
	        return locationNet;
	    }

	}
	
	@Override
	public void onClick(View v) {

		switch(v.getId())
		{
		case R.id.btn_dev_updategeolocations_start:
			try {
				doUpdate();
			} catch (Exception e) {
				ln("THERE WAS AN EXCEPTION\n"+e.getMessage());
				return;
			} 
			break;
		}
		
	}

	protected void l(String message) {
		txtvw_log.append(message);
	}
	protected void ln(String message) {
		l("\n"+message);
	}
	
	private void doUpdate() throws ParseException, IOException {
		
		// start
		ln("Starting update");
		
		// download all restaurants
		ln("Getting restaurants");
		ArrayList<Restaurant> restaurants = getRestaurants();
		ln("Gotten restaurants");
		
		// for each restaurant address, get location coordinates
		ArrayList<ParseGeoPoint> locations = new ArrayList<ParseGeoPoint>();
		int count = restaurants.size();
		ln("Beginning coordinate fetch for "+count+" rests");
		for (int i = 0; i < count; i++) {
			Restaurant r = restaurants.get(i);
			ln("Getting geo-point for {"+i+"}");
			ParseGeoPoint gp = getParseGeoPointFromRestaurant(r);
			ln("Gotten geo-point for {"+i+"}");
			locations.add(i, gp);
		}
		ln("Finished coordinate fetch");
		
		// for each restaurant, add and write coordinates
		for (int i = 0; i < count; i++) {
			Restaurant r = restaurants.get(i);
			ParseGeoPoint gp = locations.get(i);
			r.setGeoPoint(gp);
			ParseObject po = r.toParseObject();
			ln("Saving geo-point for {"+i+"}");
			po.save();
			ln("Saved geo-point for {"+i+"}");
		}
		
		// finish
		ln("Finished update");
	}

	private ParseGeoPoint getParseGeoPointFromRestaurant(Restaurant r) throws IOException {
		
		String strAddress = r.getFullAddress();
		Geocoder coder = new Geocoder(this);
		List<android.location.Address> addresses;
		try {

			strAddress = strAddress.replace(' ', '+');
		    addresses = coder.getFromLocationName(strAddress,5);
		    if (addresses == null) {
		        return null;
		    }
		    if (addresses.isEmpty()) {
		    	return null;
		    }
		    android.location.Address location = addresses.get(0);
		    double latitude = location.getLatitude();
		    double longitude = location.getLongitude();
		    
		    return new ParseGeoPoint(latitude,longitude);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
		
	}

	private ArrayList<Restaurant> getRestaurants() throws ParseException {
		
		ParseQuery q = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		q.whereExists("objectId");
		List<ParseObject> pos = new ArrayList<ParseObject>();
		ln("Fetching restaurants");
		pos = q.find();
		ln("Fetched restaurants");
		ArrayList<Restaurant> rs = new ArrayList<Restaurant>();
		for (ParseObject po: pos) {
			rs.add(new Restaurant(po));
		}
		return rs;
		
	}

}
