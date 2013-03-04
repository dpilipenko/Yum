package com.cse5236groupthirteen.dev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cse5236groupthirteen.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;

public class PrintRestaurantsActivity extends Activity {

	private ArrayList<String> restaurants;
	private TextView txtLastSelection;
	private ArrayAdapter<String> listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// create UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_restaurants);
		// this is necessary to call in onCreate, in order to use Parse
		String applicationId = getString(R.string.parse_applicationId);
		String clientKey = getString(R.string.parse_clientKey);
		Parse.initialize(this, applicationId, clientKey);

		// grab UI references
		ListView listView = (ListView) findViewById(R.id.lstvw_allRestaurantsNames);
		txtLastSelection = (TextView) findViewById(R.id.text_printRestaurants_lastSelection);

		// populate the list
		restaurants = getDataFromDefaults();
		listAdapter = new ArrayAdapter<String>(this,R.layout.row_restaurants_names, getDataFromDefaults());
		listView.setAdapter(listAdapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selection = restaurants.get(position);
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

		ParseQuery query = new ParseQuery("Restaurant");
		query.whereExists("objectId");

		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					listAdapter.clear();
					for(ParseObject po: objects) {
						listAdapter.add(po.getString("name"));
					}
				}
				
			}
			
		});
		

	}
	

}
