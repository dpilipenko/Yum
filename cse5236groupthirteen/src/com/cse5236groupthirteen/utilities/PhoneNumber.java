package com.cse5236groupthirteen.utilities;

public class PhoneNumber {

	private String fullnumber;
	
	/**
	 * Creates a new PhoneNumber with fake information
	 */
	public PhoneNumber() {
		this.fullnumber = "+12143965554";
	}
	
	/** 
	 * Creates a new PhoneNumber with specified information
	 * @param fullnumber	A phone number in full international format (e.g. +16145555555) 
	 */
	public PhoneNumber(String fullnumber) {
		setFullNumber(fullnumber);
	}
	
	/**
	 * @param fullnumber the number to set as the PhoneNumber's fullnumber, if input is formatted correctly
	 */
	public void setFullNumber(String fullnumber) {
		fullnumber = fullnumber.trim();
		if (isFormattedPhoneNumber(fullnumber)) {
			this.fullnumber = fullnumber;
		}
		
	}
	
	/**
	 * To be a valid phone number, it must start with a '+', then numerical digits
	 * @param fullnumber phone number string to check
	 */
	private static boolean isFormattedPhoneNumber(String fullnumber) {
		
		if (fullnumber == null) {
			return false;
		}
		if (fullnumber.length() <= 2) {
			// a phone number should have more than just a + and one digit
			return false;
		}
		
		char[] charArr = fullnumber.toCharArray();
		
		if (charArr[0] != '+') { //checks for leading '+' 
			return false;
		}
		
		for (int i = 1; i < charArr.length; i++) { //check for only digits after '+'
			if (!Character.isDigit(charArr[i])) {
				return false;
			}
		}
		
		return true; // input passed all checks, it is valid.
	}

	public String getFullNumber() {
		return fullnumber;
	}
}
