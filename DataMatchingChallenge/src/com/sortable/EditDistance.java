package com.sortable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.sortable.json.HashMap;
import com.sortable.json.JsonObject;
import com.sortable.json.List;

/*
 * 
 * Wagner–Fischer algorithm
 * 1 - It's a better version of Levenshtein Distance algorithm using dynamic programing.
 * 2 - Calculate the edit distance between two strings, the least edit distance should be the best match
 * 3 - It's strongly recommended to match strings
 * 4 - It was modified from the original algorithm to a version with a better running time 
 *     using only two matrix rows and calculating only the diagonal 
 * O (k min (m, n)) time (where k is max distance)
 * 
 * References:
 * 
 * https://en.wikipedia.org/wiki/Record_linkage
 * https://www.inf.ed.ac.uk/publications/thesis/online/IM080663.pdf
 * https://en.wikipedia.org/wiki/Approximate_string_matching
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html
 * 
 * https://en.wikipedia.org/wiki/Levenshtein_distance
 * https://en.wikipedia.org/wiki/Wagner-Fischer_algorithm
 */
public class EditDistance {

	public static final int BitapLimit = 31;
	
	/*
	 * 
	 */
	public int getDistance(String str1, String str2)
	{
		//check if the string are equals
		if(str1.equalsIgnoreCase(str2)) return 0; //no differences so zero
		
		//if any string is empty so the distance is the other string length
		if(str1.isEmpty()) return str2.length();
		if(str2.isEmpty()) return str1.length();

		// swap if str2 is smaller than str1 
		// it reduces the memory usage 
		if(str2.length()<str1.length())
		{
			String sTemp = str2;
			str2 = str1;
			str1 = sTemp;
		}
		
		char[] s1 = str1.toLowerCase().toCharArray();
		char[] s2 = str2.toLowerCase().toCharArray();
		
		//create two vectors of int to count the differences
		int[] v0 = new int[s2.length + 1]; // +1 because position 0 must be empty ??
		int[] v1 = new int[s2.length + 1]; // +1 because position 0 must be empty
		
		// initializes v0 ( previous row with distance )
		// it is similar to A[0][i] in original Levenshtein Distance algorithm version
	    // the distance is just the number of characters to delete from str2
		for(int i=0; i<v0.length; i++) v0[i] = i;

		for(int i=0; i<s1.length; i++)
		{
	        // calculate v1 (current row distances) from the previous row v0

	        // first element of v1 is A[i+1][0] - Similiar as the original Levenshtein version
			// edit distance is delete (i+1) chars from s to match empty t
			v1[0] = i + 1;
			
			for (int j = 0; j < s2.length; j++)
	        {
	            int cost = (s1[i] == s2[j]) ? 0 : 1; // comparing the letters
	            v1[j + 1] = Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost) );
	            
	            //avoiding a second loop 
	            if(j>0) v0[j-1] = v1[j-1];//copying the previous element
	        }

