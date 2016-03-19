package ntu.selab.phrase.suffixtree;

public class PhraseTree_Edge {
	
	private int significanceFactor;	
	private PhraseTree_Node startNode;
	private PhraseTree_Node endNode;
	private boolean telescoped;
	
	public int getSignificanceFactor() {
		return significanceFactor;
	}
	public void setSignificanceFactor(int significanceFactor) {
		this.significanceFactor = significanceFactor;
	}
	
	public PhraseTree_Node getStartNode() {
		return startNode;
	}
	public void setStartNode(PhraseTree_Node startNode) {
		this.startNode = startNode;
	}
	
	public PhraseTree_Node getEndNode() {
		return endNode;
	}
	public void setEndNode(PhraseTree_Node endNode) {
		this.endNode = endNode;
	}
	
	public boolean isTelescoped() {
		return telescoped;
	}
	public void setTelescoped(boolean telescoped) {
		this.telescoped = telescoped;
	}

	
	
	
}
