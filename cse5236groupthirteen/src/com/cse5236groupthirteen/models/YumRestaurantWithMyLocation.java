package com.cse5236groupthirteen.models;

import android.annotation.SuppressLint;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

@SuppressLint("DefaultLocale")
public class YumRestaurantWithMyLocation extends YumRestaurant {

	private ParseGeoPoint p;
	
	public YumRestaurantWithMyLocation(ParseObject po, ParseGeoPoint myLocation) {
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
