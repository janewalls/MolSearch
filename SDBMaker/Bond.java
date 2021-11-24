
/*
	Class to model bond from .sd file (MOL file format)
	DPL 10.09.14
*/

public class Bond
{
	private int bondNumber;		// numbering of bond in file (implicit in sequential list of bonds in file)
	private int atom1number;	// numbering of bond atom 1 in file (explicit from bond column 1)
	private int atom2number;	// numbering of bond atom 2 in file (explicit from column 2)
	private int bondOrder;		// 'bond order' e.g. 1 = single bond, 4 partial double-bond (explicit from bond column 3)
	private String compoundID;	// compound identifier (ZINC00...)
	
	public Bond(int bondNumber, int atom1number, int atom2number, int bondOrder, String compoundID)
	{
		this.bondNumber = bondNumber;
		this.atom1number = atom1number;
		this.atom2number = atom2number;
		this.bondOrder = bondOrder;
		this.compoundID = compoundID;
	}

	// Write line into SQL file for upload into DB
	public String getFormatted()
	{
		return (bondNumber + "\t"  + atom1number + "\t" + atom2number + "\t" + bondOrder  + "\t" +  compoundID);
	}
	
	//-------------- accessor methods (not actually used) ------------//
	
	public int getBondNumber()
	{
		return bondNumber;
	}

	
	public int getAtom1number()
	{
		return atom1number;
	}
	
	public int getAtom2number()
	{
		return atom2number;
	}
	
	public int getBondOrder()
	{
		return bondOrder;
	}
	
	
	public String getCompoundID()
	{
		return compoundID;
	}	
	// Formats for direct upload to SQL
	public String sqlUploadFormat() {
		return ("INSERT INTO bond(bondnumber, atom1number, atom2number, bondorder, compoundID) VALUES(" + bondNumber + ", " + atom1number + ", " + atom2number + ", " + bondOrder + ", '" + compoundID + "');");
	}
}