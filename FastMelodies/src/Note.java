public class Note {
	char label;
	int octave;
	char sharpOrFlat;

	char length;
	int startTime;

	public String toString() {
		return label + String.valueOf(octave) + length; 
	}

	public Note(char l) {
		this.label = l;
	}

	public Note(char l, int o) {
		this.label = l;
		this.octave = o;
	}

	public Note(char l, int o, char s) {
		this.label = l;
		this.octave = o;
		this.sharpOrFlat = s;
	}

	public void setOctave(int o) {
		this.octave = o;
	}

	public void setLength(char c) {
		this.length = c;
	}

	public void setSharpFlat(char s) {
		this.sharpOrFlat = s;
	}
}
