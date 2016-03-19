package ntu.selab.phrase.suffixtree;

import java.util.Map;
import java.util.TreeMap;

public class Table {
	
	private Map<String, Float> mixed;
	private float total = 16;
	public Table(){
	
	mixed = new TreeMap<String, Float>();
	}


	public void addToMix(String word){
		if(mixed.containsKey(word))
			mixed.put(word, mixed.get(word)+1/total);
		else
			mixed.put(word, 1/total);
	}
	public void printMix(){
		for(Map.Entry<String, Float> entry : mixed.entrySet()) {
			String key = entry.getKey();
			Float value = entry.getValue();
			System.out.println(key + "=" + value);
		}
	}
	public Map<String, Float> getMap(){
		return mixed;
	}
}