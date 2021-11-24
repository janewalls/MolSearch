/*
LoadFile
Class to handle file opening based on Nutshell Examples 8-3
Scanner version

*/

import java.awt.*;
import java.io.*;
import java.util.Scanner;

import javax.swing.JOptionPane;

class LoadFile
{	
	String filename = null;
	String directory;
	
	FileReader in = null;
	Scanner scanner = null;	
	
	Frame frame;
	
	LoadFile()
	{
	}
	
	// constructor for standard dialogue file opening	
	LoadFile(Frame frame)
	{
		this.frame = frame;
		FileDialog fd = new FileDialog(frame, "Open File", FileDialog.LOAD);
		directory = System.getProperty("user.dir");
		fd.setDirectory(directory);
		fd.setVisible(true);
		directory = fd.getDirectory();
		filename = fd.getFile();
		fd.dispose();
		makeScanner(directory, filename); 	// Send File details to method to make Scanner 
	}
	
	// makes FileReader and then Scanner from file 
	private void makeScanner(String directory, String filename)
	{
		if (filename == null || (filename.length() == 0))
		{return;}

		try
		{
			File fi = new File(directory, filename);
			in = new FileReader(fi);
			scanner = new Scanner(in);
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(frame, "Error loading file!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// Closes FileReader (should be called after the Scanner has been parsed)
    public void closeReader()
    {
    	try
    	{
	    	if (in != null) in.close();
	    }
	    catch (IOException x)
	    {
			JOptionPane.showMessageDialog(frame, "Alert", 
				"Error closing file!", JOptionPane.ERROR_MESSAGE);	
	    }
    }

	// Allows GUI class access to Scanner created from file
	public Scanner getScanner()
	{
		return scanner;
	}
	
	public String getFileName()
	{
		return filename;	
	}
	
	public String getDirectory()
	{
		return directory;	
	}		
	
}