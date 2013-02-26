package com.cse5236groupthirteen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

	private boolean isBackButtonPressed = false;
	private static final int SPLASH_DURATION = 2000;
	
	@Override
	public void onBackPressed() {
		isBackButtonPressed = true;
		super.onBackPressed();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_screen);
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable(){

			//boobs
			@Override
			public void run() {
				
				finish();
				if(!isBackButtonPressed) {
					Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
					startActivity(intent);
				}
				
			}}, SPLASH_DURATION);
	}
	
	
	
}
