package com.cse5236groupthirteen.utilities;

import com.parse.ParseObject;

public class YumUser {
	
	/*
	 * These strings are used for converting to/from ParseObjects
	 */
	public final static String US_UUID = "user_id";
	public final static String US_NAME = "name";
	public final static String US_PASSWORD = "password";
	public final static String US_ISADMIN = "isAdmin";
	//////////////////////
	
	private String userId;
	private String name;
	private String password;
	private boolean isAdmin;
	
	public YumUser() {
		setDefaults();
	}
	
	public YumUser (String name, String password, boolean isAdmin) {
		setDefaults();
		
		this.name = name;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	public YumUser (ParseObject po) {
		this.userId = po.getString(US_UUID);
		this.name = po.getString(US_NAME);
		this.password = po.getString(US_PASSWORD);
		this.isAdmin = po.getBoolean(US_ISADMIN);
	}
	
	public void setDefaults() {
		this.userId = java.util.UUID.randomUUID().toString();
		this.name = "user_"+java.util.UUID.randomUUID().toString();
		this.password = "password";
		this.isAdmin = false;
	}
	
	public ParseObject toParseObject() {
		ParseObject toReturn = new ParseObject(ParseHelper.CLASS_USERS);
		toReturn.put(US_UUID, userId);
		toReturn.put(US_NAME, name);
		toReturn.put(US_PASSWORD, password);
		toReturn.put(US_ISADMIN, isAdmin);
		return toReturn;
	}

}
