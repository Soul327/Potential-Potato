package Testing;

public class Tile {
	int id = 0;
	static int sid = 0;
	int x,y;
	
	double temp = Math.random()*100;
	double[] color;
	
	public Tile(double[] color, int x, int y) {
		id = sid++;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	public Tile() {
		color = new double[3];
		color[0] = Math.random()*255;
		color[1] = Math.random()*255;
		color[2] = Math.random()*255;
	}
}
