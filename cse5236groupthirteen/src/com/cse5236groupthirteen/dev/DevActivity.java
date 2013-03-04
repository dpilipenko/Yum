package com.cse5236groupthirteen.dev;

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
		btnAddRestaurant.setOnClickListener(this);
		Button btnPrintRestaurants = (Button)findViewById(R.id.btn_callPrintRestaurantsActivity);
		btnPrintRestaurants.setOnClickListener(this);
		
		
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		
		case R.id.btn_callAddRestaurantActivity:
			this.startActivity(new Intent(this, AddRestaurantActivity.class));
			break;
		case R.id.btn_callPrintRestaurantsActivity:
			this.startActivity(new Intent(this, PrintRestaurantsActivity.class));
			break;
			
		default:
			break;
		}
		
	}

}
