package util;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

import snowballstemmer.PorterStemmer;

@SuppressWarnings("deprecation")
public class WordCount {

	public static class Map extends MapReduceBase implements
	Mapper<LongWritable, Text, Text, Text> {
		private Text word = new Text();

		public void map(LongWritable key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
						throws IOException {
			String line = value.toString();
			System.out.println("processing line " + line);
			String split[] = line.split("#rnr#");
			String url = split[0].trim();
			System.out.println("url " + url);
			
			String temp = url;
			temp = temp.replace("http://", "");
			temp = temp.replace(".com", "");
			temp = temp.replace(" ", "");
			Text urlHadoop0 = new Text(url + "#cnt#1");

			PorterStemmer stemmer = new PorterStemmer();

			String[] slash = temp.split("/");
			for (String s : slash) {
				if (!s.contains(".") && !s.contains("-") && !s.contains(":")
						&& !s.contains("_")) {
					stemmer.setCurrent(s);
					if (stemmer.stem())
						s = stemmer.getCurrent();
					word.set(s);
					output.collect(word, urlHadoop0);
				} else {
					if (s.contains(".")) {
						String[] dot = s.split("\\.");
						for (String d : dot) {
							stemmer.setCurrent(d);
							if (stemmer.stem())
								d = stemmer.getCurrent();
							word.set(d);
							output.collect(word, urlHadoop0);
						}
					}
					if (s.contains("-")) {
						String[] hyphen = s.split("-");
						for (String h : hyphen) {
							stemmer.setCurrent(h);
							if (stemmer.stem())
								h = stemmer.getCurrent();

							word.set(h);
							output.collect(word, urlHadoop0);
						}
					}
					if (s.contains(":")) {
						String[] colon = s.split(":");
						for (String c : colon) {
							stemmer.setCurrent(c);
							if (stemmer.stem())
								c = stemmer.getCurrent();

							word.set(c);
							output.collect(word, urlHadoop0);
						}
					}

					if (s.contains("_")) {
						String[] uscore = s.split("_");
						for (String u : uscore) {
							stemmer.setCurrent(u);
							if (stemmer.stem())
								u = stemmer.getCurrent();

							word.set(u);
							output.collect(word, urlHadoop0);
						}
					}

				}
			}

			String ps = split[1].trim();
			StringTokenizer tokenizer = new StringTokenizer(ps);
			while (tokenizer.hasMoreTokens()) {
				String tkn = tokenizer.nextToken();
				int count = 0;
				count = countOccurrence(tkn, ps);
				stemmer.setCurrent(tkn);
				if (stemmer.stem())
					tkn = stemmer.getCurrent();
				word.set(tkn);
				Text urlHadoop1 = new Text(url + "#cnt#" + count);
				output.collect(word, urlHadoop1);
			}

		}

		private int countOccurrence(String word, String ps) {
			int cnt = 0;

			String[] splits = ps.split(" ");
			for (String s : splits) {
				if (s.equalsIgnoreCase(word)) {
					cnt++;
				}
			}

			return cnt;
		}
	}

	public static class Reduce extends MapReduceBase implements
	Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
						throws IOException {
			String inputString = "";
			while (values.hasNext()) {
				String txt = values.next().toString();
				if (!txt.isEmpty() && !inputString.contains(txt.toString()))
					inputString += "," + txt.toString();

			}
			Text out = new Text();
			out.set(inputString);
			output.collect(key, out);
		}
	}

	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(WordCount.class);
		conf.setJobName("wordcount");

		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);

		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
