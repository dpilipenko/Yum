package com.cse5236groupthirteen.dev;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.MenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
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

public class AddMenuActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private Spinner spinner;
	private ArrayAdapter<Restaurant> listAdapter;
	private Button addButton;
	private int selectedPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_menu);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
				
		// grab UI elements
		spinner = (Spinner) findViewById(R.id.spnr_addmenu_restaurants);
		addButton = (Button) findViewById(R.id.btn_addMenu_submit);
		
		// setup spinner specifics
		listAdapter = new ArrayAdapter<Restaurant>(this, android.R.layout.simple_dropdown_item_1line);
		spinner.setAdapter(listAdapter);
		
		// set up listeners
		spinner.setOnItemSelectedListener(this);
		addButton.setOnClickListener(this);
		

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadDataFromParse();
	}

	private void loadDataFromParse() {

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_RESTAURANTS);
		query.whereExists("objectId"); // all Parse objects contain this key, so
										// it should return all stored
										// Restaurants
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					// query successful
					listAdapter.clear();
					for (ParseObject po : objects) {
						Restaurant r = new Restaurant(po);
						listAdapter.add(r);
					}
					listAdapter.notifyDataSetChanged();

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
		
		MenuItem mi = generateMenuItemFromUI();
		ParseObject po = mi.toParseObject();
		po.saveInBackground();
		clearUI();
		
		
		
		if (mi == null) 
			return;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		this.selectedPosition = pos;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}
	
	private MenuItem generateMenuItemFromUI() {
		
		Restaurant a = (Restaurant)(spinner.getItemAtPosition(selectedPosition));
		
		EditText etName = ((EditText)findViewById(R.id.et_addmenu_itemname));
		EditText etDesc = ((EditText)findViewById(R.id.et_addmenu_itemdescr));
		EditText etPrice = ((EditText)findViewById(R.id.et_addmenu_itemprice));
		
		String name = etName.getText().toString().trim();
		String description = etDesc.getText().toString().trim();
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
		String restuarantId = a.getRestaurantId();
		
		return new MenuItem(name, description, price, restuarantId);
		
	}

	private void clearUI() {
		((EditText)findViewById(R.id.et_addmenu_itemname)).setText("");
		((EditText)findViewById(R.id.et_addmenu_itemdescr)).setText("");
		((EditText)findViewById(R.id.et_addmenu_itemprice)).setText("");
		
	}
}
