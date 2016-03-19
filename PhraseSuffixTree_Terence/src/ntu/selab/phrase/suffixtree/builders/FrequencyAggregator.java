package ntu.selab.phrase.suffixtree.builders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FrequencyAggregator {
	
	
	public void aggregateFrequency(File inCorpus) throws IOException {
		
		DocumentBuilder docBuilder = new DocumentBuilder();
		
		System.out.print("STEP1: Reading Input Corpus.... ");
		System.out.println("Done!"); 
		
		PrintWriter outFrequency;
		outFrequency = new PrintWriter(new FileWriter("complex2gram.txt"));
		try{
			BufferedReader br = new BufferedReader(new FileReader(inCorpus));
			String line;
			
			System.out.print("STEP2: Tokenizing and Parsing.... ");
			
				while((line=br.readLine()) != null){
					docBuilder.scanToTable(line, 2); //nGram = 2
				}
				
			docBuilder.printTable(outFrequency);
			System.out.println("Done!"); 
			br.close();
			
			System.out.print("STEP3: Calculating Frequencies.... ");
			System.out.println("Done!"); 
			outFrequency.close();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
		
}
