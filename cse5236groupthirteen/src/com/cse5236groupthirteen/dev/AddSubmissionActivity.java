package com.cse5236groupthirteen.dev;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.cse5236groupthirteen.R;
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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.view.View.OnClickListener;

import android.widget.AdapterView.OnItemSelectedListener;

public class AddSubmissionActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private Button submitButton;
	private Spinner restaurantSpinner;
	private ArrayAdapter<Restaurant> restaurantListAdapter;
	private int selectedRestaurantPosition = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_submission);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// grab ui elements
		restaurantSpinner = (Spinner)findViewById(R.id.spnr_addsubmission_restaurants);
		submitButton = (Button)findViewById(R.id.btn_addSubmission_submit);
		

		// setup spinner specifics
		restaurantListAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_list_item_1);
		restaurantSpinner.setAdapter(restaurantListAdapter);
		
		// set up listeners
		restaurantSpinner.setOnItemSelectedListener(this);
		submitButton.setOnClickListener(this);
		
		
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
					restaurantListAdapter.clear();
					for (ParseObject po : objects) {
						Restaurant r = new Restaurant(po);
						restaurantListAdapter.add(r);
					}
					restaurantListAdapter.notifyDataSetChanged();

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



	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		this.selectedRestaurantPosition = pos;
	}



	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}



	@Override
	public void onClick(View v) {
		
		Submission sub = generateSubmissionFromUI();
		ParseObject po = sub.toParseObject();
		try {
			po.save();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		clearUI();
		
		
	}
	
	private Submission generateSubmissionFromUI() {
		
		
		Restaurant a = (Restaurant)(restaurantSpinner.getItemAtPosition(this.selectedRestaurantPosition));
		
		EditText etRating = (EditText)findViewById(R.id.et_addsubmission_rating);
		EditText etWaitHours = (EditText)findViewById(R.id.et_addSubmission_waittime_hours);
		EditText etWaitMinutes = (EditText)findViewById(R.id.et_addSubmission_waittime_minutes);
		EditText etWaitSeconds = (EditText)findViewById(R.id.et_addSubmission_waittime_seconds);
		EditText etComments = (EditText)findViewById(R.id.et_addSubmission_waittime_comments);
		
		
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		Number rawRating;
		Number rawHours;
		Number rawMinutes;
		Number rawSeconds;
		try {
			rawRating = formatter.parse(etRating.getText().toString().trim());
			rawHours = formatter.parse(etWaitHours.getText().toString().trim());
			rawMinutes = formatter.parse(etWaitMinutes.getText().toString().trim());
			rawSeconds = formatter.parse(etWaitSeconds.getText().toString().trim());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
		int parsedRating = rawRating.intValue();
		int parsedHours = rawHours.intValue();
		int parsedMinutes = rawMinutes.intValue();
		int parsedSeconds = rawSeconds.intValue();
		
		int rating = parsedRating;
		long waittimeInSecs = parsedSeconds + (60*parsedMinutes) + (60*60*parsedHours);
		String comment = etComments.getText().toString();
		String restaurantId = a.getRestaurantId();
		
		return new Submission(rating, waittimeInSecs, comment, restaurantId);
		
		
	}
	
	private void clearUI() {
		((EditText)findViewById(R.id.et_addsubmission_rating)).setText("");
		((EditText)findViewById(R.id.et_addSubmission_waittime_hours)).setText("");
		((EditText)findViewById(R.id.et_addSubmission_waittime_minutes)).setText("");
		((EditText)findViewById(R.id.et_addSubmission_waittime_seconds)).setText("");
		((EditText)findViewById(R.id.et_addSubmission_waittime_comments)).setText("");
	}


}
