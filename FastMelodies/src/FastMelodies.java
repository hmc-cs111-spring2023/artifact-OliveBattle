import java.util.ArrayList; 

public class FastMelodies {    
    public static void main(String[] args) throws Exception {
        // First, Try to open a txt file.
        if (args.length == 0) {
            throw new Exception("Usage: java FastMelodies (filepath)");
        }

        //System.out.println("Loading file at position " + args[0]);
        
        // First off, read the passed file.
        TextParser parser = new TextParser(args[0]);
        //System.out.println("Created Parser.");
        

        // System.out.println("Read file - token has " + tokens.children.size() + " children");

        // Then, process all the notes.
        ArrayList<Note> notes = parser.readFile().getNotes();

        // System.out.println("Total output: " + notes.size() + " notes.");

        MusixTexWriter texWriter = new MusixTexWriter();
        
        // System.out.println("Created new writer.");

        texWriter.writeToTex(notes);
        
        // System.out.println("Wrote to file.");

        
    }
}

