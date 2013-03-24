package com.cse5236groupthirteen;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
import java.util.Date;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SubmissionActivity extends Activity implements OnClickListener {
	private EditText Customer_Review;
	private Button Got_Food;
	private Date EndTime;
	private Date startTime;
	private RadioGroup RatingGroup;
	private RadioButton RatingButton;

	private String selectedRestaurantId;
	private String selectedRestaurantName;
	private TextView restaurant_name;

	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//create UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		RatingGroup = (RadioGroup)findViewById(R.id.Rating);
		Customer_Review = (EditText)findViewById(R.id.CustomerReview);
		Got_Food = (Button)findViewById(R.id.GotFood);
		Got_Food.setOnClickListener(this);
		restaurant_name=(TextView)findViewById(R.id.restaurant_name_sub);
		Button btnAdd= (Button)findViewById(R.id.SubmitReview);
		btnAdd.setOnClickListener(this);
		///
		startTime = new Date();
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg , Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg);
		}
		restaurant_name.setText("You selected: "+selectedRestaurantName);
		
	}

	
	
	
	private Submission generateFromSubView(){
		String review = this.Customer_Review.getText().toString();
		int Rating = GetRating();
		long WaitTime = (this.EndTime.getTime()-this.startTime.getTime())/1000;
	
		return new Submission(Rating,WaitTime,review,selectedRestaurantId);
		
		
			
	}
	private int GetRating(){
		int r = 0;
		int selectedId = RatingGroup.getCheckedRadioButtonId();
		RatingButton = (RadioButton)findViewById(selectedId);
		String rate = (String) this.RatingButton.getText();
		if (rate=="Good"){
			r = 1;
		} 
		if (rate=="Fair"){
			r = 0;
		} 
		if (rate=="Bad"){
			r = -1;
		}
		return r;
	}
	
	
	
		
		
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to t
		//he action bar if it is present.
		getMenuInflater().inflate(R.menu.submission, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.GotFood:
			this.EndTime = new Date();
			break;	
		case R.id.SubmitReview:
			//code for submit review button
			if (Customer_Review == null){
				Toast.makeText(SubmissionActivity.this, "Missing comments",Toast.LENGTH_SHORT).show();
			}
			else if (EndTime == null){
				Toast.makeText(SubmissionActivity.this, "Please Click 'Got Food'",Toast.LENGTH_SHORT).show();
			}
			else if (RatingGroup.getCheckedRadioButtonId()==-1){
				Toast.makeText(SubmissionActivity.this, "Please Choose Rate",Toast.LENGTH_SHORT).show();
			}
			else {
				Submission s=generateFromSubView();
				ParseObject po = s.toParseObject();
				try {
					po.save();
					String succ = "Submission Saved! Thank You!";
					Toast.makeText(SubmissionActivity.this, succ, Toast.LENGTH_SHORT).show();
				} catch (ParseException e) {
					String errmsg = "Sorry, there was an error processing your submission. Please try again later.";
					Toast.makeText(SubmissionActivity.this, errmsg, Toast.LENGTH_SHORT).show();
				}
				finish();
			}
			
			break;
		
		}
		
	}

}
