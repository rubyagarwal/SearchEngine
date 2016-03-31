package data;

public class WordOccurencePerURL {

	private String word;
	private String url;
	private int count;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public WordOccurencePerURL(String word, String url, int count) {
		super();
		this.word = word;
		this.url = url;
		this.count = count;
	}
	
	@Override
	public String toString() {
		return "WordOccurencePerURL [word=" + word + ", url=" + url
				+ ", count=" + count + "]";
	}

}
