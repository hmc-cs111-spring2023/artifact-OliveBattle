import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList; 
import java.util.Set;


public class TextParser {

    String path;
    static final String LINEENDINGS = "\r\n";
    static final String NOTES = "ABCDEFG";
    static final String NOTELENGTHS = "whqes";
    static final String NOTEFLSH = "#b";

    public TextParser(String path) {
        this.path = path;
    }


    public ArrayList<FMToken> readFile(String path) throws IOException {
        FMToken globalScope = new EmptyFMToken();
        FMToken currentScope = globalScope;

        try ( // Try-with-resources - reader and buffer will automatically be closed
            FileReader reader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(reader);
        ) {
            int readChar = reader.read();
            int line = 0;
            Character currentChar; 
            String charAsStr; 

            while (readChar >= 0) {
                currentChar = (Character)(char)readChar;
                charAsStr = "" + currentChar;

                if(LINEENDINGS.contains(charAsStr)) {
                    ++line;
                    
                }
                
                
                readChar = reader.read();
            }
        }

        return globalScope.children;
    }

    public ArrayList<FMToken> readFile() throws IOException {
        return readFile(this.path);
    }



    static String readFirstLineFromFile(String path) throws IOException {
	    try (FileReader fr = new FileReader(path);
	         BufferedReader br = new BufferedReader(fr)) {
	        return br.readLine();
	    }
	}	



}
