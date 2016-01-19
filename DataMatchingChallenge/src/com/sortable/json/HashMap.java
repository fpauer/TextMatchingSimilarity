package com.sortable.json;

import java.util.*;
import java.io.*;

import org.json.JSONException;

import com.sortable.Utils;


public class HashMap<K, V> extends LinkedHashMap<K, V> implements GenericDataType{
	
	public static final long serialVersionUID = 1L;
	public String[] comparableKeys;
	public String uniqueKey;

	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public HashMap( InputStream is, String uniqueKey, String[] comparableKeys ) throws IOException, JSONException
	{
		if( uniqueKey == null || comparableKeys.length == 0) throw new IllegalArgumentException("Please, define an unique key.");
		if( comparableKeys == null || comparableKeys.length == 0) throw new IllegalArgumentException("Please, define at least one comparable key.");
		
		this.uniqueKey = uniqueKey;
		this.comparableKeys = comparableKeys;
		try 
		{
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
	
	    	   //add json to HashMap
	    	   this.put( (K)jsonObject.get(uniqueKey), (V)jsonObject);
	       }
	   } catch (IOException e) {
	       throw new IOException(e);
	   } catch (JSONException e) {
	       throw new JSONException(e);
	   }
	}
	
	/*
	 * 
	 */
	public void clearValues()
	{
		for(Map.Entry<K,V> e: this.entrySet())
		{
			this.put(e.getKey(), null);  //the value will be create dynamically
		}
	}

	public String getUniqueKey()
	{
		return uniqueKey;
	}
	
	public String[] getComparableKeys()
	{
		return comparableKeys;
	}
	
}
