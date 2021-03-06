package com.cse5236groupthirteen.protectedactivities;

import com.cse5236groupthirteen.HomeViewActivity;
import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.models.YumUser;
import com.cse5236groupthirteen.utilities.YumHelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * This activity provides functionality to add restaurants, menus etc.
 * This is protected by successfully logging in through the LoginActivity.
 * If a user is an Admin, they get extra functionality like the ability
 * to create more users and restaurant submissions
 *
 */
public class DevActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev);
		
		// load selected restaurant information
		boolean isAdmin = false;
		Bundle b = getIntent().getExtras();
		if (b != null) {
			isAdmin = b.getBoolean(YumUser.US_ISADMIN);
		} else {
			String errmsg = "There was an error Loginnig in. Sorry";
			YumHelper.handleError(this, errmsg);
			finish();
		}
			
		
		Button btnAddUser = (Button)findViewById(R.id.btn_callAddUserActivity);
		Button btnAddRestaurant = (Button)findViewById(R.id.btn_callAddRestaurantActivity);
		Button btnAddMenu = (Button)findViewById(R.id.btn_callAddMenuActivity);
		Button btnAddSubmission = (Button)findViewById(R.id.btn_callAddSubmissionActivity);
		Button btnPrintRestaurants = (Button)findViewById(R.id.btn_callPrintRestaurantsActivity);
		Button btnPrintMenuItems = (Button)findViewById(R.id.btn_callPrintMenuItemsActivity);
		Button btnPrintSubmissions = (Button)findViewById(R.id.btn_callPrintSubmissionsActivity);
		Button btnUpdateRestaurantActivites = (Button)findViewById(R.id.btn_callUpdateGeoLocationsActivity);
		Button btnGotoHome = (Button)findViewById(R.id.btn_gotoHomeView);
		
		if (!isAdmin) {
			btnAddUser.setVisibility(View.GONE);
			btnAddSubmission.setVisibility(View.GONE);
			btnUpdateRestaurantActivites.setVisibility(View.GONE);
		} else { 
			btnAddUser.setOnClickListener(this);
			btnAddSubmission.setOnClickListener(this);
			btnUpdateRestaurantActivites.setOnClickListener(this);
		}
		
		btnAddRestaurant.setOnClickListener(this);
		btnAddMenu.setOnClickListener(this);
		btnPrintRestaurants.setOnClickListener(this);
		btnPrintMenuItems.setOnClickListener(this);
		btnPrintSubmissions.setOnClickListener(this);
		btnGotoHome.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.btn_callAddUserActivity:
			this.startActivity(new Intent(this, AddUserActivity.class));
			break;
		case R.id.btn_callAddRestaurantActivity:
			this.startActivity(new Intent(this, AddRestaurantActivity.class));
			break;
		case R.id.btn_callAddMenuActivity:
			this.startActivity(new Intent(this, AddMenuActivity.class));
			break;
		case R.id.btn_callAddSubmissionActivity:
			this.startActivity(new Intent(this, AddSubmissionActivity.class));
			break;
		case R.id.btn_callPrintRestaurantsActivity:
			this.startActivity(new Intent(this, PrintRestaurantsActivity.class));
			break;
		case R.id.btn_callPrintMenuItemsActivity:
			this.startActivity(new Intent(this, PrintMenuActivity.class));
			break;
		case R.id.btn_callPrintSubmissionsActivity:
			this.startActivity(new Intent(this, PrintSubmissionsActivity.class));
			break;
		case R.id.btn_gotoHomeView:
			this.startActivity(new Intent(this, HomeViewActivity.class));
			break;
			
		default:
			Toast.makeText(this, "Rogue Button Clicked", Toast.LENGTH_LONG).show();
			break;
		}
		
	}

}
