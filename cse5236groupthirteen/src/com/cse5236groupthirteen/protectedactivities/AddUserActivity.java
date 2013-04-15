package com.cse5236groupthirteen.protectedactivities;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.models.YumUser;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumHelper;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * This Activity is responsible for creating a new user.
 * This Activity is only available for Admin users
 *
 */
public class AddUserActivity extends Activity implements OnClickListener {

	private String mUsername;
	private String mPassword;
	private String mPasswordAgain;
	private boolean mIsAdminChecked;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_add_user);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		mUsername = null;
		mPassword = null;
		mPasswordAgain = null;
		mIsAdminChecked = false;
		
		Button createButton = (Button)findViewById(R.id.btn_addUser_createButton);
		createButton.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btn_addUser_createButton:
			
			// grab input values
			
			mUsername = ((EditText)findViewById(R.id.et_addUser_name)).getText().toString();
			mPassword = ((EditText)findViewById(R.id.et_addUser_pass)).getText().toString();
			mPasswordAgain = ((EditText)findViewById(R.id.et_addUser_passAgain)).getText().toString();
			mIsAdminChecked = ((CheckBox)findViewById(R.id.chkbx_addUser_isAdminCheckbox)).isChecked();
			
			// check if passwords match
			
			if (!mPassword.equals(mPasswordAgain)) {
				
				AlertDialog alertDialog = new AlertDialog.Builder(AddUserActivity.this).create();
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Your passwords don't match");
				alertDialog.setIcon(android.R.drawable.ic_delete);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// clear passwords
						((EditText)findViewById(R.id.et_addUser_pass)).setText("");
						((EditText)findViewById(R.id.et_addUser_passAgain)).setText("");
					}
				});
				alertDialog.show();
				break;
			}
			
			// check if user name is unique
			
			if (!isUniqueUserName(mUsername)) {
				
				AlertDialog alertDialog = new AlertDialog.Builder(AddUserActivity.this).create();
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Your username already exists");
				alertDialog.setIcon(android.R.drawable.ic_delete);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// clear boxes
						((EditText)findViewById(R.id.et_addUser_name)).setText("");
						((EditText)findViewById(R.id.et_addUser_pass)).setText("");
						((EditText)findViewById(R.id.et_addUser_passAgain)).setText("");
					}
					
				});
				alertDialog.show();
				break;
			}
			
			// create new user
			YumUser u = new YumUser(mUsername, mPassword, mIsAdminChecked);
			ParseObject po = u.toParseObject();
			try {
				po.save();
				finish();
			} catch (ParseException e) {
				String errmsg = "There was an error adding user";
				YumHelper.handleException(this, e, errmsg);
				finish();
			}
			
			break;
		}
	}

	/**
	 * This method checks Parse if given username is unique
	 * @param username
	 * @return
	 */
	private boolean isUniqueUserName(String username) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(YumUser.US_NAME, username);
		try {
			int count = query.count();
			if (count == 0) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		
	}

}
