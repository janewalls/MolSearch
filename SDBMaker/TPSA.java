import java.math.BigDecimal;
import java.math.MathContext;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.TPSAPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;

/*
 * Class that calculates polar surface area
 */
public class TPSA {

	private double tpsa;
	
	public TPSA(String molecule) {
		this.calcTPSA(molecule);
	}
	
	public void calcTPSA(String molecule) {
		// read input molecule
		try {
		MolImporter mi = null;
	    Molecule target = null;
	   
	    if((target = mi.importMol(molecule)) != null) {
	    try {
	    // create plugin
	    TPSAPlugin plugin = new TPSAPlugin();

	    plugin.setpH(7.4);

	    // set target molecule
	    plugin.setMolecule(target);
	        
	    // run the calculation
	    plugin.run();

	    // get result
	    double area = plugin.getSurfaceArea();
	    
	    this.tpsa = area;
	    
	    }catch(PluginException e) {
	    	System.out.println("TPSA plugin issue");
	    	e.printStackTrace();
	    }
	  	  }
	   	}catch(MolFormatException e) {	
	   		System.out.println("TPSA mol format issue");
	   		e.printStackTrace();
	   	} 
	}
	
	// returns value in 5 significant figures
	public double getNumTPSA() {
		if(new Double(tpsa).isNaN()) {
			return tpsa;
		}
		else {
		BigDecimal bd = new BigDecimal(tpsa);
		bd = bd.round(new MathContext(5));
		double rounded = bd.doubleValue();
		return rounded;
		}
	}
}
