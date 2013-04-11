package com.cse5236groupthirteen;

import java.util.List;

import com.cse5236groupthirteen.utilities.YumMenuItem;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumRestaurant;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * This activity is responsible for listing all the Menu items associated with a restaurant
 */
public class MenuViewActivity extends YumViewActivity {

	// UI elements
	private ListView mListview;

	private ArrayAdapter<YumMenuItem> mListviewAdapter;
	private String mSelectedRestaurantId;
	private String mSelectedRestaurantName;
	private boolean mQuerying;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_view);
		
		mQuerying = false;
		
		// grab ui elements
		mListview = (ListView) findViewById(R.id.lstvw_menuView_menuitemlist);

		// setup list view specifics
		mListviewAdapter = new ArrayAdapter<YumMenuItem>(this,
				android.R.layout.simple_list_item_1);
		mListview.setAdapter(mListviewAdapter);

		// load selected restaurant information
		Bundle b = getIntent().getExtras();
		if (b != null) {
			mSelectedRestaurantId = b.getString(YumRestaurant.R_UUID);
			mSelectedRestaurantName = b.getString(YumRestaurant.R_NAME);
		} else {
			String errmsg = "There was an error passing Restaurant information from HomeView";
			YumHelper.handleError(this, errmsg);
		}

		this.setTitle(mSelectedRestaurantName + " Menu");
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateMenuList();
	}

	@Override
	public void onShake() {
		if (!mQuerying) {
			updateMenuList();
		}
		
	}

	/**
	 * This method loads menu items from back end and populates
	 */
	private void updateMenuList() {
		mQuerying = true;
		showLoadingDialog();

		// query for all menu items for a given restaurant
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_MENUITEMS);
		query.whereEqualTo(YumMenuItem.MI_RESTID, mSelectedRestaurantId);
		query.findInBackground(new FindCallback() {

			public void done(List<ParseObject> objects, ParseException e) {
				dismissLoadingDialog();
				if (e == null) {
					mListviewAdapter.clear();

					if (objects.isEmpty()) {
						// no hits :(

					} else {
						// we got hits! :)
						for (ParseObject po : objects) {
							YumMenuItem m = new YumMenuItem(po);
							mListviewAdapter.add(m);
						}
						mListviewAdapter.notifyDataSetChanged();
					}

				} else {
					String errmsg = "There was an error loading data from Parse";
					YumHelper.handleException(getParent(), e, errmsg);
				}
			}

		});

	}

}
