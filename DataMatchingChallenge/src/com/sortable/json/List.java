package com.sortable.json;

import java.io.*;
import java.util.*;
import org.json.JSONException;

import com.sortable.Utils;

/*
 * Custom List Class
 * 
 * Similar to the List class however it converts an InputStream in a collection of JsonObjects
 * 
 */
public class List<T> extends ArrayList<T> implements GenericDataType{

	private static final long serialVersionUID = 2L;
	
	public String[] comparableKeys;
	
	/*
	 * Initialize a List class using an InputStream and store the comparable keys
	 * 
	 *  @param is = InputStream with all elements in a JSON Format
	 *  @param comparableKeys = Array of String with the keys to set the Comparable Value
	 */
	@SuppressWarnings("unchecked")
	public List( InputStream is, String[] comparableKeys ) throws IOException, JSONException
	{
		if( comparableKeys == null || comparableKeys.length == 0) throw new IllegalArgumentException("Please, define at least one comparable key");
		this.comparableKeys = comparableKeys;
		
		try {
	       BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

	       String inputStr;
	       while ((inputStr = streamReader.readLine()) != null)
	       {
	    	   JsonObject jsonObject = new JsonObject(inputStr);
	    	   
	    	   //preparing the value to compare
	    	   String value = "";
		   	   try {
		   	 	    for(int i=0; i<comparableKeys.length; i++) value += jsonObject.get(comparableKeys[i]).toString() + " " ;
		   	   } catch (JSONException e) { }
	    	   jsonObject.setComparableValue( Utils.stringToSet(value) );
	
	    	   //add json to List
	    	   this.add( (T) jsonObject );
	       }
	   } catch (IOException e) {
	       throw new IOException(e);
	   } catch (JSONException e) {
	       throw new JSONException(e);
	   }
	}

	public String[] getComparableKeys()
	{
		return comparableKeys;
	}
}
