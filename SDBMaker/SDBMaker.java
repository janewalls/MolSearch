/*
	Simple GUI application for generating SQL input files from .sd (mdl files)
	David P Leader 10.09.14 
*/

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import java.sql.Connection;

import javax.swing.*;

import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class SDBMaker extends JFrame implements ActionListener
{
	private JMenuItem openFile, openFileSDF, openBatch, saveAtom, saveBond, saveCompound, saveStats, logParam, quit; 		
	private JButton upload;
	private JTextArea inputArea;
	private JScrollPane inputPane;		// so text area will scroll if required		

	
	private int width = 500;					// default app width
	private int height = 300;					// default app height
	

	AtomList aList;
	BondList bList;
	CompoundList cList;
	StatsList sList;

	public SDBMaker()
	{ 
		setSize(width, height);
		setTitle("Chemical Structure Database Maker");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setup();
	}
	
	//------------------------------ Main Method ------------------------------//
	
	public static void main (String args[])
	{
		Frame f = new SDBMaker();
		f.setBackground(Color.lightGray);
		f.setVisible(true);
	}
	
	//-------------------------------------------------------------------------//
	
	public void setup()
	{		
		// layout
		this.setLayout(new BorderLayout());
		
		
		//------------------- Menus ---------------------//
		JMenuBar menuBar = new JMenuBar();
		

		/* --- File menu --- */
		JMenu fileMenu = new JMenu("File");
		JMenu saveMenu = new JMenu("Save");
		JMenu optionsMenu = new JMenu("Options");
		menuBar.add(fileMenu);
		menuBar.add(saveMenu);	
		menuBar.add(optionsMenu);
		
		
		
		// In File Menu
		
		openFile = new JMenuItem("Open .sd File");
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));	
		openFile.addActionListener(this);
		openFile.setEnabled(true); 
		fileMenu.add(openFile);

		openFileSDF = new JMenuItem("Open .sdf File");
		openFileSDF.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));	
		openFileSDF.addActionListener(this);
		openFileSDF.setEnabled(true); 
		fileMenu.add(openFileSDF);
		
		openBatch = new JMenuItem("Open Directory of .sd Files");
		openBatch.addActionListener(this);
		openBatch.setEnabled(true); 
		fileMenu.add(openBatch);
		
		fileMenu.addSeparator();
		
		// Quit
		quit = new JMenuItem("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
		fileMenu.add(quit);
		quit.addActionListener(this);
		
		
		//In Save Menu

		saveAtom =  new JMenuItem("Save Atom.sql file"); 
		saveAtom.addActionListener(this);
		saveAtom.setEnabled(false);
		saveMenu.add(saveAtom);
		
		saveBond =  new JMenuItem("Save Bond.sql file"); 
		saveBond.addActionListener(this);
		saveBond.setEnabled(false);
		saveMenu.add(saveBond);
		
		saveCompound =  new JMenuItem("Save Compound.sql file"); 
		saveCompound.addActionListener(this);
		saveCompound.setEnabled(false);
		saveMenu.add(saveCompound);

		saveStats =  new JMenuItem("Save Parameters.sql file"); 
		saveStats.addActionListener(this);
		saveStats.setEnabled(false);
		saveMenu.add(saveStats);
		
		//In Options menu
		logParam = new JMenuItem("Change logP Paramaters");
		logParam.addActionListener(this);
		logParam.setEnabled(true);
		optionsMenu.add(logParam);
		
		// Upload to SQL button
		upload = new JButton("Upload to SQL");
		upload.addActionListener(this);
		upload.setEnabled(true);
		menuBar.add(upload);
		
		
		setJMenuBar(menuBar);
		
			
		/* --- Border Layout --- */
		
		// Text Area
		inputArea = new JTextArea(6,50);
		inputArea.setMargin(new Insets(10,10,10,10));	// White standoff for text
		inputArea.setFont(new Font("Monospaced",Font.PLAIN,12));
		inputArea.setBackground(Color.white);
		inputArea.setEditable(false);
		inputPane = new JScrollPane(inputArea);	
		this.add(inputPane,"Center");	
		
		centreInScreen();
		
	}
	
	// process menu items
	public void actionPerformed(ActionEvent event)
	{			
		
		// Open single .sd file		
		if (event.getSource() == openFile)
		{
			aList = new AtomList();
			bList = new BondList();
			cList = new CompoundList();
			sList = new StatsList();
			LoadFile ldfi = new LoadFile(this);
			Scanner s = ldfi.getScanner();
			String molecule = "";
			while(s.hasNextLine()) {
				molecule += s.nextLine() + "\n";
			}
			Scanner scanner = new Scanner(molecule);
			if(scanner != null)
			{
				// get original directory stuff
				String fileName = ldfi.getFileName();
				
				// send for parsing
				inputArea.append("parsing...\n");
				FileParser parser = new FileParser(scanner, aList, bList, cList);
				
				//send for calculations
				Calculator calcStats = new Calculator(molecule, sList);
				
				if(parser.isParsed() && calcStats != null)
				{
				// makes save buttons visible
					saveAtom.setEnabled(true);
					saveBond.setEnabled(true);
					saveCompound.setEnabled(true);
					saveStats.setEnabled(true);
					upload.setEnabled(true);
					
					inputArea.setText("\nFile: '" + fileName + "' parsed AND calculated.\n");
				}
				else if(parser.isParsed()) {
					inputArea.setText("\nFile: '" + fileName + "' parsed UNABLE TO calculate.\n");
				}
				else {
					inputArea.setText("\nFile: '" + fileName + "' UNABLE TO BE parsed OR calculated.\n");
				}
				ldfi.closeReader();		
			}	
			
		}
		
		// Open single sdf file
		else if (event.getSource() == openFileSDF) {
			
			
				// create list objects to hold tables for DB entities
				aList = new AtomList();
				bList = new BondList();
				cList = new CompoundList();
				sList = new StatsList();
				LoadFile ldfi = new LoadFile(this);
				
				// Splits into Strings containing 1 molecule
				Scanner s = ldfi.getScanner();
				ArrayList<String> molList = new ArrayList<String>();
				String output = "";
				String addLine = "";
				while(s.hasNextLine()) {
					boolean nextMol = false;
					addLine = s.nextLine();
					output += addLine + "\n";
					while(!nextMol) {
						addLine = s.nextLine();
						output += addLine + "\n";
						if(addLine.equals("$$$$")) { // if end of molecule
							nextMol = true;
							molList.add(output.trim()); //	or can use: output = output.replaceAll("[\n]+$", "");
							output = "";
						}
					}
					
				}
				
				// Go through list of Files and read into Scanner and parse
				int n = 1;
				for (int i = 0; i < molList.size(); i++) 
				{
					FileParser parser = null;
					Calculator calcStat = null;
					System.out.println(i);
					try {
					Scanner scan = new Scanner(molList.get(i));
					parser = new FileParser(scan, aList, bList, cList);
					
					// calculate 
					calcStat = new Calculator(molList.get(i), sList);
					
					if(parser != null && parser.isParsed() && calcStat != null)
					{
						inputArea.append(n++ + " " + molList.get(i).substring(0, 6) + " parsed AND calculated.\n");
					}
					else if(parser != null && parser.isParsed()) {
						inputArea.append(n++ + " " + molList.get(i).substring(0, 6) + " parsed UNABLE TO calculate.\n");
					}
					else
					{
						inputArea.append(n++ + " " + molList.get(i).substring(0, 6) + " NOT parsed.\n");						
					}
					}catch(NoSuchElementException e) {
						inputArea.append("\n" + n++ + " " + molList.get(i).substring(0, 12) + "\n UNABLE TO BE parsed.\n");
					}
				
					
					
			}
				ldfi.closeReader();	
				// makes save buttons visible	
				inputArea.append("\n  Finished parsing.\n");
				saveAtom.setEnabled(true);
				saveBond.setEnabled(true);
				saveCompound.setEnabled(true);
				saveStats.setEnabled(true);
				upload.setEnabled(true);
				

		}


		else if (event.getSource() == openBatch)
		{
			// Get list of Files in batch folder
			JFileChooser chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("Open Folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);		// Only allow choosing directories
			chooser.setAcceptAllFileFilterUsed(false); 							// Disable the "All files" filter option  (cosmetic)
			
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 	// If user has selected and hit 'Choose', 'OK' (etc)
			{ 
				File batchFolder = chooser.getSelectedFile(); 	// This is actually the chosen directory
				File[] fileList = batchFolder.listFiles();		// Array of File objects
				// create list objects to hold tables for DB entities
				aList = new AtomList();
				bList = new BondList();
				cList = new CompoundList();
				sList = new StatsList();
		
				// Go through list of Files and read into Scanner and parse
				for (int i = 0; i < fileList.length; i++) 
				{
					if (fileList[i].isFile())		// ignore directories
					{
						Calculator calcStats = null;
						FileParser parser = null;
						try
						{
							FileReader in = new FileReader(fileList[i]);
							Scanner s = new Scanner(in);
							String molecule = "";
							while(s.hasNextLine()) {
								molecule += s.nextLine() + "\n";
							}
							Scanner scanner = new Scanner(molecule);
							System.out.println(fileList[i].getName());
							parser = new FileParser(scanner, aList, bList, cList);

							//send for calculations
							calcStats = new Calculator(molecule, sList);
						}
						catch (Exception e)
						{
							JOptionPane.showMessageDialog(this, "Error loading file, " + fileList[i].getName() +" !", "Error", JOptionPane.ERROR_MESSAGE);
						}
						
						if(parser != null && parser.isParsed() && calcStats != null)
						{
							inputArea.append(fileList[i].getName() + " parsed AND calculated.\n");
						}
						else if(parser != null && parser.isParsed()) {
							inputArea.append(fileList[i].getName() + " parsed UNABLE TO calculate.\n");
						}
						else
						{
							inputArea.append(fileList[i].getName() + " NOT parsed.\n");						
						}
					} 
				}
				// makes save buttons visible
				inputArea.append("\n  Finished parsing.\n");
				saveAtom.setEnabled(true);
				saveBond.setEnabled(true);
				saveCompound.setEnabled(true);
				saveStats.setEnabled(true);
				upload.setEnabled(true);
			}

		}
	
	// 			SAVE BUTTONS
		else if (event.getSource() == saveAtom)
		{
			StringBuilder builder = new StringBuilder();
			
			for (int i=0; i<aList.getSize(); i++)
			{
				if(aList.getAtom(i).getFormatted() != null)
				{
					builder.append(aList.getAtom(i).getFormatted());
					builder.append("\n");
				}
			}			
			@SuppressWarnings("unused")
			SaveFile sf = new SaveFile(this, builder.toString());
			inputArea.append("\nAtom file saved.\n");
		}

		else if (event.getSource() == saveBond)
		{
			StringBuilder builder = new StringBuilder();
			
			for (int i=0; i<bList.getSize(); i++)
			{
				if(bList.getBond(i).getFormatted() != null)
				{
					builder.append(bList.getBond(i).getFormatted());
					builder.append("\n");
				}
			}			
			new SaveFile(this, builder.toString());
			inputArea.append("\nBond file saved.\n");
		}
		
		else if (event.getSource() == saveCompound)
		{
			StringBuilder builder = new StringBuilder();
			
			for (int i=0; i<cList.getSize(); i++)
			{
				if(cList.getCompound(i).getFormatted() != null)
				{
					builder.append(cList.getCompound(i).getFormatted());
					builder.append("\n");
				}
			}			
			new SaveFile(this, builder.toString());
			inputArea.append("\nCompound file saved.\n");
		}
		
		else if (event.getSource() == saveStats)
		{
			StringBuilder builder = new StringBuilder();
			
			for (int i=0; i<sList.getSize(); i++)
			{
				if(sList.getStat(i).getFormatted() != null)
				{
					builder.append(sList.getStat(i).getFormatted());
					builder.append("\n");
				}
			}			
			new SaveFile(this, builder.toString());
			inputArea.append("\nParameters file saved.\n");
		}
		
		
		// Button to change method paramaters 
		else if (event.getSource() == logParam) {
		
			// with more time would create pop-up gui
		}
	
		else if (event.getSource() == upload) {
			
			SQLConnectFrame sqlFrame = new SQLConnectFrame(this);
			sqlFrame.setVisible(true);
			
			
			// ------------------- VERSION WITH DATABASE HARD CODED IN - if above GUI doesn't connect ---------------------------
			
		/*	
			String build = "";
			
			try {
			
				// --- Hard coded if possible change ---
				Connect cnt = new Connect("jdbc:mysql://localhost/StructureDB", "root", "Student1!");
				Connection conn = cnt.getConnection();
				Statement statement = conn.createStatement();
		
		// ----- For Compound data ------
			if(conn != null) {	
				for (int i=0; i<cList.getSize(); i++) {
					if(cList.getCompound(i).getFormatted() != null)
					{
						build = cList.getCompound(i).sqlUploadFormat();
						statement.addBatch(build);
					
					}
				}
				statement.executeBatch();
				statement.close();
				inputArea.append("\nCompund data in server.\n");
	
		// ----- For Atom data ------
				statement = conn.createStatement();
			for (int i=0; i<aList.getSize(); i++)
			{
				if(aList.getAtom(i).getFormatted() != null)
				{
					build = aList.getAtom(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);
					
				}
			}
			statement.executeBatch();
			statement.close();
			inputArea.append("\nAtom data in server.\n");
		
		// ----- For Bond data ------
			statement = conn.createStatement();
			
			for (int i=0; i<bList.getSize(); i++)
			{
				if(bList.getBond(i).getFormatted() != null)
				{
					build = bList.getBond(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);
					
				}
			}
			
			statement.executeBatch();
			statement.close();
			inputArea.append("\nBond data in server.\n");
		
		// ----- For stats data ------
			statement = conn.createStatement();
			
			for (int i=0; i<sList.getSize(); i++)
			{
				if(sList.getStat(i).getFormatted() != null)
				{
					build = sList.getStat(i).sqlUploadFormat() + "\n";
					statement.addBatch(build);

				}
			}
			statement.executeBatch();
			statement.close();
			inputArea.append("\nParameter data in server.\n");
			
			
		//	conn = sqlFrame.getConnection();
			}else {
				System.out.print("issue connecting");
			}
			}catch(SQLException e) {
				e.printStackTrace();
				System.out.println("Unable to add to SQL");
			}
			---------------------------------------------------------------------------*/
		}

		else if (event.getSource() == quit)
		{
			shutDown();
		}			 		
	}
		
	
	// shuts down the app
	public void shutDown()
	{
		setVisible(false);
		dispose();
		System.exit(0);
	}
	
	// utility alert method	
	public void showAlert(String titleString, String alertString)
	{				
		JOptionPane.showMessageDialog(this, titleString, 
			alertString, JOptionPane.ERROR_MESSAGE);
	}
	
	// centres app in user's screen
	void centreInScreen()
	{
		Dimension appDim = this.getSize();
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screenDim.width - appDim.width)/2;
		int y = (screenDim.height - appDim.height)/2;
		this.setLocation(x, y);
	}
	
	// Allows to append input area in other class
	public void appendInputArea(String input) {
		inputArea.append(input);
	}

	
// Getters and Setters
	public AtomList getaList() {
		return aList;
	}

	public BondList getbList() {
		return bList;
	}

	public CompoundList getcList() {
		return cList;
	}

	public StatsList getsList() {
		return sList;
	}

	
}
