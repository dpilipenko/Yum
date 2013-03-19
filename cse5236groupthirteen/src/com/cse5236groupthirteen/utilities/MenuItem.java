package com.cse5236groupthirteen.utilities;

import java.util.UUID;
import java.util.regex.Pattern;

import com.parse.ParseObject;

public class MenuItem {

	/*
	 * These strings are used for converting to/from ParseObjects
	 */
	private final String mi_uuid = "menuitem_id";
	private final String mi_restId = "restaurant_id";
	private final String mi_name = "name";
	private final String mi_descr = "description";
	private final String mi_price = "price";
	
	/////////////////////////////////////////////////////////////
	
	private String menuItemId; // cannot be NULL or Empty
	private String restaurantId; // cannot be NULL or Empty
	private String name; // cannot be NULL or Empty
	private String description; // cannot be NULL, if Empty assume no menu item description
	private double price; // cannot be NULL
	
	/**
	 * Constructs a new MenuItem object populated with fake data.
	 */
	public MenuItem() {
		setDefaults();
	}
	
	/**
	 * Constructs a new MenuItem object populated with input data
	 * @param name			Name of the Menu Item
	 * @param description	Description of the Menu Item
	 * @param price			Price of the Menu Item. Price must be greater than or equal to 0.
	 * @param restaurantId	UUID of the related Restaurant
	 */
	public MenuItem(String name, String description, double price, String restaurantId) {
		setDefaults();
		
		setName(name);
		setDescription(description);
		setPrice(price);
		setRestaurantId(restaurantId);
		
	}

	/**
	 * Constructs a new MenuItem object from the input ParseObject
	 */
	public MenuItem(ParseObject po) {
		this.menuItemId = po.getString(mi_uuid);
		this.restaurantId = po.getString(mi_restId);
		this.name = po.getString(mi_name);
		this.description = po.getString(mi_descr);
		this.price = po.getDouble(mi_price);
		
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
		// Regular expression pattern that matches with UUID specifications
		String regex = "/^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i";
		//return Pattern.matches(regex, id);		
		
		try {
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
		ParseObject toReturn = new ParseObject(ParseHelper.CLASS_MENUITEM);
		toReturn.put(mi_descr, getDescription());
		toReturn.put(mi_name, getName());
		toReturn.put(mi_price, getPrice());
		toReturn.put(mi_restId, getRestaurantId());
		toReturn.put(mi_uuid, getMenuItemId());
		return toReturn;
		
	}
	
}
