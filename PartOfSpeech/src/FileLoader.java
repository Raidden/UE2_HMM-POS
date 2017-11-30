import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FileLoader {
	
	 public static  void loadCorpus(Tagger tagger, String trainingPath , String testPath)
			    throws IOException {
		 		
		 		ArrayList<String[]> tockenTags = new ArrayList<String[]>();
			    ArrayList<String> sentences = new ArrayList<>();
			    ArrayList<String> testSentences = new ArrayList<>();
			    
		 		try {
					
				 
		 		String file = trainingPath;
			    BufferedReader br = new BufferedReader(new FileReader(file));
			    
			   
			    String line;
			    while( (line = br.readLine()) != null){
			    	
			        String[] tokens =line.split(" "); 
			        
			    	for (String t : tokens) {
						tockenTags.add(t.split("="));
					}
			    	
			    	String[] tmp = line.split("\\.=\\.");
			    	for (String t : tmp) {
			    		sentences.add(t);	
					}
			    	
			    }
			    
			    BufferedReader tr = new BufferedReader(new FileReader(testPath));
			    String line_;
			    while ((line_ = tr.readLine())!=null) {
			    	line_=line_.trim();
			    	if (line_.length()>0) {
			    		String[] sentence =line_.split("\\.=\\.");
				    	
				    	for (String sntc : sentence) {
				    			if (!sntc.equals("") ) {
						    		testSentences.add(sntc);	
								}
				    			
					}
					}
			    	
				}
			    
			    tagger.setTestSentences(testSentences);
			    tagger.setSentences(sentences);
			    tagger.setTockenTags(tockenTags);

//			    System.out.println(Arrays.toString(Sentences.toArray()));
			    
		 		} catch (Exception e) {
					// TODO: handle exception
				}
			  }

			  private static String removePunctuation(String token) {
			    token = token.replaceAll("   ", " ");
			    token = token.replaceAll("/n", "");
			    return token;
			  }

			  public void createTestData(Tagger tagger) {
				// TODO Auto-generated method stub
				  int i =0;
				  ArrayList< String> sentences = tagger.getSentences();
				  PrintWriter writer;
				  Random Dice = new Random(); 
				  int n = 0;
				try {
					 writer = new PrintWriter("TestData.txt", "UTF-8");
					 
					 while (i<5) {
					
						 n = Dice.nextInt((sentences.size()-2)/1000);
						 writer.println(" "+sentences.get(n)+ " .=.");
						 
				     i++;
					}
					 
					 writer.close();
					 
					 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			}
			
			  
			  
}
