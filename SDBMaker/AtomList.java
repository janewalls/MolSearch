/*
	Class with array to hold a list of Atom objects
	DPL 09.09.14
*/

public class AtomList
{
	private Atom  atList[];					// array of Atom objects
	private int listLen = 1000;				// array size
	private int listSize = 0;				// array occupancy
	
	public AtomList()
	{
		atList = new Atom[listLen];
	}
			
	// Method to add new Atom object to next position in list
	public void addAtom(Atom at)
	{
		// expand array if necessary
		if(listSize >= listLen)
		{
			listLen = 2 * listLen;
			Atom[] newList = new Atom[listLen];
			System.arraycopy (atList, 0, newList, 0, atList.length);
			atList = newList;
		}
		// add new Atom object in next position
		atList[listSize] = at;
		listSize++;
	}
	
	// returns number of Atoms in list
	public int getSize()
	{
		return listSize;
	}
	
	// returns Atom at particular position in list numbered from 0
	public Atom getAtom(int pos)
	{
		return atList[pos];
	}	
}

