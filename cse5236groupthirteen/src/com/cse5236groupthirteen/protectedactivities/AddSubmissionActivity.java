package com.cse5236groupthirteen.protectedactivities;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.models.YumRestaurant;
import com.cse5236groupthirteen.models.YumSubmission;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumHelper;
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

/**
 * This activity allows for a registered user to add a Submission for a restaurant
 *
 */
public class AddSubmissionActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private Button mSubmitButton;
	private Spinner mRestaurantSpinner;
	private ArrayAdapter<YumRestaurant> mRestaurantListAdapter;
	private int mSelectedRestaurantPosition = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_add_submission);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// grab ui elements
		mRestaurantSpinner = (Spinner)findViewById(R.id.spnr_addsubmission_restaurants);
		mSubmitButton = (Button)findViewById(R.id.btn_addsubmission_savesubmissiontoparse);
		

		// setup spinner specifics
		mRestaurantListAdapter = new ArrayAdapter<YumRestaurant>(this, android.R.layout.simple_list_item_1);
		mRestaurantSpinner.setAdapter(mRestaurantListAdapter);
		
		// set up listeners
		mRestaurantSpinner.setOnItemSelectedListener(this);
		mSubmitButton.setOnClickListener(this);
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadAvailableRestaurantsFromParse();
	}

	/**
	 * This method loads all available restaurants from our back-end
	 */
	private void loadAvailableRestaurantsFromParse() {

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId"); 
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					mRestaurantListAdapter.clear();
					for (ParseObject po : objects) {
						YumRestaurant r = new YumRestaurant(po);
						mRestaurantListAdapter.add(r);
					}
					mRestaurantListAdapter.notifyDataSetChanged();

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
		this.mSelectedRestaurantPosition = pos;
	}



	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}



	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_addsubmission_savesubmissiontoparse:
			YumSubmission sub = generateSubmissionFromUI();
			ParseObject po = sub.toParseObject();
			try {
				po.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			clearUI();
			break;
		}
		
	}
	
	private YumSubmission generateSubmissionFromUI() {
		
		
		YumRestaurant selectedRestaurant = 
			(YumRestaurant)(mRestaurantSpinner.getItemAtPosition(this.mSelectedRestaurantPosition));
		
		// UI elements
		EditText etRating = (EditText)findViewById(R.id.et_addsubmission_rating);
		EditText etWaitHours = (EditText)findViewById(R.id.et_addsubmission_waittimehours);
		EditText etWaitMinutes = (EditText)findViewById(R.id.et_addsubmission_waittimeminutes);
		EditText etWaitSeconds = (EditText)findViewById(R.id.et_addsubmission_waittimeseconds);
		EditText etComments = (EditText)findViewById(R.id.et_addsubmission_waittimecomments);
		
		// UI strings
		String strRating = etRating.getText().toString();
		String strWaitHours = etWaitHours.getText().toString();
		String strWaitMinutes = etWaitMinutes.getText().toString();
		String strWaitSeconds = etWaitSeconds.getText().toString();
		
		// wait times handling
		if (strWaitHours.isEmpty())
			strWaitHours = "0";
		if (strWaitMinutes.isEmpty())
			strWaitMinutes = "0";
		if (strWaitSeconds.isEmpty())
			strWaitMinutes = "0";
		
		// parse strings to Numbers
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		Number rawRating;
		Number rawHours;
		Number rawMinutes;
		Number rawSeconds;
		try {
			rawRating = formatter.parse(strRating.trim());
			rawHours = formatter.parse(strWaitHours.trim());
			rawMinutes = formatter.parse(strWaitMinutes.trim());
			rawSeconds = formatter.parse(strWaitSeconds.trim());
		} catch (java.text.ParseException e) {
			String errmsg = "There was a problem parsing wait time";
			YumHelper.handleError(this, errmsg);
			return null;
		}
		// parsed integer values
		int parsedRating = rawRating.intValue();
		int parsedHours = rawHours.intValue();
		int parsedMinutes = rawMinutes.intValue();
		int parsedSeconds = rawSeconds.intValue();
	
		// final values
		int rating = parsedRating;
		long waittimeInSecs = parsedSeconds + (60*parsedMinutes) + (60*60*parsedHours);
		String comment = etComments.getText().toString();
		String restaurantId = selectedRestaurant.getRestaurantId();
		
		return new YumSubmission(rating, waittimeInSecs, comment, restaurantId);
		
		
	}
	
	private void clearUI() {
		((EditText)findViewById(R.id.et_addsubmission_rating)).setText("");
		((EditText)findViewById(R.id.et_addsubmission_waittimehours)).setText("");
		((EditText)findViewById(R.id.et_addsubmission_waittimeminutes)).setText("");
		((EditText)findViewById(R.id.et_addsubmission_waittimeseconds)).setText("");
		((EditText)findViewById(R.id.et_addsubmission_waittimecomments)).setText("");
	}


}
