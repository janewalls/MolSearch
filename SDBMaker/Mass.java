import java.math.BigDecimal;
import java.math.MathContext;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;


public class Mass {
	
	private double mass;
	
	public Mass(String molecule) {
		this.calcMass(molecule);
	}
	

	public void calcMass(String molecule) {
		System.out.println("mass");
 
		// Reads molecule String to Molecule object
	    try {
		MolImporter mi = null;
	     Molecule mol = null;
	     if((mol = mi.importMol(molecule)) != null) {
	            
	    	 // Results
	            this.mass = mol.getMass();
	        }
	    }catch(MolFormatException e) {
	    	System.out.println("Unable to calc mass");
	    }
	}
	// Returns mass as a double with 5 significant figures
	public double getNumMass() {
		BigDecimal bd = new BigDecimal(mass);
		bd = bd.round(new MathContext(5));
		double rounded = bd.doubleValue();
		return rounded;
	}
	
}
