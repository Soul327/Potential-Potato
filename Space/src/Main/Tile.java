package Main;

import java.util.HashMap;
import java.util.Map;

public class Tile {
	int id = 0;
	static int sid = 0;
	final int x,y;
	double temp = 10;
	
	Map<String, Double> resources = new HashMap<String, Double>();
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		sid++;
		id = sid;
		temp = Math.random()*100;
	}
}
