package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

import junit.framework.TestCase;

import org.junit.Test;

import ntu.selab.phrase.suffixtree.tree.PhraseSuffix_Tree;


public class forEmily extends TestCase {
	
		
	@Test
	public void test() throws Exception {
	 
		File file = new File("complex2gram2.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));
		
		PhraseSuffix_Tree st1 = new PhraseSuffix_Tree(50000);
		
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
		String searchWord; 
	 	System.out.print("Enter a phrase: ");
	 	searchWord=in.readLine();    
	 	st1.setInitialMessage(searchWord);
		st1.queryTree(1, searchWord);
	 	searchWord=in.readLine();
	 	System.out.println("");
	 	 do {
	 		st1.queryPredictionTable(1, Integer.parseInt(searchWord));
		 	searchWord=in.readLine();
	 	} while (Integer.parseInt(searchWord)!=0);	 	
	 	st1.getMessage();
	 }
 
}
