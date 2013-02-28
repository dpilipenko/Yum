package com.cse5236groupthirteen;

import com.wiley.fordummies.androidsdk.tictactoe.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class Submission extends Activity {
	private EditText Customer_Review;
	private Button Got_In_Line;
	private Button Got_Food;
	private Button Submit_Review;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submission);
		
		Customer_Review = (EditText)findViewByID(R.id.CustomerReview);
		Got_In_Line = (Button)findViewByID(R.id.GotInLine);
		Got_Food = (Button)findViewByID(R.id.GotFood);
		View btnAdd= (Button)findViewById(R.id.SubmitReview);
		btnAdd.setOnClickListener(this);
		View btnAdd= (Button)findViewByID(R.id.Sub_back_button);
		btnAdd.setOnClickListener(this);
		
	}
	
	private void InLine(){
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to t
		he action bar if it is present.
		getMenuInflater().inflate(R.menu.submission, menu);
		return true;
	}

}
