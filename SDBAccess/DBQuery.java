/*
DBQuery
Class with static methods to provide access to SQL query strings
A seperate class with a 1-item array is obviously overkill here, 
but this structure is useful when one has many queries and simplifies code in other classes
DPL 11.09.14
ONLY USED FOR BOND SEARCH
 */

public class DBQuery 
{
	//-------------------------- PARAMETERIZED QUERIES -------------------------//
	final static String name0 = "BONDS_OF_ORDER_N";

	// Query for bonds of a particular order
	final static String query0 = "SELECT a1.CompoundID, b.bondNumber, a1.AtomNumber, a1.AtomType, "
			+ "a2.AtomNumber AS BondAtom2, a2.AtomType AS AtomType2 " 
			+ "FROM Atom a1, Atom a2, Bond b "
			+ "WHERE a1.CompoundID = b.CompoundID "
			+ "AND a2.CompoundID = b.CompoundID "
			+ "AND a1.AtomNumber = b.Atom1Number "
			+ "AND a2.AtomNumber = b.Atom2Number "
			+ "AND b.BondOrder = ? ";


	// creates an array of all ParamQuerys - only one here, but would generally be several
	static ParamQuery pqList[] = 
			{ new ParamQuery(name0, query0)
			};
			
	// find ParamQuery object by queryName and returns
	public static ParamQuery getParamQuery(String name) 
	{
		for (int i = 0; i < pqList.length; i++) 
		{
			if (pqList[i].getQueryName().equals(name)) 
			{
				return pqList[i];
			}
		}
		return null;
	}
			

	//----------------------- SIMPLE QUERIES W/O PARAMETERS ----------------------//

	// Query to find number of Compounds	
	static String compNumQuery = "SELECT COUNT(*) FROM Compound ";
		
	public static String getCompNumQuery()
	{
		return compNumQuery;
	}

}
