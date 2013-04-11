package com.cse5236groupthirteen.protectedactivities;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.YumMenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumRestaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

/**
 * This activity is responsible for printing all the menu items for a selected
 * restaurant
 *
 */
public class PrintMenuActivity extends Activity implements OnItemSelectedListener {

	private Spinner mRestaurantSpinner;
	private ListView mMenuListview;
	private ArrayAdapter<YumRestaurant> mRestaurantAdapter;
	private ArrayAdapter<YumMenuItem> mMenuItemAdapter;
	private int mSelectedPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_print_menu);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// grab ui elements
		mRestaurantSpinner = (Spinner)findViewById(R.id.spnr_menuview_restaurant);
		mMenuListview = (ListView)findViewById(R.id.lstvw_menuView_menuitemlist);
		
		// setup spinner specifics
		mRestaurantAdapter = new ArrayAdapter<YumRestaurant>(this, android.R.layout.simple_spinner_item);
		mRestaurantSpinner.setAdapter(mRestaurantAdapter);
		
		// setup list view specifics 
		mMenuItemAdapter = new ArrayAdapter<YumMenuItem>(this, android.R.layout.simple_list_item_1);
		mMenuListview.setAdapter(mMenuItemAdapter);
				
		// setup listeners
		mRestaurantSpinner.setOnItemSelectedListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadRestaurantsFromParse();
	}
	
	private void loadRestaurantsFromParse() {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists(YumRestaurant.R_UUID); // all Parse objects contain this key, so
										// it should return all stored
										// Restaurants
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					mRestaurantAdapter.clear();
					for (ParseObject po : objects) {
						YumRestaurant r = new YumRestaurant(po);
						mRestaurantAdapter.add(r);
					}
					mRestaurantAdapter.notifyDataSetChanged();

				} else {
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					YumHelper.handleError(getParent(), errmsg);
				}

			}

		});
		
	}

	/**
	 * This method updates the menu list based on the current selected restaurant
	 */
	private void updateMenuList() {
		
		// get selected restaurant
		YumRestaurant a = (YumRestaurant)(mRestaurantSpinner.getItemAtPosition(mSelectedPosition));
		
		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		query.whereEqualTo(YumMenuItem.MI_RESTID, a.getRestaurantId());
		
		// query parse
		List<ParseObject> menuItems = new ArrayList<ParseObject>();
		try {
			menuItems = query.find();
		} catch (ParseException e) {
			String errmsg = "Failed on fetching MenuItem";
			YumHelper.handleException(this, e, errmsg);
			return;
		}
		
		// check amount of returned hits
		if (menuItems.size() == 0) {
			// no hits :(
			
		} else {
			// we got hits! :)
			for(ParseObject po: menuItems) {
				YumMenuItem m = new YumMenuItem(po);
				mMenuItemAdapter.add(m);
			}
			mMenuItemAdapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		mSelectedPosition = pos;
		mMenuItemAdapter.clear();
		updateMenuList();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
