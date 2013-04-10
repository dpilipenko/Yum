package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.utilities.MenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.Restaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuViewActivity extends YumViewActivity {

	private ListView listview;

	private ArrayAdapter<MenuItem> listviewAdapter;

	private String selectedRestaurantId;
	private String selectedRestaurantName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_view);

		// grab ui elements
		listview = (ListView) findViewById(R.id.lstvw_menuView_menuitemlist);

		// setup list view specifics
		listviewAdapter = new ArrayAdapter<MenuItem>(this,
				android.R.layout.simple_list_item_1);
		listview.setAdapter(listviewAdapter);

		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			selectedRestaurantId = b.getString(Restaurant.R_UUID);
			selectedRestaurantName = b.getString(Restaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			YumHelper.handleError(this, errmsg);
		}

		this.setTitle(selectedRestaurantName + " Menu");
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateMenuList();
	}

	@Override
	public void onShake() {
		updateMenuList();
	}

	private void updateMenuList() {

		// create query
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		query.whereEqualTo(MenuItem.MI_RESTID, selectedRestaurantId);

		showLoadingDialog();

		query.findInBackground(new FindCallback() {

			public void done(List<ParseObject> objects, ParseException e) {
				dismissLoadingDialog();
				if (e == null) {
					listviewAdapter.clear();

					if (objects.isEmpty()) {
						// no hits :(

					} else {
						// we got hits! :)
						for (ParseObject po : objects) {
							MenuItem m = new MenuItem(po);
							listviewAdapter.add(m);
						}
						listviewAdapter.notifyDataSetChanged();
					}

				} else {
					String errmsg = "There was an error loading data from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}
			}

		});

	}

}
