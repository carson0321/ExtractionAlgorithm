package ntu.selab.phrase.suffixtree;

import java.util.ArrayList;
import java.util.Collection;

public class PhraseTree_Node {

	private PhraseTree_Root root;
	private PhraseTree_Edge startEdge;
	private PhraseTree_Edge endEdge; 

	private Collection<PhraseTree_Node> childNodes = null;

	
	private String word; 
	private int significanceFactorNode;
	
	public PhraseTree_Node(String word) {
		setWord(word);
	}
	
	public PhraseTree_Root getRoot() {
		return root;
	}
	public void setRoot(PhraseTree_Root root) {
		this.root = root;
	}
	
	public PhraseTree_Edge getStartEdge() {
		return startEdge;
	}
	public void addStartEdge(PhraseTree_Edge startEdge) {
		this.startEdge = startEdge;
	}
	
	public PhraseTree_Edge getEndEdge() {
		return endEdge;
	}
	public void addEndEdge(PhraseTree_Edge endEdge) {
		this.endEdge = endEdge;
	}
	
	public Collection<PhraseTree_Node> getChildNodes() {
		return childNodes;
	}
	
	public void addChildNodes(Collection<PhraseTree_Node> childNodes) {
		this.childNodes = childNodes;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
		System.out.println("Node Created... adding: " + word +  " to the tree!");

	}
	
	public int getSignificanceFactorNode() {
		return significanceFactorNode;
	}
	public void setSignificanceFactorNode(int significanceFactorNode) {
		this.significanceFactorNode = significanceFactorNode;
	}
	
	
	
	
	
	

	
	
	
}
