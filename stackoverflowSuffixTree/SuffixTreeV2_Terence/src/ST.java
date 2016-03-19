import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ST {
    BufferedReader in;
    PrintWriter out;

    class SuffixTree {
        final int oo = Integer.MAX_VALUE/2;
        Node [] nodes;
        String [] text;
        int root, position = -1,
                currentNode,
                needSuffixLink,
                remainder;
        int foundNodesCt = 0;
        int[] foundNodes = new int[1000];
        int active_node, active_length, active_edge;
        ArrayList<Integer> hasLink= new ArrayList<Integer>(); 
        int[] nodesToIgnore = new int[500];
        int nodesToIgnoreCt;

        class Node {

            /*
               There is no need to create an "Edge" class.
               Information about the edge is stored right in the node.
               [start; end) interval specifies the edge,
               by which the node is connected to its parent node.
            */

            int start, end = oo, link;
          
            int frequencyCount = 10;
            public TreeMap<String, Integer> next = new TreeMap<String, Integer>();
            
          //  public TreeMap<String, Integer> isSignificant = new TreeMap<String, Integer>();
           // public TreeMap<String, Integer> isFrequent = new TreeMap<String, Integer>();
            
            
            public Node(int start, int end,  boolean isFrequent) {
                this.start = start;
                this.end = end;
               // this.isFrequent = isFrequent;
            }

            public int edgeLength() {
                return Math.min(end, position + 1) - start;
            }
        }

        public SuffixTree(int length) {
        	boolean isFrequent = true;
            nodes = new Node[2* length + 2];
            text = new String[length];
            root = active_node = newNode(-1, -1, isFrequent);
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

        int newNode(int start, int end,  boolean isFrequent) {
            nodes[++currentNode] = new Node(start, end,  isFrequent);
            //System.out.println("New Node=" + "Start="+start+" End="+end+ " isFrequent="+isFrequent +" currentNode="+currentNode);
//            if (!isFrequent)
//            {
//                nodesToIgnore[nodesToIgnoreCt++] = currentNode;
//                System.out.println("Ignored Node: "+edgeString(currentNode));
//            }

            return currentNode;
        }

        public void addWord(String c, boolean isFrequent) throws Exception {
        //	System.out.println("position before: " + position);    
            text[++position] = c;
            needSuffixLink = -1;
        //	System.out.println("position after: " + position);       
            remainder++;
            
//            for (int child : nodes[x].next.values()) {
//                out.println("\tnode"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
//                printEdges(child);
//            }
          
            while(remainder > 0) {
            //	System.out.println("adding word: " +c+ " isFrequent:"+isFrequent+" remainder:"+remainder+" activePt:node"+active_node+" length:"+active_length+" edge:"+active_edge);
                if (active_length == 0) active_edge = position;
                if (!nodes[active_node].next.containsKey(active_edge()) ){
                int leaf = newNode(position, oo, isFrequent);	
             //   System.out.println("Added node: "+edgeString(leaf));
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
                    
                    //need to filter based on Frequency
                    int split = newNode(nodes[next].start, nodes[next].start + active_length, isFrequent);
                 //   System.out.println("Added node: "+edgeString(split));
                    nodes[active_node].next.put(active_edge(), split);
                                      
                    int leaf = newNode(position, oo, isFrequent);	
                 //   System.out.println("Added node: "+edgeString(leaf));
                    nodes[split].next.put(c, leaf);

                    nodes[next].start += active_length;
                    nodes[split].next.put(text[nodes[next].start], next);
                    addSuffixLink(split); //rule 2
                }
                remainder--;
//              if(!isFrequent) break;
                if (active_node == root && active_length > 0) {  //rule 1
                    active_length--;
                    active_edge = position - remainder + 1;
                } else
                    active_node = nodes[active_node].link > 0 ? nodes[active_node].link : root; //rule 3
            }
        }
        //Use it to end a phrase
         void sep(){
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

        void printTree() {
            out.println("digraph {");
            out.println("\trankdir = LR;");
            out.println("\tedge [arrowsize=0.4,fontsize=10]");
            out.println("\tnode1 [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.1,height=.1];");
            out.println("//------leaves------");
            printLeaves(root);
            out.println("//------internal nodes------");
            printInternalNodes(root);
            out.println("//------edges------");
            printEdges(root);
            out.println("//------suffix links------");
            printSLinks(root);
            out.println("}");
           // printSignificance(root);
        } 

        void printLeaves(int x) {
            if (nodes[x].next.size() == 0)
                out.println("\tnode"+x+" [label=\"\",shape=point]");
            else {
                for (int child : nodes[x].next.values())
                    printLeaves(child);
               
            }
        }

        void printInternalNodes(int x) {
            if (x != root && nodes[x].next.size() > 0)
                out.println("\tnode"+x+" [label=\"\",style=filled,fillcolor=lightgrey,shape=circle,width=.07,height=.07]");

            for (int child : nodes[x].next.values())
                printInternalNodes(child);
        }

        void printEdges(int x) {
            for (int child : nodes[x].next.values()) {
                out.println("\tnode"+x+" -> node"+child+" [label=\""+edgeString(child)+"\",weight=3]");
                printEdges(child);
            }
        }

        void printSLinks(int x) {
            if (nodes[x].link > 0)
                out.println("\tnode"+x+" -> node"+nodes[x].link+" [label=\"\",weight=1,style=dotted]");
            for (int child : nodes[x].next.values())
                printSLinks(child);
        }
        
        void printNodes(){
        	for(int i = 1; i < nodes.length;++i)
        	{
        		if(nodes[i]==null) break;
        	//	System.out.println("node: "+" start: "+nodes[i].start+"end: "+nodes[i].end);
        	}
        }
        
        int[] searchTree(int x, String searchWord) {
        	
        	
        	
        	for (int child : nodes[x].next.values()) {
        //		System.out.println("Displaying nodes No:" + child + "Value: "+ edgeString(child));
        		
        	    //String[] s= Arrays.copyOfRange(text, nodes[child ].start, nodes[child ].start+1);
        	//    System.out.println("First word: " + firstWord(child));
        	//    System.out.println("Second word: " + secondWord(child));
        	    
        		
        		if (edgeString(child).equals(searchWord)) { 
        		//	System.out.println("Searching for: <"+searchWord+ "> Found at Node: <" + child +">");
        		//	System.out.println("Inserted Node: <"+child+ "> at Index: <" + foundNodesCt +">");
        			foundNodes[foundNodesCt++] = child;
        			
        			
        			//nodes[child].isSignificant.put(searchWord, 2);
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
        	//	System.out.println("Displaying nodes No:" + child + "Value: "+ edgeString(child));
        		
        	    //String[] s= Arrays.copyOfRange(text, nodes[child ].start, nodes[child ].start+1);
        	//    System.out.println("First word: " + firstWord(child));
        	    
        	    word = searchWord.split(" ");
        	 //   System.out.println("word length: " + word.length);    
        	    
        	 	for(int i = 0; i < word.length; ++i)
                {
        	 	 //  System.out.println("word" + i + "is" + word[i]);          	 	   
                }
        	 	
        	 	if (word[0].equals(firstWord(child)))
        	 	{
        	 		System.out.println("We got a MATCH! at Node:"+child + " with children count of: "+nodes[child].next.size());      

        	 		//continue to check for the first word in the child branch:
        	 	///	System.out.println("Its children are nodes: "+nodes[child].next.values());           	 
        	 		//matchedChildrenNode = nodes[child].next.values();
        	 		
        	 		
        	 		
        	 		for(Map.Entry<String,Integer> entry : nodes[child].next.entrySet() ) {
        	 			  String key = entry.getKey();
        	 			  Integer value = entry.getValue();
        	 			  matchedChildrenNode[matchedCount++] = value;
        	 		//	  System.out.println("With Values:" +key + " => " + value);
        	 			}
        	 		
        	 		
        	 		
//        	 		for(Map.Entry<String, Integer> entry : nodes[child].next.entrySet()){
//         			   String s="";
//         			   for(int i = nodes[entry.getValue()].start; i < nodes[entry.getValue()].end; i++)
//         			   {
//         				   s+=text[i];
////         				   if(i != nodes[entry.getValue()].end - 1)
//         					   s+=" ";
//         			   }
//         			   printAllphrase(entry.getValue(), str+s);
//         		   }
        	 		
        	 		
        	 		for (int i = 0; i < nodes[child].next.size(); i++) {
        	 	//		System.out.println("Its children are nodes2: "+nodes[child].next.);
        	 			//matchedChildrenNode[i] = (int) nodes[child].next.get(i);
        	 			//System.out.println("Its children are nodes2: "+matchedChildrenNode[i]);    
        	 				
        	 				if (word[1].equals(firstWord(matchedChildrenNode[i]))) {
        	 					System.out.println("We got a SECOND MATCH! at Node:"+ matchedChildrenNode[i] );
        	 					
        	 					
        	 					  nodesToIgnore[nodesToIgnoreCt++] = matchedChildrenNode[i];
        	 					
        	 				}

        	 	    }
        	 		
	        	 		
        	 		
        	 		
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
        
        
        
        void labelLowFrequency(String lowfreqPhrase) {
        	 	int[] targetNodes = new int[1000];
        	//for (int child : nodes[root].next.values()) {
        	//	System.out.println("LowFreqNode: "+nodes[root].next.get("you"));
        		int LowFreqNode = 1;
        		
        		 //nodes[root].isSignificant.put("how", 11);
        		 //nodes[root].isSignificant.put("are", 10);
        		 //nodes[root].isSignificant.put("you", 12);
        		 foundNodesCt = 0;
        		 //targetNodes = searchTree(root, lowfreqPhrase);   
        		 targetNodes = searchTree_Advanced(root, lowfreqPhrase);   
        		 
        		// System.out.println("targetNodes Length: "+ foundNodesCt);
        		 
        			for (int i=0; i<foundNodesCt; i++) {
                //		System.out.println("Found Nodes: "+ targetNodes[i]);
                	}
             //   	foundNodesCt=0;
        		 
        		 for (int i=0; i<foundNodesCt;i++) {        			 
        			// System.out.println("targetNode"+i+": " +targetNodes[i] );
        			 nodes[ targetNodes[i] ].frequencyCount = LowFreqNode;
            		// System.out.println("New FreqCt:"+nodes[ targetNodes[i] ].frequencyCount); 
        		 }
        		 //System.out.println("Label Node No:" + targetNode + " Content: <"+ edgeString(targetNode) + ">"); 
        		
       // 	}
        		// printSignificance(root);
        	//	 printSig(root);
        	     //printFreq(root);;
        }
        
        void printSignificance(int x) {
        //	for (int child : nodes[x].next.values()) {
        	//	System.out.println("Displaying nodes <how> at Node:" + x + ": "+ nodes[x].next.values()+"values: "+nodes[x].next.get("how") + "valuesKey:" +edgeString(nodes[x].next.get("how")) + "  sigKey: " + nodes[x].isSignificant.get("how") +" firstKey: "+ nodes[x].isSignificant.firstKey());//+ "isSignificant:¡@" + nodes[x].significance.get(key));
        	//	System.out.println("Displaying nodes <how> at Node:" + x + ": "+ nodes[x].next.values()+"values: "+nodes[x].next.get("how") + "valuesKey:" +edgeString(nodes[x].next.get("how")) + "  sigKey: " + nodes[x].isSignificant.get("how are you") +" firstKey: "+ nodes[x].isSignificant.firstKey());
        //		System.out.println("Displaying nodes <how> at Node:" + x + ": "+ nodes[x].next.values()+"values: "+nodes[x].next.get("how") + "valuesKey:" +edgeString(nodes[x].next.get("how")) + "  sigKey: " + nodes[x].isSignificant.get("are you") +" firstKey: "+ nodes[x].isSignificant.firstKey());
        	//	System.out.println("Displaying nodes <how> at Node:" + x + ": "+ nodes[x].next.values()+"values: "+nodes[x].next.get("how") + "valuesKey:" +edgeString(nodes[x].next.get("how")) + "  sigKey: " + nodes[x].isSignificant.get("you") +" firstKey: "+ nodes[x].isSignificant.firstKey());
        //		System.out.println("Displaying nodes <how> at Node:" + x + ": "+ nodes[x].next.values()+"values: "+nodes[x].next.get("how") + "valuesKey:" +edgeString(nodes[x].next.get("how")) + "  sigKey: " + nodes[x].isSignificant.get("are") +" firstKey: "+ nodes[x].isSignificant.firstKey());
//        		String test = nodes[x].isSignificant.firstKey();   
        		
        	//	String[] s= Arrays.copyOfRange(text, nodes[x].start, nodes[x].start+1);
        //		System.out.println("First word" + s);
        		
//        		System.out.println("Displaying nodes are=" + x + ": "+ nodes[child].next.values()+"values: "+nodes[child].next.get("are") + "valuesKey:" +edgeString(nodes[child].next.get("are")) + "sigKey: " + nodes[child].isSignificant.get("are"));
//        		System.out.println("Displaying nodes you=" + x + ": "+ nodes[child].next.values()+"values: "+nodes[child].next.get("you") + "valuesKey:" +edgeString(nodes[child].next.get("you")) + "sigKey: " + nodes[child].isSignificant.get("you"));
//        		System.out.println("Displaying nodes No:" + child + " Value: "+ edgeString(child)+" isSignificant: "+nodes[child].isSignificant.get("how"));
//        		System.out.println("Displaying nodes No:" + child + " Value: "+ edgeString(child)+" isSignificant: "+nodes[child].isSignificant.get("are"));
//        		System.out.println("Displaying nodes No:" + child + " Value: "+ edgeString(child)+" isSignificant: "+nodes[child].isSignificant.get("you"));
       // 		printSignificance(child);
        	//}
        }
        
        void printSig(int x) {
        	for (int child : nodes[x].next.values()) {
        	//	System.out.println("Displaying nodes No:" + child + "Value: <"+ edgeString(child) + "> SignifCt: " + nodes[child].isSignificant.get(firstWord(child)) );       	
        		 printSig(child);
        	}
        }
        
        void printFreq(int x) {
        	for (int child : nodes[x].next.values()) {
      ///  		System.out.println("Displaying nodes No:" + child + "Value: <"+ edgeString(child) + "> FreqCt: " + nodes[child].frequencyCount );       	
        		printFreq(child);
        	}
        }
        
    }

    public ST( Map<String, Integer> table) throws Exception {
    	
        in = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new FileWriter("st.dot"));
        Scanner input = new Scanner(System.in);
        System.out.println("Enter the number of phrases:");
        int numberOfPhrase = input.nextInt();
        
        SuffixTree st1 = new SuffixTree(500);
        //Suppose a document has 500 words at most
        
       	String[] word; 
       	
       	while(numberOfPhrase-- > 0){
       	System.out.println("Enter a phrase:");
       	word=in.readLine().split(" ");    
    //	System.out.println("word length: " + word.length);       
        boolean inTable = true;
       	for(int i = 0; i < word.length; ++i)
        {
       		//System.out.println("i: " + i);       
       		 inTable = true;
       		 if(i+1 < word.length){
       			String s = word[i]+" "+word[i+1];
        		 
        		 if(!table.containsKey(s))
        			inTable=false;
        		 
        		 //System.out.println("String s: " + s +" isFrequent: " + inTable);  
        	 }
        	 
        		 st1.addWord(word[i], inTable);
        }
        st1.sep();
       	}
       	
       // st1.printNodes();
        st1.printTree();
     
        
    	//st1.searchTree(1, "how are you");
    	//st1.searchTree(1, "are you");
    	//st1.searchTree(1, "you");
    	
     //   st1.labelLowFrequency("me asap");
    	st1.labelLowFrequency("call if");
    	st1.labelLowFrequency("call asap");
    	st1.labelLowFrequency("you call");

    	
    	System.out.println("Nodes To Ignore due to Low Frequency: ");
    	for(int i = 0; i < st1.nodesToIgnoreCt; ++i)
    	{
    		System.out.println("nodesToIgnore: "+st1.nodesToIgnore[i]);
    	}
    	
    	
        out.close();
    }

    public static void main(String ... args) throws Exception{
    	Map<String, Integer> table = new TreeMap<String,Integer>();
    	table.put("please call",3);
    	table.put("call me", 2);
    	table.put("if you", 2);
    	table.put("me asap", 2);
  //S  	table.put("call if", 2);  //false
    	new ST(table);
    }
}