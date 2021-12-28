package Main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Misc.KeyManager;
import Misc.Mat;
import Rendering.Graphics;

public class SolarSystem {
	static double G = 6.674 * Math.pow(10, -10); // Gravitational Constant
	double zoom = 1;
	ArrayList<World> worlds = new ArrayList<World>();
	
	public SolarSystem() {
		worlds.add( new World() );
		World world = new World(-100,-100,1,0);
//		world.mass /= 4;
		worlds.add( world );
	}
	public void gameTick() {
		for(World world:worlds) {
			for(World w2:worlds) {
				if(w2 == world) continue;
				double angle = Mat.getAngle(w2.sx, w2.sy, world.sx, world.sy);
				double distance = Mat.distance(w2.sx, w2.sy, world.sx, world.sy);
				double force = G * ( (world.mass * w2.mass) / Math.pow(distance, 2) );
				world.svx -= force * Math.cos( angle );
				world.svy -= force * Math.sin( angle );
//				System.out.printf("VX:%3.1f  VY:%3.1f Angle:%f Distance:%f Force:%f%n", world.svx, world.svy, angle, distance, force);
			}
			
			world.sx += world.svx;
			world.sy += world.svy;
//			break;
		}
	}
	public void tar(Graphics g) {
		double zoomAmount = .1;
		if(KeyManager.getKey(KeyEvent.VK_Q)) zoom += zoomAmount;
		if(KeyManager.getKey(KeyEvent.VK_E) && zoom>1) zoom -= zoomAmount;
		
		if(KeyManager.getKey(KeyEvent.VK_SPACE)) gameTick();
		
		for(World world:worlds) {
			g.setColor( world.colorOfPlanetFromSpace );
			g.fillCenterCircle(world.sx+Launcher.window.width/2, world.sy+Launcher.window.height/2, 20);
		}
	}
}
