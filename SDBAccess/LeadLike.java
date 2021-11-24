import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/* 
 * Class to query for lead-likeness filters
 */
public class LeadLike {
	private Connect cnt;
	private Connection conn;
	private int leadlikeCount;
	
	private Object[][] resultsArray;
	private boolean ifResults;
	
		
		public LeadLike(double logP, double upperlogD, double lowerlogD, int hba, int hbd, double mass, int ringCount, int rotatableBond) {
			
			leadlikeCount = 0;
			resultsArray = new Object[10][8];
			
			// get connection
			cnt = new Connect("jdbc:mysql://localhost/StructureDB?autoreconnent=true&&useSSL=false", "root", "Student1!");
			conn = cnt.getConnection();
			
			leadLikeQuery(logP, upperlogD, lowerlogD, hba, hbd, mass, ringCount, rotatableBond); 
		}
		
	private void leadLikeQuery(double logP, double upperlogD, double lowerlogD, int hba, int hbd, double mass, int ringCount, int rotatableBond) {
		String sql = "SELECT compoundid, logp, logd, hba, hbd, mass, ringcount, rotatablebond\r\n"
				+ "FROM stats\r\n"
				+ "WHERE logp <= " + logP
				+ " AND logd >= " + lowerlogD
				+ " AND logd <= " + upperlogD
				+ " AND hba <= " + hba
				+ " AND hbd <= " + hbd
				+ " AND mass <= " + mass 
				+ " AND ringcount <=" + ringCount
				+ " AND rotatableBond <=" + rotatableBond + "\r\n"
				+ "\r\n";
	
		try {
			// Execute query
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
					double resLogd = resSet.getDouble(3);
					int resHBA = resSet.getInt(4);
					int resHBD = resSet.getInt(5);
					double resMass = resSet.getDouble(6);
					int resRing = resSet.getInt(7);
					int resRot = resSet.getInt(8);
			
				// makes bigger array if necessary	
				if(leadlikeCount >= resultsArray.length) {
					int count = leadlikeCount;
					Object[][] bigArray = new Object[count+1][8];
					for(int i = 0; i < resultsArray.length; i++) {
						for(int n = 0; n < resultsArray[i].length; n++) {
							bigArray[i][n] = resultsArray[i][n];
						}
					}
					resultsArray = bigArray;
				}
				// Stores results in an array
					resultsArray[leadlikeCount][0] = cid;
					resultsArray[leadlikeCount][1] = resLogp;
					resultsArray[leadlikeCount][2] = resLogd;
					resultsArray[leadlikeCount][3] = resHBA;
					resultsArray[leadlikeCount][4] = resHBD;
					resultsArray[leadlikeCount][5] = resMass;
					resultsArray[leadlikeCount][6] = resRing;
					resultsArray[leadlikeCount][7] = resRot;
					
					leadlikeCount++;
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
	
	
	public int getLeadLikeCount() {
		return leadlikeCount;
	}
	
	public Object[][] getResultsArray() {
		return resultsArray;
	}
	
	public boolean getIfResults() {
		return ifResults;
	}
	
	// Format for saving file
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
