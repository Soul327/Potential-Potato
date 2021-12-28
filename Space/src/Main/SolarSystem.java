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
		World world;
		world = new World(0,0,0,0);
		world.mass = 5.9722 * Math.pow(10, 24);
		worlds.add( world );
		
		//The moon
		world = new World(0,384_400_000,1_022,0);
		world.mass = 7.342 * Math.pow(10, 22);
		world.colorOfPlanetFromSpace = new Color(150,150,150);
//		world.mass /= 4;
		worlds.add( world );
		
		(new Thread() {
			public void run() {
				int maxFPS = 500_000;
				double timePerTick = 1000000000 / maxFPS, delta = 0;
				long now, lastTime = System.nanoTime(), timer = 0;
				while(true){
					now = System.nanoTime();
					delta += (now - lastTime) / timePerTick;
					timer += now - lastTime;
					lastTime = now;
					if(delta >= 1) {
						gameTick();
						delta--;
					}
					if(timer >= 1000000000) {
						timer = 0;
					}
				}
			}
		}).start();
	}
	public void gameTick() {
		for(World world:worlds) {
			for(World w2:worlds) {
				if(w2 == world) continue;
				double angle = Mat.getAngle(w2.sx, w2.sy, world.sx, world.sy);
				double distance = Mat.distance(w2.sx, w2.sy, world.sx, world.sy);
				double force = G * ( (world.mass * w2.mass) / Math.pow(distance, 2) );
				double speed = force / world.mass;
				world.svx -= speed * Math.cos( angle );
				world.svy -= speed * Math.sin( angle );
//				System.out.printf("VX:%3.1f  VY:%3.1f Angle:%f Distance:%f Force:%f%n", world.svx, world.svy, angle, distance, force);
			}
			
			world.sx += world.svx;
			world.sy += world.svy;
//			System.out.printf("X:%f Y:%f%n",world.sx, world.sy);
//			break;
		}
	}
	public void tar(Graphics g) {
		double zoomAmount = .1;
		if(KeyManager.getKey(KeyEvent.VK_Q)) zoom += zoomAmount;
		if(KeyManager.getKey(KeyEvent.VK_E) && zoom>1) zoom -= zoomAmount;
		
//		if(KeyManager.getKey(KeyEvent.VK_SPACE))
//			gameTick();
		
		for(World world:worlds) {
			g.setColor( world.colorOfPlanetFromSpace );
			double ss = Math.pow(10, 6);
//			double ss = Math.pow(10, 23);
			double s = 10_000_000;
			
			double dx = world.sx/ss+Launcher.window.width/2;
			double dy = world.sy/ss+Launcher.window.height/2;
//			System.out.printf("X:%f Y:%f%n", dx, dy);
			g.fillCenterCircle( dx, dy, 20);
			g.setColor( new Color(255,0,0) );
		}
	}
}
