package com.cse5236groupthirteen;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.YumRestaurant;
import com.cse5236groupthirteen.utilities.YumSubmission;

import java.util.Date;
import com.parse.ParseException;
import com.parse.ParseObject;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This activity is responsible for allowing user submissions.
 * Expected use case is user reaching line in restaurant,
 * tapping submit on previous screen, this screen shows up,
 * and when they finish standing in line, they tap Done. 
 * The time difference is the wait time.
 *
 */
public class SubmissionViewActivity extends YumViewActivity implements OnClickListener {
	
	// UI elements
	private EditText Customer_Review;
	private Button Got_Food;
	private ImageView RatingImage;
	
	private Date mEndTime;
	private Date mStartTime;
	private String mSelectedRestaurantId;
	private String mSelectedRestaurantName;
	private int mCurrentRating;
	private boolean mGotfood;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission_view);
		
		// comments text box
		Customer_Review = (EditText)findViewById(R.id.CustomerReview);
		
		// "I Got my Food" button
		Got_Food = (Button)findViewById(R.id.GotFood);
		Got_Food.setOnClickListener(this);
		
		// "Submit" button
		Button btnAdd= (Button)findViewById(R.id.SubmitReview);
		btnAdd.setOnClickListener(this);
		
		// rating image
		RatingImage = (ImageView)findViewById(R.id.imgvw_submission_rating);
		RatingImage.setOnClickListener(this);
		
		// load any passed over information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mSelectedRestaurantId = b.getString(YumRestaurant.R_UUID);
			mSelectedRestaurantName = b.getString(YumRestaurant.R_NAME);
			mStartTime = (Date)b.get("StartTime");
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg , Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg);
		}
		
		// set init values
		this.setTitle("Submit a review for " + mSelectedRestaurantName);
		this.mCurrentRating = 1;
		this.mGotfood = false;
	}

	
	
	/**
	 * This method is responsible for generating a new Submission object from the
	 * current state of UI
	 * @return
	 */
	private YumSubmission generateSubmissionFromUI(){
		String review = this.Customer_Review.getText().toString();
		int rating = getRating();
		long waitTime = (this.mEndTime.getTime()-this.mStartTime.getTime())/1000;
	
		return new YumSubmission(rating,waitTime,review,mSelectedRestaurantId);
			
	}
	
	private int getRating(){
		return mCurrentRating;
	}
	
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.GotFood: // "Got My Food" button
			mGotfood = !mGotfood;
			this.mEndTime = new Date();
			break;	
			
		case R.id.imgvw_submission_rating: // Rating smiley face
			Drawable d = null;
			switch (mCurrentRating) {
			case 1:
				mCurrentRating = 0;
				d = getResources().getDrawable(R.drawable.rating_neutral);
				break;
			case 0:
				mCurrentRating = -1;
				d = getResources().getDrawable(R.drawable.rating_sad);
				break;
			case -1:
				mCurrentRating = 1;
				d = getResources().getDrawable(R.drawable.rating_happy);
				break;
			}
			if (d != null) {
				RatingImage.setImageDrawable(d);
			}
			break;
			
		case R.id.SubmitReview: // submit review button
			if (!mGotfood){
				Toast.makeText(SubmissionViewActivity.this, "Please Click 'Got Food'",Toast.LENGTH_SHORT).show();
			}
			else {
				YumSubmission s=generateSubmissionFromUI();
				ParseObject po = s.toParseObject();
				try {
					po.save();
					String succ = "Submission Saved! Thank You!";
					Toast.makeText(SubmissionViewActivity.this, succ, Toast.LENGTH_SHORT).show();
				} catch (ParseException e) {
					String errmsg = "Sorry, there was an error processing your submission. Please try again later.";
					Toast.makeText(SubmissionViewActivity.this, errmsg, Toast.LENGTH_SHORT).show();
				}
				finish();
			}
			
			break;
		
		}
		
	}

}
