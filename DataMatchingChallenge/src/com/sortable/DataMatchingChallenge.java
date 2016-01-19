package com.sortable;

import java.io.*;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import com.sortable.json.HashMap;
import com.sortable.json.JsonObject;
import com.sortable.json.List;

public class DataMatchingChallenge {

	
	public static void main(String[] args)
	{
		DataMatchingChallenge challenge = new DataMatchingChallenge();
		challenge.run();
	}

	/*
	 * 
	 */
	public void run()
	{
		long startTime = System.currentTimeMillis();
		long runningTime = 0;
		int matched = 0;
		
		//reading files
		InputStream inputTarget = JaccardSimilarity.class.getResourceAsStream("/textfiles/products.txt");
		InputStream inputSource = JaccardSimilarity.class.getResourceAsStream("/textfiles/listings.txt");
		
		String keyResults = "listings";
		
		//defining parameters to avoid  false positive cases
		int limitSet = 5; //max limit of words to compare
		double tolerance = 0.6; //40% of error tolerance
		
		//aux variables
		JaccardSimilarity<String> similarity = new JaccardSimilarity<String>();
		String comparableStringSource;
		String comparableStringTarget;
		Set<String> sourceSet;
		Set<String> targetSet;
		
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
				i++;
				if(i%2000 == 0)
				{
					runningTime = (System.currentTimeMillis()-startTime);
					System.out.printf("Running Time for %d comparisons: %d milisecs \n", (i*targetList.size()) , runningTime);
				}
				
				//reset control variables
				bestSimilarity = Double.MAX_VALUE;
				bestMatch = "";

				//preparing the set
				sourceSet = Utils.stringToSet(sourceItem.getComparableValue(), limitSet);
						
				//for each source item find the best match in the entire target list
				for(Map.Entry<String, JsonObject> targetItem: targetList.entrySet() )
				{
					//preparing the set
					targetSet = Utils.stringToSet( targetItem.getValue().getComparableValue() , limitSet);

					//find the similiarity
					double currSimilarity = 1-similarity.compute(targetSet, sourceSet);
					
					if(currSimilarity<=bestSimilarity && currSimilarity <= tolerance)
					{
						bestSimilarity = currSimilarity;
						bestMatch = targetItem.getKey();
					}

				}

				//analyze the best Match
				if( !bestMatch.isEmpty() )
				{
					JsonObject json = targetList.get(bestMatch);
					
					matched++;//count
					
					//storing the results
					json = resultList.get(bestMatch);
					if(json==null)
					{
						json = new JsonObject();
						json.put(targetList.getUniqueKey(), bestMatch);
						json.put( keyResults, new JSONArray());
					}

					JSONArray array = json.getJSONArray(keyResults);
					sourceItem.remove(JsonObject.COMPARABLE_KEY);
					array.put(sourceItem);
					json.put(keyResults, array );
					
					resultList.put(bestMatch, json);
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
