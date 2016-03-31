package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import snowballstemmer.PorterStemmer;

public class SegmentCreator {

	public static void main(String[] args) throws IOException {
		new SegmentCreator().parseFile();
	}

	private void parseFile() throws IOException {
		File newF = new File("newDump.txt");
		if (!newF.exists()) {
			newF.createNewFile();
		}
		FileWriter fw = new FileWriter(newF.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		Map<String, String> map = new HashMap<String, String>();
		int recordLen = 5;
		String s;
		File file = new File("dump.txt");
		FileInputStream fi = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fi));
		while ((s = br.readLine()) != null) {
			StringBuffer newLine = new StringBuffer();
			if (s.startsWith("Recno::")) {
				recordLen = 2;
				System.out.println("processing 1" + s);
				while (recordLen-- >= 0) {
					s = br.readLine();
					if (s.startsWith("URL::")) {
						s = s.replace("URL:: ", "");
						newLine.append(s);
					} else if (s.startsWith("ParseText::")) {
						s = br.readLine();
						newLine.append("#rnr#");
						newLine.append(s);
					}
				}
			}
			if (!newLine.toString().isEmpty()) {
				System.out.println("NEWLINE : " + newLine);
				bw.write(newLine.toString());
				bw.write("\n");
			}
		}
		bw.close();
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

}
