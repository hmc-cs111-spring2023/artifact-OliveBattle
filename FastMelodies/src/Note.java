public class Note {
	char label;
	int octave;
	char sharpOrFlat = ' ';

	char length;
	Boolean chordWithNext = false;

	public String toString() {
		return label + String.valueOf(octave) + length; 
	}

	public Note(char l) {
		this.label = l;
	}
}
