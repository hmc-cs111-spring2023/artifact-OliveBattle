import java.util.ArrayList; 

public class FastMelodyPiece extends FMToken {
	NoteSet printTarget;

	public FastMelodyPiece() {
		super();
	}

	public ArrayList<Note> getNotes() throws BadSyntaxException{
		if(printTarget == null) 
			throw new BadSyntaxException("File could be parsed, but no print target could be found.");
		return getNotes(printTarget);
	}

	private ArrayList<Note> getNotes(NoteSet t) {
		ArrayList<Note> notes = new ArrayList<Note>();

		for(int i = 0; i < t.children.size(); i++) {
			FMToken child = t.children.get(i);

			if(child instanceof Note) notes.add((Note)child);
			else if (child instanceof NoteSet) {
				// Process modifiers here
				notes.addAll(getNotes((NoteSet) child));
			}
		}
		return notes;
	}
}