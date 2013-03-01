package com.cse5236groupthirteen;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

	private EditText counterField;
	private Button resetButton;
	private Button pumpButton;
	private int count;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		counterField = (EditText) findViewById(R.id.text_counter);
		resetButton = (Button) findViewById(R.id.button_reset);
		pumpButton = (Button) findViewById(R.id.button_pump);
		resetButton.setOnClickListener(this);
		pumpButton.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuItem menuDevPage = menu.add("Dev Page");
		Intent intentDevPage = new Intent(this, DevActivity.class);
		menuDevPage.setIntent(intentDevPage);
		
		return true;
	}

	@Override
	protected void onDestroy() {
		Log.v("MainActivity", "CSE5236 - onDestroy is called.");
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		Log.v("MainActivity", "CSE5236 - onStart is called.");
		super.onStart();
	}

	@Override
	protected void onStop() {
		Log.v("MainActivity", "CSE5236 - onStop is called.");
		super.onStop();
	}

	@Override
	protected void onPause() {
		
		// add one to counter, save new value
		count++;
		SharedPreferences.Editor editor = getPreferences(0).edit();
		editor.putInt("count", count);
		editor.commit();
		
		Log.v("MainActivity", "CSE5236 - onPause is called.");
		
		super.onPause();		
	}

	@Override
	protected void onResume() {

		// if old value exists, load value
		// else set 0
		count = getPreferences(0).getInt("count", 0);
		counterField.setText(""+count);
		
		Log.v("MainActivitiy", "CSE5236 - onResume is called");
		
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.button_reset:
			count = 0;
			break;
		case R.id.button_pump:
			count++;
			break;
		}
		
		counterField.setText(""+count);
		
	}

}
