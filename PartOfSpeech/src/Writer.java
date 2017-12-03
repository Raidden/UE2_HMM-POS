import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.TODO;

public class Writer {

	//TODO: create a Methode that writes the Results in txt-data
	public void writeResult(Tagger tagger , ArrayList<String> result , String[] sentence ,PrintWriter writer) throws FileNotFoundException {
		// TODO Auto-generated method stub
		try {
			
			String line="";
			for (int i = 0; i < sentence.length; i++) {
				
					line += sentence[i].replace(sentence[i].substring(sentence[i].indexOf("/"), sentence[i].length())
							, "/"+result.get(i))+" ";	
				
				}
			
		if (line.contains("./.")) {
			writer.append(("\n"+line.trim()));
		}else {
			writer.append((line.trim()));
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
}
