package com.cse5236groupthirteen;

import java.util.List;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantViewActivity extends Activity implements OnClickListener {

	private Restaurant selectedRestaurant;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Don't touch these two lines!!!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_view);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		
		((Button)findViewById(R.id.btn_showRestaurantsMenu)).setOnClickListener(this);
		((Button)findViewById(R.id.button1)).setOnClickListener(this);
		
		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			String selectedRestaurantId = b.getString(Restaurant.R_UUID);
			loadRestaurant(selectedRestaurantId);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg , Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg);
		}
		
		
		
	}
	
	private void populateUI() {
		
		TextView txtView = (TextView) findViewById(R.id.txtvw_RestaurantName);
		txtView.append(selectedRestaurant.getName());
		
	}
	
	private void loadRestaurant(String restaurantId) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereEqualTo(Restaurant.R_UUID, restaurantId);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					
					if (objects.size() == 1) {
						// id is unique and there should only be one result
						ParseObject po = objects.get(0);
						selectedRestaurant = new Restaurant(po);
						populateUI();
						
					} else {
						String errmsg = "Parse returned multiple objects";
						Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT).show();
						Log.e("Yum", errmsg, e);
					}
					
				} else {
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT).show();
					Log.e("Yum", errmsg, e);
				}
				
			}
			
		});
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_showRestaurantsMenu:
			Intent intent = new Intent(RestaurantViewActivity.this, MenuViewActivity.class);
			intent.putExtra(Restaurant.R_UUID, selectedRestaurant.getRestaurantId());
			intent.putExtra(Restaurant.R_NAME, selectedRestaurant.getName());
			startActivity(intent);
			break;
		case R.id.button1:
			break;
		}
		
	}

}
