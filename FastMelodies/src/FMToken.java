import java.util.ArrayList; 

abstract class FMToken {
	FMToken parent;
	ArrayList<FMToken> children;
	int definedLine = -1;


	public FMToken(FMToken parent, int definedLine) {
		this.parent = parent;
		this.definedLine = definedLine;
		this.children = new ArrayList<FMToken>();
	}

	public FMToken() {
		this((FMToken)null, -1);
		
	}

	public FMToken(FMToken parent) {
		this(parent, -1);
	}
	
	public FMToken(int definedLine) {
		this((FMToken)null, definedLine);
	}

	public void setParent(FMToken parent) {
		this.parent = parent;
	} 

	public void setLine(int line) {
		this.definedLine = line;
	} 

	public FMToken addChild(int line) {
		EmptyFMToken child = new EmptyFMToken();
		child.setLine(line);
		this.children.add(child);
		int childIndex = this.children.size() - 1;
		this.children.get(childIndex).setParent(this);
		return child;
	} 

	public ArrayList<Note> processNotes() {
		// TODO: edit notes, taking modifiers and stuff into account
		ArrayList<Note> notes = new ArrayList<Note>();
		for(int i = 0; i < this.children.size(); i++) {
			FMToken child = this.children.get(i);
			if(child instanceof NoteSet) {
				notes.addAll(((NoteSet)child).notes);
			}
		}

		return notes;
	}
}

class Modifier extends FMToken {
	int stepChange;
	float lengthChange;

}

class Function extends FMToken { // "Print to pdf" command ? 

}