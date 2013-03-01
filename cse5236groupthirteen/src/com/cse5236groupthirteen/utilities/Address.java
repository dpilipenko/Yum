package com.cse5236groupthirteen.utilities;

public class Address {

	private String streetNumber;
	private String streetName;
	private String city;
	private String postCode;
	private String province;
	
	/**
	 * Creates a new Address object with fake data
	 */
	public Address() {

		this.streetNumber = "123";
		this.streetName = "Fake Street";
		this.city = "Fake City";
		this.postCode = "12345"; 
		this.province = "Fake Province";
		
	}
	
	/**
	 * Creates a new Address object with specified values
	 * @param addrNumber		Street Number (e.g. 123 or 42)
	 * @param addrStreetName	Street Name (e.g. Neil Ave.)
	 * @param addrCity			City (e.g. Columbus)
	 * @param addrPostCode		Postal Code (e.g. 43210)
	 * @param addrProvince		Province (e.g. Shanghai or Ohio)
	 */
	public Address(String addrNumber, String addrStreetName,
			String addrCity, String addrPostCode, String addrProvince) {
		
		setStreetNumber(addrNumber);
		setStreetName(addrStreetName);
		setCity(addrCity);
		setPostCode(addrPostCode);
		setProvince(addrProvince);
		
	}

	/**
	 * @return the street number
	 */
	public String getStreetNumber() {
		return streetNumber;
	}

	/**
	 * @param number the number to set as the Address's street number, if input is formatted correctly
	 */
	public void setStreetNumber(String number) {
		number = number.trim();
		if (isFormattedStreetNumber(number)) {
			this.streetNumber = number;
		}
	}

	/**
	 * @return the street name
	 */
	public String getStreetName() {
		return streetName;
	}

	/**
	 * @param streetName the street name to set as the Address's street name, if input is formatted correctly
	 */
	public void setStreetName(String streetName) {
		streetName = streetName.trim();
		if (isFormattedStreetName(streetName)) {
			this.streetName = streetName;
		}
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set as the Address's city, if input is formatted correctly
	 */
	public void setCity(String city) {
		city = city.trim();
		if (isFormattedCity(city)) {
			this.city = city;
		}
	}

	/**
	 * @return the postal code
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postal code to set as the Address's postal code, if input is formatted correctly
	 */
	public void setPostCode(String postCode) {
		postCode = postCode.trim();
		if (isFormattedPostCode(postCode)) {
			this.postCode = postCode;
		}
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set as the Address's province, if input is formatted correctly
	 */
	public void setProvince(String province) {
		province = province.trim();
		if (isFormattedProvince(province)) {
			this.province = province;
		}
	}
	
	/**
	 * To be a valid City, it should not contain only letters
	 * @param city City string to be checked
	 */
	public static boolean isFormattedCity(String city) {
		if (city == null) {
			return false;
		}
		char[] charArray = city.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (!Character.isLetter(charArray[i])) {
				// a city name should only contain letters
				return false;
			}
		}
		return true;
	}
	
	/**
	 * To be a valid postal code, it can only contain letters, numbers and one '-'
	 * @param postCode postal code to be checked
	 */
	public static boolean isFormattedPostCode(String postCode) {
		if (postCode == null) {
			return false;
		}
		char[] charArray = postCode.toCharArray();
		boolean sawHyphen = false;
		for(int i = 0; i < charArray.length; i++) {
			boolean isLetter = Character.isLetter(charArray[i]);
			boolean isNumber = Character.isDigit(charArray[i]);
			boolean isHyphen = (charArray[i] == '-');
			if (isHyphen) {
				if (sawHyphen) {
					// there is more than one hyphen. illegal.
					return false;
				} else {
					// toggle flag that first hyphen was seen
					sawHyphen = true;
				}
			} else if (! (isLetter || isNumber) ) {
				// character is not a letter nor a number
				return false;
			}
			
		}
		return true;
	}
	
	/**
	 * To be a valid province, it should only contain letters
	 * @param province Province string to be checked
	 */
	public static boolean isFormattedProvince(String province) {
		if (province == null) {
			return false;
		}
		char[] charArray = province.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (!Character.isLetter(charArray[i])) {
				// a province should only contain letters
				return false;
			}
		}
		return true;
	}
	
	/**
	 * To be a valid street name, it can only contain letters, numbers, '.', ',',  and '-'
	 * @param streetname Street name string to be checked
	 */
	public static boolean isFormattedStreetName(String streetname) {
		if (streetname == null) {
			return false;
		}
		char[] charArray = streetname.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			boolean isLetter = Character.isLetter(charArray[i]);
			boolean isNumber = Character.isDigit(charArray[i]);
			boolean isPeriod = (charArray[i] == '.');
			boolean isComma = (charArray[i] == ',');
			boolean isHyphen = (charArray[i] == '-');
			if (!(isLetter || isNumber || isPeriod || isComma || isHyphen)) {
				// a street name can only contain letters, numbers, periods, commas, and hyphens
				return false;
			}
		}
		return true;
	}
	
	/**
	 * To be a valid a street number, for now it must exist (I don't know if there are any special rules -Dima)
	 * @param streetnumber
	 * @return
	 */
	public static boolean isFormattedStreetNumber(String streetnumber) {
		if (streetnumber == null) {
			return false;
		}
		return true;
	}
	
}
