package com.cse5236groupthirteen.utilities;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

public class LocalRestaurant extends Restaurant {

	private ParseGeoPoint p;
	
	public LocalRestaurant(ParseObject po, ParseGeoPoint myLocation) {
		super(po);
		p = myLocation;
	}
	
	public double getDistanceInKilometers() {
		return p.distanceInKilometersTo(getParseGeoPoint());
	}

	@Override
	public String toString() {
		String s = super.toString();
		String dst = String.format(" %.3f km away", getDistanceInKilometers());
		s += dst;
		return s;
	}

	
	
}
