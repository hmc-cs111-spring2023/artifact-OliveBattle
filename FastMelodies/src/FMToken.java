import java.util.ArrayList; 

abstract class FMToken {
	FMToken parent;
	ArrayList<FMToken> children;

	public FMToken() {
		this.parent = null;
	}

	public void setParent(FMToken parent) {
		this.parent = parent;
	} 

	public void addChild(FMToken child) {
		this.children.add(child);
	} 
}

class EmptyFMToken extends FMToken {
	
}

class NoteSet extends FMToken {
	Note[] notes;
	Modifier[] modifiers;

}

class Modifier extends FMToken {
	int stepChange;
	float lengthChange;

}

class Function extends FMToken { // "Print to pdf" command ? 

}