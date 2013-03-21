package com.cse5236groupthirteen.utilities;

import java.util.Date;
import java.util.UUID;

import com.parse.ParseObject;

public class Submission {

	public final static int RATING_HAPPY = 1;
	public final static int RATING_NORMAL = 0;
	public final static int RATING_SAD = -1;
	
	/*
	 * These strings are used for converting to/from ParseObjects
	 */
	public final static String S_UUID = "submission_id";
	public final static String S_RESTID = "restuarant_id";
	public final static String S_RATING = "rating";
	public final static String S_WAITTIME = "waittime";
	public final static String S_COMMENT = "comment";
	
	///////////////////////////////////////////////////////////////
	
	private String submissionId; // cannot be NULL or empty
	private String restuarantId; // cannot be NULL or empty
	private int rating; // must be Submission.RATING_*
	private long waittime;
	private String comment; // cannot be NULL, if Empty assume no comments
		
	
	public Submission () {
		setDefaults();
	}
	
	public Submission (ParseObject po) {
		
		this.submissionId = po.getString(S_UUID);
		this.restuarantId = po.getString(S_RESTID);
		this.rating = po.getInt(S_RATING);
		this.waittime = po.getLong(S_WAITTIME);
		this.comment = po.getString(S_COMMENT);
		
		//
		/*
		this.startTime = po.getDate(s_startTime);
		this.endTime = po.getDate(s_endTime);
		*/
		
	}
	
	public Submission (int rating, long waittime, String comment, String restuarantId) {
		setDefaults();
		setRating(rating);
		setWaitTime(waittime);
		setComment(comment);
		setRestaruantId(restuarantId);
		
	}
	
	private void setDefaults() {
		this.submissionId = java.util.UUID.randomUUID().toString();
		this.restuarantId = java.util.UUID.randomUUID().toString();
		this.rating = RATING_HAPPY;
		this.waittime = 0l;
		this.comment = "FAKE - Best food ever!";
	}
	
	public String getSubmissionId() {
		return this.submissionId;
	}
	
	public String getRestaurantId() {
		return this.restuarantId;
	}
	
	public void setRestaruantId(String id) {
		if (isValidRestaurantId(id)) {
			this.restuarantId = id;
		}
	}
	
	public int getRating() {
		return this.rating;
	}
	
	public void setRating(int rating) {
		if (isValidRating(rating)) {
			this.rating = rating;
		}
	}
	
	public long getWaitTime() {
		return this.waittime;
	}
	
	public void setWaitTime(long waitTime) {
		if (isValidWaitTime(waitTime)) {
			this.waittime = waitTime;
		}
	}
	
	private boolean isValidWaitTime(long waitTime) {
		if (waitTime < 0) 
			return false;
		
		return true;
	}

	public String getComment() {
		return this.comment;
	}
	
	public void setComment(String comment) {
		if (isValidComment(comment)) {
			this.comment = comment;
		}
	}
	
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
	
	public static boolean isValidRating(int rating) {
		
		switch (rating) {
		case RATING_HAPPY:
			return true;
		case RATING_NORMAL:
			return true;
		case RATING_SAD:
			return true;
		default:
			return false;
		}
		
	}
	
	public static boolean isValidDate(Date date) {
		if (date == null) {
			return false;
		}
		if (date.getYear() < 2013) {
			return false;
		}
		return true;
		
	}
	
	public static boolean isValidComment(String comment) {
		if (comment == null) {
			return false;
		}
		return true;
	}
	
	public ParseObject toParseObject() {
		ParseObject toReturn = new ParseObject(ParseHelper.CLASS_SUBMISSIONS);
		toReturn.put(S_COMMENT, getComment());
		toReturn.put(S_RATING, getRating());
		toReturn.put(S_RESTID, getRestaurantId());
		toReturn.put(S_WAITTIME, getWaitTime());
		toReturn.put(S_UUID, getSubmissionId());
		return toReturn;
	}
	
	/**
	 * This is what gets displayed by the ListView with ArrayAdapter<Submission>
	 */
	@Override
	public String toString() {
		
		String ratingStr;
		switch(getRating()) {
		case RATING_HAPPY:
			ratingStr = ":)";
			break;
		case RATING_NORMAL:
			ratingStr = ":|";
		case RATING_SAD:
			ratingStr = ":(";
			break;
			
		default :
			ratingStr = ":)";
			setRating(RATING_HAPPY);
			break;
		}
		
		return "" + ratingStr + " " + this.getComment() + " " + this.getWaitTime() + " seconds wait time";
	}
	
}
