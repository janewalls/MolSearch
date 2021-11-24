/*
	Simple GUI-based app to illustrate direct JDBC connection using StructureDB
	David P Leader 
	last update: 11.10.2016 (Generic JComboBox version for Java 7)
	Adjusted 2598002w - 04.21
*/	

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class SDBAccess extends JFrame implements ActionListener
{	
	// GUI components that need to be instance variables 
	private JTextArea feedbackBS, feedbackl, feedbackLL, feedbackB, feedbackA, inputLogPl, inputLogPLL, inputHBAl, inputHBDl, inputHBALL, inputHBDLL, inputMassLL, inputMassl, inputTPSA, inputRingLL, inputRotatableLL, inputLogPB, inputHBAB, inputHBDB, inputMassB, inputRotatableB, inputAromatic, inputLogDUpper, inputLogDLower;	
	private JButton bSubmitBS, bSubmitl, bSubmitLL, bSubmitB, submitA, saveBS, savel, saveLL, saveB, saveA;
	private JComboBox<Integer> orderCombo;
	
	private JTabbedPane tabPane;
	private JPanel bondSearch, lapinski, leadLike, bioavailability, browseAll, pCentreBS, pCentrel, pCentreLL, pCentreB, pCentreA;
	private JLabel logP, hba, hbd, mass, ringCount, rotatableBond, aromaticRing, logDLower, logDUpper, tpsa;
	
	private Bioavailability bio;
	private BondSearch bs;
	private Lapinskis lap;
	private LeadLike ll;
	private BrowseAll all;
	
	private final int ORDER_MAX = 4;		// Limit of orders for populating JComboBox	
	private int orderChoice;				// Value of bond order (for SQL query)
	
	Listener listener = new Listener();		// For JComboBox
	
	public SDBAccess()
	{		
	//	this.setLayout(new BorderLayout(10,10));
		setTitle("Chemical Structure Database Query for Bioactives");
		setSize(700,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setup();
	}
	
	
	// main method is placed within the GUI class to avoid surplus starter class
	public static void main(String args[])
	{
		JFrame jf = new SDBAccess();
		jf.pack();
		jf.setVisible(true);
	}
	
	
	
	// set up GUI and initialize
	void setup()
	{
		tabPane = new JTabbedPane();
		
		
		/*-------------- Lapinski tab ---------------*/	
		
		lapinski = new JPanel();
		lapinski.setLayout(new BorderLayout());
		
		/*--- West panel for paramaters ---*/
		JPanel pWestl = new JPanel(new GridLayout(0,2));
		pWestl.setBorder(new TitledBorder("Set Parameters:"));
		lapinski.add(pWestl, BorderLayout.WEST);
		
		logP = new JLabel("LogP\t<= ");
		hba = new JLabel("H Bond Acceptors\t<= ");
		hbd = new JLabel("H Bond Donors\t<= ");
		mass = new JLabel("Molecular Mass\t<= ");
		
		inputLogPl = new JTextArea("5",0,3);
		inputHBAl = new JTextArea("10", 0,3);
		inputHBDl = new JTextArea("5", 0,3);
		inputMassl = new JTextArea("500", 0,3);
		
		// adds labels and input areas to panel
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(logP);
		pWestl.add(inputLogPl);
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(hba);
		pWestl.add(inputHBAl);
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(hbd);
		pWestl.add(inputHBDl);
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(mass);
		pWestl.add(inputMassl);
		pWestl.add(Box.createVerticalStrut(30));
		pWestl.add(Box.createVerticalStrut(30));

		/*--- Centre panel for results---*/
		pCentrel = new JPanel();
		lapinski.add(pCentrel, BorderLayout.CENTER);
		pCentrel.setBackground(Color.white);
		pCentrel.setPreferredSize(new Dimension(600,500));
		
		/*--- South panel for feedback---*/
		JPanel pSouthl = new JPanel();
		lapinski.add(pSouthl, BorderLayout.SOUTH);
		feedbackl = new JTextArea(0, 30);
		feedbackl.setMargin(new Insets(10,10,10,10));	// Aesthetic stand-off for text
		pSouthl.add(feedbackl);
		
		/*--- East panel for Buttons---*/
		JPanel pEastl = new JPanel(new GridLayout(0,1));
		lapinski.add(pEastl, BorderLayout.EAST);
			
		// Submit button
		bSubmitl = new JButton("Submit");
		bSubmitl.addActionListener(this);
		pEastl.add(bSubmitl);
		
		// save button
		savel = new JButton("Save Results");
		savel.addActionListener(this);
		savel.setVisible(false);
		pEastl.add(savel);
		
		tabPane.addTab("Lipinski's Rules", lapinski);
		
		
	/*-------------- LeadLike tab ---------------*/	
		
		leadLike = new JPanel();
		leadLike.setLayout(new BorderLayout());
		
		/*--- West panel for paramaters ---*/	
		JPanel pWestLL = new JPanel(new GridLayout(0,2));
		leadLike.add(pWestLL, BorderLayout.WEST);
		pWestLL.setBorder(new TitledBorder("Set Parameters:"));

		
		logP = new JLabel("LogP <= ");
		logDLower = new JLabel("LogD >= ");
		logDUpper = new JLabel("LogD <= ");
		hba = new JLabel("H Bond Acceptors <= ");
		hbd = new JLabel("H Bond Donors <= ");
		mass = new JLabel("Molecular Mass <= ");
		ringCount = new JLabel("Ring Count <= ");
		rotatableBond = new JLabel("Rotatable Bond Count <= ");
		
		inputLogPLL = new JTextArea("3",0,3);
		inputLogDUpper = new JTextArea("4",0,3);
		inputLogDLower = new JTextArea("-4",0,3);
		inputHBALL = new JTextArea("8", 0,3);
		inputHBDLL = new JTextArea("5", 0,3);
		inputMassLL = new JTextArea("450", 0,3);
		inputRingLL = new JTextArea("4", 0,3);
		inputRotatableLL = new JTextArea("10", 0,3);
		
		// adds labels and input areas to panel
		pWestLL.add(logP);
		pWestLL.add(inputLogPLL);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(logDLower);
		pWestLL.add(inputLogDLower);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(logDUpper);
		pWestLL.add(inputLogDUpper);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(hba);
		pWestLL.add(inputHBALL);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(hbd);
		pWestLL.add(inputHBDLL);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(mass);
		pWestLL.add(inputMassLL);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(ringCount);
		pWestLL.add(inputRingLL);
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(Box.createVerticalStrut(30));
		pWestLL.add(rotatableBond);
		pWestLL.add(inputRotatableLL);
		
		
		/*--- Centre panel for results---*/
		pCentreLL = new JPanel();
		leadLike.add(pCentreLL, BorderLayout.CENTER);
		pCentreLL.setBackground(Color.white);
		
		
		/*--- South panel for feedback---*/
		JPanel pSouthLL = new JPanel();
		leadLike.add(pSouthLL, BorderLayout.SOUTH);
		feedbackLL = new JTextArea(0, 30);
		feedbackLL.setMargin(new Insets(10,10,10,10));	// Aesthetic stand-off for text
		pSouthLL.add(feedbackLL);
		
		
		/*--- East panel for submit/save ---*/
		JPanel pEastLL = new JPanel(new GridLayout(0,1));
		leadLike.add(pEastLL, BorderLayout.EAST);
			
		// Submit button
		bSubmitLL = new JButton("Submit");
		bSubmitLL.addActionListener(this);
		pEastLL.add(bSubmitLL);
		
		// Save button
		saveLL = new JButton("Save Results");
		saveLL.addActionListener(this);
		saveLL.setVisible(false);
		pEastLL.add(saveLL);

		// adds lead-like panel as a tab
		tabPane.addTab("Lead-Likeness", leadLike);
		
		
	/*-------------- Bond Search tab ---------------*/	
		
		/*--- West panel for JCombo box ---*/	
		
		bondSearch = new JPanel();
		bondSearch.setLayout(new BorderLayout());
		
		JPanel pWestBS = new JPanel();	
		bondSearch.add(pWestBS, BorderLayout.WEST);
		pWestBS.setBorder(new TitledBorder("Set Parameters:"));
		
		JLabel lcat = new JLabel("Bond Order: ", JLabel.LEFT);
		pWestBS.add(lcat);
		
		orderCombo = new JComboBox<>();	
		orderCombo.setMaximumRowCount(6);
		populateOrderCombo();
		pWestBS.add(orderCombo);
		orderCombo.addItemListener(listener);
		
		/*--- Center panel for table results ---*/
		pCentreBS = new JPanel();
		bondSearch.add(pCentreBS, BorderLayout.CENTER);
		pCentreBS.setBackground(Color.white);
		
		
		// Feedback text area
		JPanel pSouthBS = new JPanel();
		bondSearch.add(pSouthBS, BorderLayout.SOUTH);
		feedbackBS = new JTextArea(0, 30);
		feedbackBS.setMargin(new Insets(10,10,10,10));	// Aesthetic stand-off for text
		pSouthBS.add(feedbackBS);
				
		/*--- South panel for buttons ---*/
		JPanel pEastBS = new JPanel(new GridLayout(0,1));
		bondSearch.add(pEastBS, BorderLayout.EAST);
			
		// Submit button
		bSubmitBS = new JButton("Submit");
		bSubmitBS.addActionListener(this);
		pEastBS.add(bSubmitBS);		
		
		// save button
		saveBS = new JButton("Save Results");
		saveBS.addActionListener(this);
		saveBS.setVisible(false);
		pEastBS.add(saveBS);
		
		tabPane.addTab("Bond Search", bondSearch);

		
		
		
		
		
	/*-------------- Bioavailability tab ---------------*/	
		
		bioavailability = new JPanel();
		bioavailability.setLayout(new BorderLayout());
		
		/*--- West panel for paramaters ---*/
		JPanel pWestB = new JPanel(new GridLayout(0,2));	
		bioavailability.add(pWestB, BorderLayout.WEST);
		pWestB.setBorder(new TitledBorder("Set Parameters:"));
		
		logP = new JLabel("LogP <= ");
		hba = new JLabel("H Bond Acceptors <= ");
		hbd = new JLabel("H Bond Donors <= ");
		mass = new JLabel("Molecular Mass <= ");
		ringCount = new JLabel("Ring Count <= ");
		rotatableBond = new JLabel("Rotatable Bond Count <= ");
		aromaticRing = new JLabel("Fused Aromatic Ring Count <=");
		tpsa = new JLabel("Polar Surface Area <= ");
		
		inputLogPB = new JTextArea("5",0,3);
		inputHBAB = new JTextArea("10",0,3);
		inputHBDB = new JTextArea("5",0,3);
		inputMassB = new JTextArea("500", 0,3);
		inputTPSA = new JTextArea("200", 0,3);
		inputRotatableB = new JTextArea("10", 0,3);
		inputAromatic = new JTextArea("5", 0,3);
		
		// adds labels and input areas to panel
		pWestB.add(logP);
		pWestB.add(inputLogPB);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(hba);
		pWestB.add(inputHBAB);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(hbd);
		pWestB.add(inputHBDB);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(mass);
		pWestB.add(inputMassB);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(tpsa);
		pWestB.add(inputTPSA);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(rotatableBond);
		pWestB.add(inputRotatableB);
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(Box.createVerticalStrut(30));
		pWestB.add(aromaticRing);
		pWestB.add(inputAromatic);
		
		/*--- Centre panel for results---*/
		pCentreB = new JPanel();
		bioavailability.add(pCentreB, BorderLayout.CENTER);
		pCentreB.setBackground(Color.white);
		
		/*--- South panel for feedback---*/
		JPanel pSouthB = new JPanel();
		bioavailability.add(pSouthB, BorderLayout.SOUTH);
		feedbackB = new JTextArea(0, 30);
		feedbackB.setMargin(new Insets(10,10,10,10));	// Aesthetic stand-off for text
		pSouthB.add(feedbackB);
		
		/*--- East panel for submit/save ---*/
		JPanel pEastB = new JPanel(new GridLayout(0,1));
		bioavailability.add(pEastB, BorderLayout.EAST);
		
		// Submit button
		bSubmitB = new JButton("Submit");
		bSubmitB.addActionListener(this);
		pEastB.add(bSubmitB);
		
		// Save button
		saveB = new JButton("Save Results");
		saveB.addActionListener(this);
		saveB.setVisible(false);
		pEastB.add(saveB);
		
		
		tabPane.addTab("Bioavailability", bioavailability);
		
		/*-------------- Browse all tab ---------------*/	

		browseAll = new JPanel();
		browseAll.setLayout(new BorderLayout());
		
		/*--- Centre panel for results---*/
		pCentreA = new JPanel();
		browseAll.add(pCentreA, BorderLayout.CENTER);
		pCentreA.setBackground(Color.white);
		
		/*--- South panel for feedback---*/
		JPanel pSouthA = new JPanel();
		browseAll.add(pSouthA, BorderLayout.SOUTH);
		feedbackA = new JTextArea(0, 30);
		feedbackA.setMargin(new Insets(10,10,10,10));	// Aesthetic stand-off for text
		pSouthA.add(feedbackA);
		
		// Submit button
		submitA = new JButton("Get all Results");
		submitA.addActionListener(this);
		pCentreA.add(submitA);
		
		// Save button
		saveA = new JButton("Save Results");
		saveA.addActionListener(this);
		pSouthA.add(saveA);
		
		tabPane.addTab("Browse All", browseAll);
		
		
		// Adding colours to the tabs 	
		
		tabPane.setBackgroundAt(0, Color.BLUE);
		tabPane.setBackgroundAt(1, Color.RED);
		tabPane.setBackgroundAt(2, Color.GREEN);
		tabPane.setBackgroundAt(3, Color.CYAN);
		tabPane.setBackgroundAt(4, Color.GRAY);
		
		// Adding tabbed pane to container 
		Container pane = this.getContentPane();
		pane.add(tabPane);
		
		
	}	
	
	// event-handling method for button-press
	public void actionPerformed(ActionEvent event)
	{
		// ----------------------------- Bond Search Button ----------------------------------	
		if (event.getSource() == bSubmitBS)
		{					
			bs = new BondSearch(orderChoice);
			
			pCentreBS.removeAll();
			feedbackBS.setText("");
			
			String[] columns = {"Compound ID", "Atom 1", "Type", "Atom 2", "Type"};
			Object[][] resultsArrayBS = bs.getResultsArray();
			
			JTable resultsTableBS = new JTable(resultsArrayBS, columns);
			
			resultsTableBS.setAutoCreateRowSorter(true);
	
			JScrollPane scrollPanelBS = new JScrollPane(resultsTableBS);
			scrollPanelBS.setPreferredSize(pCentreBS.getSize());
			
			pCentreBS.add(scrollPanelBS);
			
			saveBS.setVisible(true);
			
			if(bs.getIfResults()) {
				feedbackBS.append("There are " + bs.getBondCount() + " results");
			}
			else{
				
				feedbackBS.append("No bonds of this order");
			}
		}	
		
		// ----------------------------- Lipinskis Button ----------------------------------	
		else if (event.getSource() == bSubmitl) 
		{
			double entreLogP = Double.parseDouble(inputLogPl.getText());
			int entreHBA = Integer.parseInt(inputHBAl.getText());
			int entreHBD = Integer.parseInt(inputHBDl.getText());
			double entreMass = Double.parseDouble(inputMassl.getText());
			
			lap = new Lapinskis(entreLogP, entreHBA, entreHBD, entreMass);
			
			pCentrel.removeAll();
			feedbackl.setText("");
			
			String[] columns = {"Compound ID", "LogP", "H Bond Acceptor Count", "H Bond Donor Count", "Mass"};
			Object[][] resultsArrayl = lap.getResultsArray();
			
			JTable resultsTablel = new JTable(resultsArrayl, columns);
			
			resultsTablel.setAutoCreateRowSorter(true);
			
			JScrollPane scrollPanel = new JScrollPane(resultsTablel);
			scrollPanel.setPreferredSize(pCentrel.getSize());
			
			pCentrel.add(scrollPanel);
			
			savel.setVisible(true);
			
			if(lap.getIfResults()) {
				feedbackl.append("There are " + lap.getLapinskiCount() + " results");
			}
			else{
				feedbackl.append("No molecules within Lapinski's rules");
			}
		}
		
		// ----------------------------- Lead-Likeness Button ----------------------------------	
		else if (event.getSource() == bSubmitLL) 
		{
			ll = new LeadLike(Double.parseDouble(inputLogPLL.getText()), Double.parseDouble(inputLogDUpper.getText()), Double.parseDouble(inputLogDLower.getText()), Integer.parseInt(inputHBALL.getText()), Integer.parseInt(inputHBDLL.getText()), Double.parseDouble(inputMassLL.getText()), Integer.parseInt(inputRingLL.getText()), Integer.parseInt(inputRotatableLL.getText()));
			
			pCentreLL.removeAll();
			feedbackLL.setText("");
			
			String[] columns = {"Compound ID", "LogP", "LogD", "H Bond Acceptor Count", "H Bond Donor Count", "Mass", "Ring Count", "Rotatable Bond Count"};
			Object[][] resultsArrayLL = ll.getResultsArray();
			
			JTable resultsTableLL = new JTable(resultsArrayLL, columns);
			
			resultsTableLL.setAutoCreateRowSorter(true);
		
			JScrollPane scrollPaneLL = new JScrollPane(resultsTableLL);
			scrollPaneLL.setPreferredSize(pCentreLL.getSize());
			
			pCentreLL.add(scrollPaneLL);
			
			saveLL.setVisible(true);
			
			if(ll.getIfResults()) {
				feedbackLL.append("There are " + ll.getLeadLikeCount() + " results");
			}
			else{
				feedbackLL.append("No molecules within Lead-Likeness rules");
			}
			
		}
		
	// ----------------------------- Bioavailability Button ----------------------------------	
		else if (event.getSource() == bSubmitB) {
			
			double entreLogP = Double.parseDouble(inputLogPB.getText());
			int entreHBA = Integer.parseInt(inputHBAB.getText());
			int entreHBD = Integer.parseInt(inputHBDB.getText());
			double entreMass = Double.parseDouble(inputMassB.getText());
			double entrePSA = Double.parseDouble(inputTPSA.getText());
			int entreRot = Integer.parseInt(inputRotatableB.getText());
			int entreArom = Integer.parseInt(inputAromatic.getText());
			
			bio = new Bioavailability(entreLogP, entreHBA, entreHBD, entreMass, entrePSA, entreRot, entreArom);
			
			pCentreB.removeAll();
			feedbackB.setText("");
			
			String[] columns = {"Compound ID", "LogP", "H Bond Acceptor Count", "H Bond Donor Count", "Mass", "Polar Surface Area", "Rotatable Bond Count", "Fused Aromatic Ring Count"};
			Object[][] resultsArrayB = bio.getResultsArray();
			
			JTable resultsTableB = new JTable(resultsArrayB, columns);
			
			JScrollPane scrollPaneB = new JScrollPane(resultsTableB);
			scrollPaneB.setPreferredSize(pCentreB.getSize());
			
			resultsTableB.setAutoCreateRowSorter(true);
			
			pCentreB.add(scrollPaneB);
			
			saveB.setVisible(true);
			
			if(bio.getIfResults()) {
				feedbackB.append("There are " + bio.getBioCount() + " results");
			}
			else{
				feedbackB.append("No molecules within Bioavailability rules");
			}
		}
		
		// ----------------------------- Browse All Button ----------------------------------
		else if (event.getSource() == submitA) {
			
			pCentreA.removeAll();
			
			all = new BrowseAll();
			
			String[] columns = {"Compound ID", "# Atoms", "# Bonds", "LogP", "LogD", "H Bond Acceptors", "H Bond Donors", "Mass", "Polar Surface Area", "Ring Count", "Rotatable Bond Count", "Fused Aromatic Rings"};
			
			Object[][] resultsArray = all.getResultsArray();
			
			JTable resultsTableA = new JTable(resultsArray, columns);
			
			resultsTableA.setAutoCreateRowSorter(true);

			JScrollPane scrollPanelA = new JScrollPane(resultsTableA);
			scrollPanelA.setPreferredSize(pCentreA.getSize());
			
			pCentreA.add(scrollPanelA);
			
			feedbackA.append("There are " + all.getCount() + " molecules");
			
		}
	/* -------- SAVE BUTTONS ----------------------*/
		else if (event.getSource() == saveBS) {
			String file = bs.getFormatted();
			new SaveFile(this,file);
			feedbackBS.setText("");
			feedbackBS.append("Results File Saved.");
		}
		
		else if (event.getSource() == savel) {
			String file = lap.getFormatted();
			new SaveFile(this,file);
			feedbackl.setText("");
			feedbackl.append("Results File Saved.");
		}
		
		else if (event.getSource() == saveLL) {
			String file = ll.getFormatted();
			new SaveFile(this,file);
			feedbackLL.setText("");
			feedbackLL.append("Results File Saved.");
		}
		
		else if (event.getSource() == saveB) {
			String file = bio.getFormatted();
			new SaveFile(this,file);
			feedbackB.setText("");
			feedbackB.append("Results File Saved.");
		}
	}
	
	// set bond order from Combo selection
	public void setComboChoice()
	{
		orderChoice = (Integer) orderCombo.getSelectedItem();	
	}
	
	// populate JComboBox and set current choice
	public void populateOrderCombo()
	{		
		for(int i = 0; i< ORDER_MAX; i++)
		{
			orderCombo.addItem(i+1);
		}		
		setComboChoice();	// initialize orderChoice
	}
	
	// Inner class for ItemListener
	class Listener implements ItemListener
	{
		// When JComboBox selection is changed, reset variable holding choice
		public void itemStateChanged(ItemEvent event)
		{			
			setComboChoice();
		}
	}
}
