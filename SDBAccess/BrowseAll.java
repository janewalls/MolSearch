import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * Class to return all calculated results from molecule file
 */
public class BrowseAll {
	private Connect cnt;
	private Connection conn;
	private int count;
	
	private Object[][] resultsArray;
	
	public BrowseAll() {
		
		resultsArray = new Object[10][12];
		count = 0;

		cnt = new Connect("jdbc:mysql://localhost/StructureDB?autoreconnent=true&&useSSL=false", "root", "Student1!");
		conn = cnt.getConnection();
		
		getAll();
	}
	
	public void getAll() {
		
		String sql = "SELECT c.compoundid, c.numatoms, c.numbonds, s.logp, s.logd, s.hba, s.hbd, s.mass, s.tpsa, s.ringcount, s.rotatablebond, s.aromaticring"
				+ " FROM compound AS C, stats AS S\r\n"
				+ " WHERE c.compoundid = s.compoundid\r\n";
		
		try {
			// executes query
			Statement stmt = conn.createStatement();
			ResultSet resSet = stmt.executeQuery(sql);
			
			if(resSet.first())
			{
				resSet.beforeFirst();		// hack to reset cursor as 'if' moves it on a row!
				while (resSet.next())
				{
					// gets results
					String cid = resSet.getString(1);
					int resAtom = resSet.getInt(2);
					int resBond = resSet.getInt(3);
					double resLogp = resSet.getDouble(4);
					double resLogd = resSet.getDouble(5);
					int resHBA = resSet.getInt(6);
					int resHBD = resSet.getInt(7);
					double resMass = resSet.getDouble(8);
					double resPSA = resSet.getDouble(9);
					int resRing = resSet.getInt(10);
					int resRot = resSet.getInt(11);
					int resAro = resSet.getInt(12);
					
					// makes array bigger if necessary
					if(count >= resultsArray.length) {
						int p = count;
						Object[][] bigArray = new Object[p+1][12];
						for(int i = 0; i < resultsArray.length; i++) {
							for(int n = 0; n < resultsArray[i].length; n++) {
								bigArray[i][n] = resultsArray[i][n];
							}
						}
						resultsArray = bigArray;
					}
					// Stores results in array
					resultsArray[count][0] = cid;
					resultsArray[count][1] = resAtom;
					resultsArray[count][2] = resBond;
					resultsArray[count][3] = resLogp;
					resultsArray[count][4] = resLogd;
					resultsArray[count][5] = resHBA;
					resultsArray[count][6] = resHBD;
					resultsArray[count][7] = resMass;
					resultsArray[count][8] = resPSA;
					resultsArray[count][9] = resRing;
					resultsArray[count][10] = resRot;
					resultsArray[count][11] = resAro;
					
					count++;
				}
				resSet.close();
			}
		}catch(SQLException ex) {
			System.out.println("Unable to do it - sorry");
			ex.printStackTrace();
		}
		
		
	}
	
	public int getCount() {
		return count;
	}
	
	public Object[][] getResultsArray() {
		return resultsArray;
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
