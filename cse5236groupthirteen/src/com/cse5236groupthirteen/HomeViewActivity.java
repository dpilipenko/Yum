package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.dev.DevActivity;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class HomeViewActivity extends Activity {

	private ListView listView;
	private ArrayAdapter<Restaurant> listAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// necessary Android code
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_view);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		
		// grab UI elements
		listView = (ListView) findViewById(R.id.lstvw_homeView);
		
		// setup ListView stuff
		listAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Restaurant r = listAdapter.getItem(position);
				
				Intent intent = new Intent(HomeViewActivity.this, RestaurantViewActivity.class);
				intent.putExtra(Restaurant.R_UUID, r.getRestaurantId());
				intent.putExtra(Restaurant.R_NAME, r.getName());
				startActivity(intent);
				
			}

		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem menuDevPage = menu.add("Dev Page");
		Intent intentDevPage = new Intent(this, DevActivity.class);
		menuDevPage.setIntent(intentDevPage);
		
		return true;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		loadDataFromParse();
	}

	private void loadDataFromParse() {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId"); // all Parse objects contain this key, so it should return all stored Restaurants
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					listAdapter.clear();
					for (ParseObject po: objects) {
						Restaurant r = new Restaurant(po);
						listAdapter.add(r);
					}
					listAdapter.notifyDataSetChanged();
					
				} else {
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT).show();
					Log.e("Yum", errmsg, e);
				}
				
			}
			
		});
		
		
	}
	
	

}
