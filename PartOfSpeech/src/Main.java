import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

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
			ArrayList<String> testSet = tagger.loadTestData(testFolder);

			
			//initialize HMM
			if (args[2].isEmpty()) {
				System.out.println("Please provide an output directory");
				return;
			}
			File Fileright = new File(args[2]+"/results.txt");
	        PrintWriter pw = new PrintWriter(Fileright);
			double totalAccuracy = 0.0;
			
			
			for (String sentence : testSet) {
				
				String[] sentenceTokens = sentence.trim().split(" ");
				int i= 0;
				String[] tags = new String[sentenceTokens.length];
				String[] tockens = new String[sentenceTokens.length];
				
				
				for (String t : sentenceTokens) {
					if (t.indexOf("/")!=-1) {
						tags[i] = t.substring(t.indexOf("/")+1, t.length());
						tockens[i] = t.substring( 0 , t.indexOf("/"));	
					}else {
						//throw new Exception("Sentence not Valid");
						
					}
					i++;
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
				for (int j = 0; j < tockens.length; j++) {
					if (tags[j].equals(estimatedTags.get(j))) {
						accuracy++; 
					}
					System.out.println("Guessed : " + estimatedTags.get(j) + "     Actual : " + tags[j]);
				}
				System.out.println();
				System.out.println("Accuracy = "  +  accuracy/(tockens.length));
				
				totalAccuracy += accuracy/(tockens.length);
				
	
				tagger.writeResult(tagger, estimatedTags, sentenceTokens, pw);
			
			}


			
			System.out.println("Total accuracy: " + totalAccuracy/testSet.size());
			pw.close();
			
		}
		else if (args[0].isEmpty()) {
			System.out.println("Please specify a mode.");
		}
		else {
			System.out.println("No mode named " + args[0] +" supported.");
		}
	}
}
