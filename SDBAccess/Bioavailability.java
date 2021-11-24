import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Bioavailability {
	private Connect cnt;
	private Connection conn;
	private int bioCount;
	
	private Object[][] resultsArray;
	private boolean ifResults;
	
	public Bioavailability(double logP, int hba, int hbd, double mass, double psa, int rotatableBond, int aromaticRing) {
		
		bioCount = 0;
		resultsArray = new Object[10][8];
		// gets connection
		cnt = new Connect("jdbc:mysql://localhost/StructureDB?autoreconnent=true&&useSSL=false", "root", "Student1!");
		conn = cnt.getConnection();
		
		bioQuery(logP, hba, hbd, mass, psa, rotatableBond, aromaticRing);
	}
	
	public void bioQuery(double logP, int hba, int hbd, double mass, double psa, int rotatableBond, int aromaticRing) {
		
		String sql = "SELECT compoundid, logp, hba, hbd, mass, tpsa, rotatableBond, aromaticring\r\n"
				+ "FROM stats\r\n"
				+ "WHERE logp <= " + logP
				+ " AND hba <= " + hba
				+ " AND hbd <= " + hbd
				+ " AND mass <= " + mass
				+ " AND TPSA <= " + psa
				+ " AND rotatableBond <= " + rotatableBond
				+ " AND aromaticring <= " + aromaticRing
				+ "\r\n";
	
		try {
			// Executes query 
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
					double resPSA = resSet.getDouble(6);
					int resRot = resSet.getInt(7);
					int resAro = resSet.getInt(8);
				
					// makes array bigger if necessary
					if(bioCount >= resultsArray.length) {
						int count = bioCount;
						Object[][] bigArray = new Object[count+1][8];
						for(int i = 0; i < resultsArray.length; i++) {
							for(int n = 0; n < resultsArray[i].length; n++) {
								bigArray[i][n] = resultsArray[i][n];
							}
						}
						resultsArray = bigArray;
					}
					// stores results in array
					resultsArray[bioCount][0] = cid;
					resultsArray[bioCount][1] = resLogp;
					resultsArray[bioCount][2] = resHBA;
					resultsArray[bioCount][3] = resHBD;
					resultsArray[bioCount][4] = resMass;
					resultsArray[bioCount][5] = resPSA;
					resultsArray[bioCount][6] = resRot;
					resultsArray[bioCount][7] = resAro;
					
				
					bioCount++;
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
	
	public int getBioCount() {
		return bioCount;
	}
	
	public Object[][] getResultsArray() {
		return resultsArray;
	}
	
	public boolean getIfResults() {
		return ifResults;
	}
	
	// Formats to be saved in file
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
