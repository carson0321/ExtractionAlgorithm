package ntu.selab.phrase.suffixtree;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

//import ST.SuffixTree.Node;

public class PhraseSuffix_Tree {
	
		Map<String, Integer> table = new TreeMap<String,Integer>();
		ArrayList existWordsTable;
		
	   final int oo = Integer.MAX_VALUE/2;
       PhraseSuffix_Node [] nodes;
       String [] text;
       static int root, position = -1,
               currentNode,
               needSuffixLink,
               remainder;
       
       int foundNodesCt = 0;
       int[] foundNodes = new int[1000];
       int nodesToIgnoreCt = 0;
       int[] nodesToIgnore = new int[1000];
       int[] nodesToIgnoreParent = new int[1000];
       String[] nodesToIgnoreKey = new String[1000];
       
       int active_node, active_length, active_edge;
       ArrayList<Integer> hasLink= new ArrayList<Integer>(); 
       
       
       public PhraseSuffix_Tree(int length) {
           nodes = new PhraseSuffix_Node[2* length + 2];
           text = new String[length];
           root = active_node = newNode(-1, -1);
       }

       private void addSuffixLink(int node) {
           if (needSuffixLink > 0){
               nodes[needSuffixLink].link = node;
               hasLink.add(node);
           }
           needSuffixLink = node;
           
       }

       String active_edge() {
           return text[active_edge];
       }

       boolean walkDown(int next) {
           if (active_length >= nodes[next].edgeLength()) {
               active_edge += nodes[next].edgeLength();
               active_length -= nodes[next].edgeLength();
               active_node = next;
               return true;
           }
           return false;
       }

       int newNode(int start, int end) {
           nodes[++currentNode] = new PhraseSuffix_Node(start, end);
           return currentNode;
       }

       public void addWord(String c) throws Exception {
           text[++position] = c;
           needSuffixLink = -1;
           remainder++;
           while(remainder > 0) {
//           	System.out.println("active pt: node "+active_node+" length: "+active_length+" edge: "+active_edge);
               if (active_length == 0) active_edge = position;
               if (!nodes[active_node].next.containsKey(active_edge()) ){
                   int leaf = newNode(position, oo);
                   nodes[active_node].next.put(active_edge(), leaf); 
                   addSuffixLink(active_node); //rule 2
               } else {
                   int next = nodes[active_node].next.get(active_edge());
                   if (walkDown(next)) continue;   //observation 2
                   if (text[nodes[next].start + active_length].equals(c) ) { //observation 1
                       active_length++;
                       addSuffixLink(active_node); // observation 3
                      	break;
                   }
                   int split = newNode(nodes[next].start, nodes[next].start + active_length);
                   nodes[active_node].next.put(active_edge(), split);
                   int leaf = newNode(position, oo);
                   nodes[split].next.put(c, leaf);
                   nodes[next].start += active_length;
                   nodes[split].next.put(text[nodes[next].start], next);
                   addSuffixLink(split); //rule 2
               }
               remainder--;
               
               if (active_node == root && active_length > 0) {  //rule 1
                   active_length--;
                   active_edge = position - remainder + 1;
               } else
                   active_node = nodes[active_node].link > 0 ? nodes[active_node].link : root; //rule 3
           }
       }
       public void sep(){
       	 remainder = 0;
       	 active_node = root;
       	 active_length = 0;
       	 active_edge = 0;
       	 for(int i = 0; i < hasLink.size(); i++)
       		 nodes[hasLink.get(i)].link=0;
       	 for(int i = 1; i < nodes.length; ++i)
       	 {
       		 if(nodes[i] == null)
       			 break;
       		 nodes[i].end=Math.min(position + 1, nodes[i].end);
       	 }
   
       }
      
       /*
           printing the Suffix Tree in a format understandable by graphviz. The output is written into
           st.dot file. In order to see the suffix tree as a PNG image, run the following command:
           dot -Tpng -O st.dot
        */

       String edgeString(int node) {
          String[] s= Arrays.copyOfRange(text, nodes[node].start, Math.min(position + 1, nodes[node].end));
          String a = "";
          int count = 0;
          for(String t:s){
       	   ++count;
        	   a+=t;
       	   if(count != s.length) a+=" ";
          }
          return a;
       }

