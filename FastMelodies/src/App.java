public class App {

    class Note {
        char label;
        int octave;
        char sharpOrFlat;

        int length;
        int startTime;

        public String toString() {
            return label + String.valueOf(octave) + length; 
        }
    }

    class NoteSet {
        Note[] notes;
        Modifier[] modifiers;
    }

    class Modifier {
        int stepChange;
        float lengthChange;

    }

   
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
    }
}
