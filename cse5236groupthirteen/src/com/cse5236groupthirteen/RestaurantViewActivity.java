package com.cse5236groupthirteen;

import java.util.Date;
import java.util.List;

import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantViewActivity extends Activity implements OnClickListener, OnItemClickListener {

	private Restaurant selectedRestaurant;
	private ArrayAdapter<Submission> listAdapter;
	
	private ListView reviewsListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Don't touch these two lines!!!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_view);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			String selectedRestaurantId = b.getString(Restaurant.R_UUID);
			loadRestaurant(selectedRestaurantId);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT)
					.show();
			Log.e("Yum", errmsg);
		}
		
		// set up buttons
		((Button)findViewById(R.id.btn_showRestaurantsMenu)).setOnClickListener(this);
		((Button)findViewById(R.id.InLine)).setOnClickListener(this);
		
		// set up reviews box
		listAdapter = new ArrayAdapter<Submission>(this, android.R.layout.simple_list_item_1);
		reviewsListView = (ListView)findViewById(R.id.lstvw_submissionSummary);
		reviewsListView.setAdapter(listAdapter);
		reviewsListView.setOnItemClickListener(this);
		
		
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (selectedRestaurant != null) {
			loadReviews(selectedRestaurant.getRestaurantId());
		}
	}



	private void populateUI() {
		
		this.setTitle(selectedRestaurant.getName() + " Information");
		
		TextView txtView1 = (TextView) findViewById(R.id.txtvw_RestaurantName);
		txtView1.setText(selectedRestaurant.getName());
		TextView txtView2 = (TextView) findViewById(R.id.txtvw_RestaurantRating);
		switch (calculateRestaurantRating()) {
		case 1:
			txtView2.setText("Recently it has been :)");
			break;
		case 0:
			txtView2.setText("Recently it has been :|");
			break;
		case -1:
			txtView2.setText("Recently it has been :(");
			break;
		case -2:
			txtView2.setText("Recently it has been (?)");
			break;
		}
		
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
						loadReviews(selectedRestaurant.getRestaurantId());
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
	
	
	
	private void loadReviews(String restaurantId) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(Restaurant.R_UUID, restaurantId);
		query.orderByDescending("createdAt");
		query.setLimit(5);
		
		try {
			List<ParseObject> objects = query.find();
			listAdapter.clear();
			for (ParseObject po: objects) {
				Submission s = new Submission(po);
				listAdapter.add(s);
			}
			listAdapter.notifyDataSetChanged();
			
		} catch (ParseException e1) {
			// error occurred
			String errmsg = "There was an error loading data from Parse";
			Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg, e1);
		}
		
	}
	
	private int calculateRestaurantRating() {
		
		// for now we will find the mean average of the ratings that are shown in the summary box
		
		double average = 0.0;
		int count = listAdapter.getCount();
		if (count == 0) {
			return -2;
		}
		
		for (int i = 0; i < count; i++) {
			average += listAdapter.getItem(i).getRating();
		}
		average /= count;
		
		// return based on arbitrary bounds
		// -1 to -1/3 is sad
		// -1/3 to 1/3 is okay
		// 1/3 to 1 is happy
		if (average >= 0.333) {
			return 1;
		} else if (average <= -0.333) {
			return -1;
		} else {
			return 0;
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_showRestaurantsMenu:
			Intent intentMenu = new Intent(RestaurantViewActivity.this, MenuViewActivity.class);
			intentMenu.putExtra(Restaurant.R_UUID, selectedRestaurant.getRestaurantId());
			intentMenu.putExtra(Restaurant.R_NAME, selectedRestaurant.getName());
			startActivity(intentMenu);
			break;
		case R.id.lstvw_submissionSummary:
			Intent intentHistory = new Intent(RestaurantViewActivity.this, HistoryViewActivity.class);
			intentHistory.putExtra(Restaurant.R_UUID, selectedRestaurant.getRestaurantId());
			intentHistory.putExtra(Restaurant.R_NAME, selectedRestaurant.getName());
			startActivity(intentHistory);
			break;
		case R.id.InLine:
			Intent intentSubmission = new Intent(RestaurantViewActivity.this, SubmissionActivity.class);
			intentSubmission.putExtra(Restaurant.R_UUID, selectedRestaurant.getRestaurantId());
			intentSubmission.putExtra(Restaurant.R_NAME, selectedRestaurant.getName());
			intentSubmission.putExtra("StartTime", new Date());
			startActivity(intentSubmission);
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		onClick(findViewById(R.id.lstvw_submissionSummary));
		
	}


}
