/*
Class holds details for a parameterized query.
ONLY USED FOR BOND SEARCH
 */

import java.sql.*;

class ParamQuery 
{
	private PreparedStatement prepStat;
	private String name; 			// descriptive name of query for retrieval
	private String querySQL; 		// SQL for paramaterized query

	public ParamQuery(String name, String querySQL) 
	{
		this.name = name;
		this.querySQL = querySQL;
	}

	/*--Key Methods for getting a paramatized SQL query--*/

	// orginal
	public void setPrepStatement(Connection con) throws SQLException 
	{
		setPrepStatement(con, false);
	}

	// allows possibility of row count retrieval from 'count=ResultSet.getRow()'
	public void setPrepStatement(Connection con, boolean rowCount)
			throws SQLException 
	{
		if (rowCount == true) 
		{
			prepStat = con.prepareStatement(querySQL, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
		} 
		else 
		{
			prepStat = con.prepareStatement(querySQL);
		}
	}

	public PreparedStatement getPrepStatement() 
	{
		return prepStat;
	}

	/*--'Get' Methods for instance variables--*/

	public String getQueryName() 
	{
		return name;
	}

	public String getQuery() 
	{
		return querySQL;
	}
}
