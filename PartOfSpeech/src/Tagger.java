import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.lang.*;
import java.time.format.ResolverStyle;
import java.util.TreeMap;

import javax.activation.MailcapCommandMap;

public class Tagger extends FileLoader {
	 final static double PI_SMOUTH = 1e-10;
	 final static double A_SMOUTH = 1e-10;
	 final static double B_SMOUTH = 1e-10;
	 final static String[] TAGS = { "NOUN", "VERB", "ADJ", "ADV", "PRON", "DET", "ADP", "NUM", "CONJ","PRT", ".", "X"} ;
	//pi
	 
	private ArrayList<String> sentences = new ArrayList<>();
	private TreeMap<String, Double> tagFrequency = new TreeMap<String , Double>();
	private TreeMap< String , TreeMap<String , Double>> prevActualMap = new TreeMap< String , TreeMap<String , Double>>();
	
	private TreeMap< String , TreeMap<String , Double>> tagTockenMap = new TreeMap< String , TreeMap<String , Double>>();
	
	private ArrayList<String[]> tockenTags = new ArrayList<String[]>();
	//
	
	private ArrayList<String> testSentences = new ArrayList<>();
	
	public void setTestSentences(ArrayList<String> testSentences) {
		this.testSentences = testSentences;
	}
	
	public ArrayList<String> getTestSentences() {
		return testSentences;
	}
	
	
	public void setSentences(ArrayList<String> sentences) {
		this.sentences = sentences;
	}
	
	public ArrayList<String> getSentences() {
		return sentences;
	}
	
	
	public Tagger() {
		// TODO Auto-generated constructor stub
		
		TreeMap< String , Double> a = new TreeMap<String , Double>();
		
		TreeMap< String , Double> NOUN = new TreeMap<String , Double>();
		TreeMap< String , Double> VERB = new TreeMap<String , Double>();
		TreeMap< String , Double> ADJ = new TreeMap<String , Double>();
		TreeMap< String , Double> PRON = new TreeMap<String , Double>();
		TreeMap< String , Double> DET = new TreeMap<String , Double>();
		TreeMap< String , Double> ADP = new TreeMap<String , Double>();
		TreeMap< String , Double> NUM = new TreeMap<String , Double>();
		TreeMap< String , Double> POINT = new TreeMap<String , Double>();
		TreeMap< String , Double> X = new TreeMap<String , Double>();
		TreeMap< String , Double> PRT = new TreeMap<String , Double>();
		TreeMap< String , Double> CONJ = new TreeMap<String , Double>();
		TreeMap< String , Double> ADV = new TreeMap<String , Double>();
		try {
			for (String tt : TAGS) {
				
				a.put(tt , (double) 0);
				
				NOUN.put(tt, .0);
				VERB.put(tt, .0);
				ADJ.put(tt, .0);
				PRON.put(tt, .0);
				DET.put(tt, .0);
				ADP.put(tt, .0);
				NUM.put(tt, .0);
				CONJ.put(tt, .0);
				PRT.put(tt, .0);
				POINT.put(tt, .0);
				X.put(tt, .0);
				ADV.put(tt, .0);
				
		
		}
			
		prevActualMap.put("NOUN", NOUN);
		prevActualMap.put("VERB", VERB);
		prevActualMap.put("ADJ", ADJ);
		prevActualMap.put("ADV", ADV);
		prevActualMap.put("PRON", PRON);
		prevActualMap.put("DET", DET);
		prevActualMap.put("ADP", ADP);
		prevActualMap.put("NUM", NUM);
		prevActualMap.put("CONJ", CONJ);
		prevActualMap.put("PRT", PRT);
		prevActualMap.put("X", X);
		prevActualMap.put(".", POINT);
		
			
			setMap(a);
			setMap_2d(prevActualMap);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;		}
	
	}
	
