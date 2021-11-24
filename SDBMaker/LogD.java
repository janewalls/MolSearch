import java.math.BigDecimal;
import java.math.MathContext;

import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.logDPlugin;
import chemaxon.struc.Molecule;
import chemaxon.marvin.plugin.PluginException;


public class LogD {
	private double logD;
	
	public LogD(String molecule) {
		this.calcLogD(molecule);
	}
	
	public void calcLogD(String molecule) {
        // instantiate plugin
        logDPlugin plugin = new logDPlugin();

        // set pH
      plugin.setpH(7.4);

      try {
  		MolImporter mi = null;
  	    Molecule target = null;
  	   
  	  if((target = mi.importMol(molecule)) != null) {
  		try {
  		  // set the input molecule
  		  plugin.setMolecule(target);

  		  // run the calculation
  		  plugin.run();

  		  // get result
  		  double logD = plugin.getlogD();
  		  
  		  this.logD = logD;
  		  
  		}catch(PluginException e) {	}
  	  }
      	}catch(MolFormatException e) {	} 
      
	}
	// returns value with 5 significant figures
	public double getNumlogD() {
		if(new Double(logD).isNaN()) {
			return logD;
		}
		else {
		BigDecimal bd = new BigDecimal(logD);
		bd = bd.round(new MathContext(5));
		double rounded = bd.doubleValue();
		return rounded;
		}
	}

}
