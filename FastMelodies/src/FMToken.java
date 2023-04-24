import java.util.ArrayList; 

abstract class FMToken {
	FMToken parent;
	ArrayList<FMToken> children;
	int definedLine = -1;

	public FMToken() {
		this.parent = null;
	}

	public FMToken(FMToken parent) {
		this.parent = parent;
	}

	public FMToken(FMToken parent, int definedLine) {
		this.parent = parent;
		this.definedLine = definedLine;
	}

	public FMToken(int definedLine) {
		this.parent = null;
		this.definedLine = definedLine;
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

class EmptyFMToken extends FMToken {
	
}

class NoteSet extends FMToken {
	ArrayList<Note> notes;
	ArrayList<Modifier> modifiers;

	public NoteSet(EmptyFMToken oldtoken) {
		this.parent = oldtoken.parent;
		this.children = oldtoken.children;
		this.definedLine = oldtoken.definedLine;

		int childIndex = this.parent.children.indexOf(oldtoken);
		this.parent.children.set(childIndex, this);
	}

}

class Modifier extends FMToken {
	int stepChange;
	float lengthChange;

}

class Function extends FMToken { // "Print to pdf" command ? 

}