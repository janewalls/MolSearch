/*
Class to read .sd file
and create set of Atom objects, Bond objects and Compound objects
DPL 10.09.14

*/

import java.util.*;

class FileParser
{
	private boolean parsed = false;		// Flag for successful parsing
	private AtomList aList;
	private BondList bList;
	private CompoundList cList;
	
		
	FileParser(Scanner scanner, AtomList aList, BondList bList, CompoundList cList)
	{
		this.aList = aList;
		this.bList = bList;
		this.cList = cList;
		parse(scanner);
	}

	private void parse(Scanner scanner)
	{ 
		String compoundID = scanner.nextLine();		// First Line is ID of compound
		scanner.nextLine();							// ignore data on second line
		scanner.nextLine();							// third line is blank
		
		try
		{
			// Get number of atoms and number of bonds
			String line4 = scanner.nextLine();
			StringTokenizer st4 = new StringTokenizer(line4);
			String tok1 = st4.nextToken();
			int numAtoms = Integer.parseInt(tok1);
			String tok2 = st4.nextToken();
			int numBonds = Integer.parseInt(tok2);
			
			// Create new Compound object and add to compoundList
			Compound cp = new Compound(compoundID, numAtoms, numBonds);
			cList.addCompound(cp);
			
			// Go through lines with atom data
			for(int i=0; i<numAtoms; i++)
			{
				String line = scanner.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				
				String xString = st.nextToken();
				Double xDouble = Double.valueOf(xString);
				double xcoord = xDouble.doubleValue();
				
				String yString = st.nextToken();
				Double yDouble = Double.valueOf(yString);
				double ycoord = yDouble.doubleValue();
				
				String zString = st.nextToken();
				Double zDouble = Double.valueOf(zString);
				double zcoord = zDouble.doubleValue();
				
				String atomType = st.nextToken();
	
				Atom at = new Atom(i+1, atomType, xcoord, ycoord, zcoord, compoundID);
				aList.addAtom(at);			
			}
			
			// Go through lines with bond data
			for(int j=0; j<numBonds; j++)
			{
				String line = scanner.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				
				String a1String = st.nextToken();
				int atom1number = Integer.parseInt(a1String);
				
				String a2String = st.nextToken();
				int atom2number = Integer.parseInt(a2String);
				
				String oString = st.nextToken();
				int order = Integer.parseInt(oString);
	
				Bond bd = new Bond(j+1, atom1number, atom2number, order, compoundID);
				bList.addBond(bd);			
			}
			parsed = true;
		}
		catch (NumberFormatException nfe)
		{
			parsed = false;
		}
	}

	//---------- Get methods -----------//
	public boolean isParsed()
	{
		return parsed;	
	}
}