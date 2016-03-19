package com.test;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
 
/**
* This class finds the common pattern in 'N' words in O(N) time using Suffix Tree.
* I could also use this to do auto-completion and word suggestion
*
* @author raju rama krishna
*
*/

public class SuffixTree {
	private static int n_words = 0;
	
	public static void main(String[] args) {
//		String[] words = {"tommorrow", "sparrows", "rowing", "borrow"};
		String[] words = {"please call me asap", "please call if you", "please call asap", "If you call me asap"};
		n_words = words.length;
		System.out.println(SuffixTree.printCommon( words ));
	}
	
	public static String printCommon( String[] sArr) {
		Node root = new Node(' ', 0);
		int i = 1;
		for( String s: sArr ) {
			createTree( s, i++, root );
		}
		return commonPattern( root );
	}
	
	public static String commonPattern( Node root ) {
		String res = "";
		List<Node> children = root.children;
		for(Node temp: children ) {
			String word = getMatch( temp );
			if( word.length() > res.length() ) {
				res = word;
			}
		}
		return res;
	}
	
	private static String getMatch( Node node ) {
		if( node.indexes.size() == n_words) {
			String res = "";
			for( int i = 0; i < node.children.size(); i++ ) {
				String temp = node.c + getMatch( node.children.get(i));
				if( temp.length() > res.length() ) {
					res = temp;
				}
			}
			return res;
		} else {
			return "";
			}
	}
	
	public static void createTree( String s, int idx, Node root ) {
		do {
			addBranch( s, root, idx );
			s = s.substring(1, s.length());
		} while( s.length() > 0 );
	}
	
	private static void addBranch( String s, Node root, int idx ) {
		Node node = null;
		for(int i=0; i < s.length(); i++) {
			boolean found = false;
			char c = s.charAt(i);
			if( i == 0 ) {
				node = new Node( c, idx );
				List<Node> children = root.children;
				if( children == null ) {
					children = new ArrayList<Node>();
				} else {
					for( Node temp: children ) {
						if(temp.c == c) {
							node = temp;
							List<Integer> indexes = node.indexes;
							if(!indexes.contains(idx)) {
								indexes.add(idx);
							}
							found = true;
							break;
						}
					}
				}
				if( !found ) {
					children.add( node );
					root.children = children;
				}
				} else {
					Node node1 = new Node( c, idx );
					if( i == s.length()-1) {
						node1.isEnd = true;
					}
					List<Node> children = node.children ;
					if( children == null ) {
						children = new ArrayList<Node>();
					} else {
						for( Node temp: children ) {
							if(temp.c == c) {
								node = temp;
								List<Integer> indexes = node.indexes;
								if(!indexes.contains(idx)) {
									indexes.add(idx);
								}
								found = true;
								break;
							}
						}
					}
					if( !found ) {
						children.add( node1 );
						node.children = children;
						node = node1;
					}
				}
		}
	}
}


class Node {
	char c;
	List<Node> children;
	boolean isEnd;
	int idx;
	List<Integer> indexes;
	
	public Node( char c, int idx ) {
		this.c = c;
		indexes = new ArrayList<Integer>();
		indexes.add(idx);
	}
	
	public String toString() {
		return String.valueOf(c);
	}
}



/*public class SuffixTree {
	SuffixTreeNode root = new SuffixTreeNode();
	
	public static void main(String[] args) {
	}
	
	public SuffixTree(String s){
		for(int i = 0; i < s.length(); i++){
			String suffix = s.substring(i);
			root.insertString(suffix, i);
		}
	}
	public ArrayList<Integer> getIndexes(String s){
		return root.getIndexes(s);
	}
}

class SuffixTreeNode{
	HashMap<Character,SuffixTreeNode> children =
			new HashMap<Character,SuffixTreeNode>();
	ArrayList<Integer> indexes =
			new ArrayList<Integer>();
	char value;
	
	public SuffixTreeNode(){
	}
	
	public void insertString(String s, int index){
		indexes.add(index);
		if(s != null && s.length() > 0){
			value = s.charAt(0);
			SuffixTreeNode child = null;
			if(children.containsKey(value)){
				child = children.get(value);
			} else {
				child = new SuffixTreeNode();
				children.put(value, child);
			}
			String remainder = s.substring(1);
			child.insertString(remainder, index);
		}
	}
	
	public ArrayList<Integer> getIndexes(String s){
		if (s == null || s.length() == 0){
			return indexes;
		} else {
			char first=s.charAt(0);
			if(children.containsKey(first)){
				String remainder = s.substring(1);
				return children.get(first).getIndexes(remainder);
			}
		}
		return null;
	}
	
}*/