package Testing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import Main.Launcher;
import Misc.KeyManager;
import Misc.Mat;
import Rendering.Graphics;

public class WorldState {
	Tile[][] globalTiles;
	
	int cameraX = 0, cameraY = 0;
	int offsetX = 1, offsetY = 0;
	int renderLimit = 4+1;
	Tile[][] loadedTiles;
	
	public WorldState() {
		init();
	}
	public void init() {
		loadedTiles = new Tile[renderLimit][renderLimit];
		globalTiles = new Tile[renderLimit*2][renderLimit*2];
		
		for(int x=0;x<globalTiles.length;x++) {
			double[] color = new double[3];
			color[0] = Math.random()*255;
			color[1] = Math.random()*255;
			color[2] = Math.random()*255;
			for(int y=0;y<globalTiles[x].length;y++) {
				globalTiles[x][y] = new Tile(color, x ,y);
			}
		}
	}
	public void tar(Graphics g) {
		g.setFont("Courier New", Font.PLAIN, 15);
		int tileSize = 64;
		// Get center of render screen
		int cx = Launcher.window.width/2 - (tileSize*loadedTiles.length)/2;
		int cy = Launcher.window.height/2 - (tileSize*loadedTiles[0].length)/2;
		
		//Render global tiles
		if(true)
			for(int x=0;x<globalTiles.length;x++)
				for(int y=0;y<globalTiles[x].length;y++) {
					if(globalTiles[x][y] == null) continue;
					double rx = (x+cameraX-offsetX)*tileSize+cx;
					double ry = (y+cameraY-offsetY)*tileSize+cy;
					g.setColor( new Color(100,100,100) );
					g.outlineRect(rx, ry, tileSize, tileSize);
//					g.drawOutlinedString("("+x+","+y+")", rx, ry+g.fontSize);
				}
		for(int x=0;x<loadedTiles.length;x++)
			for(int y=0;y<loadedTiles[x].length;y++) {
				Tile t = loadedTiles[x][y];
				if(t == null) continue;
				double rx = (x+cameraX)*tileSize+cx;
				double ry = (y+cameraY)*tileSize+cy;
				g.setColor( new Color((int)t.color[0],(int)t.color[1],(int)t.color[2]) );
				g.outlineRect(rx, ry, tileSize, tileSize);
				g.drawOutlinedString("("+(x+offsetX)+","+(y+offsetY)+")", rx, ry+g.fontSize);
				g.drawOutlinedString(""+(int)loadedTiles[x][y].id, rx, ry+g.fontSize*2);
			}
		
		g.setColor(new Color(255,0,0));
		g.drawRect(cx, cy, tileSize*renderLimit, tileSize*renderLimit);
		
		g.setColor( new Color(255,0,0) );
		g.fillCenterCircle(Launcher.window.width/2, Launcher.window.height/2, 5);
		
		//Move
		if(KeyManager.keyRelease(KeyEvent.VK_W)) { offsetY--; }
		if(KeyManager.keyRelease(KeyEvent.VK_S)) { offsetY++; }
		if(KeyManager.keyRelease(KeyEvent.VK_A)) { offsetX--; }
		if(KeyManager.keyRelease(KeyEvent.VK_D)) { offsetX++; }
		
		//Wrap player arround
		g.drawOutlinedString("offsetX: "+offsetX+" offsetY: "+offsetY, 5, g.fontSize*1);
		g.drawOutlinedString("Global Tile Length: "+globalTiles.length, 5, g.fontSize*2);
		if(offsetX > globalTiles.length - renderLimit/2-1) offsetX = -renderLimit/2;
		if(offsetX < -renderLimit/2) offsetX = globalTiles.length - renderLimit/2-1;
		if(offsetY > globalTiles[offsetX+renderLimit/2].length - renderLimit/2-1) offsetY = -renderLimit/2;
		if(offsetY < -renderLimit/2) offsetY = globalTiles[offsetX+renderLimit/2].length - renderLimit/2-1;
		
		//Load tiles
		for(int x=0;x<loadedTiles.length;x++)
			for(int y=0;y<loadedTiles[x].length;y++) {
				loadedTiles[x][y] = null;
				try {
					if(x+offsetX < 0) {
						int lx = globalTiles.length- 1;
						loadedTiles[x][y] = globalTiles[lx][y+offsetY];
						continue;
					}
					if(y+offsetY < 0) continue;
					if(x+offsetX >= globalTiles.length) {
						continue;
					}
					if(y+offsetY >= globalTiles[x].length) continue;
					loadedTiles[x][y] = globalTiles[x+offsetX][y+offsetY];
				} catch(Exception e) {}
			}
	}
}
