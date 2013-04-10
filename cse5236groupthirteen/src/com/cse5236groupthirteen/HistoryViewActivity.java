package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.utilities.CustomAdapter;
import com.cse5236groupthirteen.utilities.MessageDetail;
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
	
	private CustomAdapter listviewAdapter;
    private ArrayList<MessageDetail> SubList;
	private String selectedRestaurantId;
	private String selectedRestaurantName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_view);

		//listviewAdapter = new ArrayAdapter<Submission>(this, android.R.layout.simple_list_item_1);
		SubList = new ArrayList<MessageDetail>();
		listview = (ListView) findViewById(R.id.lstvw_histView_submissionsList);
		listview.setAdapter(new CustomAdapter(this,SubList));

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
		if(SubList != null){
		updateSubmissionsList();}
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
			    MessageDetail m = new MessageDetail();
			    m.setIcon(s.getRating());
			    m.setComm(s.getComment());
			    m.setWaitingTime(String.valueOf(s.getWaitTime()));
			    m.setTime(s.getHowLongAgoCreatedAsAString());
			    SubList.add(m);	
			}
			////////SubList.notifyDataSetChanged();
			listview.setAdapter(new CustomAdapter(this,SubList));
			dismissLoadingDialog();
		}

		
		
	}

}
