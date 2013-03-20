package com.cse5236groupthirteen.dev;

import com.cse5236groupthirteen.HomeViewActivity;
import com.cse5236groupthirteen.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DevActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dev);
		
		Button btnAddRestaurant = (Button)findViewById(R.id.btn_callAddRestaurantActivity);
		Button btnAddMenu = (Button)findViewById(R.id.btn_callAddMenuActivity);
		Button btnPrintRestaurants = (Button)findViewById(R.id.btn_callPrintRestaurantsActivity);
		Button btnGotoHome = (Button)findViewById(R.id.btn_gotoHomeView);
		Button btnPrintMenuItems = (Button)findViewById(R.id.btn_callPrintMenuItemsActivity);
		btnAddRestaurant.setOnClickListener(this);
		btnAddMenu.setOnClickListener(this);
		btnPrintRestaurants.setOnClickListener(this);
		btnGotoHome.setOnClickListener(this);
		btnPrintMenuItems.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.btn_callAddRestaurantActivity:
			this.startActivity(new Intent(this, AddRestaurantActivity.class));
			break;
		case R.id.btn_callAddMenuActivity:
			this.startActivity(new Intent(this, AddMenuActivity.class));
			break;
		case R.id.btn_callPrintRestaurantsActivity:
			this.startActivity(new Intent(this, PrintRestaurantsActivity.class));
			break;
		case R.id.btn_gotoHomeView:
			this.startActivity(new Intent(this, HomeViewActivity.class));
			break;
		case R.id.btn_callPrintMenuItemsActivity:
			this.startActivity(new Intent(this, PrintMenuActivity.class));
			break;
			
		default:
			break;
		}
		
	}

}
