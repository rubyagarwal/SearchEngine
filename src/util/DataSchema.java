package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import data.WordOccurencePerURL;



public class DataSchema {

	public static Map<String, Set<String>> keyUrlMap = new TreeMap<String, Set<String>>();
	public static Set<String> urls = new TreeSet<String>();
	public static List<WordOccurencePerURL> wopu = new ArrayList<WordOccurencePerURL>();
	public static List<String> stopWords = Arrays.asList( "me", "my", "myself", "we", "us","in","on",
			"are", "by","to","of","if","ours", "ourselves", "you", "your", "yours", "yourself",
			"yourselves", "he", "him", "his", "himself", "she", "her", "hers",
			"herself", "it", "its", "itself", "they", "them", "there", "their",
			"theirs", "themselves", "what", "which", "who", "whom", "this",
			"that", "these", "those", "am", "is", "are", "was", "were", "be",
			"been", "being", "have", "has", "had", "having", "do", "does", "into", "too",
			"did", "doing", "done", "all", "any", "both", "each", "few", "when","the",
			"more", "most", "other", "some", "such", "R", "no", "nor", "not", "all", "arent", "havent", "didnt",
			"only", "own", "same", "so", "than", "too", "null", "very", "td", "tnt", "rss" );

	public void createMap() throws IOException {
		File file = new File("dataSet.txt");
		// System.out.println("reading " + file.getName());
		FileInputStream fi = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fi));

		// int[][] read = new int[size][size];
		String s;
		while ((s = br.readLine()) != null) {

			// System.out.println(s);
			String[] sp = s.split(",");
			String word = new String();
			Set<String> urlList = new TreeSet<String>();
			for (int i = 1; i < sp.length; i++) {
				if (!sp[i].isEmpty() && sp[i].startsWith("http")) {
					word = sp[0];
					word = processWord(word);
					if(!"null".equalsIgnoreCase(word)){
						String spl[] = sp[i].split("#cnt#");
						int count = Integer.valueOf(spl[1].trim());
						String url = spl[0].trim().replace("'", "");
						WordOccurencePerURL wo = new WordOccurencePerURL(word, url,
								count);
						// System.out.println(wo.toString());
						wopu.add(wo);
						urlList.add(url);
					}
				}
			}
			if(!urlList.isEmpty())
				urls.addAll(urlList);

			if (!urlList.isEmpty()) {
				Set<String> prevList = new TreeSet<String>();
				if (keyUrlMap.containsKey(word)) {
					prevList = keyUrlMap.get(word);
					// System.out.println(prevList);
					urlList.addAll(prevList);

				}
				keyUrlMap.put(word, urlList);
			}
		}
		br.close();

		 for (Entry<String, Set<String>> entry : keyUrlMap.entrySet()) {
		 System.out.println(entry.getKey() + ":" + entry.getValue());
		 }
		System.out.println("Size:" + keyUrlMap.size());
		System.out.println("WOPU:" + wopu.size());
	}

	private String processWord(String word) {
		// stop word
		// length > 1
		// only alphabets
		// remove ', ", ), (, !,
		// lowercase

		Pattern PATTERN = Pattern.compile( "^(-?0|-?[1-9]\\d*)(\\.\\d+)?(E\\d+)?$" );
		String pWord = "null";
		word = word.toLowerCase().trim();
		word = word.replace("-","").replace("/","").replace(":","").replace("?","").replace("'", "").replace("\"", "").replace(")", "").replace("(", "").replace("!", "").replace(".", "");
		if(!word.isEmpty() && word.length() > 1 && !PATTERN.matcher( word ).matches()){
			if(stopWords.contains(word)){
				pWord = "null";
			} else {
				pWord = word;
			}
		}
		return pWord;
	}

	public static void main(String[] args) throws IOException {
		new DataSchema().createMap();
	}
}