package com.cse5236groupthirteen.protectedactivities;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.models.YumMenuItem;
import com.cse5236groupthirteen.models.YumRestaurant;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * This activity allows a logged in user to add menu items to a restaurant
 *
 */
public class AddMenuActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private Spinner mRestaurantSpinner;
	private ArrayAdapter<YumRestaurant> mRestaurantListAdapter;
	private Button mSaveButton;
	private int mSelectedRestaurantPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_add_menu);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
				
		// grab UI elements
		mRestaurantSpinner = (Spinner) findViewById(R.id.spnr_addmenu_restaurants);
		mSaveButton = (Button) findViewById(R.id.btn_addMenu_submit);
		
		// setup spinner specifics
		mRestaurantListAdapter = new ArrayAdapter<YumRestaurant>(this, android.R.layout.simple_spinner_item);
		mRestaurantSpinner.setAdapter(mRestaurantListAdapter);
		
		// set up listeners
		mRestaurantSpinner.setOnItemSelectedListener(this);
		mSaveButton.setOnClickListener(this);
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadAvailableRestaurantsFromParse();
	}

	
	private void loadAvailableRestaurantsFromParse() {

		// query for all restaurants
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId");
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					mRestaurantListAdapter.clear();
					for (ParseObject po : objects) {
						YumRestaurant r = new YumRestaurant(po);
						mRestaurantListAdapter.add(r);
					}
					mRestaurantListAdapter.notifyDataSetChanged();

				} else {
					// error occurred
					String errmsg = "There was an error loading data from Parse";
					Toast.makeText(getApplicationContext(), errmsg,
							Toast.LENGTH_SHORT).show();
					Log.e("Yum", errmsg, e);
				}

			}

		});

	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_addMenu_submit:
			
			YumMenuItem mi = generateMenuItemFromUI();
			ParseObject po = mi.toParseObject();
			try {
				po.save();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			clearUI();
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		this.mSelectedRestaurantPosition = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}
	
	
	/**
	 * This method generates a MenuItem from the current state of the UI
	 * @return
	 */
	private YumMenuItem generateMenuItemFromUI() {
		
		
		YumRestaurant selectedRestaurant = 
				(YumRestaurant)(mRestaurantSpinner.getItemAtPosition(mSelectedRestaurantPosition));
		String restuarantId = selectedRestaurant.getRestaurantId();
		
		EditText etName = ((EditText)findViewById(R.id.et_addmenu_itemname));
		EditText etDesc = ((EditText)findViewById(R.id.et_addmenu_itemdescr));
		EditText etPrice = ((EditText)findViewById(R.id.et_addmenu_itemprice));
		
		String nameStr = etName.getText().toString().trim();
		String descriptionStr = etDesc.getText().toString().trim();
		String priceStr = etPrice.getText().toString().trim();
		
		NumberFormat formatter = NumberFormat.getInstance(Locale.US);
		Number number;
		try {
			 number = formatter.parse(priceStr);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
		double price = number.doubleValue();
		
		return new YumMenuItem(nameStr, descriptionStr, price, restuarantId);
		
	}

	private void clearUI() {
		((EditText)findViewById(R.id.et_addmenu_itemname)).setText("");
		((EditText)findViewById(R.id.et_addmenu_itemdescr)).setText("");
		((EditText)findViewById(R.id.et_addmenu_itemprice)).setText("");
		
	}
}
