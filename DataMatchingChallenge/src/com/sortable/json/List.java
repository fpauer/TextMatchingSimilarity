package com.sortable.json;

import java.io.*;
import java.util.*;
import org.json.JSONException;

import com.sortable.Utils;


public class List<T> extends ArrayList<T> implements GenericDataType{

	private static final long serialVersionUID = 2L;
	
	public String[] comparableKeys;
	
	/*
	 * 
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
	    	   jsonObject.put(JsonObject.COMPARABLE_KEY, Utils.replaceMultipleCharactersToSingleSpace(value) );
	
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
