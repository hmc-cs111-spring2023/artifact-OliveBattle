import java.util.ArrayList; 

public class NoteSet extends FMToken {
	ArrayList<Modifier> modifiers; //TO USE

	public NoteSet() {
		super();
		this.modifiers = new ArrayList<Modifier>();
	}

	public NoteSet(EmptyFMToken oldtoken) {
		this();
		
		this.parent = oldtoken.parent;
		this.children = oldtoken.children;
		this.definedLine = oldtoken.definedLine;

		int childIndex = this.parent.children.indexOf(oldtoken);
		this.parent.children.set(childIndex, this);
	}

}