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

	public FMToken addChildNoteset(int line) {
		NoteSet child = new NoteSet();
		child.setLine(line);
		this.children.add(child);
		int childIndex = this.children.size() - 1;
		this.children.get(childIndex).setParent(this);
		return child;
	} 
}

