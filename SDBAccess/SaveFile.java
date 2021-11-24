/*
SaveFile
Class to take String and save to a text file.
N.B. In this implementation String must end with "\n" 
*/

import java.awt.*;
import java.io.*;
import javax.swing.JOptionPane;

public class SaveFile
{	
	String fileString;				// Text string to be written to file
	String filename = null;			// User-selected filename
	String directory;				// User-selected directory
	File fi;						// File object to be created from above
	Frame frame;
	
	public SaveFile(String fileString)
	{
		this.fileString = fileString; 
	}
	
	public SaveFile (Frame frame, String fileString)
	{
		this.frame = frame;
		this.fileString = fileString;
		
		// open file dialogue box for user to specify file name and dir
		FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE);
		directory = System.getProperty("user.dir");
		fd.setDirectory(directory);
		fd.setVisible(true);
		directory = fd.getDirectory();
		filename = fd.getFile();
		
		// call method to write 
		stringToFile(directory, filename);
		
		// close dialogue box
		fd.dispose();
	}	
	
	//Takes text string specified in the constructor and writes to file chosen by user
	public void stringToFile(String directory, String filename)
	{
		// Prevent error message to System.out if user cancels
		if (filename == null || (filename.length() == 0))
		{return;}		
		try
		{
			fi = new File(directory, filename);		
			FileWriter fw = new FileWriter(fi);
	        PrintWriter pw = new PrintWriter(fw);	
			pw.print(fileString);					// print assumes "\n" ends fileString
            pw.close();
		}
		catch (IOException ioe)
		{
			JOptionPane.showMessageDialog(frame, "Error saving file!", 
								"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
}