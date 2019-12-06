package cipher;

import java.util.LinkedList;

public class WordGraph {

	public String word;
	public WordGraph parent;
	public LinkedList<WordGraph> children = new LinkedList<WordGraph>();
	public int score;
	
	public WordGraph(String word, WordGraph parent) {
		this.word = word;
		this.parent = parent;
	}

}
