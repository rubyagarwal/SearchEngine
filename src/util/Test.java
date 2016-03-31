package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import data.WordOccurencePerURL;
import database.DatabaseHelper;

public class Test {

	private static Map<String, Set<String>> mapping = new HashMap<String, Set<String>>();
	private static Set<String> urls = new TreeSet<String>();
	public static List<WordOccurencePerURL> wopu = new ArrayList<WordOccurencePerURL>();
	private static String dbName = "dataSet.db";

	public static void main(String[] args) throws IOException {
		new DataSchema().createMap();
//		mapping = DataSchema.keyUrlMap;
		urls = DataSchema.urls;
//		wopu = DataSchema.wopu;
//
		DatabaseHelper db = new DatabaseHelper();
//
//		createUrlsTable(dbName, db);
//		createMappingTable(dbName, db);
//		createWordFreqPerUrlTable(dbName, db);
//
//		insertUrls(dbName, db);
//		insertMapping(dbName, db);
//		insertWordFreqPerUrlTable(dbName, db);

//		updateTotalWordsForUrl(dbName, db);
		updateTfIdf(dbName, db);

//		Set<String> querySet = new HashSet<String>();
//		querySet.add("Computer");
//		querySet.add("Degre");
//		querySet.add("toda");
//		findUrls(dbName, db, querySet);
	}

	private static void updateTfIdf(String dbName, DatabaseHelper db) {
		int totalDocuments = urls.size();
		totalDocuments = 1000;
		String sql = new String();
		sql = "SELECT KEY FROM MAPPING;";
		// sql =
		// "SELECT KEY FROM MAPPING where key like '%rut%' or key like '%university%' or key like '%admiss%' or key like '%tuit%' or key like '%foot%' or key like '%degre%' or key like '%cour%' or key like '%departm%' or key like '%busch%';";

		List<String> keys = db.fetchKeys(dbName, sql);
		for (String k : keys) {
			sql = "SELECT URL,FREQ FROM WORD_COUNT_PER_URL WHERE KEY = '" + k
					+ "';";
			List<String> urlfreq = db.fetchURLFreq(dbName, sql);
			int numOfUrlsWithTerms = urlfreq.size();
			for (String u : urlfreq) {
				String spl[] = u.split(":");
				int urlID = Integer.valueOf(spl[0]);
				int freqInUrl = Integer.valueOf(spl[1]);
				sql = "SELECT TOTAL_WORDS FROM URLS WHERE ID = " + urlID + ";";
				int totalTermsInUrl = db.fetchTotalTermsInUrl(dbName, sql);

				float tf = Float.valueOf(freqInUrl)
						/ (Float.MIN_VALUE + Float.valueOf(totalTermsInUrl));
				System.out.println(tf);
				float idf = (float) Math
						.log10(Float.valueOf(totalDocuments)
								/ (Float.MIN_VALUE + Float
										.valueOf(numOfUrlsWithTerms)));
				System.out.println(idf);
				float tfidf = tf * idf;

				sql = "UPDATE WORD_COUNT_PER_URL SET TFIDF = " + tfidf
						+ " WHERE  KEY = '" + k + "' and  URL = " + urlID + ";";
				System.out.println(sql);
				db.insertIntoTable(dbName, sql);
			}
		}

	}

	private static void insertWordFreqPerUrlTable(String dbName,
			DatabaseHelper db) {
		System.out.println("WORD COUNT PER TABLE STARTED");
		String sql = new String();
		int i = 0;
		for (WordOccurencePerURL w : wopu) {
			sql = "SELECT * FROM URLS WHERE URL = ";
			sql = sql.concat("\'").concat(w.getUrl()).concat("\'");
			String urlId = db.readFromTableBasedOnURL(dbName, sql);
			sql = "INSERT INTO WORD_COUNT_PER_URL (ID,KEY,URL,FREQ, TFIDF) VALUES ("
					+ (++i)
					+ ", '"
					+ w.getWord()
					+ "', "
					+ Integer.valueOf(urlId) + "," + w.getCount() + ",0.0);";
			System.out.println(sql);
			db.insertIntoTable(dbName, sql);
		}
	}

	private static void createWordFreqPerUrlTable(String dbName,
			DatabaseHelper db) {
		String sql = "CREATE TABLE WORD_COUNT_PER_URL "
				+ "(ID INT PRIMARY KEY     NOT NULL,"
				+ " KEY			 TEXT 	 NOT NULL," + " URL           INT	 NOT NULL,"
				+ " FREQ		  INT	 NOT NULL," + " TFIDF		  REAL   NOT NULL)";
		db.createTable(dbName, sql);

	}

	public Map<List<String>, List<String>> findURLsForQuery(Set<String> querySet) {
		dbName = "/media/ruby/Projects/eclipseWorkspace/rusearch/RUSearchEngine/searchTest2.db";
		return findUrls(dbName, new DatabaseHelper(), querySet);
	}

