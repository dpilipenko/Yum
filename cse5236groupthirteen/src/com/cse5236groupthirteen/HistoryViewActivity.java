package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HistoryViewActivity extends YumViewActivity {

	private ListView listview;
	
	private ArrayAdapter<Submission> listviewAdapter;

	private String selectedRestaurantId;
	private String selectedRestaurantName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_view);

		listviewAdapter = new ArrayAdapter<Submission>(this, android.R.layout.simple_list_item_1);
		listview = (ListView) findViewById(R.id.lstvw_histView_submissionsList);
		listview.setAdapter(listviewAdapter);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			YumHelper.handleError(this, errmsg);
		}

		this.setTitle(selectedRestaurantName);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateSubmissionsList();
	}
	
	
	@Override
	public void onShake() {
		updateSubmissionsList();
	}

	private void updateSubmissionsList() {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(Submission.S_RESTID, selectedRestaurantId);
		query.orderByDescending("createdAt");
		
		showLoadingDialog();
		query.findInBackground(new FindCallback() {
			
			public void done(List<ParseObject> objects, ParseException e) {
				dismissLoadingDialog();
				if (e == null) {
					listviewAdapter.clear();
					
					if (objects.isEmpty()) {
						// no hits :(
						
					} else {
						// we got hits! :)
						for (ParseObject po: objects) {
							Submission s = new Submission(po);
							listviewAdapter.add(s);
						}
						listviewAdapter.notifyDataSetChanged();
					}
					
					
				} else {
					String errmsg = "There was an error loading data from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}
			}
			
		});
		
		
	}

}
