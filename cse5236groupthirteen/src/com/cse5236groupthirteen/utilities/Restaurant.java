package com.cse5236groupthirteen.utilities;

import com.parse.ParseObject;

public class Restaurant {
	
	private String name;
	private Address address;
	private PhoneNumber phoneNumber;
	
	public Restaurant() {
		//TODO set default values
	}
	
	public Restaurant(ParseObject po) {
		
		this.name = po.getString("name");
		
		
		
		
	}
	
	public ParseObject toParseObject() {
		
		ParseObject toReturn = new ParseObject("Restaurant");
		
		toReturn.put("name", name);
		toReturn.put("address_city", address.getCity());
		toReturn.put("address_number", address.getNumber());
		toReturn.put("address_postCode", address.getPostCode());
		toReturn.put("address_province", address.getProvince());
		toReturn.put("address_streetname", address.getStreetName());
		toReturn.put("phone_fullnumbers", phoneNumber.getFullNumber());
		
		return toReturn;
		
	}
	

}
