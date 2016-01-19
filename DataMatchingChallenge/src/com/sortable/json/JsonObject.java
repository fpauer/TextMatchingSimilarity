package com.sortable.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.sortable.Utils;

/*
 * Custom JSONObject class
 * 
 * Similar to the JSONObject class however it stores and gets
 * a comparable string which will be used to match the elements
 */
public class JsonObject extends JSONObject {

	public static final String COMPARABLE_KEY = "comparableKey";

	public JsonObject()
	{
		super();
	}

	public JsonObject(String string) throws JSONException
	{
		super(string);
	}

	/*
	 * Comparable Value used to match the elements
	 */
	public String getComparableValue()
	{
		try {
			return this.get(COMPARABLE_KEY).toString();
		} catch (JSONException e) {
			return "";
		}
	}
}
