package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import junit.framework.TestCase;
import ntu.selab.phrase.suffixtree.tree.PhraseSuffix_Tree;

public class calculateCollocations extends TestCase {
	
	
	public void testCollectionFreqency() throws Exception {
		File inCorpus = new File("input3.txt");
		BufferedReader in = new BufferedReader(new FileReader(inCorpus));
		PrintWriter out = new PrintWriter(new FileWriter("complex2gram2"));
		PhraseSuffix_Tree st = new PhraseSuffix_Tree(500000);
		String phrase;
		
		
		//CollocationsBuilder CB = new CollocationsBuilder();
		
		while((phrase=in.readLine()) != null){
			String [] word=phrase.split(" ");
			for(int i = 0; i < word.length; i++){
				if(i==word.length-1) word[i]+="$";
				st.addWord(word[i]);
			}
			st.sep();
		}
		in.close();
		st.signSignificance();
		st.printSignificanceNodes();
		st.caculateCollocationFrequencies(inCorpus, out);
		
		
		//It can write into printTree method
//		double [] Normalization= new double [500];
//		double [] Frequency= new double [500];
//		for(int i=0 ; i <Collocation.collocationCount;i++){
//			System.out.print("Score: "+PhraseSuffix_Tree.collocationStrings[i]+" with :"+Collocation.collcationSignificance[i]);
//			Normalization[i]=Collocation.collcationSignificance[i]/Collocation.totalScorce;
//			System.out.print("\tNormalization: "+Normalization[i]);
//			Frequency[i]=Normalization[i]/2;
//			System.out.println("\tFrequency: "+Frequency[i]);
//		}
		
		
		
		
//		try(PrintWriter out2 = new PrintWriter(new BufferedWriter(new FileWriter("complex2gram.txt", true)))) {
//		    out2.println("the text");
//		}catch (IOException e) {
//		    //exception handling left as an exercise for the reader
//		}
		
		
        out.close();
	}
	
	
}