package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import junit.framework.TestCase;
import ntu.selab.phrase.suffixtree.tree.PhraseSuffix_Tree;

public class runStackOverF_Tree extends TestCase {
	
	

	
//	public void testSO_TreeGeneration() throws Exception{
//		
//			BufferedReader in;
//		    PrintWriter out = null;
////		 	String[] word = null; 
//		
//		  	in = new BufferedReader(new InputStreamReader(System.in));
//	        try {
//				out = new PrintWriter(new FileWriter("st.dot"));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	        Scanner input = new Scanner(System.in);
//	        int f = input.nextInt();
//	        
//	        PhraseSuffix_Tree st1 = new PhraseSuffix_Tree(500);
//	        //Suppose a document has 500 words at most
//	        
//	        String[] word; 
//	       	
//	       	while(f-->0){
//	       	word=in.readLine().split(" ");
//	        for(int i = 0; i < word.length; ++i)
//	        {
//	        	 st1.addWord(word[i]);
//	        }
//	        st1.sep();
//	       	}
//	  
//	       // st1.printNodes();
//	       	st1.signSignificance();
//	       	st1.printSignificanceNodes();
//	        st1.printTree(out);
//	        in.close();
//	        out.close();
//	
//	}
	
	public void testTelescope() throws Exception {
		File file = new File("doc1.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));
		PrintWriter out = new PrintWriter(new FileWriter("st.dot"));
		
		PhraseSuffix_Tree st = new PhraseSuffix_Tree(500000);
		
		
		 String phrase;
		while((phrase=in.readLine()) != null){
			String [] word=phrase.split(" ");
			for(int i = 0; i < word.length; i++){
//				if(i==word.length-1) word[i]+="$";
				st.addWord(word[i]);
			}
			st.sep();
		}
		in.close();
		st.signSignificance();
		st.printSignificanceNodes();
		st.printTelescopeTree(out);
        out.close();
	}
	
//	public void testFile() throws Exception {
//		BufferedReader in = new BufferedReader(new FileReader("input.txt"));
//		PhraseSuffix_Tree st = new PhraseSuffix_Tree(500);
//	
//		while(true){
//		String phrase=in.readLine();
//		System.out.println(phrase);
//		if(phrase == null) break;
//			String[] ta = phrase.split(" ");
//			  for(int i = 0; i < ta.length; ++i)
//		        {
//		        	 st.addWord(ta[i]);
//		        }
//			st.sep();
//		}
//		st.printAllPhrases(1,"");//1 is the root
//	}
	
}