import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.TopologyAnalyserPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;

public class RingAndRot {
	private int ringCount;
	private int rotCount; // Rotatable bond count 
	private int fusedAromaticRingCount;
	
	public RingAndRot(String molecule) {
		this.calcRR(molecule);
	}
	
	public void calcRR(String molecule) {
		System.out.println("rr");
		// read input molecule
	  try {
		MolImporter mi = null;
	    Molecule mol = mi.importMol(molecule);
	

	    // create plugin
	    TopologyAnalyserPlugin plugin = new TopologyAnalyserPlugin();
	    
	    try {
	    // set target molecule
	    plugin.setMolecule(mol);

	    // run the calculation
	    plugin.run();
	    }catch(PluginException e){
	    	System.out.println("Plugin trouble");
	    }
	    // get molecular results
	    int ringCount = plugin.getRingCount();
	    int rotatableBondCount = plugin.getRotatableBondCount();
	    int fusedAromaticRingCount = plugin.getAromaticRingCount();
	    
	    this.ringCount = ringCount;
	    rotCount = rotatableBondCount;
	    this.fusedAromaticRingCount = fusedAromaticRingCount;
	    
	  }catch(MolFormatException e) {
	    	System.out.println("Unable to calc ring count or rotatable bond");
	  }	
	}
	
	public int getNumRing() {
		return ringCount;
	}
	
	public int getNumRot() {
		return rotCount;
	}
	
	public int getNumAromatic() {
		return fusedAromaticRingCount;
	}
}
