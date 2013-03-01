package com.cse5236groupthirteen;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Submission extends Activity implements OnClickListener {
	private EditText Customer_Review;
	private Button Got_In_Line;
	private Button Got_Food;
	private Button Submit_Review;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);
		
		Customer_Review = (EditText)findViewById(R.id.CustomerReview);
		Got_In_Line = (Button)findViewById(R.id.GotInLine);
		Got_Food = (Button)findViewById(R.id.GotFood);
		View btnAdd= (Button)findViewById(R.id.SubmitReview);
		btnAdd.setOnClickListener(this);
		View btnBack= (Button)findViewById(R.id.Sub_back_button);
		btnBack.setOnClickListener(this);
		
	}
	
	private void InLine(){
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to t
		//he action bar if it is present.
		getMenuInflater().inflate(R.menu.submission, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.SubmitReview:
			//code for submit review button
			break;
			
		case R.id.Sub_back_button:
			//code for back button
			break;
		
		}
		
	}

}
