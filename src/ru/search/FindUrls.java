package ru.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Test;
import util.Test_MT;

public class FindUrls {

	public Map<List<String>, List<String>> findUrls(Set<String> qs){
		Map<List<String>, List<String>> urls = new HashMap<List<String>, List<String>>();
		urls = new Test().findURLsForQuery(qs);
		
		return urls;
	}
	
}
