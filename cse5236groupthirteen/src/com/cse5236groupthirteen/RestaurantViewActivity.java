package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cse5236groupthirteen.models.YumMenuItem;
import com.cse5236groupthirteen.models.YumRestaurant;
import com.cse5236groupthirteen.models.YumSubmission;
import com.cse5236groupthirteen.utilities.CustomAdapter;
import com.cse5236groupthirteen.utilities.MessageDetail;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.content.Intent;

import android.text.Html;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This activity is responsible for displaying all of the restaurant information
 * 
 * 
 */
public class RestaurantViewActivity extends YumViewActivity implements
		OnClickListener, OnItemClickListener {

	// UI elements
	private TextView txtvw_restaurantName;
	private TextView txtvw_restaurantAddress;
	private TextView txtvw_restaurantPhone;
	private TextView txtvw_restaurantWebsite;
	private TextView txtvw_restaurantRating;
	private Button btn_callMenuViewActivity;
	private ListView lstvw_recentSubmissions;
	private Button btn_callSubmissionViewActivity;

	private ArrayList<MessageDetail> mSubmissionsList;
	private YumRestaurant mSelectedRestaurant;
	private boolean mQuerying;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_view);

		mQuerying = false;

		// set up menu button
		btn_callMenuViewActivity = (Button) findViewById(R.id.btn_restview_callmenuviewactivity);
		btn_callMenuViewActivity.setOnClickListener(this);

		// set up submission button
		btn_callSubmissionViewActivity = (Button) findViewById(R.id.btn_restview_callsubmissionviewactivity);
		btn_callSubmissionViewActivity.setOnClickListener(this);

		// set up reviews box
		mSubmissionsList = new ArrayList<MessageDetail>();
		lstvw_recentSubmissions = (ListView) findViewById(R.id.lstvw_restview_submissionsummary);
		lstvw_recentSubmissions.setAdapter(new CustomAdapter(this, mSubmissionsList));
		lstvw_recentSubmissions.setOnItemClickListener(this);
		
		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			String selectedRestaurantId = b.getString(YumRestaurant.R_UUID);
			loadRestaurant(selectedRestaurantId);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			YumHelper.handleError(this, errmsg);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (hasInternet()) {
			if ((mSelectedRestaurant != null) && (!mSubmissionsList.isEmpty())) {
				loadSubmissions(mSelectedRestaurant.getRestaurantId());
			}
		}
	}

	@Override
	public void onShake() {
		if (hasInternet() && !mQuerying) {
			loadSubmissions(mSelectedRestaurant.getRestaurantId());
		}
	}

	/**
	 * This method loads information about the selected restaurant (name, address etc.)
	 * and populates the UI
	 */
	private void loadRestaurantInformation() {
		this.setTitle(mSelectedRestaurant.getName() + " Information");

		txtvw_restaurantName = (TextView) findViewById(R.id.txtvw_restview_restaurantname);
		txtvw_restaurantName.setText(mSelectedRestaurant.getName());

		txtvw_restaurantAddress = (TextView) findViewById(R.id.txtvw_restview_restaurantaddress);
		txtvw_restaurantAddress.setText(mSelectedRestaurant.getFullAddress());

		txtvw_restaurantPhone = (TextView) findViewById(R.id.txtvw_restview_restaurantphonenumber);
		txtvw_restaurantPhone.setText(mSelectedRestaurant.getPhoneNumber());

		txtvw_restaurantWebsite = (TextView) findViewById(R.id.txtvw_restview_restaurantwebsite);
		txtvw_restaurantWebsite.setText(Html.fromHtml(mSelectedRestaurant
				.getWebsite()));

		if (doesRestaurantHaveMenu(mSelectedRestaurant.getRestaurantId())) {
			btn_callMenuViewActivity.setEnabled(true);
		} else {
			btn_callMenuViewActivity.setEnabled(false);
		}
	}

	/**
	 * This method checks to see if the restaurant has any menu items associated with it
	 * @param restaurantId	The UUID of selected restaurant
	 * @return	if restaurant has menu items
	 */
	private boolean doesRestaurantHaveMenu(String restaurantId) {

		// fetch all menu items associated with restaurant
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		query.whereEqualTo(YumMenuItem.MI_RESTID, restaurantId);
		try {
			int count = query.count();
			if (count > 0) {
				// there ARE menu items for this restaurant
				return true;
			} else {
				// there are NO menu items for this restaurant
				return false;
			}
		} catch (ParseException e) {
			// error occurred
			String errmsg = "There was an error loading data from Parse";
			YumHelper.handleException(this, e, errmsg);
			return false;
		}

	}

	/**
	 * This method loads the selected restaurant's information and submissions
	 * @param restaurantId
	 */
	private void loadRestaurant(String restaurantId) {

		mQuerying = true;
		showLoadingDialog();

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereEqualTo(YumRestaurant.R_UUID, restaurantId);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				mQuerying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					if (objects.size() == 1) {
						// id is unique and there should only be one result
						ParseObject po = objects.get(0);
						mSelectedRestaurant = new YumRestaurant(po);
						
						loadRestaurantInformation();
						loadSubmissions(mSelectedRestaurant.getRestaurantId());
						
					} else {
						String errmsg = "Parse returned multiple objects";
						YumHelper.handleException(getParent(), e, errmsg);
						YumHelper.displayAlert(RestaurantViewActivity.this,
								errmsg);
					}
				} else {
					String errmsg = "There was an error loading data from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}

			}

		});

	}

	/**
	 * This method loads the selected restaurants 5 most recent submissions
	 * @param restaurantId
	 */
	private void loadSubmissions(String restaurantId) {

		mQuerying = true;
		showLoadingDialog();

		// query for 5 most recent submissions associated with restaurant
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(YumSubmission.S_RESTID, restaurantId);
		query.orderByDescending("createdAt");
		query.setLimit(5);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				mQuerying = false;
				dismissLoadingDialog();
				if (e == null) {

					// check amount of returned hits
					if (objects.size() == 0) {
						// no hits :(

					} else {
						// we got hits! :)
						mSubmissionsList.clear();
						for (ParseObject po : objects) {

							YumSubmission s = new YumSubmission(po);
							MessageDetail m = new MessageDetail();
							m.setIcon(s.getRating());
							m.setComm(s.getComment());
							m.setWaitingTime(String.valueOf(s.getWaitTime()));
							m.setTime(s.getHowLongAgoCreatedAsAString());
							mSubmissionsList.add(m);
						}

						// update list of submissions
						lstvw_recentSubmissions.setAdapter(new CustomAdapter(
								RestaurantViewActivity.this, mSubmissionsList));

						// recalculate and update restaurant rating
						txtvw_restaurantRating = (TextView) findViewById(R.id.txtvw_restview_restaurantrating);
						switch (calculateRestaurantRating()) {
						case 1:
							txtvw_restaurantRating.setText("Recently it has been :)");
							break;
						case 0:
							txtvw_restaurantRating.setText("Recently it has been :|");
							break;
						case -1:
							txtvw_restaurantRating.setText("Recently it has been :(");
							break;
						case -2:
							// no available ratings
							txtvw_restaurantRating.setText("Recently it has been (?)");
							break;
						}
					}

				} else {
					String errmsg = "There was an error loading submissions from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}

			}

		});

	}

	/**
	 * This method calculates a rating from the recent submissions
	 * @return
	 */
	private int calculateRestaurantRating() {

		double mean = 0.0;
		int count = mSubmissionsList.size();
		if (count == 0) {
			return -2;
		}

		for (int i = 0; i < count; i++) {
			mean += mSubmissionsList.get(i).getIcon();
		}
		mean /= count;

		// return based on arbitrary bounds
		// 1 to 1/3 is happy 
		// 1/3 to -1/3 is okay
		// -1/3 to -1 is sad 

		if (mean >= 0.333) {
			return 1;
		} else if (mean <= -0.333) {
			return -1;
		} else {
			return 0;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btn_restview_callmenuviewactivity: // show menu
			Intent intentMenu = new Intent(RestaurantViewActivity.this, MenuViewActivity.class);
			intentMenu.putExtra(YumRestaurant.R_UUID, mSelectedRestaurant.getRestaurantId());
			intentMenu.putExtra(YumRestaurant.R_NAME, mSelectedRestaurant.getName());
			startActivity(intentMenu);
			break;
			
		case R.id.lstvw_restview_submissionsummary: // show history
			Intent intentHistory = new Intent(RestaurantViewActivity.this,
					HistoryViewActivity.class);
			intentHistory.putExtra(YumRestaurant.R_UUID,
					mSelectedRestaurant.getRestaurantId());
			intentHistory.putExtra(YumRestaurant.R_NAME,
					mSelectedRestaurant.getName());
			startActivity(intentHistory);
			break;
			
		case R.id.btn_restview_callsubmissionviewactivity: // submit review
			Intent intentSubmission = new Intent(RestaurantViewActivity.this,
					SubmissionViewActivity.class);
			intentSubmission.putExtra(YumRestaurant.R_UUID,
					mSelectedRestaurant.getRestaurantId());
			intentSubmission.putExtra(YumRestaurant.R_NAME,
					mSelectedRestaurant.getName());
			intentSubmission.putExtra("StartTime", new Date());
			startActivity(intentSubmission);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		onClick(findViewById(R.id.lstvw_restview_submissionsummary));
	}

}
