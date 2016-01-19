package com.sortable;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {


    private final static Pattern PATTERN1 = Pattern.compile("[_-]|[;+]");
    private final static Pattern PATTERN2 = Pattern.compile("( )+");
    public final static String replaceSplit = " ";
    
    /*
     * Replace symbols or special characters to a single space
     * 
     * @param src = sorce string
     * 
     * @return replaced string
     */
    public static String replaceMultipleCharactersToSingleSpace(String src)
    {
    	src = PATTERN1.matcher(src).replaceAll(replaceSplit).toLowerCase();
    	return PATTERN2.matcher(src).replaceAll(replaceSplit);
    }
    
    /*
     * Convert a string to a Set and limit the size of the Set
     * 
     * @param string = source string
     * @param limit = number max of elements in the Set
     * 
     * @return Set of strings
     */
    public static Set<String> stringToSet(String string, int limit)
    {
    	//create an array based on the string and set a limit of elements in the set
		String[] array = string.trim().split(replaceSplit);
    	
    	int i=0;
    	int t = Math.min(array.length, limit);//find the minimum size of the Set
    	Set<String> set = new HashSet<String>(); 
    	while(i<t) set.add( array[i++] );
    	return set;
    }
    
}
