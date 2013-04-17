package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;

/**
 * This Activity is responsible for all of our saved submissions for a particular restaurant
 *
 */
public class HistoryViewActivity extends YumViewActivity {

	// UI elements
	private ListView mListview;
	
	
    private ArrayList<MessageDetail> mSubList;
	private String mSelectedRestaurantId;
	private String mSelectedRestaurantName;
	private boolean mIsQuerying;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_view);

		// set initial values
		mIsQuerying = false; 
		mSubList = new ArrayList<MessageDetail>();
		mListview = (ListView) findViewById(R.id.lstvw_histView_submissionsList);
		mListview.setAdapter(new CustomAdapter(this,mSubList));

		// gets restaurant information that was passed over from previous activity
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mSelectedRestaurantId = b.getString(YumRestaurant.R_UUID);
			mSelectedRestaurantName = b.getString(YumRestaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			YumHelper.handleError(this, errmsg);
		}

		this.setTitle(mSelectedRestaurantName);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(hasInternet() && (mSubList != null)){
			updateHistoryList();
		}
	}
	
	
	@Override
	public void onShake() {
		if (hasInternet() && !mIsQuerying) {
			updateHistoryList();
		}
	}

	private void updateHistoryList() {
		
		mIsQuerying = true;
		showLoadingDialog();
		
		// query for all submissions for a given restaurant, sorted newest first.
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(YumSubmission.S_RESTID, mSelectedRestaurantId);
		query.orderByDescending("createdAt");
				
		query.findInBackground(new FindCallback() {
			
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				
				mIsQuerying = false;
				dismissLoadingDialog();
				
				if (e == null) {
					
					// check amount of returned hits
					if (objects.size() == 0) {
						// no hits :(
						
					} else {
						// we got hits! :)
						mSubList.clear();
						
						for (ParseObject po : objects) {
							YumSubmission s = new YumSubmission(po);
							MessageDetail m = new MessageDetail();
							m.setIcon(s.getRating());
							m.setComm(s.getComment());
							m.setWaitingTime(String.valueOf(s.getWaitTime()));
							m.setTime(s.getHowLongAgoCreatedAsAString());
							mSubList.add(m);
						}
						
						mListview.setAdapter(new CustomAdapter(HistoryViewActivity.this,mSubList));
						
					}
					
				} else {
					String errmsg = "There was an error loading submissions from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}
				
			}
			
		});

		
		
	}

}
