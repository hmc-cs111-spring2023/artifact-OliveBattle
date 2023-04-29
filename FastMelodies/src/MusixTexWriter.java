import java.io.File; 
import java.io.IOException; 
import java.io.FileWriter;
import java.util.ArrayList; 


public class MusixTexWriter {
	File texFile;
	FileWriter writer;
	

	static final Double DOFFSET = 0.000001;

	Double lenPerBar = 4.0;
	Double lenThisBar = 0.0;
	Double lenSinceStaffSwitch = 0.0;
	Boolean onTreble = true;
	Boolean wrappingUpMeasure = false;
	char lastUD = 'u';

	public MusixTexWriter(String filename) throws IOException {
		texFile = new File(filename);
		texFile.createNewFile();
		writer = new FileWriter(texFile);

	}
	public MusixTexWriter() throws IOException{
		this("fileToOutput.tex");
	}

	public File getFile() {
		return this.texFile;
	}

	public void writeToTex(ArrayList<Note> notes) throws IOException {
		lenThisBar = 0.0;
		lenSinceStaffSwitch = 0.0;

		String stringToWrite = (
			"\\input musixtex.tex\n"
			+ "\\parindent10mm\n"
			+ "\\setname1{Piano}\n"
			+ "\\setstaffs1{2}\n"
			+ "\\setclef{1}{60}\n" // Treble; Bass
			+ "\\generalmeter{\\meterfrac44}\n"
			+ "\\startpiece\n"
		);

		stringToWrite += "\\Notes";
		stringToWrite += "\\nextstaff";

		for(int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);

			//If this note is too big to fit in this bar, add a new one.
			lenThisBar += noteLengthNum(note);
			if( lenThisBar > lenPerBar + DOFFSET) {
				//add spacing to the other staff.
				stringToWrite += otherStaffsSpacing();
				
				lenThisBar = noteLengthNum(note);
				stringToWrite+="\\en\n";
				stringToWrite+="\\bar\n";
				stringToWrite+="\\Notes";
				if(onTreble) stringToWrite += "\\nextstaff";
			}
			lenSinceStaffSwitch += noteLengthNum(note);

			//Maybe change the staff. 
			stringToWrite += changeStaff(note);

			//Print the note itself;
			stringToWrite += '\\';
			stringToWrite += getNoteType(note)+ " ";
			stringToWrite += '{' + getNotePitch(note) + '}';
			stringToWrite += getNoteGap(note);
		}
		stringToWrite += otherStaffsSpacing();
		stringToWrite += "\\en\n";

		stringToWrite += "\\endpiece\n";
		stringToWrite += "\\end\n";

		writer.write(stringToWrite);
		writer.close();
	}

	private String changeStaff(Note n) {
		if(onTreble && n.octave <= 3 ) {
			onTreble = false;
			return "\\prevstaff" + spacingSinceStaffSwitch();
		}
		else if (!onTreble && ( n.octave > 4 || n.octave == 4 && 
				 (n.label == 'F' || n.label == 'G'))) {
			onTreble = true;
			
			return "\\nextstaff" + spacingSinceStaffSwitch();
		}
		return "";
	}

	private String otherStaffsSpacing() {
		if (lenSinceStaffSwitch <= 0.0 + DOFFSET) return "";

		wrappingUpMeasure = true;
		if(onTreble) {
			return "\\prevstaff" + spacingSinceStaffSwitch();
		} else {
			return "\\nextstaff" + spacingSinceStaffSwitch();
		}
	}

	private String spacingSinceStaffSwitch() {
		String retStr = generateSpacing(lenSinceStaffSwitch);
		lenSinceStaffSwitch = 0.0;
		return retStr;
	}

	private String generateSpacing(double s) {
		// Exit early if you need no spacing.
		if (s+DOFFSET < 0.25) return "";

		String retStr = "";

		if(wrappingUpMeasure) {
			wrappingUpMeasure = false;
			// Build forward from small pieces to complete halves
			if ((s+DOFFSET) % 0.5 >= 0.25) { s -= 0.25; retStr += "\\qsk\\qs";}
			if ((s+DOFFSET) % 1.0 >= 0.5) { s -= 0.5; retStr += "\\sk\\ds";}
			if ((s+DOFFSET) % 2.0 >= 1.0) { s -= 1.0; retStr += "\\sk\\sk\\qp";}
			if ((s+DOFFSET) % lenPerBar >= 2.0) { s -= 2.0; retStr += "\\sk\\sk\\sk\\hp";}

		}

		// Then ramp back to small pieces
		if ( s > lenPerBar - DOFFSET) { s -= lenPerBar; retStr += "pause";}
		if ( s > 2.0 - DOFFSET) { s -= 2.0; retStr += "\\sk\\hp";}
		if ( s > 1.0 - DOFFSET) { s -= 1.0; retStr += "\\qp";}
		if ( s > 0.5 - DOFFSET) { s -= 0.5; retStr += "\\ds";}
		if ( s > 0.25 - DOFFSET) { s -= 0.25; retStr += "\\qs";}

		return retStr;
	}

	private String getNoteType(Note n) {
		if(n.chordWithNext && (n.length == 'h' || n.length == 'w')) return "zh";
		if(n.chordWithNext ) return "zq";

		if(n.length == 'w') return "wh";
		if(n.length == 's') return "cc" + getNoteUpDown(n);
		if(n.length == 'e') return "c" + getNoteUpDown(n);

		return "" + n.length + getNoteUpDown(n);
	}

	private Double noteLengthNum(Note n) {
		if(n.chordWithNext) return 0.0;
		switch(n.length) {
			case 'w': return lenPerBar;
			case 'h': return 2.0;
			case 'q': return 1.0;
			case 'e': return 0.5;
			case 's': return 0.25;
			default: return 0.0;
		}

	}

	private char getNoteUpDown(Note n) {
		if (n.octave >= 4 ) { //If C5 or higher, down
			lastUD = 'l';
		} else if (n.octave == 4 && n.label == 'B' ) { //If B4, special
			//Don't change lastUD
		} else { //If A4 or lower, up
			lastUD = 'u';
		}

		return lastUD;
	}

	private String getNoteGap(Note n) {
		if(n.chordWithNext) return "";
		switch(n.length) {
			case 'h': return "\\sk";
			case 'e': return "\\bsk";
			default: return "";
		}

	}

	private String getNotePitch(Note n) {
		String retStr = "";

		//Add an accidental before the note label
		if(n.sharpOrFlat == '#' ) retStr += "^";
		else if(n.sharpOrFlat == 'b' ) retStr += "_";
		else if(n.sharpOrFlat == 'n' ) retStr += "=";

		//Then include the main note.
		if(n.octave > 3) { // Treble notes are lowercase
			retStr += (char)((int)'a' + wholeStepsAboveA4(n));
		} else { // Bass notes are uppercase
			retStr += (char)((int)'A' + wholeStepsAboveA2(n));
		}
		

		return retStr;
	}

	/* Convert a letter to the number of steps above A2 */
	private int wholeStepsAboveA2(Note n) {
		switch(n.label) {
			case 'A': return 0 + 7 * (n.octave-2);
			case 'B': return 1 + 7 * (n.octave-2);
			case 'C': return 2 + 7 * (n.octave-2);
			case 'D': return 3 + 7 * (n.octave-2);
			case 'E': return 4 + 7 * (n.octave-2);
			case 'F': return 5 + 7 * (n.octave-2);
			case 'G': return 6 + 7 * (n.octave-2);
			default: return 0;
		}
	}

	private int wholeStepsAboveA4(Note n) { return wholeStepsAboveA2(n) - 14; }


}
