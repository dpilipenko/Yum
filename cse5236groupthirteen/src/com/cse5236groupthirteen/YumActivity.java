package com.cse5236groupthirteen;

import com.cse5236groupthirteen.utilities.ParseHelper;
import com.parse.Parse;

import android.app.Activity;
import android.os.Bundle;

public class YumActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
	}

	
	
}