	public void countCat(Tagger tagger) {
		
		
		// TODO Auto-generated method stub
		ArrayList<String[]> tockeTags= tagger.getTockenTag();
		TreeMap< String , TreeMap<String , Double>> tagTockenMap = new TreeMap<>(); 
		TreeMap< String , Double>  tagMapp = new TreeMap<>();
		
		
		for(String tag : TAGS) {
		tagTockenMap.put(tag, new TreeMap<>());
		tagMapp.put(tag, 0.0);
		}
		
		
		try {
			
		
		for (int i = 0; i < tockeTags.size()-1; i++) {
			TreeMap< String, Double> temptemp = new TreeMap<>();
			
			
			String tag= tockeTags.get(i)[1];
			
			
			String tocken= tockeTags.get(i)[0];
			
			
			temptemp = tagTockenMap.get(tag);
			
			if (temptemp.containsKey(tocken)) {
				temptemp.put(tocken, temptemp.get(tocken)+1.0);
			}else {
				
				temptemp.put(tocken, 1.0);
			}
			
			tagMapp.put(tockeTags.get(i)[1],tagMapp.get(tockeTags.get(i)[1])+1.0 );
			
			
			TreeMap< String , Double> aa = new TreeMap<>();

			String prevTag = tockeTags.get(i)[1];

			if(prevActualMap.containsKey(prevTag)) {
			
			String actualTag = tockeTags.get(i+1)[1];

			if(prevActualMap.containsKey(actualTag)) {
			
			 aa = prevActualMap.get(prevTag);
			aa.put(actualTag, aa.get(actualTag)+1.0);
			}
			}
			
			
		}


		
/////////////////////////////////////////////////////////////		
		//TagTockenMap probability
		double total1 =  0.0;
		for (String tag : TAGS){
			 total1= 0.0;	
			TreeMap< String , Double> itm = prevActualMap.get(tag);

			for (String t : TAGS) {
				if (itm.containsKey(t)) {
					total1 += itm.get(t)+A_SMOUTH;	
					
				}
			}
			
			for (String tt : TAGS) {
				if (itm.containsKey(tt)) {
					itm.put(tt, (itm.get(tt)+A_SMOUTH)/total1);	
					
				}
			}
			
			tagTockenMap.get(tag).put("<UNK>", B_SMOUTH/total1);
			
		}
		
/////////////////////////////////////////////////////////////////		
		
		
			double total2 = 0.0;

			for (String t : TAGS) {
				if (tagMapp.containsKey(t)) {
					total2 += tagMapp.get(t)+PI_SMOUTH;	
					
				}
			}
			
			for (String tt : TAGS) {
				if (tagMapp.containsKey(tt)) {
					tagMapp.put(tt, (tagMapp.get(tt)+PI_SMOUTH)/total2);	
					
				}
			}
			
		
		
//////////////////////////////////////////////////////////////////
		
			
			
			
			double total3 =  B_SMOUTH;
			for (String tag : TAGS){
				total3 =  B_SMOUTH;	
				TreeMap< String , Double> tockens = tagTockenMap.get(tag);
				for (Entry<String, Double> a : tockens.entrySet()) {
					
					total3 += a.getValue();					
			}
				
				for (Entry<String, Double> a : tockens.entrySet()) {
					
					tockens.put(a.getKey(), (a.getValue()+B_SMOUTH)/total3);

					
			}

			}
			
		
		
		
		
			 
			 
		tagger.setMap_2d(prevActualMap);
		setTagTockenMap(tagTockenMap);
		setMap(tagMapp);
		
		} catch (Exception e) {
			// TODO: handle exception
			
			
		}
	}
	
	
	
	public void setMap(TreeMap<String, Double> map) {
		this.tagFrequency = map;
	}
	
	public void setMap_2d(TreeMap<String, TreeMap<String, Double>> map_2d) {
		this.prevActualMap = map_2d;
	}
	
	public TreeMap<String, Double> getMap() {
		return tagFrequency;
	}
	
	public TreeMap<String, TreeMap<String, Double>> getMap_2d() {
		return prevActualMap;
	}

	
	
	public ArrayList<String[]> getTockenTag() {
		return tockenTags;
	}
	
	public void setTockenTags(ArrayList<String[]> tockenTags) {
		this.tockenTags = tockenTags;
	}
	
	public void setTagTockenMap(TreeMap<String, TreeMap<String, Double>> tagTockenMap) {
		this.tagTockenMap = tagTockenMap;
	}
	
	public TreeMap<String, TreeMap<String, Double>> getTagTockenMap() {
		return tagTockenMap;
	}
	
	
	
	//split corpus in tokens + split tockens in [word,tag]

	
	public static ArrayList<String> mostProbableTag(String[] tokens ,   Tagger tagger) {
		double val = .0;
		
		
		TreeMap< String , TreeMap<String , Double>> tagTockMap =tagger.getTagTockenMap();
		ArrayList<String> ret= new ArrayList<>();
		 
		for (String str : tokens) {
			
			double max_val = -1;
			String max_tag = "";
			for (String t : TAGS) {
				
				TreeMap<String , Double> temp = tagTockMap.get(t);
				
				if(temp.containsKey(str)) {
					
					val = temp.get(str);
					//System.out.println(val);
					if (val > max_val) {
	                    max_val = val;
	                    max_tag = t;
				 }
					
				}
				
				 
			}
			
			ret.add(max_tag);
			
		}
		
		return ret;
	}
	
	
	
