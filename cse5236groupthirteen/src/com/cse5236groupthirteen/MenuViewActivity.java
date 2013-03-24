package com.cse5236groupthirteen;

import java.util.ArrayList;
import java.util.List;

import com.cse5236groupthirteen.utilities.MenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MenuViewActivity extends Activity {

	private ListView listview;
	private ArrayAdapter<MenuItem> listviewAdapter;
	
	private String selectedRestaurantId;
	private String selectedRestaurantName;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_view);
		// this is necessary to call in order to use Parse, Parse recommends keeping in onCreate
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		// grab ui elements
		listview = (ListView)findViewById(R.id.lstvw_menuView_menuitemlist);
		
		// setup list view specifics 
		listviewAdapter = new ArrayAdapter<MenuItem>(this, android.R.layout.simple_list_item_1);
		listview.setAdapter(listviewAdapter);
		
		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			Toast.makeText(getApplicationContext(), errmsg , Toast.LENGTH_SHORT).show();
			Log.e("Yum", errmsg);
		}
		
		this.setTitle(selectedRestaurantName + " Menu"); 
	}


	@Override
	protected void onResume() {
		super.onResume();
		updateMenuList();
	}

	private void updateMenuList() {
		
		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		String b = MenuItem.MI_RESTID;
		String c = selectedRestaurantId;
		query.whereEqualTo(b, c);
		
		// query parse
		List<ParseObject> menuItems = new ArrayList<ParseObject>();
		try {
			menuItems = query.find();
		} catch (ParseException e) {
			// error with parse occurred
			e.printStackTrace();
			return;
		}
		
		// check amount of returned hits
		if (menuItems.size() == 0) {
			// no hits :(
			
		} else {
			// we got hits! :)
			for(ParseObject po: menuItems) {
				MenuItem m = new MenuItem(po);
				listviewAdapter.add(m);
			}
			listviewAdapter.notifyDataSetChanged();
		}
		
	}

}
