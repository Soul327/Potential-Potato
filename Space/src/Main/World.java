package Main;

import java.awt.Color;
import java.util.ArrayList;

import Misc.KeyManager;
import Rendering.Graphics;
import java.awt.event.KeyEvent;

public class World {
	int camX = 0, camY = 0;
	double tileDrawSize = 40;
	
	Tile[][] tiles;
	ArrayList<Entity> entities;
	
	public World() {
		int size = 100;
		tiles = new Tile[size*3][size];
		for(int x=0;x<tiles.length;x++) {
			for(int y=0;y<tiles[x].length;y++) {
				tiles[x][y] = new Tile(x,y);
			}
		}
		
		entities = new ArrayList<Entity>();
	}
	
	public void gameTick() {
		for(int x=0;x<tiles.length;x++) {
			for(int y=0;y<tiles[x].length;y++) {
				
			}
		}
	}
//	
//	public Tile[][] getAdjacentTiles(int lx, int ly) {
//		Tile[][] reTiles= new Tile[3][3];
//		reTiles[][] = getTile(x,y);
//		return reTiles;
//	}
	Tile getTile(int x, int y) {
		try {
			if(x>0 && y>0 && x<tiles.length && y<tiles.length)
				return tiles[x][y];
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void tar(Graphics g) {
		if(KeyManager.getKey(KeyEvent.VK_W)) camY++;
		if(KeyManager.getKey(KeyEvent.VK_S)) camY--;
		if(KeyManager.getKey(KeyEvent.VK_A)) camX++;
		if(KeyManager.getKey(KeyEvent.VK_D)) camX--;
		
		if(KeyManager.getKey(KeyEvent.VK_E)) tileDrawSize+= .1;
		if(KeyManager.getKey(KeyEvent.VK_Q) && tileDrawSize > 5) tileDrawSize-= .1;
		
		if(KeyManager.keyRelease(KeyEvent.VK_SPACE)) gameTick();
		
		g.setColor( new Color(255,255,255) );
		for(int x=0;x<tiles.length;x++) {
			for(int y=0;y<tiles[x].length;y++) {
				double dx = x*tileDrawSize+camX;
				double dy = y*tileDrawSize+camY;
				
				if(dx < 0 || dy < 0 || dx > Launcher.window.width || dy > Launcher.window.height)
					continue;
				
				g.drawRect(dx, dy, tileDrawSize, tileDrawSize);
				g.drawOutlinedString( "" + (int)tiles[x][y].temp, dx+2, dy+g.fontSize);
			}
		}
	}
	
}
