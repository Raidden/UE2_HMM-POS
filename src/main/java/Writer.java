import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Writer {

    //TODO: create a Methode that writes the Results in txt-data
    public void writeResult(Tagger tagger, ArrayList<String> result, String[] sentence, PrintWriter writer) throws FileNotFoundException {
        // TODO Auto-generated method stub


        try {

            String line = "";
            for (int i = 0; i < sentence.length; i++) {

                line += sentence[i].replace(sentence[i].substring(sentence[i].lastIndexOf("/"), sentence[i].length())
                        , "/" + result.get(i)) + " ";

            }

            if (line.contains("./.")) {
                writer.append(line + "\n");
            } else {
                writer.append(line);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }


    }
}
