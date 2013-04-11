package com.cse5236groupthirteen.dev;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.Address;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddRestaurantActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// create UI
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_add_restaurant);
		// grab UI references
		Button btnAddRestaraunt = (Button)findViewById(R.id.btn_addrestaurant_saverestauranttoparse);		
		Button btnFillRestarauntInfo = (Button)findViewById(R.id.btn_addrestaurant_fillinui);
		// setup OnClick listeners
		btnAddRestaraunt.setOnClickListener(this);
		btnFillRestarauntInfo.setOnClickListener(this);
		
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
				 
	}

	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.btn_addrestaurant_saverestauranttoparse:
			Restaurant r = generateRestaurantFromUI();
			ParseObject po = r.toParseObject();
			po.saveInBackground();
			clearUI();
			
		break;
		case R.id.btn_addrestaurant_fillinui:
			populateUI();
			break;
		}
		
	}
	
	private void clearUI() {
		((EditText)findViewById(R.id.et_addrestaurant_name)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_city)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_postalcode)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_province)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_streetname)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_streetnumber)).setText("");
		((EditText)findViewById(R.id.et_addrestaurant_phonenumber)).setText("");
	}
	
	private void populateUI() {
		((EditText)findViewById(R.id.et_addrestaurant_name)).setText("Wendy's");
		((EditText)findViewById(R.id.et_addrestaurant_city)).setText("Columbus");
		((EditText)findViewById(R.id.et_addrestaurant_postalcode)).setText("43212");
		((EditText)findViewById(R.id.et_addrestaurant_province)).setText("Ohio");
		((EditText)findViewById(R.id.et_addrestaurant_streetname)).setText("Olentangy River Rd");
		((EditText)findViewById(R.id.et_addrestaurant_streetnumber)).setText("1483");
		((EditText)findViewById(R.id.et_addrestaurant_phonenumber)).setText("16144211277");
	}
	
	private Restaurant generateRestaurantFromUI() {
		
		String resName = ((EditText)findViewById(R.id.et_addrestaurant_name)).getText().toString();
		String city = ((EditText)findViewById(R.id.et_addrestaurant_city)).getText().toString();
		String postcode = ((EditText)findViewById(R.id.et_addrestaurant_postalcode)).getText().toString();
		String province = ((EditText)findViewById(R.id.et_addrestaurant_province)).getText().toString();
		String streetName = ((EditText)findViewById(R.id.et_addrestaurant_streetname)).getText().toString();
		String streetNumber = ((EditText)findViewById(R.id.et_addrestaurant_streetnumber)).getText().toString();
		String website = ((EditText)findViewById(R.id.et_website)).getText().toString();
		String phoneNumber = "+"+((EditText)findViewById(R.id.et_addrestaurant_phonenumber)).getText().toString();
		
		Address a = new Address(streetNumber, streetName, city, postcode, province);
		
		String addr = a.getFullAddress();
		ParseGeoPoint gp = YumHelper.getParseGeoPointFromRestaurantFullAddress(this, addr);
		double lat = gp.getLatitude();
		double lon = gp.getLongitude();
		
		return new Restaurant(resName, a, phoneNumber, website, lat, lon);
		
	}

	
	
}
