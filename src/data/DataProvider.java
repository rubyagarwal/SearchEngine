package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataProvider {

	public Map<String, Set<String>> getSampleList(){
		List<String> urls = new ArrayList<String>();
		urls.add("http://www.rutgers.edu");
		urls.add("http://www.rutgers.edu/admissions");
		urls.add("http://www.harvard.edu");
		urls.add("http://www.harvard.edu/admission");
		urls.add("http://www.princeton.edu");
		
		Map<String, Set<String>> mapping = new HashMap<String, Set<String>>();
		Set<String> list = new HashSet<String>();
		list.add("http://www.rutgers.edu");
		list.add("http://www.rutgers.edu/admissions");
		mapping.put("rutgers", list);
		
		list = new HashSet<String>();
		list.add("http://www.harvard.edu");
		list.add("http://www.harvard.edu/admission");
		mapping.put("harvard", list);
		
		list = new HashSet<String>();
		list.add("http://www.harvard.edu/admission");
		list.add("http://www.rutgers.edu/admissions");
		mapping.put("admission", list);
		
		list = new HashSet<String>();
		list.add("http://www.princeton.edu");
		mapping.put("princeton", list);
		
		return mapping;
	}
	
}