	public ArrayList<String> veterbiTags(Tagger tagger , String[] tockens) throws Exception {
		if (tockens.length==1) {
			throw new Exception("No Tockens")  ;
		}
		
		String tag_i = "";
        String tag_j = "";
		
		ArrayList<String> result = new ArrayList<>();
		ArrayList<Double[]> delta = new ArrayList<>();
		ArrayList<Double[]> back= new ArrayList<>();
		
		for(String t : tockens) {
			
			Double[] tmp =new Double[TAGS.length];
			Double[] tmp1 =new Double[TAGS.length];
			
		for (int i = 0 ; i<tmp.length ; i++) {
			
			tmp[i] = .0;
			tmp1[i]= .0;
		}
		delta.add(tmp);
		back.add(tmp1);
		}
		
		
		
		double max_i = 0;
		double max_val = 0;
		String t="";
		double b =.0;
		
		for (int i = 0; i < TAGS.length; i++) {
			 
			 max_i = 0;
			 max_val = 0;
			 t="";
			 b =.0;
			 
			 t = TAGS[i];
			if (tagTockenMap.get(t).containsKey(tockens[0])) {
				
				b = tagTockenMap.get(t).get(tockens[0]);
				
			}else{
				b = tagTockenMap.get(t).get("<UNK>");
				}
			
			delta.get(0)[i] = tagFrequency.get(t) * b;
			
			//System.out.println(Arrays.toString(delta.get(0)));
		}
		
		for (int i = 0; i < delta.size()-1; i++) {
			//System.out.println(delta.get(0)[i]);
		}
		
		
		for (int i = 1; i < tockens.length-1; i++) {
			for (int j = 0; j < TAGS.length; j++) {
				  max_i = 0;
				  max_val = -1;
				  for (int k = 0; k < TAGS.length; k++) {
					  tag_i = TAGS[k];
			          tag_j = TAGS[j];
			          double val = delta.get(i-1)[k]* prevActualMap.get(tag_i).get(tag_j);
	                  if( val > max_val) {
	                  
	                	  max_val = val;
	                      max_i = i;
	                  
	                  }
				  }
				  back.get(i)[j] = max_i;
			      tag_j = TAGS[j];
			      
			      if( tagTockenMap.get(tag_j).containsKey(tockens[i])) {
			    	  b=tagTockenMap.get(tag_j).get(tockens[i]);
			      }else { b = tagTockenMap.get(tag_j).get("<UNK>");}
                  
			      delta.get(i)[j] = max_val*b;
			}
		}
		
		for (Double[] doubles : delta) {
			result.add(TAGS[Arrays.asList(doubles).indexOf(Collections.max(Arrays.asList((doubles))))]);
		}
		
		
		
		            
		            
		

		
		
		/*for (Double[] doubles : delta) {
			System.out.println(Arrays.toString(doubles));
		}*/
		
		return result;
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		try {
			
		
		Tagger tagger = new  Tagger();
		
		
		String path = "text/corpus.txt";
//		System.out.println(args[0]);
		System.out.println();
		loadCorpus(tagger , args[0] , args[1] );
//		loadCorpus(tagger , path , "text/testData.txt");	
		tagger.countCat(tagger);
		
		
		ArrayList< String > testSet= tagger.getTestSentences();
		
		for (String sentence : testSet) {
				
		
		
		String[] tmp = sentence.trim().split(" ");
		int i= 0;
		String[] tags = new String[tmp.length];
		String[] tockens = new String[tmp.length];
		
		for (String t : tmp) {
			if (t.indexOf("=")!=-1) {
				tags[i] = t.substring(t.indexOf("=")+1, t.length());
				tockens[i] = t.substring( 0 , t.indexOf("="));	
			}else {
				//throw new Exception("Sentence not Valid");
				
			}
			i++;
		}
		
		
		
		ArrayList< String > b = tagger.veterbiTags(tagger, tockens);
		String result = "";
		for (String str : b) {
			result+= str + " ";
		}
		result = "[ "+result+"]";
		System.out.println();
		System.out.println(sentence);
//		System.out.println();
//		System.out.println(result);
		System.out.println();
		
		double accuracy = 0.0;
		for (int j = 0; j < tockens.length-1; j++) {
			if (tags[j].equals(b.get(j))) {
				accuracy++; 
			}
			System.out.println("Guessed : " + b.get(j) + "     Actual : " + tags[j]);
		}
		
		System.out.println("Accuracy = "  +  accuracy/(tockens.length-1));
		}
		
		//tagger.createTestData(tagger);
		
		
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		 
		 
	}
	 
	 
	 
	
	
	
}