			v0[s2.length-1] = v1[s2.length-1];//copying the previous element
			v0[s2.length] = v1[s2.length];//copying the last element
		}
		
		return v1[s2.length];
	}
	

	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		long startTime = System.currentTimeMillis();
		long runningTime = 0;
		int matched = 0;
		
		InputStream inputTarget = EditDistance.class.getResourceAsStream("/textfiles/products.txt");
		InputStream inputSource = EditDistance.class.getResourceAsStream("/textfiles/listings.txt");
		
		String keyResults = "listings";
		
		//defining parameters to avoid  false positive cases
		int limitDistance = 50; //max limit of characters to compare
		double tolerance = 1.2; //20% tolerance limit of distance
			
		//aux variables
		EditDistance distance = new EditDistance();
		String comparableStringSource;
		String comparableStringTarget;
		
		//control variables
		int leastDistance = Integer.MAX_VALUE;
		String bestMatch = "";
		
		try {
			//load the target list ( products ) and defining the key which will used to match
			HashMap<String, JsonObject> targetList = new HashMap<String, JsonObject>(inputTarget, "product_name", new String[]{"product_name"});

			//load the source list ( listings ) and defining the key which will used to match
			List<JsonObject> sourceList = new List<JsonObject>(inputSource, new String[]{"title"});

			//initialize the result list ( products+listings )
			HashMap<String, JsonObject> resultList = (HashMap<String, JsonObject>)targetList.clone();
			resultList.clearValues();
			
			int i=0;
			
			// for each source item find at most one target item
			for(JsonObject sourceItem: sourceList)
			{
				i++;
				if(i%2000 == 0)
				{
					runningTime = (System.currentTimeMillis()-startTime);
					System.out.println("Running Time for " + (i*targetList.size()) + " comparisons: "+ runningTime + " milisecs");
				}
				
				//reset control variables
				leastDistance = Integer.MAX_VALUE;
				bestMatch = "";
				
				//preparing the source item
				comparableStringSource = sourceItem.getComparableValueAsString();
				
				//reducing the string size up to 50+20% characters to avoid incorrect matches
				if(comparableStringSource.length()>(limitDistance*tolerance)) comparableStringSource = comparableStringSource.substring(0, (int)(limitDistance*tolerance) );

				//for each source item look find the best match in the entire source list
				for(Map.Entry<String, JsonObject> targetItem: targetList.entrySet() )
				{
					//preparing the target item
					comparableStringTarget = targetItem.getValue().getComparableValueAsString();
					
					//reducing the string size up to 50+20% characters to avoid incorrect matches
					if(comparableStringTarget.length()>(limitDistance*tolerance)) comparableStringTarget = comparableStringTarget.substring(0, (int)(limitDistance*tolerance) );
					
					//find the distance between the both strings
					int currDistance = distance.getDistance(comparableStringTarget, comparableStringSource);

					// if the best edit distance is greater than limitDistance , considering a limit of (limitDistance+20%) characters 
					// so the result isn't accurate to confirm the match
					if(currDistance<=leastDistance && currDistance <= limitDistance)
					{
						leastDistance = currDistance;
						bestMatch = targetItem.getKey();
					}

				}

				//analyze the best Match
				if( !bestMatch.isEmpty() )
				{
					boolean bindResult = true;
					JsonObject json = targetList.get(bestMatch);
					
					//check if the manufacturer is the same to a better result and avoid false positives
					if( !sourceItem.getString("manufacturer").isEmpty() && !sourceItem.getString("title").isEmpty() )
					{
						try {
							String manufacturerSource = sourceItem.getString("title").split(" ")[0];
							String manufacturerTarget = json.getString("manufacturer");
							
							manufacturerSource = manufacturerSource.toLowerCase();
							manufacturerTarget = manufacturerTarget.toLowerCase();
							
							if( manufacturerTarget.length() > manufacturerSource.length() ) bindResult = manufacturerTarget.contains( manufacturerSource );
							else bindResult = manufacturerSource.contains( manufacturerTarget );
							
							//if(!bindResult) System.out.println( manufacturerSource + " == " +  manufacturerTarget );
						} catch (Exception e) {
							System.out.println(json);
						}
					}
					
					//only register the match if the manufacturer are the same or if it is empty
					if( bindResult )
					{
						matched++;
						//storing the results
						json = resultList.get(bestMatch);
						if(json==null)
						{
							json = new JsonObject();
							json.put(targetList.getUniqueKey(), bestMatch);
							json.put( keyResults, new JSONArray());
						}

						JSONArray array = json.getJSONArray(keyResults);
						array.put(sourceItem);
						json.put(keyResults, array );
						
						resultList.put(bestMatch, json);
					}
				}

			}

			//generate the JSON file result
			File file = new File("results.txt");
		    FileWriter writer = null;
		    try {
		        writer = new FileWriter(file);
		        
				for(Map.Entry<String, JsonObject> resultItem: resultList.entrySet() )//look the best match in the source list
				{
					JsonObject json = resultItem.getValue();
					
					if(json == null)
					{
						json = new JsonObject();
						json.put(targetList.getUniqueKey(), resultItem.getKey());
						json.put( keyResults, new JSONArray());
					}
					
					writer.write( json.toString() );
			        writer.write( "\n" );
				}
				
		    } catch (IOException e) {
		        e.printStackTrace(); // I'd rather declare method with throws IOException and omit this catch.
		    } finally {
		        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		    }
		    System.out.printf("Result File is located at %s%n", file.getAbsolutePath());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		runningTime = (System.currentTimeMillis()-startTime);
		System.out.println("Running Time Total:"+ (runningTime/1000) + " seconds" );
		System.out.println("Items matched:" + matched );
		
		
	}

}
