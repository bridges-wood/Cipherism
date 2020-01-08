package cipher;

import java.util.LinkedList;

public class SearchNode {
	private double score;
	private int visited;
	private final Mapping[] KEY;
	private SearchNode parent;
	private LinkedList<SearchNode> children;

	SearchNode(double score, Mapping[] key, SearchNode parent) {
		this.score = score;
		this.visited = 0;
		this.KEY = key;
		this.parent = parent;
		this.children = new LinkedList<SearchNode>();
	}
	
	public void incrementVisited() {
		this.visited += 1;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public int getVisited() {
		return visited;
	}

	public Mapping[] getKEY() {
		return KEY;
	}

	public SearchNode getParent() {
		return parent;
	}

	public void setParent(SearchNode parent) {
		this.parent = parent;
	}

	public LinkedList<SearchNode> getChildren() {
		return children;
	}

	public void setChildren(LinkedList<SearchNode> children) {
		this.children = children;
	}
}
