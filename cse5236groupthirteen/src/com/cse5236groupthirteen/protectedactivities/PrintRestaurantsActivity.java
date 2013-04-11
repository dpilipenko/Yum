package com.cse5236groupthirteen.protectedactivities;

import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumRestaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;

/**
 * This activity is responsible for listing all the restaurants available
 *
 */
public class PrintRestaurantsActivity extends Activity {

	private TextView mTxtvw_lastSelection;
	private ListView mLstvw_restaurants;
	private ArrayAdapter<String> mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// create UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_print_restaurants);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);

		// grab UI references
		mLstvw_restaurants = (ListView) findViewById(R.id.lstvw_printrestaurants_allrestaurantnames);
		mTxtvw_lastSelection = (TextView) findViewById(R.id.txtvw_printrestaurants_lastselectionlabel);

		// populate the list
		mListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		mLstvw_restaurants.setAdapter(mListAdapter);
		
		mLstvw_restaurants.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = mListAdapter.getItem(position);
				mTxtvw_lastSelection.setText(selection);
			}

		});

		loadRestaurantsFromParse();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}



	@Override
	protected void onStart() {
		super.onStart();
		
	}
	
	private void loadRestaurantsFromParse() {
		mListAdapter.clear();
		mListAdapter.add("Downloading");

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId");

		query.findInBackground(new FindCallback() {

			@SuppressLint("DefaultLocale")
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					mListAdapter.clear();
					ParseGeoPoint myLoc = getMyParseGeoPoint();
					for(ParseObject po: objects) {
						
						ParseGeoPoint pogp = po.getParseGeoPoint(YumRestaurant.R_GEOLOC);
						ParseGeoPoint mygp = myLoc;
						double dst = pogp.distanceInKilometersTo(mygp);
						
						String name = po.getString("name");

						String dstStr = String.format("%.3f", dst);
						String msg = name + " " + dstStr +"km away";
						mListAdapter.add(msg);
					}
					if (objects.size() == 0) {
						mListAdapter.add("No Restaurants Found");
					}
					mListAdapter.notifyDataSetChanged();
				}
				
			}

			private ParseGeoPoint getMyParseGeoPoint() {
				Location l = YumHelper.getLastBestLocation(PrintRestaurantsActivity.this);
				return new ParseGeoPoint(l.getLatitude(), l.getLongitude());
			}
			
		});
		

	}

}
