import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
// import java.util.ArrayList; 
// import java.util.Set;

public class TextParser {

    String path;

    // Constants defining the language
    static final char OPENSTRUCTURE = '{';
    static final char CLOSESTRUCTURE = '}';

    static final char COMMENT = '/'; 

    static final String CHORDUNION = "+|";
    static final String ASSIGNMENT = "="; //TODO

    static final String LINEENDINGS = "\r\n";
    static final String SPACING = " \t";
    static final String EXPRENDINGS = SPACING + LINEENDINGS + CHORDUNION
                         + OPENSTRUCTURE + CLOSESTRUCTURE + COMMENT + ASSIGNMENT;

    static final String NOTES = "ABCDEFG";
    static final String NOTELENGTHS = "whqes";
    static final String NOTEFLSH = "#bn";
    static final String OCTAVES = "12345";

    public TextParser(String path) {
        this.path = path;
    }

    public FastMelodyPiece readFile(String path) throws IOException, BadSyntaxException {
        FastMelodyPiece globalScope = new FastMelodyPiece();
        FMToken currentScope = globalScope;
        try ( // Try-with-resources - reader and buffer will automatically be closed
            FileReader reader = new FileReader(path);
            BufferedReader buffer = new BufferedReader(reader);
        ) {
            int line = 1;
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
                            ((NoteSet)currentScope).children.add(currentNote);
                        } else {
                            throw new BadSyntaxException("Note " + currentStr + " at line " + line + " is defined outside of a set of notes.");
                        }
                       
                    } 
                    // else if (currentStr.length() >= 2) {
                    //     //If this isn't a note, but still has chracters of substance, save it as the last var name.
                    //     lastLabel = currentStr.substring(0, currentStr.length() - 1);
                    // }

                    if (OPENSTRUCTURE == currentChar) {
                        currentScope = currentScope.addChildNoteset(line);
                    } 
                    else if (CLOSESTRUCTURE == currentChar){
                        if(currentScope == globalScope) {
                           throw new BadSyntaxException("Unexpected " + CLOSESTRUCTURE + " at line " + line);
                        }
                        currentScope = currentScope.parent;
                    } 
                    else if ( COMMENT == currentChar) {
                        readChar = buffer.read();
                        currentChar = (char)readChar;
                        if (currentChar == COMMENT) {
                            //If you have two '/' in a row, then skip to the end of the line
                            while(readChar != 0 && !LINEENDINGS.contains(oneCharAsStr)) {
                                readChar = buffer.read();
                                currentChar = (char)readChar;
                                oneCharAsStr = "" + currentChar;
                            }
                            //Then incrememnt the line, as usual.
                            ++line;
                            
                        } else {
                            throw new BadSyntaxException("Your inputted music file has an unexpected " + COMMENT + " at line " + line);
                        }
                    } 
                    else if ( CHORDUNION.contains(oneCharAsStr)) {
                        if (currentScope instanceof NoteSet 
                            && currentScope.children.size() > 0
                            && currentScope.children.get(currentScope.children.size()-1) instanceof Note) {
                            
                            ((Note)currentScope.children.get(currentScope.children.size()-1)).chordWithNext = true;
                        } else {
                            throw new BadSyntaxException("Your inputted music file has an unexpected " + currentChar + " at line " + line);
                        }
                    }
                    
                    //Then zero out the currentStr.
                    currentStr = "";
                } 
                
                readChar = buffer.read(); // Then read the next char
            }
        }

        if(currentScope != globalScope) {
            throw new BadSyntaxException("Your inputted music file defines a structure on line " + currentScope.definedLine + ", but it has no matching }.");
        }

        globalScope.printTarget = (NoteSet)globalScope.children.get(globalScope.children.size()-1);

        return globalScope;
    }

    public FastMelodyPiece readFile() throws IOException, BadSyntaxException {
        return readFile(this.path);
    }

    static String readFirstLineFromFile(String path) throws IOException {
	    try (FileReader fr = new FileReader(path);
	         BufferedReader br = new BufferedReader(fr)) {
	        return br.readLine();
	    }
	}

    char currentNoteTime;


    /* 
     * Returns a Note object if the given string is a valid note.
     * Returns null otherwise.
     */
    char lastLength = 'q';
    int lastOctave = 4;
    private Note strToNote(String input) {
        if (input.length() < 2) return null;
        if (! (NOTES.contains("" + input.charAt(0))) ) return null;

        Note note = new Note(input.charAt(0));
        note.length = lastLength;
        note.octave = lastOctave;

        //Traverse every char except the first one, and the last one (terminator)
        for(int i = 1; i < input.length() - 1; ++i ) {
            if (NOTEFLSH.contains("" + input.charAt(i))) {
                note.sharpOrFlat = input.charAt(i);
            } else if (NOTELENGTHS.contains("" + input.charAt(i))) {
                note.length = input.charAt(i);
            } else if (OCTAVES.contains("" + input.charAt(i))) {
                note.octave = input.charAt(i) - 48;
                // To convert from char (0-255) to int (0-9), subtract 48
            } else {
                return null;
            }
        }

        // If this is a valid note, set the last used octave correctly before
        // returning the note.
        lastLength = note.length;
        lastOctave = note.octave;
        return note;
    }
}
