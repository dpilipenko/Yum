package com.cse5236groupthirteen.dev;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.R.layout;
import com.cse5236groupthirteen.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AddRestaurantActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_restaurant);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_restaurant, menu);
		return true;
	}

}
