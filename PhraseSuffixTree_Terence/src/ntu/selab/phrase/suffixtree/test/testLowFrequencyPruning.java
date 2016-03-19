package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import junit.framework.TestCase;
import org.junit.Test;
import ntu.selab.phrase.suffixtree.tree.PhraseSuffix_Tree;

public class testLowFrequencyPruning extends TestCase {
	
		
	@Test
	public void test() throws Exception {
	 
//		BufferedReader in;
//	    PrintWriter out;
//	    
//	    File file = new File("input.txt");
//		BufferedReader in = new BufferedReader(new FileReader(file));
//	    
//	    Map<String, Integer> table = new TreeMap<String,Integer>();
//    	table.put("please call",3);
//    	table.put("call me", 2);
//    	table.put("if you", 2);
//    	table.put("me asap", 2);
//    	//table.put("call if", 2);  //false
//    	
//	 
//	 	in = new BufferedReader(new InputStreamReader(System.in));
//	    out = new PrintWriter(new FileWriter("st.dot"));
//	    Scanner input = new Scanner(System.in);
//	    System.out.println("Enter the number of phrases:");
//	    int numberOfPhrase = input.nextInt();
//	    
//	    PhraseSuffix_Tree st1 = new PhraseSuffix_Tree(500);
//	    //Suppose a document has 500 words at most
//	    
//	   	String[] word; 
//	   	
//	   	while(numberOfPhrase-- > 0){
//	   	System.out.println("Enter a phrase:");
//	   	word=in.readLine().split(" ");    
//	//	System.out.println("word length: " + word.length);       
//	    boolean inTable = true;
//	   	for(int i = 0; i < word.length; ++i)
//	    {
//	   		//System.out.println("i: " + i);       
//	   		 inTable = true;
//	   		 if(i+1 < word.length){
//	   			String s = word[i]+" "+word[i+1];
//	    		 
//	    		 if(!table.containsKey(s))
//	    			inTable=false;
//	    		 
//	    		 //System.out.println("String s: " + s +" isFrequent: " + inTable);  
//	    	 }
//	    	 
//	    		 st1.addWord(word[i]);
//	    }
//	    st1.sep();
//	   	}
	   	
	   // st1.printNodes();
	  //  st1.printFullTree(out);
	 
		File file = new File("complex2gram.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));
		PrintWriter out = new PrintWriter(new FileWriter("st.dot"));
		
		PhraseSuffix_Tree st1 = new PhraseSuffix_Tree(500000);
		
		
		 String phrase;
		while((phrase=in.readLine()) != null){
			String [] word=phrase.split(" ");
			for(int i = 0; i < word.length; i++){
				//if(i==word.length-1) word[i]+="$";
				st1.addWord(word[i]);
			}
			st1.sep();
		}
		in.close();
		
		in = new BufferedReader(new InputStreamReader(System.in));
		
		
	    //st1.labelLowFrequency("me asap");
//		st1.labelLowFrequency("call if");
//		st1.labelLowFrequency("call asap");
//		st1.labelLowFrequency("you call");
	//	st1.displayLowFrequencyNodes();
		
		//STEP1: �R����C��frequency�bST1�̭�
		st1.removeLowFrequencyNodes();
		
		
		//STEP2: st1����TELESCOPE
		//st1.telescope(1);
		//st1.traveralNode(1);
		
		st1.printFullTree(out);
		
		String searchWord; 
	 	System.out.print("Enter a phrase: ");
	 	searchWord=in.readLine();    
	 	st1.setInitialMessage(searchWord);
	// 	System.out.println("Select a prediction: ");
		st1.queryTree(1, searchWord);
	 	searchWord=in.readLine();
	 	System.out.println("");
	 	 do {
	 		st1.queryPredictionTable(1, Integer.parseInt(searchWord));
		 	searchWord=in.readLine();
	 	} while (Integer.parseInt(searchWord)!=0);	 	
	 	st1.getMessage();
	    out.close();
	 	
	 }

	

	
 
 
}
