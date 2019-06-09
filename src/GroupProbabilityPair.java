
public class GroupProbabilityPair {
	private int rank;
	private String group;

	/**
	 * Stores the rank and text of a group of words in one object.
	 * 
	 * @param rank  The rank in the given text file the word group is drawn from.
	 * @param group The group of words to be ranked.
	 */
	public GroupProbabilityPair(int rank, String group) {
		this.rank = rank;
		this.group = group;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
