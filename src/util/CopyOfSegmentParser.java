package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import snowballstemmer.PorterStemmer;

public class CopyOfSegmentParser {

	public static void main(String[] args) throws IOException {
		new CopyOfSegmentParser().parseFile();
	}

	private void parseFile() throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		int recordLen = 5;
		String s;
		File file = new File("sample.txt");
		FileInputStream fi = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fi));
		while ((s = br.readLine()) != null) {
			if (s.startsWith("Recno::")) {
				String url = new String();
				recordLen = 2;
				System.out.println("processing 1" + s);
				while (recordLen-- >= 0) {
					s = br.readLine();
					if (s.startsWith("URL::")) {
						System.out.println("processing 2" + s);
						s = s.replace("URL:: ", "");
						// StringTokenizer tokenizer = new StringTokenizer(s);
						// while (tokenizer.hasMoreTokens()) {
						url = s;
						String temp = url;
						temp = temp.replace("http://", "");
						temp = temp.replace(".com", "");
						temp = temp.replace(" ", "");
						PorterStemmer stemmer = new PorterStemmer();

						String[] slash = temp.split("/");
						for (String str : slash) {
							if (!str.contains(".") && !str.contains("-")
									&& !str.contains(":") && !str.contains("_")) {
								stemmer.setCurrent(str);
								if (stemmer.stem())
									str = stemmer.getCurrent();
								map.put(str, url+"$$1");
							} else {
								if (str.contains(".")) {
									String[] dot = str.split("\\.");
									for (String d : dot) {
										stemmer.setCurrent(d);
										if (stemmer.stem())
											d = stemmer.getCurrent();
										map.put(d, url+"$$1");
									}
								}
								if (str.contains("-")) {
									String[] hyphen = str.split("-");
									for (String h : hyphen) {
										stemmer.setCurrent(h);
										if (stemmer.stem())
											h = stemmer.getCurrent();

										map.put(h, url+"$$1");
									}
								}
								if (str.contains(":")) {
									String[] colon = str.split(":");
									for (String c : colon) {
										stemmer.setCurrent(c);
										if (stemmer.stem())
											c = stemmer.getCurrent();
										map.put(c, url+"$$1");
									}
								}

								if (str.contains("_")) {
									String[] uscore = str.split("_");
									for (String u : uscore) {
										stemmer.setCurrent(u);
										if (stemmer.stem())
											u = stemmer.getCurrent();
										map.put(u, url+"$$1");
									}
								}

							}
						}

						// } // while
					} else if (s.startsWith("ParseText::")) {
						String ps = br.readLine();
						System.out.println("processing 3" + ps);
						StringTokenizer tokenizer = new StringTokenizer(ps);
						while (tokenizer.hasMoreTokens()) {
							String word = tokenizer.nextToken();
							//countWordOcuurence
							int count  = 0 ;
							count = countOccurrence(word, ps);
							PorterStemmer stemmer = new PorterStemmer();
							stemmer.setCurrent(word);
							if (stemmer.stem())
								word = stemmer.getCurrent();
							map.put(word, url+"$$"+count);
						} // while
					}
				}
			}
		}
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	private int countOccurrence(String word, String ps) {
		int cnt = 0;
		
		String [] splits = ps.split(" ");
		for(String s:splits){
			if(s.equalsIgnoreCase(word)){
				cnt++;
			}
		}
		
		return cnt;
	}
}
