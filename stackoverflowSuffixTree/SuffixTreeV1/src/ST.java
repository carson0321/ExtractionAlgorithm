import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

        int active_node, active_length, active_edge;
        ArrayList<Integer> hasLink= new ArrayList<Integer>(); 

        class Node {

            /*
               There is no need to create an "Edge" class.
               Information about the edge is stored right in the node.
               [start; end) interval specifies the edge,
               by which the node is connected to its parent node.
            */

            int start, end = oo, link;
            public TreeMap<String, Integer> next = new TreeMap<String, Integer>();

            public Node(int start, int end) {
                this.start = start;
                this.end = end;
            }

            public int edgeLength() {
                return Math.min(end, position + 1) - start;
            }
        }

        public SuffixTree(int length) {
            nodes = new Node[2* length + 2];
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
            nodes[++currentNode] = new Node(start, end);
            return currentNode;
        }

        public void addWord(String c, boolean isFrequent) throws Exception {
            text[++position] = c;
            needSuffixLink = -1;
            
            remainder++;
          
            while(remainder > 0) {
      //      	System.out.println("active pt: node "+active_node+" length: "+active_length+" edge: "+active_edge);
                if (active_length == 0) active_edge = position;
                if (!nodes[active_node].next.containsKey(active_edge()) ){
                	if(!isFrequent) {
                		remainder--;
                		break;
                	}
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
              if(!isFrequent) break;
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
        		System.out.println("node: "+" start: "+nodes[i].start+"end: "+nodes[i].end);
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
        boolean inTable = true;
       	for(int i = 0; i < word.length; ++i)
        {
       		 inTable = true;
        	 if(i+1 < word.length){
        		 String s = word[i]+" "+word[i+1];
        		 if(!table.containsKey(s))
        			inTable=false;
        	 }
        	 
        		 st1.addWord(word[i], inTable);
        }
        st1.sep();
       	}
       	
       // st1.printNodes();
        st1.printTree();
        out.close();
    }

    public static void main(String ... args) throws Exception{
    	Map<String, Integer> table = new TreeMap<String,Integer>();
    	table.put("please call",3);
    	table.put("call me", 2);
    	table.put("if you", 2);
    	table.put("me asap", 2);
    	new ST(table);
    }
}