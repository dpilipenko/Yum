package com.cse5236groupthirteen.protectedactivities;

import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.YumUser;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * This activity provides a login screen to access the protected activities
 *
 */
public class LoginActivity extends Activity implements OnClickListener{

	private EditText mUsernameBox = null;
	private EditText mPasswordBox = null;
	
	private String mUsername;
	private String mPassword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_login);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		mUsernameBox = ((EditText)findViewById(R.id.et_login_username));
		mPasswordBox = ((EditText)findViewById(R.id.et_login_password));
		mUsername = null;
		mPassword = null;
		
		Button btn = (Button)findViewById(R.id.btn_login_loginButton);
		btn.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mUsernameBox != null) {
			mUsernameBox.setText("");
		}
		if (mPasswordBox != null) {
			mPasswordBox.setText("");
		}
		
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn_login_loginButton:
			
			// get ui inputs
			mUsername = ((EditText)findViewById(R.id.et_login_username)).getText().toString();
			mPassword = ((EditText)findViewById(R.id.et_login_password)).getText().toString();
			
			// check for user name
			if (!usernameExists(mUsername)) {
				
				// user doesn't exist
				AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Your username doesn't exist");
				alertDialog.setIcon(android.R.drawable.ic_delete);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// clear passwords
						((EditText)findViewById(R.id.et_login_password)).setText("");
					}
				});
				alertDialog.show();
				break;
				
			}
			
			// check for user name and password
			if (!usernameAndPasswordExists(mUsername, mPassword)) {
				
				// password is wrong
				AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
				alertDialog.setTitle("Error");
				alertDialog.setMessage("Your password is wrong");
				alertDialog.setIcon(android.R.drawable.ic_delete);
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// clear passwords
						((EditText)findViewById(R.id.et_login_password)).setText("");
					}
				});
				alertDialog.show();
				break;
			}
			
			// forward
			boolean isAdmin = isUsernameAdmin(mUsername);
			Intent intent = new Intent(LoginActivity.this, DevActivity.class);
			intent.putExtra(YumUser.US_ISADMIN, isAdmin);
			startActivity(intent);
			
			break;
		}
	}

	/**
	 * This method checks Parse if given username is an Admin user
	 * @param username
	 * @return
	 */
	private boolean isUsernameAdmin(String username) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(YumUser.US_NAME, username);
		query.whereEqualTo(YumUser.US_PASSWORD, mPassword);
		try {
			List<ParseObject> find = query.find();
			if (find.size() == 1) {
				ParseObject po = find.get(0);
				boolean isAdmin = po.getBoolean(YumUser.US_ISADMIN);
				return isAdmin;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	/**
	 * This methods checks Parse if username/password combo is correct
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean usernameAndPasswordExists(String username, String password) {

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(YumUser.US_NAME, username);
		query.whereEqualTo(YumUser.US_PASSWORD, password);
		try {
			int count = query.count();
			if (count == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		
	}

	/**
	 * This method checks parse if username exists
	 * @param username
	 * @return
	 */
	private boolean usernameExists(String username) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(YumUser.US_NAME, username);
		try {
			int count = query.count();
			if (count == 1) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		
	}

}
