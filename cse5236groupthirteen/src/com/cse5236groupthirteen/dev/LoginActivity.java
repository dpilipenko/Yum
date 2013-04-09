package com.cse5236groupthirteen.dev;

import java.util.List;

import com.cse5236groupthirteen.R;
import com.cse5236groupthirteen.utilities.ParseHelper;
import com.cse5236groupthirteen.utilities.User;
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

public class LoginActivity extends Activity implements OnClickListener{

	private EditText usernameBox = null;
	private EditText passwordBox = null;
	
	private String username;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dev_login);
		Parse.initialize(this, ParseHelper.APPLICATION_ID, ParseHelper.CLIENT_KEY);
		
		usernameBox = ((EditText)findViewById(R.id.et_login_username));
		passwordBox = ((EditText)findViewById(R.id.et_login_password));
		username = null;
		password = null;
		
		Button btn = (Button)findViewById(R.id.btn_login_loginButton);
		btn.setOnClickListener(this);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (usernameBox != null) {
			usernameBox.setText("");
		}
		if (passwordBox != null) {
			passwordBox.setText("");
		}
		
	}



	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.btn_login_loginButton:
			
			// get ui inputs
			username = ((EditText)findViewById(R.id.et_login_username)).getText().toString();
			password = ((EditText)findViewById(R.id.et_login_password)).getText().toString();
			
			// check for user name
			if (!usernameExists(username)) {
				
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
			if (!usernameAndPasswordExists(username, password)) {
				
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
			boolean isAdmin = isUsernameAdmin(username);
			Intent intent = new Intent(LoginActivity.this, DevActivity.class);
			intent.putExtra(User.US_ISADMIN, isAdmin);
			startActivity(intent);
			
			break;
		}
	}

	private boolean isUsernameAdmin(String username) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(User.US_NAME, username);
		query.whereEqualTo(User.US_PASSWORD, password);
		try {
			List<ParseObject> find = query.find();
			if (find.size() == 1) {
				ParseObject po = find.get(0);
				boolean isAdmin = po.getBoolean(User.US_ISADMIN);
				return isAdmin;
			}
		} catch (ParseException e) {
			return false;
		}
		return false;
	}

	private boolean usernameAndPasswordExists(String username, String password) {

		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(User.US_NAME, username);
		query.whereEqualTo(User.US_PASSWORD, password);
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

	private boolean usernameExists(String username) {
		
		ParseQuery query = new ParseQuery(ParseHelper.CLASS_USERS);
		query.whereEqualTo(User.US_NAME, username);
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
