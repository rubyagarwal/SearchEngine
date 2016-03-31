package ru.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import snowballstemmer.PorterStemmer;
import util.DataSchema;

@WebServlet(name = "Simple Servlet", urlPatterns = "/getServlet")
public class SimpleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static String checker(String word) {
		String line = word;
		try {

			String runCmd = "/usr/bin python /media/ruby/Projects/eclipseWorkspace/rusearch/RUSearchEngine/spell.py " + word;
			Process p = Runtime.getRuntime().exec(runCmd);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			line = reader.readLine();

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		return line;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("get request");
		response.sendRedirect("register.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
//		System.out.println("search"+request.getParameter("name"));
//		if(request.getParameter("name").equals("search"))
//		{
//			System.out.println("search");
//		}
//		if(request.getParameter("name").equals("searchInstd"))
//		{
//			System.out.println("Instreda");
//		}
		
		String query = request.getParameter("name");
		PorterStemmer stemmer = new PorterStemmer();
		Set<String> qs = new HashSet<String>();
		StringBuffer corrected = new StringBuffer();
		String[] splits = query.split(" ");
		for (String s : splits) {
			s=s.trim();
			s = s.toLowerCase();
			if(!s.isEmpty() && !DataSchema.stopWords.contains(s) && s.length() > 1){
				String queryCorrected = checker(s);
//				System.out.println("Search Query : " + query + " Corrected "
//						+ queryCorrected);
				corrected.append(queryCorrected);
				corrected.append(" ");

				stemmer.setCurrent(s);
				if (stemmer.stem())
					s = stemmer.getCurrent();
				qs.add(s);
			}
		}

		long start = System.currentTimeMillis();
		Map<List<String>, List<String>> urls = new FindUrls().findUrls(qs);
		long end =System.currentTimeMillis();
		
		int cnt = 0;
		for(List<String> l:urls.values()){
			cnt = cnt+l.size();
		}
		
		String info = "About " + cnt + " results (" + Double.valueOf((end-start)/1000.0) + " seconds)";
		
		request.setAttribute("info", info);
		request.setAttribute("urls", urls);
		request.setAttribute("query", query);
		request.setAttribute("queryCor", corrected.toString());
		request.getRequestDispatcher("details.jsp").forward(request, response);
	}

}
