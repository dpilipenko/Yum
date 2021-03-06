package com.cse5236groupthirteen.utilities;

import java.text.NumberFormat;
import java.util.UUID;

import com.parse.ParseObject;

public class YumMenuItem {

	/*
	 * These strings are used for converting to/from ParseObjects
	 */
	public final static String MI_UUID = "menuitem_id";
	public final static String MI_RESTID = "restaurant_id";
	public final static String MI_NAME = "name";
	public final static String MI_DESCR = "description";
	public final static String MI_PRICE = "price";
	
	/////////////////////////////////////////////////////////////
	
	private String menuItemId; // cannot be NULL or Empty
	private String restaurantId; // cannot be NULL or Empty
	private String name; // cannot be NULL or Empty
	private String description; // cannot be NULL, if Empty assume no menu item description
	private double price; // cannot be NULL
	
	/**
	 * Constructs a new MenuItem object populated with fake data.
	 */
	public YumMenuItem() {
		setDefaults();
	}
	
	/**
	 * Constructs a new MenuItem object populated with input data
	 * @param name			Name of the Menu Item
	 * @param description	Description of the Menu Item
	 * @param price			Price of the Menu Item. Price must be greater than or equal to 0.
	 * @param restaurantId	UUID of the related Restaurant
	 */
	public YumMenuItem(String name, String description, double price, String restaurantId) {
		setDefaults();
		
		setName(name);
		setDescription(description);
		setPrice(price);
		setRestaurantId(restaurantId);
		
	}

	/**
	 * Constructs a new MenuItem object from the input ParseObject
	 */
	public YumMenuItem(ParseObject po) {
		this.menuItemId = po.getString(MI_UUID);
		this.restaurantId = po.getString(MI_RESTID);
		this.name = po.getString(MI_NAME);
		this.description = po.getString(MI_DESCR);
		this.price = po.getDouble(MI_PRICE);
		
	}
	
	/**
	 * Sets the MenuItem private variables to fake data
	 */
	private void setDefaults() {
		this.menuItemId = java.util.UUID.randomUUID().toString();
		this.restaurantId = java.util.UUID.randomUUID().toString();
		this.name = "Fake Bread";
		this.description = "Fake We have: White, Whole Grain, Half Grain, Double Grain";
		this.price = (double) 1.15;
	}
	
	/**
	 * @return the MenuItem's universally unique id. This Id is generated by Java and cannot and should not be changed
	 */
	public String getMenuItemId() {
		return this.menuItemId;
	}
	
	/**
	 * @return the id of the restaurant that this MenuItem is related to
	 */
	public String getRestaurantId() {
		return this.restaurantId;
	} 
	
	
	/**
	 * Relates this MenuItem to the Restaurant whose UUID is the input, if input is valid
	 * @param restaurantId	UUID of the Restaurant
	 */
	public void setRestaurantId(String restaurantId) {
		if (isValidRestaurantId(restaurantId)) {
			this.restaurantId = restaurantId;
		}
	}
	
	/**
	 * @return the menu item's name
	 */
	public String getName() {
		return this.name;		
	}
	
	/**
	 * Sets the name of the menu item, if it is formatted correctly
	 * @param name
	 */
	public void setName(String name) {
		if (isFormattedName(name)) {
			this.name = name.trim();
		}
	}
	
	/**
	 * @return the description of the menu item
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Sets the description of the menu item, if it is formatted correctly
	 * @param desc
	 */
	public void setDescription(String desc) {
		if (isFormattedDescription(desc)) {
			this.description = desc.trim();
		}
	}
	
	/**
	 * @return the price of the menu item
	 */
	public double getPrice() {
		return this.price;
	}
	
	/**
	 * Sets the price of the menu item, if it is valid
	 * @param price
	 */
	public void setPrice(double price) {
		if (isValidPrice(price)) {
			this.price = price;
		}
	}
	
	/**
	 * Checks if the input is a satisfactory name 
	 * @param name
	 * @return True if satisfactory. False if not satisfactory.
	 */
	public static boolean isFormattedName(String name) {
		if (name == null) {
			return false;
		}
		if (name == "") {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the input is a satisfactory description
	 * @param description
	 * @return True if satisfactory. False if not satisfactory.
	 */
	public static boolean isFormattedDescription(String description) {
		if (description == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the input is a satisfactory Java UUID string
	 * @param id
	 * @return True if satisfactory. False if not satisfactory.
	 */
	public static boolean isValidRestaurantId(String id) {
		if (id == null) {
			return false;
		}
		
		try {
			@SuppressWarnings("unused")
			UUID a = UUID.fromString(id);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
		
	}
	
	/**
	 * Checks if the input is a satisfactory price entry
	 * @param price
	 * @return True if satisfactory. False if not satisfactory
	 */
	public static boolean isValidPrice(double price) {
		if (price < 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * Constructs a new ParseObject object populated with this Restaurant's information
	 * @return ParseObject with this Restaurant's information
	 */
	public ParseObject toParseObject() {
		ParseObject toReturn = new ParseObject(ParseHelper.CLASS_MENUITEMS);
		toReturn.put(MI_DESCR, getDescription());
		toReturn.put(MI_NAME, getName());
		toReturn.put(MI_PRICE, getPrice());
		toReturn.put(MI_RESTID, getRestaurantId());
		toReturn.put(MI_UUID, getMenuItemId());
		return toReturn;
		
	}
	
	/**
	 * This is what gets displayed by the ListView with ArrayAdapter<MenuItem>
	 */
	@Override
	public String toString() {
		
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		String price = formatter.format(getPrice());
		return "" + this.getName() + ": " + this.getDescription() + " " + price;
	}
	
}
