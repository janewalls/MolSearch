
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Properties;

import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.LogPMethod;
import chemaxon.marvin.calculations.logPPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
import chemaxon.formats.MolFormatException;

public class LogP {
	private double logP;

	
	public LogP(String molecule) {
		this.calcLogP(molecule);
		
	}
	
	public void calcLogP(String molecule) {
		System.out.println("logp");
		// fill parameters
		Properties params = new Properties();
	    params.put("type", "logP");

	    // create plugin & set logP calculation method
	    logPPlugin plugin = new logPPlugin();
	    plugin.setlogPMethod(LogPMethod.CONSENSUS);

	    // set parameters
	    plugin.setCloridIonConcentration(0.1);
	    plugin.setNaKIonConcentration(0.1);

	    // set result types
	    try {
	    plugin.setUserTypes("logPTrue");
	    }catch(PluginException e) {
	    	e.printStackTrace();
	    }
	    // for each input molecule run the calculation and display the results
	    double logp = -1;
	   try {
		MolImporter mi = null;
	    Molecule target = null;
	   
	    if((target = mi.importMol(molecule)) != null) {
	    	try {
	        // set the input molecule
	        plugin.setMolecule(target);

	        // run the calculation
	        plugin.run();
	    	}catch(PluginException e) {
	    		e.printStackTrace();
	    	}

	        // get the overall logP value
	        logp = plugin.getlogPTrue(); 
	   
	  
	    }  
	   }catch(MolFormatException e) {
		   
	   }
	   
	    this.logP = logp;
	}
	
	public LogP getLogP() {
		return this;
	}

	// returns value with 5 significant figures
	public double getNumlogP() {
		if(new Double(logP).isNaN()) {
			return logP;
		}
		else {
		BigDecimal bd = new BigDecimal(logP);
		bd = bd.round(new MathContext(5));
		double rounded = bd.doubleValue();
		return rounded;
		}
	}
	
	
}
