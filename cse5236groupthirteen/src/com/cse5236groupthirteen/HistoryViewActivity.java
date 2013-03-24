package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryViewActivity extends Activity {

	private TextView textView;
	private ListView listview;
	private ArrayAdapter<Submission> listviewAdapter;

	private String selectedRestaurantId;
	private String selectedRestaurantName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_view);
		// this is necessary to call in order to use Parse, Parse recommends
		// keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);

		// grab ui elements
		textView = (TextView) findViewById(R.id.txtvw_histView_restaurantName);
		listview = (ListView) findViewById(R.id.lstvw_histView_submissionsList);

		// setup list view specifics
		listviewAdapter = new ArrayAdapter<Submission>(this, android.R.layout.simple_list_item_1);
		listview.setAdapter(listviewAdapter);

		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg, Toast.LENGTH_SHORT)
					.show();
			Log.e("Yum", errmsg);
		}

		textView.setText("You selected: " + selectedRestaurantName);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateSubmissionsList();
	}

	private void updateSubmissionsList() {
		
		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_SUBMISSIONS);
		query.whereEqualTo(Submission.S_RESTID, selectedRestaurantId);
		query.orderByDescending("createdAt");
		
		// query parse
		List<ParseObject> submissions = new ArrayList<ParseObject>();
		try {
			submissions = query.find();
		} catch (ParseException e) {
			// error with parse occurred
			e.printStackTrace();
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
