/*
 * Class to send molecule to calculate parameters, and add them into a list
 * 
 * 2598002w
 */
public class Calculator {

	private StatsList sList;


	public Calculator(String molecule, StatsList sList) {
		this.sList = sList;
		calc(molecule);
	}
	
	public void calc(String molecule) {
		// Sends molecule for calculations
		Stats calcMolecule = new Stats(molecule);
		
		// Stores molecule in list
		sList.addStats(calcMolecule);
	
	}
}
