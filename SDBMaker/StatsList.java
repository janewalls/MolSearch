import java.util.ArrayList;

/*
 * Class that makes an array list of Stats objects
 */

public class StatsList {
	private ArrayList<Stats> statList;
	private int listSize;
	
	// creates array
	public StatsList() {
		statList = new ArrayList<Stats>();
		listSize = 0;
	}
	// adds objects to list
	public void addStats(Stats stat) {
		statList.add(stat);
		listSize++;
	}
	// returns an object at a position
	public Stats getStat(int pos) {
		return statList.get(pos);
	}
	
	public int getSize() {
		return listSize;
	}
}
