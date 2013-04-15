package com.cse5236groupthirteen.protectedactivities;

import java.util.ArrayList;
import java.util.List;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This activity is responsible for displaying all the submissions
 * associated with the selected restaurant
 *
 */
public class PrintSubmissionsActivity extends Activity implements
		OnItemSelectedListener {

	private Spinner mRestaurantSpinner;
	private ListView mSubmissionsListview;
	private ArrayAdapter<YumRestaurant> mRestaurantAdapter;
	private ArrayAdapter<YumSubmission> mSubmissionsAdapter;
	private int mSelectedPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_print_submissions);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);

		// grab ui elements
		mRestaurantSpinner = (Spinner) findViewById(R.id.spnr_submissionsview_restaurant);
		mSubmissionsListview = (ListView) findViewById(R.id.lstvw_submissionsview_submissionslist);

		// setup spinner specifics
		mRestaurantAdapter = new ArrayAdapter<YumRestaurant>(this,
				android.R.layout.simple_spinner_item);
		mRestaurantSpinner.setAdapter(mRestaurantAdapter);

		// setup list view specifics
		mSubmissionsAdapter = new ArrayAdapter<YumSubmission>(this,
				android.R.layout.simple_list_item_1);
		mSubmissionsListview.setAdapter(mSubmissionsAdapter);

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
		query.whereExists("objectId"); // all Parse objects contain this key, so
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

	private void loadSubmissionsFromParse() {
		// get selected restaurant
		YumRestaurant a = (YumRestaurant) (mRestaurantSpinner.getItemAtPosition(mSelectedPosition));

		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(YumSubmission.S_RESTID, a.getRestaurantId());

		// query parse
		List<ParseObject> submissions = new ArrayList<ParseObject>();
		try {
			submissions = query.find();
		} catch (ParseException e) {
			String errmsg = "Failed on fetching submissions";
			YumHelper.handleException(this, e, errmsg);
			return;
		}

		// check amount of returned hits
		if (submissions.size() == 0) {
			// no hits :(

		} else {
			// we got hits! :)
			for (ParseObject po : submissions) {
				YumSubmission s = new YumSubmission(po);
				mSubmissionsAdapter.add(s);
			}
			mSubmissionsAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		mSelectedPosition = pos;
		mSubmissionsAdapter.clear();
		loadSubmissionsFromParse();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

}