	private static Map<List<String>, List<String>> findUrls(String dbName, DatabaseHelper db,
			Set<String> querySet) {
		Map<List<String>, List<String>> finalList = new LinkedHashMap<List<String>, List<String>>();
		List<String> qList = new ArrayList<String>(querySet);
		String[] qArr = new String[qList.size()];
		qArr = qList.toArray(qArr);

		// Create an initial vector/set
		ICombinatoricsVector<String> initialSet = Factory.createVector(qArr);
		for (int k = qArr.length; k > 0; k--) {
			Generator<String> gen = Factory.createSimpleCombinationGenerator(
					initialSet, k);
			for (ICombinatoricsVector<String> combination : gen) {
				List<String> subset = combination.getVector();
				System.out.println("subset " + subset.size() + " " + subset);
				Map<String, Map<String, Float>> urls = new HashMap<String, Map<String, Float>>();
				for (String s : subset) {
					String sql = "SELECT KEY,URL,TFIDF FROM WORD_COUNT_PER_URL WHERE KEY like '%"
							+ s + "%';";
					Map<String, Float> resp = new HashMap<String, Float>();
					resp = db.fetchUrls(dbName, sql);
					urls.put(s, resp);
				}
				List<String> tempL = new ArrayList<String>();
				tempL = findIntersection(urls);
				List<String> strL = new ArrayList<String>(qList);
				if (!tempL.isEmpty()) {
					strL.removeAll(subset);
				}
				finalList.put(strL, tempL);
			}
		}
		System.out.println("FINAL LIST " + finalList);
		return finalList;
	}

	private static List<String> findIntersection(
			Map<String, Map<String, Float>> urls) {
		Collection<String> urlList = new ArrayList<String>();
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		Map<String, Float> scoreMap = new HashMap<String, Float>();

		for (Map<String, Float> url : urls.values()) {
			for (Entry<String, Float> info : url.entrySet()) {
				if (countMap.containsKey(info.getKey())) {
					int cnt = countMap.get(info.getKey());
					cnt++;
					countMap.put(info.getKey(), cnt);

					float tf = scoreMap.get(info.getKey());
					tf = tf + info.getValue();
					scoreMap.put(info.getKey(), tf);
				} else {
					countMap.put(info.getKey(), 1);
					scoreMap.put(info.getKey(), info.getValue());
				}
			}
		}

//		System.out.println("Score map " + scoreMap);
		Map<Float, String> fMap = new TreeMap<Float, String>();
		for (Entry<String, Integer> c : countMap.entrySet()) {
			if (c.getValue() == urls.keySet().size()) {
				fMap.put((1 - scoreMap.get(c.getKey())), c.getKey());
			}
		}
		urlList = fMap.values();
//		System.out.println("URLs found " + urlList);
		return new ArrayList<String>(urlList);
	}

	private static void displayFetchedUrls(Map<String, Set<String>> resp) {
		System.out.println("Response size : " + resp.size());
		for (Entry<String, Set<String>> entry : resp.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}

	}

	private static void insertMapping(String dbName, DatabaseHelper db) {
		String sql = new String();
		int i = 0;
		for (Entry<String, Set<String>> entry : mapping.entrySet()) {
			System.out.println("*******KEY********" + entry.getKey());
			StringBuffer ids = new StringBuffer();
			for (String s : entry.getValue()) {
				if (s != null && !s.isEmpty()) {
					s = s.replace("'", "");
					sql = "SELECT * FROM URLS WHERE URL = ";
					sql = sql.concat("\'").concat(s).concat("\'");
					System.out.println(sql);
					String result = db.readFromTableBasedOnURL(dbName, sql);
					if (!result.isEmpty()) {
						ids.append(result);
						ids.append(",");
					}
				}
			}
			if (ids.toString() != null && !ids.toString().isEmpty()
					&& ids.length() > 0) {
				sql = "INSERT INTO MAPPING (ID,KEY,URLS,URL_COUNT) "
						+ "VALUES (" + (++i) + ", '" + entry.getKey() + "', '"
						+ ids.toString() + "'," + entry.getValue().size()
						+ ");";
				System.out.println(sql);
				db.insertIntoTable(dbName, sql);
			}
		}
	}

	private static void insertUrls(String dbName, DatabaseHelper db) {
		String sql = new String();
		int i = 0;
		for (String u : urls) {
			sql = "INSERT INTO URLS (ID,URL) " + "VALUES (" + (++i) + ", '" + u
					+ "');";
			System.out.println(sql);
			db.insertIntoTable(dbName, sql);
		}
	}

	private static void updateTotalWordsForUrl(String dbName, DatabaseHelper db) {
		String sql = new String();
		for (int j = 1; j <= urls.size(); j++) {
			sql = "SELECT SUM(FREQ) AS CNT FROM WORD_COUNT_PER_URL WHERE URL = "
					+ j + ";";
			System.out.println(sql);
			int cnt = db.readCount(dbName, sql);

			sql = "UPDATE URLS SET TOTAL_WORDS = " + cnt + " where ID = " + j
					+ ";";
			System.out.println(sql);
			db.insertIntoTable(dbName, sql);
		}
	}

	private static void createMappingTable(String dbName, DatabaseHelper db) {
		String sql = "CREATE TABLE MAPPING "
				+ "(ID INT PRIMARY KEY     NOT NULL,"
				+ " KEY			 TEXT 	 NOT NULL,"
				+ " URLS           TEXT	 NOT NULL,"
				+ " URL_COUNT		INT	 NOT NULL)";
		db.createTable(dbName, sql);
	}

	private static void createUrlsTable(String dbName, DatabaseHelper db) {
		String sql = "CREATE TABLE URLS " + "(ID INT PRIMARY KEY     NOT NULL,"
				+ " URL            TEXT	 NOT NULL," + " TOTAL_WORDS    INT )";
		db.createTable(dbName, sql);
	}
}
