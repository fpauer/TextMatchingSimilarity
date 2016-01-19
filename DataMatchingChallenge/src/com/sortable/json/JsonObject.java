package com.sortable.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.sortable.Utils;

public class JsonObject extends JSONObject {

	public static final String COMPARABLE_KEY = "comparableKey";

	/*
	 * 
	 */
	public JsonObject()
	{
		super();
	}

	/*
	 * 
	 */
	public JsonObject(String string) throws JSONException
	{
		super(string);
	}

	/*
	 * 
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
