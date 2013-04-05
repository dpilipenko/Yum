package com.cse5236groupthirteen;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.Submission;
import java.util.Date;
import com.parse.ParseException;
import com.parse.ParseObject;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SubmissionActivity extends YumViewActivity implements OnClickListener {
	private EditText Customer_Review;
	private Button Got_Food;
	private Date EndTime;
	private Date startTime;
	private ImageView ratingImage;

	private String selectedRestaurantId;
	private String selectedRestaurantName;
	private int currentRating;
	private boolean gotfood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);
		
		Customer_Review = (EditText)findViewById(R.id.CustomerReview);
		
		Got_Food = (Button)findViewById(R.id.GotFood);
		Got_Food.setOnClickListener(this);
		
		Button btnAdd= (Button)findViewById(R.id.SubmitReview);
		btnAdd.setOnClickListener(this);
		
		ratingImage = (ImageView)findViewById(R.id.imgvw_submission_rating);
		ratingImage.setOnClickListener(this);
		
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
			startTime = (Date)b.get("StartTime");
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg , Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg);
		}
		this.setTitle("Submit a review for " + selectedRestaurantName);
		this.currentRating = 1;
		this.gotfood = false;
	}

	
	
	
	private Submission generateFromSubView(){
		String review = this.Customer_Review.getText().toString();
		int rating = getRating();
		long waitTime = (this.EndTime.getTime()-this.startTime.getTime())/1000;
	
		return new Submission(rating,waitTime,review,selectedRestaurantId);
		
		
			
	}
	private int getRating(){
		return currentRating;
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
			gotfood = !gotfood;
			this.EndTime = new Date();
			break;	
		case R.id.imgvw_submission_rating:
			Drawable d = null;
			switch (currentRating) {
			case 1:
				currentRating = 0;
				d = getResources().getDrawable(R.drawable.rating_neutral);
				break;
			case 0:
				currentRating = -1;
				d = getResources().getDrawable(R.drawable.rating_sad);
				break;
			case -1:
				currentRating = 1;
				d = getResources().getDrawable(R.drawable.rating_happy);
				break;
			}
			if (d != null) {
				ratingImage.setImageDrawable(d);
			}
			break;
		case R.id.SubmitReview:
			//code for submit review button
			if (Customer_Review == null){
				Toast.makeText(SubmissionActivity.this, "Missing comments",Toast.LENGTH_SHORT).show();
			}
			else if (!gotfood){
				Toast.makeText(SubmissionActivity.this, "Please Click 'Got Food'",Toast.LENGTH_SHORT).show();
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
