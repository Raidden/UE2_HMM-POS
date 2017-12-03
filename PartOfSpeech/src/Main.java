import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	
public static void main(String[] args) throws Exception {
		
		
		try {
			
		
		Tagger tagger = new  Tagger();
		
		//load tags from Folder
		File folder = new File("brown_training");
		tagger.loadTags(tagger, folder);
		
//		display Tags
		System.out.println(Arrays.toString(tagger.getBROWNECORPUSTAGS()));
		
		String path = "text/corpus.txt";
		

		tagger.loadCorpus(tagger , folder , path );
		
		
		tagger.countCat(tagger);
		
		
		ArrayList< String > testSet= tagger.getTestSentences();
		
				
		String sentence = "Implementation/nn of/in Georgia's/np$ automobile/nn title/nn law/nn was/bedz also/rb recommended/vbn by/in the/at outgoing/jj jury/nn";
		
		String[] tmp = sentence.trim().split(" ");
		int i= 0;
		String[] tags = new String[tmp.length];
		String[] tockens = new String[tmp.length];
		
		for (String t : tmp) {
			if (t.indexOf("/")!=-1) {
				tags[i] = t.substring(t.indexOf("/")+1, t.length());
				tockens[i] = t.substring( 0 , t.indexOf("/"));	
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
		System.out.println();
		
		double accuracy = 0.0;
		for (int j = 0; j < tockens.length; j++) {
			if (tags[j].equals(b.get(j))) {
				accuracy++; 
			}
			System.out.println("Guessed : " + b.get(j) + "     Actual : " + tags[j]);
		}
		System.out.println();
		System.out.println("Accuracy = "  +  accuracy/(tockens.length));
		
		
		
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
