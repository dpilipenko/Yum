package com.cse5236groupthirteen.dev;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.R.id;
import com.cse5236groupthirteen.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
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
		
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.btn_callAddRestaurantActivity:
			this.startActivity(new Intent(this, AddRestaurantActivity.class));
			break;
		default:
			break;
		}
		
	}

}
