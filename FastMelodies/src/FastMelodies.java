import java.util.ArrayList; 

public class FastMelodies {    
    public static void main(String[] args) throws Exception {
        // First, Try to open a txt file.
        if (args.length == 0) {
            // TODO: raise error about usage
        }
        
        // First off, read the passed file.
        TextParser parser = new TextParser(args[0]);
        EmptyFMToken tokens = parser.readFile();

        // Then, process all the notes.
        ArrayList<Note> notes = tokens.processNotes();

        MusixTexWriter texWriter = new MusixTexWriter();
        texWriter.writeToTex(notes);

        
    }
}

