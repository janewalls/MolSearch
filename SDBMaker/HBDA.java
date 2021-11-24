import java.io.IOException;

import chemaxon.formats.MolImporter;
import chemaxon.marvin.calculations.HBDAPlugin;
import chemaxon.marvin.calculations.MajorMicrospeciesAccessorPlugin;
import chemaxon.marvin.plugin.PluginException;
import chemaxon.struc.Molecule;
/*
 * Class to calculate H Bond Donor/Acceptor
 */
public class HBDA extends MajorMicrospeciesAccessorPlugin {
	private int acceptor = 0;
	private int donor = 0;
	
	public HBDA(String molecule) {
		this.HBDAcalc(molecule);
	}
	
	public void HBDAcalc(String molecule) {
		System.out.println("HBDA");
		 // create plugin
		 HBDAPlugin plugin = new HBDAPlugin();
		 
		 // set plugin parameters
		 plugin.setDoublePrecision(2);
		 plugin.setpHLower(2.0);
		 plugin.setpHUpper(12.0);
		 plugin.setpHStep(2.0);
		 
		 plugin.setpH(7.4);
		 
		 try {
		 // read target molecule
		 MolImporter mi = null;
		 Molecule mol = mi.importMol(molecule);

		 try {
		 // set target molecule
		 plugin.setMolecule(mol);
		 
		 // run the calculation
		 plugin.run();
		 }catch(PluginException e) {
			 
		 }
		 // molecular data
		 // with multiplicity
		 int molecularAcceptorCount = plugin.getAcceptorSiteCount();
		 int molecularDonorCount = plugin.getDonorSiteCount();
		
		 this.acceptor = molecularAcceptorCount;
		 this.donor = molecularDonorCount;
		 
		 

		 }catch(IOException e) {
			 System.out.println("issue with HBDA");
		 }
	}
	
	public HBDA getAcceptDon() {
		return this;
	}
	
	public int getAcceptor() {
		return acceptor;
	}
	
	public int getDonor() {
		return donor;
	}

	@Override
	public String getProductName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean run() throws PluginException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void setInputMolecule(Molecule arg0) throws PluginException {
		// TODO Auto-generated method stub
		
	}
}
