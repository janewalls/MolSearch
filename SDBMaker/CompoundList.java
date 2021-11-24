/*
	Class with array to hold a list of Compound objects
	DPL 09.09.14
*/

public class CompoundList
{
	private Compound  cpList[];					// array of Compound objects
	private int listLen = 1000;				// array size
	private int listSize = 0;				// array occupancy
	
	public CompoundList()
	{
		cpList = new Compound[listLen];
	}
			
	// Method to add new Compound object to next position in list
	public void addCompound(Compound cp)
	{
		// expand array if necessary
		if(listSize >= listLen)
		{
			listLen = 2 * listLen;
			Compound[] newList = new Compound[listLen];
			System.arraycopy (cpList, 0, newList, 0, cpList.length);
			cpList = newList;
		}
		// add new Compound object in next position
		cpList[listSize] = cp;
		listSize++;
	}
	
	// returns number of Compounds in list
	public int getSize()
	{
		return listSize;
	}
	
	// returns Compound at particular position in list numbered from 0
	public Compound getCompound(int pos)
	{
		return cpList[pos];
	}
	
	public String getCompoundID(int pos) {
		return cpList[pos].getCompoundID();
	}
}
