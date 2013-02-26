package com.cse5236groupthirteen;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class RestaurantView extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Don't touch these two lines!!!
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_view);
		
		final Button InLine = (Button) findViewById(R.id.button1);
		InLine.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.restaurant_view, menu);
		return true;
	}

}
