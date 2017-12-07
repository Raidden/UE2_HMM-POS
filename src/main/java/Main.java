import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Main {

	
public static void main(String[] args) throws Exception {
		
		if(args.length!=2 && args.length!=3) System.out.println("Argument count insufficient; Please provide a mode name and at least one directory.");
		else if(args[0].equals("train")) {

			//trainingFolder Hier
			File trainingFolder = new File(args[1]);

			Tagger tagger = new  Tagger();

			//load Tags
			tagger.loadTags(tagger, trainingFolder);
			//load TrainingSet
			tagger.loadCorpus(tagger , trainingFolder);
			tagger._init_(tagger);
			
			
//			System.out.println("Tags : \n"+Arrays.toString(tagger.getBROWNECORPUSTAGS()));
			tagger.writeModelToExecDir();
			
		}
		else if (args[0].equals("annotate")) {

			// args[0]: Mode
			// args[1]: Input_Dir (Test Data)
			// args[2]: Output_Dir (Annotated File)
			
			//testFolder hier
			File testFolder = new File(args[1]);

			Tagger tagger = new  Tagger();
			tagger.loadModelFromExecDir();
			//load testSet
			Map<String,ArrayList<String>> testMap = tagger.loadTestData(testFolder);
			
			String[] typeStringArray = new String[0];
			String[] testSetKeys = testMap.keySet().toArray(typeStringArray);
			
			//initialize HMM
			if (args[2].isEmpty()) {
				System.out.println("Please provide an output directory");
				return;
			}
			
			String outputPath = args[2];
			if (outputPath.charAt(outputPath.length()-1) != '/') outputPath += "/";
			
			int totalTokenCount = 0;
			double totalAccuracy = 0.0;
			int docCount=0;
			
			for(int i = 0; i < testSetKeys.length; i++) {
				// ITERATING THROUGH FILES
				docCount ++;
				String fileName = testSetKeys[i];
				File file = new File(outputPath+fileName);
		        PrintWriter pw = new PrintWriter(file);
		        
		        int documentTokenCount = 0;
		        double documentAccuracy = 0.0;
		        
		        ArrayList<String>testSet = testMap.get(fileName);
		        
		        for (String sentence : testSet) {
					// ITERATING THROUGH SENTENCES IN FILE
					String[] sentenceTokens = sentence.trim().split(" ");
					int j= 0;
					String[] tags = new String[sentenceTokens.length];
					String[] tockens = new String[sentenceTokens.length];
					
					
					for (String t : sentenceTokens) {
						if (t.indexOf("/")!=-1) {
							tags[j] = t.substring(t.indexOf("/")+1, t.length());
							tockens[j] = t.substring( 0 , t.indexOf("/"));	
						}else {
							//throw new Exception("Sentence not Valid");
							
						}
						j++;
					}
					
					
					
					ArrayList< String > estimatedTags = tagger.veterbiTags(tagger, tockens);
					String result = "";
					
					for (String str : estimatedTags) {
						result+= str + " ";
					}
					
					result = "[ "+result+"]";
					System.out.println();
					System.out.println(sentence);
					System.out.println();
					
					double accuracy = 0.0;
					for (int k = 0; k < tockens.length; k++) {
						documentTokenCount++;
						if (tags[k].equals(estimatedTags.get(k))) {
							accuracy++;
							documentAccuracy++;
						}
						System.out.println("Guessed : " + estimatedTags.get(k) + "     Actual : " + tags[k]);
					}
					
					System.out.println();
					System.out.println("Accuracy = "  +  accuracy/(tockens.length));
					
					
					tagger.writeResult(tagger, estimatedTags, sentenceTokens, pw);
				
				}
		        
		        totalAccuracy += documentAccuracy;
		        totalTokenCount += documentTokenCount;
		        
		        documentAccuracy /= documentTokenCount;
		        
		        System.out.println("Document accuracy: " + documentAccuracy);
				pw.close();
		        
			}
			
			totalAccuracy /= totalTokenCount;
			
			System.out.println("Total accuracy: " + totalAccuracy);
	
		}
		else if (args[0].isEmpty()) {
			System.out.println("Please specify a mode.");
		}
		else {
			System.out.println("No mode named " + args[0] +" supported.");
		}
	}
}
