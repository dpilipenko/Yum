package com.cse5236groupthirteen.utilities;

import com.parse.ParseObject;

public class Restaurant {
	
	private String name;
	private Address address;
	private PhoneNumber phoneNumber;
	
	private final String r_name = "name";
	private final String r_addr_city = "address_city";
	private final String r_addr_streetnumber = "address_number";
	private final String r_addr_postcode = "address_postcode";
	private final String r_addr_province = "address_province";
	private final String r_addr_streetname = "address_streetname";
	private final String r_phone_fullnumber = "phone_fullnumbers";
	
	/**
	 * Constructs a new Restaurant object populated with fake data
	 */
	public Restaurant() {
		this.name = "Fake Restaurant";
		this.address = new Address();
		this.phoneNumber = new PhoneNumber();
	}
	
	/**
	 * Constructs a new Restaurant object with inputed information
	 */
	public Restaurant(String restaurantName, Address restaurantAddress, PhoneNumber restaurantPhoneNumber) {
		setName(restaurantName);
		setAddress(restaurantAddress);
		setPhoneNumber(restaurantPhoneNumber);
	}
	
	/**
	 * @return the Restaurant's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set as the Restaurant's name, if formatted correctly
	 */
	public void setName(String name) {
		name = name.trim();
		if (isFormattedName(name)) {
			this.name = name;
		}
	}

	/**
	 * @return the Restaurant's address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set as the Restaurant's address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the Restaurant's phone number
	 */
	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phone number to set as the Restaurant's phone number
	 */
	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	/**
	 * To be a valid restaurant name, (for now) it must exist
	 * @param restaurantName The restaurant name that will be checked
	 */
	public static boolean isFormattedName(String restaurantName) {
		if (restaurantName == null) {
			return false;
		}
		return true;
	}

	/**
	 * Constructs a new Restaurant object populated with ParseObject's information
	 * @param po ParseObject that will create the Restaurant object
	 */
	public Restaurant(ParseObject po) {
		
		this.name = po.getString(r_name);
		
		Address a = new Address();
		a.setCity(po.getString(r_addr_city));
		a.setStreetNumber(po.getString(r_addr_streetnumber));
		a.setPostCode(po.getString(r_addr_postcode));
		a.setProvince(po.getString(r_addr_province));
		a.setStreetName(po.getString(r_addr_streetname));
		this.address = a;
		
		PhoneNumber p = new PhoneNumber();
		p.setFullNumber(po.getString(r_phone_fullnumber));
		this.phoneNumber = p;
		
	}
	
	/**
	 * Constructs a new ParseObject object populated with this Restaurant's information
	 * @return ParseObject with this Restaurant's information
	 */
	public ParseObject toParseObject() {
		
		ParseObject toReturn = new ParseObject("Restaurant");
		
		toReturn.put(r_name, name);
		toReturn.put(r_addr_city, address.getCity());
		toReturn.put(r_addr_streetnumber, address.getStreetNumber());
		toReturn.put(r_addr_postcode, address.getPostCode());
		toReturn.put(r_addr_province, address.getProvince());
		toReturn.put(r_addr_streetname, address.getStreetName());
		toReturn.put(r_phone_fullnumber, phoneNumber.getFullNumber());
		
		return toReturn;
		
	}
	

}
