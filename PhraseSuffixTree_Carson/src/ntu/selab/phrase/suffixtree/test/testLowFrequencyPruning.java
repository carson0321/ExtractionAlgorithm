package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import junit.framework.TestCase;

import org.junit.Test;

import ntu.selab.phrase.suffixtree.PhraseSuffix_Tree;


public class testLowFrequencyPruning extends TestCase {
	
		
	@Test
	public void test() throws Exception {
	 
		BufferedReader in;
	    PrintWriter out;
	    
	    Map<String, Integer> table = new TreeMap<String,Integer>();
    	table.put("please call",3);
    	table.put("call me", 2);
    	table.put("if you", 2);
    	table.put("me asap", 2);
    	//table.put("call if", 2);  //false
    	
	 
	 	in = new BufferedReader(new InputStreamReader(System.in));
	    out = new PrintWriter(new FileWriter("st.dot"));
	    Scanner input = new Scanner(System.in);
	    System.out.println("Enter the number of phrases:");
	    int numberOfPhrase = input.nextInt();
	    
	    PhraseSuffix_Tree st1 = new PhraseSuffix_Tree(500);
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
	    	 
	    		 st1.addWord(word[i]);
	    }
	    st1.sep();
	   	}
	   	
	   // st1.printNodes();
	  //  st1.printFullTree(out);
	 
	    
		//st1.searchTree(1, "how are you");
		//st1.searchTree(1, "are you");
		//st1.searchTree(1, "you");
		
	  //  st1.labelLowFrequency("me asap");
		st1.labelLowFrequency("call if");
		st1.labelLowFrequency("call asap");
		st1.labelLowFrequency("you call");
		st1.displayLowFrequencyNodes();
		st1.removeLowFrequencyNodes();
		
		st1.printFullTree(out);
		
	    out.close();
	 	
	 	
	 	
	 	
	 }
 
 
}
