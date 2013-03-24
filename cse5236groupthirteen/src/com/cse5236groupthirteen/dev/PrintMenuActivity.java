package com.cse5236groupthirteen.dev;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.MenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class PrintMenuActivity extends Activity implements OnItemSelectedListener {

	private Spinner spinner;
	private ListView listview;
	private ArrayAdapter<Restaurant> spinnerAdapter;
	private ArrayAdapter<MenuItem> listviewAdapter;
	private int selectedPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_menu);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// grab ui elements
		spinner = (Spinner)findViewById(R.id.spnr_menuview_restaurant);
		listview = (ListView)findViewById(R.id.lstvw_menuView_menuitemlist);
		
		// setup spinner specifics
		spinnerAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1);
		spinner.setAdapter(spinnerAdapter);
		
		// setup list view specifics 
		listviewAdapter = new ArrayAdapter<MenuItem>(this, android.R.layout.simple_list_item_1);
		listview.setAdapter(listviewAdapter);
				
		// setup listeners
		spinner.setOnItemSelectedListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadDataFromParse();
	}
	
	private void loadDataFromParse() {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId"); // all Parse objects contain this key, so
										// it should return all stored
										// Restaurants
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					spinnerAdapter.clear();
					for (ParseObject po : objects) {
						Restaurant r = new Restaurant(po);
						spinnerAdapter.add(r);
					}
					spinnerAdapter.notifyDataSetChanged();

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

	private void updateMenuList() {
		
		// get selected restaurant
		Restaurant a = (Restaurant)(spinner.getItemAtPosition(selectedPosition));
		
		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		String b = MenuItem.MI_RESTID;
		String c = a.getRestaurantId();
		query.whereEqualTo(b, c);
		
		// query parse
		List<ParseObject> menuItems = new ArrayList<ParseObject>();
		try {
			menuItems = query.find();
		} catch (ParseException e) {
			// error with parse occurred
			e.printStackTrace();
			return;
		}
		
		// check amount of returned hits
		if (menuItems.size() == 0) {
			// no hits :(
			
		} else {
			// we got hits! :)
			for(ParseObject po: menuItems) {
				MenuItem m = new MenuItem(po);
				listviewAdapter.add(m);
			}
			listviewAdapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		selectedPosition = pos;
		listviewAdapter.clear();
		updateMenuList();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

}
