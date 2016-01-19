package com.sortable;

import java.io.*;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import com.sortable.json.HashMap;
import com.sortable.json.JsonObject;
import com.sortable.json.List;

/*
 * References:
 * 
 * https://en.wikipedia.org/wiki/Record_linkage
 * http://rduin.nl/papers/ssspr_06_protsel.pdf
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html
 * 
 * http://en.wikipedia.org/wiki/MinHash
 * http://web.archive.org/web/20081224234350/http://www.dcs.shef.ac.uk/~sam/stringmetrics.html#jaccard
 * 
 * time complexity
 * 
 * O(n * m)
 */
public class DataMatchingChallenge {

	
	public static void main(String[] args)
	{
		DataMatchingChallenge challenge = new DataMatchingChallenge();
		challenge.run();
	}

	@SuppressWarnings("unchecked")
	public void run()
	{
		long startTime = System.currentTimeMillis();
		long runningTime = 0;
		int matched = 0;

	    System.out.println("----- Text Similarity Matching using Jaccard coefficient -------------------------");
	    
		//reading files
		InputStream inputTarget = JaccardSimilarity.class.getResourceAsStream("/textfiles/products.txt");
		InputStream inputSource = JaccardSimilarity.class.getResourceAsStream("/textfiles/listings.txt");
		
		//defining the key string used to store the results
		String keyResults = "listings";
		
		//defining parameters to avoid  false positive cases
		double tolerance = 0.6; //40% of error tolerance
		
		//aux variables
		JaccardSimilarity<String> similarity = new JaccardSimilarity<String>();
		
		//control variables
		double bestSimilarity = Double.MAX_VALUE;
		String bestMatch = "";
		
		try {
			//load the target list ( products ) and defining the keys which will be used to match the data
			HashMap<String, JsonObject> targetList = new HashMap<String, JsonObject>(inputTarget, "product_name", new String[]{"manufacturer", "model", "family"} );

			//load the source list ( listings ) and defining the keys which will be used to match the data
			List<JsonObject> sourceList = new List<JsonObject>(inputSource, new String[]{"title"});
			
			//initialize the result list ( products+listings )
			HashMap<String, JsonObject> resultList = (HashMap<String, JsonObject>)targetList.clone();
			resultList.clearValues();
			
			int i=0;
			
			// for each source item find at most one target item
			for(JsonObject sourceItem: sourceList)
			{
				//just to show the running time
				i++;
				if(i%2000 == 0)
				{
					runningTime = (System.currentTimeMillis()-startTime);
					System.out.printf("Running Time for %d comparisons: %d milisecs \n", (i*targetList.size()) , runningTime);
				}
				
				//reset control variables
				bestSimilarity = Double.MAX_VALUE;
				bestMatch = "";
						
				//for each source item find the best match in the entire target list
				for(Map.Entry<String, JsonObject> targetItem: targetList.entrySet() )
				{

					//find the similiarity
					double currSimilarity = 1-similarity.compute(targetItem.getValue().getComparableValue(), sourceItem.getComparableValue());
					
					//check if the current similarity is better than the best found
					//check if the current similarity is lower than the defined tolerance 
					if(currSimilarity<=bestSimilarity && currSimilarity <= tolerance)
					{
						bestSimilarity = currSimilarity;
						bestMatch = targetItem.getKey();//take the best matched key
					}

				}

				//analyze the best Match
				if( !bestMatch.isEmpty() )
				{
					JsonObject json = targetList.get(bestMatch);
					
					matched++;//count
					
					//store the results
					json = resultList.get(bestMatch);
					if(json==null)//if that key wasn't inserted yet, add first
					{
						json = new JsonObject();
						json.put(targetList.getUniqueKey(), bestMatch);// Ex: "product_name":"Samsung_TL240"
						json.put( keyResults, new JSONArray());//Ex: "listings":[{"title":"Sony..."}]
					}

					JSONArray array = json.getJSONArray(keyResults);
					array.put(sourceItem);
					json.put(keyResults, array );
					
					resultList.put(bestMatch, json);
				}

			}

			//generate the JSON result file
			File file = new File("results.txt");
		    FileWriter writer = null;
		    try {
		        writer = new FileWriter(file);
		        
				for(Map.Entry<String, JsonObject> resultItem: resultList.entrySet() )
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
		        e.printStackTrace();
		    } finally {
		        if (writer != null) try { writer.close(); } catch (IOException ignore) {}
		    }
		    System.out.println("------------------------------------------------");
		    System.out.printf("Result File is located at %s%n", file.getAbsolutePath());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		runningTime = (System.currentTimeMillis()-startTime);
		System.out.printf("Total Running Time: %d seconds\n", (runningTime/1000));
		System.out.printf("Items matched: %d\n", matched);
		
	}
	
}
