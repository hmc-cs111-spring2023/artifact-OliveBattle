import java.io.File; 
import java.io.IOException; 
import java.io.FileWriter;
import java.util.ArrayList; 

public class MusixTexWriter {
	File texFile;
	FileWriter writer;

	public MusixTexWriter(String filename) {
		try {
			texFile = new File(filename);
			texFile.createNewFile();
			writer = new FileWriter(texFile);
			// TODO: handle existing files
		} catch (IOException e) {
			// TODO: handle errors
		}

	}
	public MusixTexWriter() {
		this("fileToOutput.tex");
	}

	public File getFile() {
		return this.texFile;
	}

	public void writeToTex(ArrayList<Note> notes) {
		String stringToWrite = (
			"\\begin{music}\n"
			+ "  \\parindent10mm\n"
			+ "  \\setname1{Piano}\n"
			+ "  \\setstaffs1{2}\n"
			+ "  \\generalmeter{\\meterfrac44}\n"
			+ "  \\startextract"
		);

		stringToWrite += "    \\Notes";

		for(int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			stringToWrite += "\\qu " + note.label;
			
		}
		stringToWrite += "\\en\n";

		stringToWrite += "  \\zendextract\n";
		stringToWrite += "  \\end{music}\n";
		
		
		
		

		try {
			System.out.println(stringToWrite);
			writer.write(stringToWrite);
			writer.close();
		} catch (IOException e) {
			// TODO: handle errors
			System.out.println("Could not write: " + e.getMessage());
		}
	}
}
