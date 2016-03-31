//package util;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.paukov.combinatorics.Factory;
//import org.paukov.combinatorics.Generator;
//import org.paukov.combinatorics.ICombinatoricsVector;
//
//import database.DatabaseHelper;
//
////public class WorkerThread implements Runnable {
////	private String command;
////	private ICombinatoricsVector<String> init;
////	private int size;
////	private List<String> list;
////	private DatabaseHelper db = new DatabaseHelper();
////	private static String dbName = "I:\\eclipseWorkspace\\assignments\\cloud\\phase3\\RUSearchEngine\\searchTest2.db";
////
////	public WorkerThread(ICombinatoricsVector<String> initialSet, int k,
////			List<String> qList) {
////		this.init = initialSet;
////		this.size = k;
////		this.list = qList;
////	}
////
////	@Override
////	public void run() {
////		System.out.println(Thread.currentThread().getName()
////				+ " Start. Command = " + command);
////		processCommand(this.init, this.size);
////		System.out.println(Thread.currentThread().getName() + " End.");
////	}
////
////	private void processCommand(ICombinatoricsVector<String> initialSet, int k) {
////		try {
////			Thread.sleep(5000);
////			Generator<String> gen = Factory.createSimpleCombinationGenerator(
////					initialSet, k);
////			for (ICombinatoricsVector<String> combination : gen) {
////				List<String> subset = combination.getVector();
////				System.out.println("subset " + subset.size() + " " + subset);
////				Map<String, Map<String, Float>> urls = new HashMap<String, Map<String, Float>>();
////				for (String s : subset) {
////					String sql = "SELECT KEY,URL,TFIDF FROM WORD_COUNT_PER_URL WHERE KEY like '%"
////							+ s + "%';";
////					Map<String, Float> resp = new HashMap<String, Float>();
////					resp = db.fetchUrls(dbName, sql);
////					urls.put(s, resp);
////				}
////				List<String> tempL = new ArrayList<String>();
////				tempL = Test_MT.findIntersection(urls);
////				List<String> strL = new ArrayList<String>(this.list);
////				if (!tempL.isEmpty()) {
////					strL.removeAll(subset);
////				}
////				Test_MT.finalList.put(strL, tempL);
////			}
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
////	}
////
////	@Override
////	public String toString() {
////		return this.command;
////	}
//}
