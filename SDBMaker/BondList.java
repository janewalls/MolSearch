
/*
	Class with array to hold a list of Bond objects
	DPL 09.09.14
*/

public class BondList
{
	private Bond  bdList[];					// array of Bond objects
	private int listLen = 1000;				// array size
	private int listSize = 0;				// array occupancy
	
	public BondList()
	{
		bdList = new Bond[listLen];
	}
			
	// Method to add new Bond object to next position in list
	public void addBond(Bond bd)
	{
		// expand array if necessary
		if(listSize >= listLen)
		{
			listLen = 2 * listLen;
			Bond[] newList = new Bond[listLen];
			System.arraycopy (bdList, 0, newList, 0, bdList.length);
			bdList = newList;
		}
		// add new Bond object in next position
		bdList[listSize] = bd;
		listSize++;
	}
	
	// returns number of Bonds in list
	public int getSize()
	{
		return listSize;
	}
	
	// returns Bond at particular position in list numbered from 0
	public Bond getBond(int pos)
	{
		return bdList[pos];
	}	
}
