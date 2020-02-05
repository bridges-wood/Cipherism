package main.java.cipher;

import java.util.LinkedList;

public class WordGraph {

	private String word;
	private WordGraph parent;
	private LinkedList<WordGraph> children = new LinkedList<WordGraph>();
	public int score;

	public WordGraph(String word, WordGraph parent) {
		this.word = word;
		this.parent = parent;
	}

	/**
	 * Resets a node to an empty state.
	 */
	void Clear() {
		this.children = new LinkedList<WordGraph>();
		this.score = 0;
		this.word = "";
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public WordGraph getParent() {
		return parent;
	}

	public void setParent(WordGraph parent) {
		this.parent = parent;
	}

	public LinkedList<WordGraph> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<WordGraph> children) {
		this.children = children;
	}

	/**
	 * Adds a node to another given node's children.
	 * 
	 * @param child The node to be added to the given node's children.
	 */
	public void addChild(WordGraph child) {
		this.children.add(child);
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
