package com.sortable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sortable.json.HashMap;
import com.sortable.json.List;

/**
 * Jaccard similarity coefficient http://en.wikipedia.org/wiki/MinHash
 */
public class JaccardSimilarity<T> {
    private final Set<T> intersect = new HashSet<>();
    private final Set<T> union = new HashSet<>();

	/*
	 * 
	 */
    public double compute(Set<T> set1, Set<T> set2)
    {
        intersect.clear();
        intersect.addAll(set1);
        intersect.retainAll(set2);
        union.clear();
        union.addAll(set1);
        union.addAll(set2);
        return (double)intersect.size()/(double)union.size();
    }
    
}
