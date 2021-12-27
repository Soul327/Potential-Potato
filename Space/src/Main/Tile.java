package Main;

public class Tile {
	final int x,y;
	double temp = 10;
	
	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
		temp = Math.random()*100;
	}
}
