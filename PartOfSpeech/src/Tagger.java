import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.time.format.ResolverStyle;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.activation.MailcapCommandMap;

public class Tagger extends FileLoader {
	 final static double PI_SMOUTH = 1e-10;
	 final static double A_SMOUTH = 1e-10;
	 final static double B_SMOUTH = 1e-10;
	 final static String[] TAGS = { "NOUN", "VERB", "ADJ", "ADV", "PRON", "DET", "ADP", "NUM", "CONJ","PRT", ".", "X"} ;
	//pi
	 
	public static List<String> BROWNECORPUSTAGS = new ArrayList<String>();
	
	
	public static void setBROWNECORPUSTAGS(List<String> bROWNECORPUSTAGS) {
		BROWNECORPUSTAGS = bROWNECORPUSTAGS;
	}
	public String[]  getBROWNECORPUSTAGS() {
		String[] tags = new String[BROWNECORPUSTAGS.size()];
		
		return BROWNECORPUSTAGS.toArray(tags);
	}
	 
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
	
	}
	
	public void _init_(Tagger tagger) throws Exception {
		
		

		String[] tags=tagger.getBROWNECORPUSTAGS();
		TreeMap< String , Double> aaa = new TreeMap<String , Double>();
		
		
		
			for (String tt : tags) {
				aaa.put(tt, 0.0);
				prevActualMap.put(tt, new TreeMap<>());
				
				for (String t : tags) {
					prevActualMap.get(tt).put(t, 0.0);
				}
			}
			
			
			setMap(aaa);
			setMap_2d(prevActualMap);
	
		
		
		// TODO Auto-generated method stub
		ArrayList<String[]> tockeTags= tagger.getTockenTag();
		
		TreeMap< String , TreeMap<String , Double>> tagTockenMap = new TreeMap<>(); 
		TreeMap< String , Double>  tagMapp = new TreeMap<>();
		
		
		for(String tag : tags) {
		tagTockenMap.put(tag, new TreeMap<>());
		tagMapp.put(tag, 0.0);
		}
		
		
			
		for (int i = 0; i < tockeTags.size()-1; i++) {
			TreeMap< String, Double> temptemp = new TreeMap<>();
			
			
			String tag= tockeTags.get(i)[1];
			
			String tocken= tockeTags.get(i)[0];
			
			if(tagTockenMap.containsKey(tag)) {
			temptemp = tagTockenMap.get(tag);
			
			
				if (!tocken.equals("\\.")) {
					
				if (temptemp.containsKey(tocken)) {
					temptemp.put(tocken, temptemp.get(tocken)+1.0);
				}else {
					temptemp.put(tocken, 1.0);
				   }	
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
		}
		
/////////////////////////////////////////////////////////////		
		//TagTockenMap probability
		double total1 =  0.0;
		for (String tag : tags){
			 total1= 0.0;	
			TreeMap< String , Double> itm = prevActualMap.get(tag);

			for (String t : tags) {
				if (itm.containsKey(t)) {
					total1 += itm.get(t)+A_SMOUTH;	
					
				}
			}
			
			for (String tt : tags) {
				if (itm.containsKey(tt)) {
					itm.put(tt, (itm.get(tt)+A_SMOUTH)/total1);	
					
				}
			}
			
			tagTockenMap.get(tag).put("<UNK>", B_SMOUTH/total1);
			
		}
		
/////////////////////////////////////////////////////////////////		
		
		
			double total2 = 0.0;

			for (String t : tags) {
				if (tagMapp.containsKey(t)) {
					total2 += tagMapp.get(t)+PI_SMOUTH;	
					
				}
			}
			
			for (String tt : tags) {
				if (tagMapp.containsKey(tt)) {
					tagMapp.put(tt, (tagMapp.get(tt)+PI_SMOUTH)/total2);	
					
				}
			}
			
		
		
//////////////////////////////////////////////////////////////////
		
			
			
			
			double total3 =  B_SMOUTH;
			for (String tag : tags){
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
		System.out.println("Model was trained.");
		
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
		String[] tags = tagger.getBROWNECORPUSTAGS();
		
		
//		if (tockens.length==1) {
//			throw new Exception("No Tockens")  ;
//		}
		
		
		String tag_i = "";
        String tag_j = "";
		
		ArrayList<String> result = new ArrayList<>();
		ArrayList<Double[]> delta = new ArrayList<>();
		ArrayList<Double[]> back= new ArrayList<>();
		
		
		for(String t : tockens) {
			
			Double[] tmp =new Double[tags.length];
			Double[] tmp1 =new Double[tags.length];
			
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
		
		for (int i = 0; i < tags.length; i++) {
			 
			 max_i = 0;
			 max_val = 0;
			 t="";
			 b =.0;
			 
			 t = tags[i];
			 
			 //System.out.println(tagTockenMap);
			 
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
			for (int j = 0; j < tags.length; j++) {
				  max_i = 0;
				  max_val = -1;
				  for (int k = 0; k < TAGS.length; k++) {
					  tag_i = tags[k];
			          tag_j = tags[j];
			          double val = delta.get(i-1)[k]* prevActualMap.get(tag_i).get(tag_j);
	                  if( val > max_val) {
	                  
	                	  max_val = val;
	                      max_i = i;
	                  
	                  }
				  }
				  back.get(i)[j] = max_i;
			      tag_j = tags[j];
//			      
//			      System.out.println(tockens[i]);
//			      System.out.println(tagTockenMap.get(tag_j));
			      
			      
			      if( tagTockenMap.get(tag_j).containsKey(tockens[i])) {
			    	  b=tagTockenMap.get(tag_j).get(tockens[i]);
			      }else { b = tagTockenMap.get(tag_j).get("<UNK>");}
                  
			      delta.get(i)[j] = max_val*b;
			}
		}
		
		for (Double[] doubles : delta) {
			
			result.add(tags[Arrays.asList(doubles).indexOf(Collections.max(Arrays.asList((doubles))))]);
		}
		
		
		
		            
		            
		

		
		
		/*for (Double[] doubles : delta) {
			System.out.println(Arrays.toString(doubles));
		}*/
		
		return result;
	}
	
	
	public void loadModelFromExecDir() {
		System.out.println("Loading Model from execution directory ...");
		ObjectMapper mapper = new ObjectMapper();
		
		File readFrom = new File("model.json");
		Model model = new Model();
		try {
			model = mapper.readValue(readFrom, Model.class);
			
			this.setMap_2d(model.getPrevActualMap());
			this.setTagTockenMap(model.getTagTockenMap());
			this.setMap(model.getTagFrequency());
			this.setBROWNECORPUSTAGS(Arrays.asList(model.getBrownCorpusTags()));
			
		} catch (JsonParseException e) {
			System.out.println("Failed to read model due to parsing exception: " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			System.out.println("Failed to read model due to mapping exception: " + e.getLocalizedMessage());
		} catch (IOException e) {
			System.out.println("Failed to read model from file " + readFrom.getAbsolutePath());
		}
	}
	
	public void writeModelToExecDir() {
		ObjectMapper mapper = new ObjectMapper();
		Model model = new Model(this.tagFrequency, this.prevActualMap, this.tagTockenMap, this.getBROWNECORPUSTAGS());
        
		try {
			File modelDestination = new File("model.json");
            System.out.println("Exporting model to " + modelDestination.getAbsolutePath());
            mapper.writeValue(modelDestination, model);
        } catch (IOException e) {
            System.out.println("Failed to write model to file. " + e);
        }
	}
 	
}
