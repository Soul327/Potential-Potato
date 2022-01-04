package Main;

public class Slave extends Entity {
	double speed = 1;
	
	public void gameTick() {
		if (Math.abs(World.mouseOnTileX - xPos) < speed) {
			xPos = World.mouseOnTileX;
		}
		else if (xPos < World.mouseOnTileX) {
			xPos += speed;
		}
		else if (xPos > World.mouseOnTileX) {
			xPos -= speed;
		}
		
		
		if (Math.abs(World.mouseOnTileY - yPos) < speed) {
			yPos = World.mouseOnTileY;
		} 
		else if (yPos < World.mouseOnTileY) {
			yPos += speed;
		}
		else if (yPos > World.mouseOnTileY) {
			yPos -= speed;
		}
		
	}
}
