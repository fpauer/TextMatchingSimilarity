package com.sortable.json;

import java.util.Set;

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
	
	private Set<String> comparableValues;
	
	public JsonObject()
	{
		super();
	}

	public JsonObject(String string) throws JSONException
	{
		super(string);
	}


	public String getComparableValueAsString()
	{
		String string = "";
		for(String s: comparableValues) string += s + " ";
		return string;
	}
	
	public void setComparableValue(Set<String> comparableValues)
	{
		this.comparableValues = comparableValues;
	}

	/*
	 * Comparable Value used to match the elements
	 */
	public Set<String> getComparableValue()
	{
		return comparableValues;
	}
}
