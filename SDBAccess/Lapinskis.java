import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Lapinskis {
	private Connect cnt;
	private Connection conn;
	private int lapinskiCount;
	
	private Object[][] resultsArray;
	private boolean ifResults;

	
	
	public Lapinskis(double logP, int hba, int hbd, double mass) {
		lapinskiCount = 0;
		resultsArray = new Object[10][5];
		
		// gets connection
		cnt = new Connect("jdbc:mysql://localhost/StructureDB?autoreconnent=true&&useSSL=false", "root", "Student1!");
		conn = cnt.getConnection();
		
		lapinskiQuery(logP, hba, hbd, mass);
	}
	
	private void lapinskiQuery(double logP, int hba, int hbd, double mass) {
		
		String sql = "SELECT compoundid, logp, hba, hbd, mass\r\n"
				+ "FROM stats\r\n"
				+ "WHERE logp <= " + logP
				+ " AND hba <= " + hba
				+ " AND hbd <= " + hbd
				+ " AND mass <= " + mass + "\r\n";
	
		try {
			//executes query
			Statement stmt = conn.createStatement();
			ResultSet resSet = stmt.executeQuery(sql);
			
			if(resSet.first())
			{
				ifResults = true;
				resSet.beforeFirst();		// hack to reset cursor as 'if' moves it on a row!
				while (resSet.next())		// moves to next row while rows remain
				{
					// gets results
					String cid = resSet.getString(1);
					double resLogp = resSet.getDouble(2);	
					int resHBA = resSet.getInt(3);
					int resHBD = resSet.getInt(4);
					double resMass = resSet.getDouble(5);
				
					// makes array bigger if necessary
					if(lapinskiCount >= resultsArray.length) {
						int count = lapinskiCount;
						Object[][] bigArray = new Object[count+1][5];
						for(int i = 0; i < resultsArray.length; i++) {
							for(int n = 0; n < resultsArray[i].length; n++) {
								bigArray[i][n] = resultsArray[i][n];
							}
						}
						resultsArray = bigArray;
					}
					// stores results in array
					resultsArray[lapinskiCount][0] = cid;
					resultsArray[lapinskiCount][1] = resLogp;
					resultsArray[lapinskiCount][2] = resHBA;
					resultsArray[lapinskiCount][3] = resHBD;
					resultsArray[lapinskiCount][4] = resMass;
					
				
					lapinskiCount++;
				}
				resSet.close(); // ! before conn.close()
			}
			else 
			{
				ifResults = false;
			}
		}catch(SQLException ex) {
			System.out.println("Unable to do it - sorry");
			ex.printStackTrace();
		}
	}
	
	public int getLapinskiCount() {
		return lapinskiCount;
	}
	
	public Object[][] getResultsArray() {
		return resultsArray;
	}
	
	public boolean getIfResults() {
		return ifResults;
	}
	
	// Formats for file save
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
