package ntu.selab.phrase.suffixtree;

import java.util.*;


public class PhraseTree_Root implements Aggregator {
	
	private String rootName;
	//private HashSet<PhraseTree_Node> nodes;
	private Collection<PhraseTree_Node> nodes = null;

	private int nodeCount = 0;
	private int nodeDepth = 0;
	private int edgeCount = 0;
	
	
	public PhraseTree_Root(String rootName) {		
		this.rootName = rootName;
		//nodes = new HashSet<PhraseTree_Node>();
		nodes = new ArrayList<PhraseTree_Node>();
		nodeDepth = 0;
		//label = 0;
	}
	
	protected void finalize() {
		nodes = null;
	}
	

	
	public void addNode(String word) {
		PhraseTree_Node nodeData = new PhraseTree_Node(word);
		nodes.add(nodeData);
		
	}
	
	public void displayPhraseTree(String rootName) {		
		System.out.println("Dislaying Tree: " + rootName);		
		if (rootName == this.rootName) {
		
			java.util.Iterator<PhraseTree_Node> iterator = nodes.iterator();
			
			while( iterator.hasNext()) {		
				System.out.println("Node:" + iterator.next().getWord() + "");
			}
		
		}
	}
	
}