       public void printTree(PrintWriter out) {
    	   table.put("please call",3);
    	   table.put("call me", 2);
    	   table.put("if you", 2);
    	   table.put("me asap", 2);
    	   existWordsTable = new ArrayList(table.keySet());
    	   for (int i = 0; i < existWordsTable.size(); i++) {
//    		    System.out.println(existWordsTable.get(i));
    		}
           out.println("digraph {");
           out.println("\trankdir = LR;");
           out.println("\tedge [arrowsize=0.4,fontsize=10]");
           out.println("\tnode1 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
//           out.println("//------leaves------");
//           printLeaves(root, out);
//           out.println("//------internal nodes------");
//           printInternalNodes(root, out);
           out.println("//------edges------");
           printEdges(root, out);
//           out.println("//------suffix links------");
//           printSLinks(root);
           out.println("}");
//           prune(root);
//           test(root);
           telescope(root);
           traveralNode(root);
       } 
       void test(int x){
    	   for (int child : nodes[x].next.values()) {
        	   if(nodes[child]!=null){
        		   System.out.println("\tnode"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
        		   test(child);
        	   }
           }
       }

//       void printLeaves(int x, PrintWriter out) {
//           if (nodes[x].next.size() == 0)
//               out.println("\tnode"+x+" [label=\"\",shape=point]");
//           else {
//               for (int child : nodes[x].next.values())
//                   printLeaves(child, out);
//           }
//       }
//
//       void printInternalNodes(int x, PrintWriter out) {
//           if (x != root && nodes[x].next.size() > 0)
//               out.println("\tnode"+x+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");
//
//           for (int child : nodes[x].next.values())
//               printInternalNodes(child, out);
//       }
       
       String testPhrase;
       int pruneParentNode;
       void prune(int x) {
    	   if (x == root && nodes[x]!=null)
    		   for (int child : nodes[x].next.values()){
//    			   System.out.println("node: "+child+" start: "+nodes[child].start+" end: "+nodes[child].end+ " context: " + edgeString(child));
    			   String [] phraseLength = edgeString(child).split(" ");
    				if(phraseLength.length>1){
    					testPhrase = phraseLength[0]+" "+phraseLength[1];
    					if(!existWordsTable.contains(testPhrase)){
    						System.out.println("fuck");
    						deleteChildNode(child);
    					}
    				}
    				else{
    					pruneParentNode=child;
    					traveralPruneNode(child);
    				}
    			   prune(child);
    		   }
       }
       
       void traveralPruneNode(int x) {
    	   if (nodes[x]!=null)
    		   for (int child : nodes[x].next.values()){
    			   String [] phraseLength = edgeString(child).split(" ");
    			   testPhrase=edgeString(pruneParentNode)+" "+phraseLength[0];
    			   System.out.println(testPhrase);
    			   if(!existWordsTable.contains(testPhrase)){
    				   System.out.println("fuck");
    				   deleteChildNode(child);
    			   }
    				traveralPruneNode(child);
    		   }
       }
       
       void deleteChildNode(int x) {
    	   for (int child : nodes[x].next.values()){
    		   nodes[child]=null;
    		   deleteChildNode(child);
    	   }
    	   nodes[x]=null;
       }
       
       
       
       
       String [] telescopeStrings = new String[500];
       int count=0;
       int [] parentNode = new int[500];
       int [] childNode = new int[500];
       void telescope(int x) {
    	   if (x != root && nodes[x].next.size() == 1 && nodes[x]!=null){
			   parentNode[count]=x;
//    		   System.out.println("node: "+parentNode[count]+" start: "+nodes[x].start+" end: "+nodes[x].end+ " context: " + edgeString(parentNode[count]));
    		   for (int child : nodes[x].next.values()){
    				   telescopeStrings[count]=edgeString(parentNode[count])+" "+edgeString(child);
//        			   System.out.println(telescopeStrings[count]);
//        			   System.out.println("node"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
        			   childNode[count]=child;
        			   count++;
    		   }
           }
    	   for (int child : nodes[x].next.values()){
    		   if (nodes[child]!=null)
    			   telescope(child);
           }
       }
       
       boolean telescoping=false;
       void traveralNode (int x) {
    	   for (int child : nodes[x].next.values()){
    		   if(nodes[child]!=null){
    			   for(int i = 0 ; i < count ; i++){
    				   if(child==parentNode[i]){
        				   System.out.println("node"+x+" -> node"+child+" [label=\""+telescopeStrings[i]+"\",weight=3]");
        				   nodes[childNode[i]]=null;
        				   telescoping=true;
        			   }
    			   }
    			   if (!telescoping)  System.out.println("node"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
    			   telescoping=false;
    			   traveralNode(child);
    		   }
           }
       }
       
       void printEdges(int x, PrintWriter out) {
           for (int child : nodes[x].next.values()) {
        	   if(nodes[child]!=null){
        		   out.println("\tnode"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
        		   printEdges(child, out);
        	   }
           }
       }

//       void printSLinks(int x) {
//           if (nodes[x].link > 0)
//               out.println("\tnode"+x+" -> node"+nodes[x].link+" [label=\"\",weight=1,style=dotted]");
//           for (int child : nodes[x].next.values())
//               printSLinks(child);
//       }
//       
//       void printNodes(){
//       	for(int i = 1; i < nodes.length;++i)
//       	{
//       		if(nodes[i]==null) break;
//       		System.out.println("node: "+" start: "+nodes[i].start+"end: "+nodes[i].end);
//       	}
//       }
       
       
       public void signSignificance(){
        	for(int i = 1; i < nodes.length;++i){
        		if(nodes[i]==null) break;
        		if (nodes[i].next.size() == 0)   nodes[i].Significance=true;
        	}
       }
       
       public void printSignificanceNodes(){
          	for(int i = 1; i < nodes.length;++i){
          		if(nodes[i]==null) break;
          		if (nodes[i].Significance){
//          			System.out.println("node: "+i);
          		}
          	}
       }
       
       
       void searchTree(int x, String searchWord) {
       	for (int child : nodes[x].next.values()) {
       		System.out.println("Displaying nodes No:" + child + "Value: "+ edgeString(child));
       		if (edgeString(child).equals(searchWord)) { 
       			System.out.println("Searching for:"+searchWord+ " Found Node is:" + child);
       		}
       		searchTree(child, searchWord);
       	}
       }
       
       public void printAllPhrases(int nodeNumber, String str){
    	   if (nodes[nodeNumber].next.isEmpty()){
    		   System.out.println(str);
    		   return;
    	   }
    		   
    	   else
    		   for(Map.Entry<String, Integer> entry : nodes[nodeNumber].next.entrySet()){
    			   String s="";
    			   for(int i = nodes[entry.getValue()].start; i < nodes[entry.getValue()].end; i++)
    			   {
    				   s+=text[i];
//    				   if(i != nodes[entry.getValue()].end - 1)
    					   s+=" ";
    			   }
    			   printAllPhrases(entry.getValue(), str+s);
    		   }
    	   
       }
       
//Terence's Code: *********************************************************************************************************************
       
       public void printFullTree(PrintWriter out) {
    	   telescope(root);
           traveralNode(root);
           out.println("digraph {");
           out.println("\trankdir = LR;");
           out.println("\tedge [arrowsize=0.4,fontsize=10]");
           out.println("\tnode1 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
//           out.println("//------leaves------");
//           printFullLeaves(root, out);
//           out.println("//------internal nodes------");
//           printFullInternalNodes(root, out);
           out.println("//------edges------");
           printEdges(root, out);
//           out.println("//------suffix links------");
//           printFullSLinks(root, out);
           out.println("}");
          // printSignificance(root);
       } 
       
       void printFullLeaves(int x, PrintWriter out) {
           if (nodes[x].next.size() == 0)
               out.println("\tnode"+x+" [label=\"\",shape=point]");
           else {
               for (int child : nodes[x].next.values())
            	   printFullLeaves(child, out);
              
           }
       }

       void printFullInternalNodes(int x,PrintWriter out) {
           if (x != root && nodes[x].next.size() > 0)
               out.println("\tnode"+x+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");

           for (int child : nodes[x].next.values())
        	   printFullInternalNodes(child, out);
       }
       
       void printFullSLinks(int x ,PrintWriter out) {
           if (nodes[x].link > 0)
               out.println("\tnode"+x+" -> node"+nodes[x].link+" [label=\"\",weight=1,style=dotted]");
           for (int child : nodes[x].next.values())
        	   printFullSLinks(child, out);
       }
       
       
       public void labelLowFrequency(String lowfreqPhrase) {
   	 	int[] targetNodes = new int[1000];
   	 	int[] targetNodes2 = new int[1000];
   		int LowFreqNode = 1;
   		
   		 foundNodesCt = 0;
   		 
   		 
   		 targetNodes2 = searchTree_Basic(root, lowfreqPhrase);   
   		 targetNodes = searchTree_Advanced(root, lowfreqPhrase);   
   		 
   		 
   			for (int i=0; i<foundNodesCt; i++) {
           //		System.out.println("Found Nodes: "+ targetNodes[i]);
           	}
   		 
   		 for (int i=0; i<foundNodesCt;i++) {        			 
   			// System.out.println("targetNode"+i+": " +targetNodes[i] );
   			 nodes[ targetNodes[i] ].frequencyCount = LowFreqNode;
       		// System.out.println("New FreqCt:"+nodes[ targetNodes[i] ].frequencyCount); 
   		 }
   		 //System.out.println("Label Node No:" + targetNode + " Content: <"+ edgeString(targetNode) + ">"); 
       }
       
       String firstWord(int node) {
           String[] s= Arrays.copyOfRange(text, nodes[node].start, nodes[node].start+1);
           String a = "";
           int count = 0;
           for(String t:s){
        	   ++count;
         	   a+=t;
        	   if(count != s.length) a+=" ";
           }
           return a;
        }
       
       String secondWord(int node) {
           String[] s= Arrays.copyOfRange(text, nodes[node].start, nodes[node].start+2);
           String a = "";
           int count = 0;
           for(String t:s){
        	   ++count;
         	   a+=t;
        	   if(count != s.length) a+=" ";
           }
           return a;
        }
       
       
       int[] searchTree_Basic(int x, String searchWord) {
       	
       	for (int child : nodes[x].next.values()) {
       //		System.out.println("Displaying nodes No:" + child + "Value: "+ edgeString(child));
       		
       	    //String[] s= Arrays.copyOfRange(text, nodes[child ].start, nodes[child ].start+1);
       	//    System.out.println("First word: " + firstWord(child));
       	//    System.out.println("Second word: " + secondWord(child));
       		
       		if (edgeString(child).equals(searchWord)) { 
       		//	System.out.println("Searching for: <"+searchWord+ "> Found at Node: <" + child +">");
       		//	System.out.println("Inserted Node: <"+child+ "> at Index: <" + foundNodesCt +">");
       			
       			//foundNodes[foundNodesCt++] = child;
       			
       			//nodesToIgnore[nodesToIgnoreCt++] = child;
       			
       			//removeLowFrequencyNodes(1, child);
       			//nodes[child].isSignificant.put(searchWord, 2);
       			
       			
       				 nodesToIgnore[nodesToIgnoreCt] = child;
	 				 nodesToIgnoreKey[nodesToIgnoreCt] = firstWord(child);
	 				 nodesToIgnoreParent[nodesToIgnoreCt] = 1;
	 				 nodesToIgnoreCt++;	
       		}
            searchTree(child, searchWord);
       	}
       	
       
       	return foundNodes;
       }
       
       int[] searchTree_Advanced(int x, String searchWord) {
          	
       	
        	String[] word; 
        	int[] matchedChildrenNode = new int[10];
        	int matchedCount = 0;
       	
		       	for (int child : nodes[x].next.values()) {
		       	    
		       	    word = searchWord.split(" ");
		       	    
		       	 		for(int i = 0; i < word.length; ++i) {
		       	 	 	  System.out.println("word" + i + "is" + word[i]);          	 	   
		       	 		}
		       	 	
			       	 	if (word[0].equals(firstWord(child))) {
			       	 		System.out.println("We got a MATCH! at Node:"+child + " with children count of: "+nodes[child].next.size());      
			
			       	 		//continue to check for the first word in the child branch:
			       	 		///	System.out.println("Its children are nodes: "+nodes[child].next.values());           	 
			       	 		//matchedChildrenNode = nodes[child].next.values();
			       	 		
			       	 			for(Map.Entry<String,Integer> entry : nodes[child].next.entrySet() ) {
			       	 			  String key = entry.getKey();
			       	 			  Integer value = entry.getValue();
			       	 			  matchedChildrenNode[matchedCount++] = value;
			       	 			  System.out.println("With Key:" +key + " => Value:" + value);
			       	 			  
			       	 			  
			       	 			  
				       	 			if (word[1].equals(firstWord(value))) {
				       	 				 System.out.println("We got a VALUE MATCH! at Node:"+ value );

				       	 							       	 				 
				       	 				 nodesToIgnore[nodesToIgnoreCt] = value;
				       	 				 nodesToIgnoreKey[nodesToIgnoreCt] = key;
				       	 				 nodesToIgnoreParent[nodesToIgnoreCt] = child;
				       	 				// removeLowFrequencyNodes(child, value, key);
				       	 				 nodesToIgnoreCt++;	

				       	   				System.out.println("nodesToIgnore1: "+ nodesToIgnore[nodesToIgnoreCt] + " with ParentNode:  " + nodesToIgnoreParent[nodesToIgnoreCt] + "and Key: "+ nodesToIgnoreKey[nodesToIgnoreCt] );
				       	   			
				       	 			}
				       	 			
				       	 			
				       	 			
			       	 			}

			       	 		
//				       	 			for (int i = 0; i < nodes[child].next.size(); i++) {
//				       	 			//		System.out.println("Its children are nodes2: "+nodes[child].next.);
//				       	 			//		matchedChildrenNode[i] = (int) nodes[child].next.get(i);
//				       	 					System.out.println("Its children are nodes2: "+matchedChildrenNode[i]);    
//				       	 				if (word[1].equals(firstWord(matchedChildrenNode[i]))) {
//				       	 					System.out.println("We got a SECOND MATCH! at Node:"+ matchedChildrenNode[i] );
//				       	 					  nodesToIgnore[nodesToIgnoreCt++] = matchedChildrenNode[i];
//				       	 		
//				       	 					  
//				       	 		//			  nodes[matchedChildrenNode[i]].next.
//				       	 					  
//				       	 					  
//				       	 					  //removeLowFrequencyNodes(child, matchedChildrenNode[i]);
//				       	 				}
//				       	 			}
			       	 			
			       	 	}
		       		
		       		if (edgeString(child).equals(searchWord)) { 
		       			//System.out.println("Searching for: <"+searchWord+ "> Found at Node: <" + child +">");
		       			//System.out.println("Inserted Node: <"+child+ "> at Index: <" + foundNodesCt +">");
		       			foundNodes[foundNodesCt++] = child;
		       			
		       			//nodes[child].isSignificant.put(searchWord, 2);
		       		}
		       		searchTree_Advanced(child, searchWord);
		       	}
       	return foundNodes;
       }
       
       
       public void displayLowFrequencyNodes() {
    	
    	   System.out.println(nodesToIgnoreCt+" Nodes To Ignore due to Low Frequency: ");
   			for(int i = 0; i < nodesToIgnoreCt; ++i)
   			{
   				System.out.println("nodesToIgnore2: "+ nodesToIgnore[i] + " with ParentNode:  " + nodesToIgnoreParent[i] + " and Key: "+ nodesToIgnoreKey[i] );
   			}
    	   
   			//removeLowFrequencyNodes(root);
       }
       
       
       public void removeLowFrequencyNodes() {
    	
    	   
    	 
    	   
    	   for(int i = 0; i < nodesToIgnoreCt; ++i)
  			{
  			  System.out.println("Remove link from parent node=" + nodesToIgnoreParent[i] + "to child node=" + nodesToIgnore[i] + " with Key: " + nodesToIgnoreKey[i]  );
  			 nodes[nodesToIgnoreParent[i]].next.remove(nodesToIgnoreKey[i]);
  			}
   	   
    	   
    	//   nodes[parentInput].next.r
    	  // nodes[parentInput].next.remove(key);
    	   
//    	   for(Map.Entry<String,Integer> entry : nodes[parentInput].next.entrySet() ) {
//	 			  String key = entry.getKey();
//	 			  Integer value = entry.getValue();
//	 			  matchedChildrenNode[matchedCount++] = value;
//	 			  System.out.println("With Values:" +key + " => " + value);
//	 			}
//    	   
    	   
    	    
//         	for (int child : nodes[root].next.values()) {
//                //		System.out.println("Displaying nodes No:" + child + "Value: "+ edgeString(child));
//                		
//                	    //String[] s= Arrays.copyOfRange(text, nodes[child ].start, nodes[child ].start+1);
//                	//    System.out.println("First word: " + firstWord(child));
//                	//    System.out.println("Second word: " + secondWord(child));
//                		
//                		if (edgeString(child).equals(searchWord)) { 
//                		//	System.out.println("Searching for: <"+searchWord+ "> Found at Node: <" + child +">");
//                		//	System.out.println("Inserted Node: <"+child+ "> at Index: <" + foundNodesCt +">");
//                			
//                			foundNodes[foundNodesCt++] = child;
//                			
//                			nodesToIgnore[nodesToIgnoreCt++] = child;
//                			//nodes[child].isSignificant.put(searchWord, 2);
//                		}
//                		removeLowFrequencyNodes(child);
//                	}
    	   
    	   
       }
     
       
       
//Terence's Code END: *********************************************************************************************************************
       
   } //End of Class PhraseSuffix_Node.java

       
		

