package ru.search;

import java.io.*;

public class spell {

	public static void main(String args[]) {

		System.out.println(checker("korrect"));
		
	}
	
	public static String checker(String word)
	{
		String line = word;
		try {
			
			String runCmd = "cmd /c python E:/spell.py " + word;
			Process p = Runtime.getRuntime().exec(runCmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			line = reader.readLine();
			//while (line != null) {
				//System.out.println(line);
				//line = reader.readLine();
			//}

		} catch (IOException e1) {
		} catch (InterruptedException e2) {
		}
		return line;
	}
}
