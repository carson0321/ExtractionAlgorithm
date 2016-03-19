package ntu.selab.phrase.suffixtree.visitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import ntu.selab.phrase.suffixtree.tree.PhraseSuffix_Tree;
import ntu.selab.phrase.suffixtree.tree.TreeBuilder;
import ntu.selab.phrase.suffixtree.tree.TreeElement;

public class TreeVisitor implements Visitor {
	
	/*
	 *Start of Program 
	 */
	public static void main(String[] args) throws Exception 
	{
		System.out.println("Starting Phrase Prediction Program.... ");
		
		TreeVisitor concreteVist = new TreeVisitor();
			try {
				concreteVist.visitTree();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/*
	 *Suffix Tree Search and Traversal 
	 */
	public void visitTree() throws Exception{

		/*
		 *Suffix Tree Setup and Input 
		 */
		File inputCorpus = new File("input5000.txt");
		TreeBuilder tb = new TreeElement();
		PhraseSuffix_Tree st = tb.buildTree(500000);
		PrintWriter outCollocations = new PrintWriter(new FileWriter("complex2gram.txt",true));
		tb.collectFrequenciesFromFile(inputCorpus);
		tb.calculateCollocations(st, inputCorpus, outCollocations);
		tb.addToTree(st);
		tb.updateTree(st);
		outCollocations.close();
		
		/*
		 *Suffix Tree Search and Traversal 
		 */
		BufferedReader inSystem = new BufferedReader(new InputStreamReader(System.in));
		String searchWord; 
	 	System.out.print("Enter a phrase: ");
	 	searchWord=inSystem.readLine();    
	 	st.setInitialMessage(searchWord);
		st.queryTree(1, searchWord);
	  	if (st.suggestionNo == 0)  System.out.println("No Prediction Yet!");
	 	System.out.println("");
	 	st.querySuggestionKey();
	 	System.out.println("");
	 	st.queryPredsuggestionsMap();
	 	searchWord=inSystem.readLine();
	 	System.out.println("");
	 	 do {
	 		st.queryPredictionTable(1, Integer.parseInt(searchWord));
	 	 	System.out.println("");
	 	 	st.querySuggestionKey();
	 	 	System.out.println("");
	 	 	st.queryPredsuggestionsMap();
		 	searchWord=inSystem.readLine();
	 	} while (Integer.parseInt(searchWord)!=0);	 	
	 	st.getMessage();
	 }
}
