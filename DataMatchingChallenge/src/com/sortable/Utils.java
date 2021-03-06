package com.sortable;

import java.util.*;
import java.util.regex.Pattern;

public class Utils {


    private final static Pattern PATTERN1 = Pattern.compile("[_-]|[;+]");
    private final static Pattern PATTERN2 = Pattern.compile("( )+");
    public final static String replaceSplit = " ";

    private final static int limitSet = 5; //max limit of words to compare
    
    /*
     * Replace symbols or special characters to a single space
     * 
     * @param src = sorce string
     * 
     * @return replaced string
     */
    private static String replaceMultipleCharactersToSingleSpace(String src)
    {
    	src = PATTERN1.matcher(src).replaceAll(replaceSplit).toLowerCase();
    	return PATTERN2.matcher(src).replaceAll(replaceSplit);
    }
    
    /*
     * Convert a string to a Set and limit the size of the Set
     * 
     * @param string = source string
     * 
     * @return Set of strings
     */
    public static Set<String> stringToSet(String string)
    {
    	string = replaceMultipleCharactersToSingleSpace(string);
    	//create an array based on the string and set a limit of elements in the set
		String[] array = string.trim().split(replaceSplit);
    	
    	int i=0;
    	int t = Math.min(array.length, limitSet);//find the minimum size of the Set
    	Set<String> set = new HashSet<String>(); 
    	while(i<t) set.add( array[i++] );
    	return set;
    }
    
}
