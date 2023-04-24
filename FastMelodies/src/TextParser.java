import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
// import java.util.ArrayList; 
// import java.util.Set;


public class TextParser {

    String path;

    // Constants defining the language
    static final char OPENEXPR = '{';
    static final char CLOSEEXPR = '{';

    static final String COMMENT = "//"; //TODO

    static final String CHORDUNION = "|"; //TODO
    static final String ASSIGNMENT = "="; //TODO

    static final String LINEENDINGS = "\r\n"; //TODO: FIX
    static final String EXPRENDINGS = " " + LINEENDINGS + CHORDUNION + OPENEXPR + CLOSEEXPR;

    static final String NOTES = "ABCDEFG";
    static final String NOTELENGTHS = "whqes";
    static final String NOTEFLSH = "#bn";
    static final String OCTAVES = "12345678";

    public TextParser(String path) {
        this.path = path;
    }

    public EmptyFMToken readFile(String path) throws IOException {
        EmptyFMToken globalScope = new EmptyFMToken();
        FMToken currentScope = globalScope;

        try ( // Try-with-resources - reader and buffer will automatically be closed
            FileReader reader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(reader);
        ) {
            int line = 0;
            Character currentChar; 
            String oneCharAsStr; 
            String currentStr = ""; 
            Note currentNote; 


            int readChar = buffer.read();

            while (readChar >= 0) {

                //Read one character at a time.
                currentChar = (char)readChar;
                oneCharAsStr = "" + currentChar;
                currentStr += currentChar;


                if ( EXPRENDINGS.contains(oneCharAsStr)) {
                    //If this is the end of an expression, process it.

                    //If the line ended, increment the counter by one.
                    if (LINEENDINGS.contains(oneCharAsStr)) {
                        ++line;
                    }

                    currentNote = strToNote(currentStr);
                    if (currentNote != null) {
                        if (currentScope instanceof NoteSet) {
                            ((NoteSet)currentScope).notes.add(currentNote);
                        } else if (currentScope instanceof EmptyFMToken) {
                            currentScope = new NoteSet((EmptyFMToken)currentScope);
                            ((NoteSet)currentScope).notes.add(currentNote);
                        } else {
                            // TODO: NOTE OUT OF PLACE ERROR
                        }
                       
                    }
                    else if (CLOSEEXPR == currentChar){
                        if(currentScope == globalScope) {
                            // TODO: EXTRA CLOSEPAREN ERROR
                        }
                        currentScope = currentScope.parent;
                    }
                    else if (OPENEXPR == currentChar) {
                        currentScope = currentScope.addChild(line);
                    }
                    

                    else {
                        //TODO: UNKNOWN TOKEN ERROR
                    }

                    //Then zero out the currentStr.
                    currentStr = "";
                } 
                
                readChar = buffer.read(); // Then read the next char
            }
        }

        return globalScope;
    }

    public EmptyFMToken readFile() throws IOException {
        return readFile(this.path);
    }



    static String readFirstLineFromFile(String path) throws IOException {
	    try (FileReader fr = new FileReader(path);
	         BufferedReader br = new BufferedReader(fr)) {
	        return br.readLine();
	    }
	}

    char currentNoteTime;

    /* Returns a Note object if the given string is a valid note.
     * Returns null otherwise.
     */
    private Note strToNote(String input) {
        if (input.length() == 0) return null;
        if (!NOTES.contains("" + input.charAt(0))) return null;

        Note note = new Note(input.charAt(0), 3);

        for(int i = 1; i < input.length(); ++i ) {
            if (NOTEFLSH.contains("" + input.charAt(i))) {
                note.setSharpFlat(input.charAt(i));
            } else if (NOTELENGTHS.contains("" + input.charAt(i))) {
                note.setLength(input.charAt(i));
            } else if (OCTAVES.contains("" + input.charAt(i))) {
                note.setOctave((int)input.charAt(i));
            } else {
                return null;
            }
        }
        return note;
    }

}
