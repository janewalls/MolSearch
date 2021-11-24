// Class to handle SQL bond searches and format results

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BondSearch
{
	private Connect cnt;
	private Connection conn;
	private int order;						// bond order
	private int bondCount;					// No of bonds found
	
	private Object[][] resultsArray;
	private boolean ifResults;
	
	public BondSearch (int order)
	{
		this.order = order;
		
		resultsArray = new Object[10][5];
		// gets connection
		cnt = new Connect("jdbc:mysql://localhost/StructureDB?autoreconnent=true&&useSSL=false", "root", "Student1!");
		conn = cnt.getConnection();
		findBonds(order);
		closeConnection();
	}
	
	// Makes query to get bonds of a particular order and formats results
	private void findBonds(int order)
	{

		
		String query = "BONDS_OF_ORDER_N";
		ParamQuery parQ = DBQuery.getParamQuery(query);
		try 
		{
			parQ.setPrepStatement(conn);
		} 
		catch (SQLException e) 
		{System.out.println(e.toString());}
		
		try 
		{
			PreparedStatement stmt = parQ.getPrepStatement();
			stmt.setInt(1, order);								// count from 1, not 0
			ResultSet resSet = cnt.runPreparedQuery(stmt);
			
			if(resSet.first())
			{
				ifResults = true;
				resSet.beforeFirst();		// hack to reset cursor as 'if' moves it on a row!
				while (resSet.next())		// moves to next row while rows remain
				{
					String cid = resSet.getString(1);
					// int bondID = resSet.getInt(2);	// not used in display
					int atom1 = resSet.getInt(3);
					String type1 = resSet.getString(4);
					int atom2 = resSet.getInt(5);
					String type2 = resSet.getString(6);

					// makes array bigger if necessary
					if(bondCount >= resultsArray.length) {
						int count = bondCount;
						Object[][] bigArray = new Object[count+1][5];
						for(int i = 0; i < resultsArray.length; i++) {
							for(int n = 0; n < resultsArray[i].length; n++) {
								bigArray[i][n] = resultsArray[i][n];
							}
						}
						resultsArray = bigArray;
					}
					// stores results in array
					resultsArray[bondCount][0] = cid;
					resultsArray[bondCount][1] = atom1;
					resultsArray[bondCount][2] = type1;
					resultsArray[bondCount][3] = atom2;
					resultsArray[bondCount][4] = type2;
					
					
					bondCount++;
				}
				resSet.close(); // ! before conn.close()
			} 
			else 
			{
				ifResults = false;
			}
		}
		catch (SQLException e)
		{
			System.out.println("SQL Exception: " + e.toString());
		}
	}
	
	
	
	// close the connection
	private void closeConnection()
	{
		if(conn != null)
		{
			try { conn.close();}
			catch(Exception e){System.out.println("Can't close.");}
		}		
	}
	
	public int getBondCount() {
		return bondCount;
	}
	
	public Object[][] getResultsArray() {
		return resultsArray;
	}
	
	public boolean getIfResults() {
		return ifResults;
	}

	// Formats results for file save
	public String getFormatted() {
		String output = "";
		for(int i = 0; i < resultsArray.length; i++) {
			for(int n = 0; n < resultsArray[i].length; n++) {
				output += resultsArray[i][n] + "\t";
			}
			output.trim();
			output += "\n";
		}
		
		return output;
	}
}
