package Main;

public class Slave extends Entity {
	double speed = 1;
	
	public Slave(World homeWorld) {
		this.homeWorld = homeWorld;
	}

	public void gameTick() {
		int maxX = homeWorld.tiles.length;
		int maxY = homeWorld.tiles[0].length;
		
		// This is broken. Entity goes brrrr and then disappears (Rayna | 1/10/2021)
		if ((World.mouseOnTileX > maxX) || (World.mouseOnTileX < 0)) {
			return;
		}
		else if (Math.abs(World.mouseOnTileX - xPos) < speed) {
			xPos = World.mouseOnTileX;
		}
		else if (xPos < World.mouseOnTileX) {
			// I'm an idiot 
			if (World.cameraPos > 0) {
				xPos -= speed;
			}
			else {
				xPos += speed;
			}
		}
		else if (xPos > World.mouseOnTileX) {
			// I'm also an idiot here
			if (World.cameraPos < World.tileMath) {
				xPos += speed;
			}
			else {
				xPos -= speed;
			}
		}
		
		if ((World.mouseOnTileY > maxY) || (World.mouseOnTileY < 0)) {
			return;
		}
		else if (Math.abs(World.mouseOnTileY - yPos) < speed) {
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
