package com.cse5236groupthirteen.dev;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;

public class PrintRestaurantsActivity extends Activity {

	private TextView txtLastSelection;
	private ArrayAdapter<String> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// create UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_restaurants);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);

		// grab UI references
		ListView listView = (ListView) findViewById(R.id.lstvw_allRestaurantsNames);
		txtLastSelection = (TextView) findViewById(R.id.text_printRestaurants_lastSelection);

		// populate the list
		listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, getDataFromDefaults());
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = listAdapter.getItem(position);
				txtLastSelection.setText(selection);
			}

		});

		loadDataFromParse();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}



	@Override
	protected void onStart() {
		super.onStart();
		
	}


	private ArrayList<String> getDataFromDefaults() {
		
		ArrayList<String> al = new ArrayList<String>();
		al.add("Click Me");
		return al;
		
		
	}
	
	private void loadDataFromParse() {
		listAdapter.clear();
		listAdapter.add("Downloading");

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId");

		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					listAdapter.clear();
					for(ParseObject po: objects) {
						
						ParseGeoPoint pogp = po.getParseGeoPoint(Restaurant.R_GEOLOC);
						ParseGeoPoint mygp = getMyParseGeoPoint();
						double dst = pogp.distanceInKilometersTo(mygp);
						
						String name = po.getString("name");
						String dstStr = String.format("%.3f", dst);
						String msg = name + " " + dstStr +"km away";
						listAdapter.add(msg);
					}
					if (objects.size() == 0) {
						listAdapter.add("No Restaurants Found");
					}
					listAdapter.notifyDataSetChanged();
				}
				
			}

			private ParseGeoPoint getMyParseGeoPoint() {
				Location l = getLastBestLocation();
				return new ParseGeoPoint(l.getLatitude(), l.getLongitude());
			}
			
		});
		

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
	

}
