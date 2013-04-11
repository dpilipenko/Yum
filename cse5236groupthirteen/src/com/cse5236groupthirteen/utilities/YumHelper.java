package com.cse5236groupthirteen.utilities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;

/**
 * This class provides with helpful static methods to use
 *
 */
public class YumHelper {

	/**
	 * This method checks phone if it is able to get user's location
	 * @param context
	 * @return
	 */
	public static boolean testLocationServices(Context context) {
		
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location locationGPS = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location locationNet = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		boolean haveGPS = (locationGPS != null);
		boolean haveNet = (locationNet != null);
		
		return (haveGPS || haveNet);
	}
	
	/**
	 * This method checks phone if it is able to communicate with Parse, our back end
	 * @param context
	 * @return
	 */
	public static boolean testParseConnection(Context context) {
		ParseQuery q = new ParseQuery(ParseHelper.CLASS_USERS);
		q.whereExists(YumUser.US_UUID);
		try {
			q.count();
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	/**
	 * This method shows an alert box with specified message
	 * @param context
	 * @param message
	 */
	public static void displayAlert(final Context context, String message) {
		
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("Yumm! Alert:");
		alert.setMessage(message);
		alert.setCancelable(false);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				
			}
		});
		alert.show();
	}
	
	/**
	 * This method provides uniform logging functionality for given message
	 * @param context
	 * @param errmsg
	 */
	public static void handleError(Context context, String errmsg) {
		Toast.makeText(context, errmsg, Toast.LENGTH_LONG).show();
		Log.e("Yum Exception!", errmsg);
	}
	
	/**
	 * This method provides uniform exception handling functionality
	 * @param context
	 * @param e
	 * @param errmsg
	 */
	public static void handleException(Context context, Exception e, String errmsg) {
		Toast.makeText(context, "(e) "+errmsg, Toast.LENGTH_LONG).show();
		Log.e("Yum Exception!", errmsg);
		Log.e("Yum Exception!", e.toString());
	}
	
	/**
	 * This method returns the last,best known current location for user
	 * @param context
	 * @return
	 */
	public static Location getLastBestLocation(Context context) {
		
		// get location providers
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location locationGPS = lm
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location locationNet = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		// check which ones are available
		boolean haveGPS = (locationGPS != null);
		boolean haveNet = (locationNet != null);
		
		if (!haveGPS && !haveNet) { // no providers available
			String msg = "Could not find a location. Using random location now.";
			displayAlert(context, msg);
			Location l = new Location("yum fake location");
			l.setLatitude(45.0);
			l.setLongitude(45.0);
			// uh oh... no location
			return new Location(l);
			
		} else if (haveGPS && !haveNet) { // only gps available
			
			return locationGPS;
			
		} else if (!haveGPS && haveNet) { // only cell location available
			
			return locationNet;
			
		} else { // choose the most recent one
			
			long GPSLocationTime = locationGPS.getTime();
			long NetLocationTime = locationNet.getTime();
			
			if (GPSLocationTime - NetLocationTime > 0) {
				
				return locationGPS;
				
			} else {
				
				return locationNet;
				
			}
		} 
		
	}
	
	/**
	 * This method returns user's last,best location as a ParseGeoPoint
	 * @param context
	 * @return
	 */
	public static ParseGeoPoint getLastBestLocationForParse(Context context) {

		Location myLocation = getLastBestLocation(context);
		double lat = myLocation.getLatitude();
		double lon = myLocation.getLongitude();

		return new ParseGeoPoint(lat, lon);
	}

	/**
	 * This method returns a ParseGeoPoint from a given restaurant address
	 * @param context
	 * @param address
	 * @return
	 */
	public static ParseGeoPoint getParseGeoPointFromRestaurantFullAddress(
			Context context, String address) {

		String strAddress = address;
		Geocoder coder = new Geocoder(context);
		List<android.location.Address> addresses;
		try {

			strAddress = strAddress.replace(' ', '+');
			addresses = coder.getFromLocationName(strAddress, 1);
			if (addresses == null) {
				return null;
			}
			if (addresses.isEmpty()) {
				return null;
			}
			android.location.Address location = addresses.get(0);
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

			return new ParseGeoPoint(latitude, longitude);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
