package Main;

import java.awt.Color;
import java.util.ArrayList;

import Misc.KeyManager;
import Misc.MouseManager;
import Rendering.Graphics;
import java.awt.event.KeyEvent;

public class World {
	double sx = 0, sy = 0;// Location is the system
	double svx = 0, svy = 0; // Velocity in the system
	int size = 10; // Size of the world, does not necessarily indicative the amount of tiles
	double mass = size * 5000;
	Color colorOfPlanetFromSpace = new Color(50,220,75);
	
	double camX = 0, camY = 0;
	double tileDrawSize = 80;
	
	Tile[][] tiles;
	ArrayList<Entity> entities;
	
	// Initializer
	public World() {
		init();
	}
	public World(int x, int y, int vx, int vy) {
		sx = x; sy = y;
		svx = vx; svy = vy;
		init();
	}
	
	public void init() {
		tiles = new Tile[size*3][size];
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[x].length;y++)
				tiles[x][y] = new Tile(x,y);
		
		entities = new ArrayList<Entity>();
		
//		for(int z=0;z<10;z++) {
//			Entity friend = new Slave();
//			friend.xPos = Math.random() * tiles.length;
//			friend.yPos = Math.random() * tiles[0].length;
//			entities.add(friend);
//		}
	}
	
	// This tick will update everything on the word
	public void gameTick() {
		// Cycles though all tiles from top left, the all the way left of the screen then procedes downwards
		for(int x=0;x<tiles.length;x++) {
			for(int y=0;y<tiles[x].length;y++) {
				Tile tile = tiles[x][y];
				tickTemp( tile );
			}
		}
		for (int x=0; x<entities.size(); x++) {
			entities.get(x).gameTick();
		}
	}
	
	// This function will average the tiles around the tile given then set the temperature to the tile
	public void tickTemp(Tile tile) {
		Tile[][] adjacentTiles = getAdjacentTiles(tile.x,tile.y);
		
		int amount = 0;
		double sum = 0;
		for(int lx=0;lx<adjacentTiles.length;lx++)
			for(int ly=0;ly<adjacentTiles[lx].length;ly++)
				if(adjacentTiles[lx][ly]!=null) {
					sum += adjacentTiles[lx][ly].temp;
					amount++;
				}
		
		double avg = sum / amount;
		tile.temp = avg;
	}

	// Returns adjacent tiles to the given tiles then returns them
	public Tile[][] getAdjacentTiles(int lx, int ly) {
		Tile[][] reTiles = new Tile[3][3];
		reTiles[0][0] = getTile(lx-1,ly-1);
		reTiles[1][0] = getTile(lx,ly-1);
		reTiles[2][0] = getTile(lx+1,ly-1);
		reTiles[0][1] = getTile(lx-1,ly);
		reTiles[2][1] = getTile(lx+1,ly);
		reTiles[0][2] = getTile(lx-1,ly+1);
		reTiles[1][2] = getTile(lx,ly+1);
		reTiles[2][2] = getTile(lx+1,ly+1);
		return reTiles;
	}
	
	// Returns tiles at the location if the tile is not null and in bounds, else returns null
	Tile getTile(int x, int y) {
		try {
			if(x>0 && y>0 && x<tiles.length-1 && y<tiles[x].length-1)
				return tiles[x][y];
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static double mouseOnTileX = 0, mouseOnTileY = 0;

	// tar, tick and render
	public void tar(Graphics g) {
		mouseOnTileX = (MouseManager.mouseX-camX)/tileDrawSize;
		mouseOnTileY = (MouseManager.mouseY-camY)/tileDrawSize;
		if(mouseOnTileX < 0)
			mouseOnTileX = tiles.length + mouseOnTileX;
		if(mouseOnTileX > tiles.length)
			mouseOnTileX -= tiles.length;
		
		// Controls
		// getKey returns true only when pressed
		double camMoveSpeed = tileDrawSize * .01;
		if(KeyManager.getKey(KeyEvent.VK_W)) camY += camMoveSpeed;
		if(KeyManager.getKey(KeyEvent.VK_S)) camY -= camMoveSpeed;
		if(KeyManager.getKey(KeyEvent.VK_A)) camX += camMoveSpeed;
		if(KeyManager.getKey(KeyEvent.VK_D)) camX -= camMoveSpeed;
		
		if(KeyManager.getKey(KeyEvent.VK_E)) tileDrawSize+= .1;
		if(KeyManager.getKey(KeyEvent.VK_Q) && tileDrawSize > 5) tileDrawSize-= .1;
		
		// keyRelease return true only when the key is let go after being pressed, good for typing
		//if(KeyManager.keyRelease(KeyEvent.VK_SPACE)) gameTick();
		if(KeyManager.getKey(KeyEvent.VK_SPACE)) gameTick();
		
		tarTiles(g);
		
		// Rendering entities (Rayna)
		g.setColor( new Color(255, 255, 255));
		for(int x=0; x < entities.size(); x++) {
			double dx = (entities.get(x).xPos * tileDrawSize) + camX;
			double dy = (entities.get(x).yPos * tileDrawSize) + camY;
			
			// Checks if the entity is rendering on the screen, if it is not, continue
			if(dx < -tileDrawSize || dy < -tileDrawSize || dx > g.width || dy > g.height)
				continue;
			
			// Renders entity
			g.fillCenterCircle(dx, dy, 10);
		}
		tarDebug(g);
	} // End of tar(Graphics g)
	
	public void tarTiles(Graphics g) {
		// Rendering tiles
		if(camX > g.width)
			camX = -tiles.length*tileDrawSize + g.width;
		if(camX < -(tiles.length * tileDrawSize) )
			camX = 0;
		
		// Draw Primary
		for(int x=0;x<tiles.length;x++)
			for(int y=0;y<tiles[x].length;y++) {
				// Create and set draw locations for later use
				double dx = x*tileDrawSize+camX;
				double dy = y*tileDrawSize+camY;
				
				// Checks if the tile is rendering on the screen, if it is not, continue
				if(dx < -tileDrawSize || dy < -tileDrawSize || dx > g.width || dy > g.height)
					continue;
				
				// Draw rectangle of the tile
				g.drawRect(dx, dy, tileDrawSize, tileDrawSize);
				// Draws the temp data on the tile
//					g.drawOutlinedString( "" + (int)tiles[x][y].temp, dx+2, dy+g.fontSize);
				g.drawOutlinedString( "X:" + (int)tiles[x][y].x+" Y:" + (int)tiles[x][y].y, dx+2, dy+g.fontSize);
			}
		
	// If camera can see to the left of the primary area, draw map again on the left
		if(camX > 0) {
			for(int x=(int)(tiles.length - g.width/tileDrawSize);x<tiles.length;x++)
				for(int y=0;y<tiles[x].length;y++) {
					g.setColor( new Color(255,0,0) );
					// Create and set draw locations for later use
					double dx = x*tileDrawSize+camX-tileDrawSize*tiles.length;
					double dy = y*tileDrawSize+camY;
					
					g.drawRect(dx, dy, tileDrawSize, tileDrawSize);
					g.drawOutlinedString( "X:" + (int)tiles[x][y].x+" Y:" + (int)tiles[x][y].y, dx+2, dy+g.fontSize);
				}
		}
		
	// If camera can see to the right of the primary area, draw map again on the right
		if(camX < tiles.length * tileDrawSize) {
			for(int x=0;x<g.width/tileDrawSize;x++)
				for(int y=0;y<tiles[x].length;y++) {
					g.setColor( new Color(0,0,255) );
					// Create and set draw locations for later use
					double dx = x*tileDrawSize+camX+tileDrawSize*tiles.length;
					double dy = y*tileDrawSize+camY;
					
					g.drawRect(dx, dy, tileDrawSize, tileDrawSize);
					g.drawOutlinedString( "X:" + (int)tiles[x][y].x+" Y:" + (int)tiles[x][y].y, dx+2, dy+g.fontSize);
				}
		}
	}
	
	public void tarDebug(Graphics g) {
		// Draw debug info
		int debugInfoLine = 0;
		g.drawOutlinedString( "X:"+mouseOnTileX + " Y:"+mouseOnTileY, 0, g.fontSize*++debugInfoLine);
		g.drawOutlinedString( "CamX:"+camX+" CamY:"+camY, 0, g.fontSize*++debugInfoLine);
	}
}
