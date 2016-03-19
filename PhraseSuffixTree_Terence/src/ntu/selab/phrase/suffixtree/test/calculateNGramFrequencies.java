package ntu.selab.phrase.suffixtree.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import ntu.selab.phrase.suffixtree.builders.DocumentBuilder;
import ntu.selab.phrase.suffixtree.builders.SignificanceBuilder;

import org.junit.Test;

public class calculateNGramFrequencies {

	@Test
	public void test() throws IOException {
	
		DocumentBuilder sc = new DocumentBuilder();
		File file = new File("input3.txt");
		
		PrintWriter out;
		out = new PrintWriter(new FileWriter("complex2gram.txt"));
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while((line=br.readLine()) != null){
				sc.scanToTable(line, 2);
			}
			sc.printTable(out);
			br.close();
			out.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	//	System.out.println("------------------------------");
		float thr = (float) 0;
		SignificanceBuilder sig = new SignificanceBuilder();
		sig.threshold(sc.getTable(), thr);
//		for(Map.Entry<String, Float> entry: sc.getTable().entrySet())
//		{
//			System.out.println(entry.getKey() + " : "+entry.getValue());
//		}
		
		//System.out.println("------------------------------");
//		HashSet<String> si = sig.markSignificant(sc.getTable());
//		for(String s: si)
//			System.out.println("* "+s);
	}
}
