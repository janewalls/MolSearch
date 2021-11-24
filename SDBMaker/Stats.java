import java.util.Scanner;
/*
 * Class that calls for all the calculations
 */
public class Stats  {
	private LogP logP;
	private HBDA acceptDonor;
	private Mass mass;
	private RingAndRot rrCount;
	private LogD logD;
	private TPSA tpsa;
	private String compoundID;
	private String molecule;
	
	public Stats(String molecule) {
		// calls for all calculations
		this.molecule = molecule;
		logP = new LogP(molecule);
		acceptDonor = new HBDA(molecule);
		mass = new Mass(molecule);
		rrCount = new RingAndRot(molecule);
		logD = new LogD(molecule);
		tpsa = new TPSA(molecule);
		this.findCompoundID();
		
	}
	// Write line into SQL file for upload into DB
	public String getFormatted() {
		return (logP.getNumlogP() + "\t" + acceptDonor.getAcceptor() + "\t" + acceptDonor.getDonor() + "\t" + mass.getNumMass() + "\t" + rrCount.getNumRing() + "\t" + rrCount.getNumRot() + "\t" + rrCount.getNumAromatic() + "\t" + logD.getNumlogD() + "\t" + tpsa.getNumTPSA() + "\t" + compoundID);
	}
	
	// gets CompoundID from molecule
	public void findCompoundID() {
		Scanner s = new Scanner(molecule);
		this.compoundID = s.nextLine();
	}
	
	// Formats for direct upload to SQL, if NaN result then NULL is entered into database
	public String sqlUploadFormat() {
		
		String output = "INSERT INTO stats(logP, hba, hbd, mass, ringcount, rotatablebond, aromaticring, logd, tpsa, compoundID) VALUES("; 
		
		if(new Double(logP.getNumlogP()).isNaN()) {
			output+= "NULL";
		} else {
			output+= logP.getNumlogP();
		}
		output += (", " + acceptDonor.getAcceptor() + ", " + acceptDonor.getDonor() + ", " + mass.getNumMass() + ", " + rrCount.getNumRing() + ", " + rrCount.getNumRot() + ", " + rrCount.getNumAromatic() + ", ");  
		
		if(new Double(logD.getNumlogD()).isNaN()) {
			output += "NULL";
		} else {
			output += logD.getNumlogD();
		}
		output += (", " + tpsa.getNumTPSA() + ", '" + compoundID + "');");
		return output;
		
	}
	

	
}

/* ---------- NOT USING ANYMORE ------------
 * 
	public LogP findlogP(LogP log) {
		log.calcLogP(molecule);
		return logP.getLogP();
	}
	
	public HBDA findAcceptDon(HBDA acceptDon) {
		acceptDon.HBDAcalc(molecule);
		return acceptDon.getAcceptDon();
	}
	*/
