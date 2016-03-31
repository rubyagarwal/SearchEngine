package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper {

	public Connection connectToDB(String dbName) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return c;
	}

	public void createTable(String dbName, String sql) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public void insertIntoTable(String dbName, String sql) {
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			c.commit();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}

	public String readFromTableBasedOnURL(String dbName, String sql) {
		StringBuffer str = new StringBuffer();
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				str.append(id);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return str.toString();
	}

	public String readFromTableBasedOnID(String dbName, String sql) {
		String str = new String();
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				str = rs.getString("url");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return str;
	}
	
	public Map<String, Float> fetchUrls(String dbName, String sql) {
		Map<String, Float> ret = new HashMap<String, Float>();
		Connection c = null;
		Statement stmt = null;
		try {
			c= connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
//			System.out.println("Will execute query in db" + dbName);
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				int urlId = rs.getInt("URL");
				float tfidf = rs.getFloat("TFIDF");
				String sqlID = "SELECT URL FROM URLS WHERE ID = " + urlId +";";
				String urlStr = readFromTableBasedOnID(dbName, sqlID);
				ret.put(urlStr, tfidf);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return ret;
	}

	public int readCount(String dbName, String sql) {
		int count = 0;
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt("CNT");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return count;
	}

	public int fetchTotalTermsInUrl(String dbName, String sql) {

		int count = 0;
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt("TOTAL_WORDS");
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return count;
	
	}

	public List<String> fetchKeys(String dbName, String sql) {
		List<String> keys = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String key = rs.getString("KEY");
				keys.add(key);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return keys;
	
	}

	public List<String> fetchURLFreq(String dbName, String sql) {
		List<String> urlFreq = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;
		try {
			c = connectToDB(dbName);
			c.setAutoCommit(false);

			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				int urlId = rs.getInt("URL");
				int freq = rs.getInt("FREQ");
				urlFreq.add(urlId+":"+freq);
			}
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return urlFreq;
	
	}

}
