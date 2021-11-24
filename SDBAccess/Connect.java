/*
Class to handle JDBC connections to db on localhost
Based on examples in JDBC chapters in various O'Reilly books
DPL 11.09.14
*/

import java.sql.*;

public class Connect
{
	Connection conn = null;	
	String username;
	String password;
	String host;
	
	ResultSet resSet;
	
	public Connect(String host, String user, String pass)
	{
		this.host = host;
		username = user;
		password = pass;
		connect(this.host);
	}	
		
	private void connect(String url)
	{
		try
		{
			Class.forName("org.gjt.mm.mysql.Driver");	
			conn = DriverManager.getConnection(url, username, password); 
		}
		catch(ClassNotFoundException ex)
		{
			System.out.println(ex.toString());
		}
		catch(SQLException e)
		{
			System.out.println("Trying to connect to MySQL " + e);
		}		
	}
	
	// Allows user to construct prepared query
	public Connection getConnection()
	{
		return conn;	
	}
	
	// Takes PreparedStatement to run query
	public ResultSet runPreparedQuery(PreparedStatement stmt)
	{
		try 
    	{
    		resSet = stmt.executeQuery();					
		}
		catch(SQLException e)
		{
			System.out.println("Trying to make query" + e.toString());
		}
		return resSet;		
	}

}