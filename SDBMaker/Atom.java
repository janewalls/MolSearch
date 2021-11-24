/*
	Class to model atom from .sd file (MOL file format)
	DPL 10.09.14
*/

public class Atom
{
	private int atomNumber;				// numbering of atom in file starting from 1 (implicit in sequential list of atoms in file)
	private String atomType;			// N, C, ZN etc.
	private double xcoord;				// orthogonal x coordinate
	private double ycoord;				// orthogonal y coordinate
	private double zcoord;				// orthogonal z coordinate
	private String compoundID;			// compound identifier (ZINC00...) 
	
	public Atom(int atomNumber, String atomType, double xcoord, double ycoord, double zcoord, String compoundID)
	{
		this.atomNumber = atomNumber;
		this.atomType = atomType;
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		this.zcoord = zcoord;
		this.compoundID = compoundID;
	}

	// Write line into SQL file for upload into DB
	public String getFormatted()
	{
		return (atomNumber + "\t" + atomType + "\t" + xcoord + "\t" + ycoord + "\t" + zcoord  + "\t" +  compoundID);
	}	
	
	//-------------- accessor methods (not actually used) ------------//
	
	public int getAtomNumber()
	{
		return atomNumber;
	}
	
	public String getAtomType()
	{
		return atomType;
	}
	
	public double getXcoord()
	{
		return xcoord;
	}
	
	public double getYcoord()
	{
		return ycoord;
	}
	
	public double getZcoord()
	{
		return zcoord;
	}
	
	public String getCompoundID()
	{
		return compoundID;
	}	
	
	// Formats for direct upload to SQL
	public String sqlUploadFormat() {
		return ("INSERT INTO atom(atomnumber, atomtype, xcoord, ycoord, zcoord, compoundID) VALUES(" + atomNumber + ", '" + atomType + "', " + xcoord + ", " + ycoord + ", " + zcoord + ", '" + compoundID + "');");
	}
}
