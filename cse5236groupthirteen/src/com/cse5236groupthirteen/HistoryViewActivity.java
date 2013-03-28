package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		Toast.makeText(this, "Updating Submissions", Toast.LENGTH_SHORT).show();
		updateSubmissionsList();
	}

	private void updateSubmissionsList() {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(Submission.S_RESTID, selectedRestaurantId);
		query.orderByDescending("createdAt");
		
		List<ParseObject> submissions = new ArrayList<ParseObject>();
		try {
			submissions = query.find();
		} catch (ParseException e) {
			String errmsg = "Parse had a problem updating submissions";
			YumHelper.handleException(this, e, errmsg);
			return;
		}
		
		// check amount of returned hits
		if (submissions.size() == 0) {
			// no hits :(
			
		} else {
			// we got hits! :)
			for(ParseObject po: submissions) {
				Submission s = new Submission(po);
				listviewAdapter.add(s);
			}
			listviewAdapter.notifyDataSetChanged();
		}
		
	}

}
