package com.sortable;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {


    private final static Pattern PATTERN1 = Pattern.compile("[_-]|[;+]");
    private final static Pattern PATTERN2 = Pattern.compile("( )+");
    public final static String replaceSplit = " ";
    
    /*
     * 
     */
    public static String replaceMultipleCharactersToSingleSpace(String src)
    {
    	src = PATTERN1.matcher(src).replaceAll(replaceSplit).toLowerCase();
    	return PATTERN2.matcher(src).replaceAll(replaceSplit);
    }
    
    /*
     * 
     */
    public static Set<String> stringToSet(String string, int limit)
    {
		String[] array = string.trim().split(replaceSplit);
    	
    	int i=0;
    	int t = Math.min(array.length, limit);
    	Set<String> set = new HashSet<String>(); 
    	while(i<t) set.add( array[i++] );
    	return set;
    }
    
}
