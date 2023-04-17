public class Note {
	char label;
	int octave;
	char sharpOrFlat;

	int length;
	int startTime;

	public String toString() {
		return label + String.valueOf(octave) + length; 
	}

	public Note(char l) {
		this.label = l;
	}

	public Note(char l, int o, char s) {
		this.label = l;
		this.octave = o;
		this.sharpOrFlat = s;
	}
}
