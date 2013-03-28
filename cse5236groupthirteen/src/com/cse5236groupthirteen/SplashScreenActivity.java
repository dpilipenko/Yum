package com.cse5236groupthirteen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {

	@SuppressWarnings("unused")
	private ImageView imgvw_splashImage;

	private boolean isBackButtonPressed;
	private static final int SPLASH_DURATION = 2000;

	@Override
	public void onBackPressed() {
		isBackButtonPressed = true;
		super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		isBackButtonPressed = false;
		imgvw_splashImage = (ImageView) findViewById(R.id.imgvw_splashscreen_image);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				finish();
				if (!isBackButtonPressed) {
					Intent intent = new Intent(SplashScreenActivity.this,
							HomeViewActivity.class);
					startActivity(intent);
				}

			}
		}, SPLASH_DURATION);
	}

}
