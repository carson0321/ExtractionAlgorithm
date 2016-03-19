package ntu.selab.phrase.suffixtree.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import ntu.selab.phrase.suffixtree.*;
import junit.framework.TestCase;

import org.junit.Test;






import static org.junit.Assert.*;



public class RunTree extends TestCase {


		//CompactSuffixTree tree = new CompactSuffixTree(new SimpleSuffixTree("bananas$"));
	
	 public void testBasicTreeGeneration() {
		 String childNodes = null;
		 String root1 = "FirstTree";
			
		 SuffixTokenizer document = new SuffixTokenizer();
		 File file = new File("doc1.txt");
		 try{
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 String line;
				while((line=br.readLine()) != null){
					childNodes = document.readLine(line);
					System.out.println("childNodes: " + childNodes);
				}
				
				br.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		 
		 PhraseTree_Root phraseTree = new PhraseTree_Root(root1);
		 
	//	 phraseTree.addNode(st.readLine(line));

		 //Parser
	    
		 
		 
		 //Sentence 1: "please call me asap if you"
//		 phraseTree.addNode("please");
//		 phraseTree.addNode("call");
//		 phraseTree.addNode("me");
//		 phraseTree.addNode("asap");
//		 phraseTree.addNode("if");
//		 phraseTree.addNode("you");
		 
		//Sentence 2:
//		 phraseTree.addNode("call");
		 
		 
		 
		 
		 phraseTree.displayPhraseTree(root1);
		 
		 	 
		 	
		 
	 }
	 	
	 	
	 	
}

