package Main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Misc.KeyManager;
import Misc.Mat;
import Rendering.Graphics;

public class SolarSystem {
	static final double G = 6.674 * Math.pow(10, -10); // Gravitational Constant
	double zoom = 1;
	ArrayList<World> worlds = new ArrayList<World>();
	
	
	String name = "System";
	static int sid = 0;
	int id = 0;
	
	public SolarSystem() {
		id = sid++;
		name += " "+id;
		
		example();
		
		(new Thread() {
			public void run() {
				int maxFPS = (60*60*12);
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
	
	public void example() {
		World world;
		world = new World(0,0,0,0);
		world.mass = 5.9722 * Math.pow(10, 24);
		worlds.add( world );
		
		//The moon
		world = new World(0,384_400_000,1_022,0);
		world.mass = 7.342 * Math.pow(10, 22);
		world.colorOfPlanetFromSpace = new Color(150,150,150);
		worlds.add( world );
		
//		//The moon2
//		world = new World(0,-384_400_000,-1_022,0);
//		world.mass = 7.342 * Math.pow(10, 22);
//		world.colorOfPlanetFromSpace = new Color(150,150,150);
//		worlds.add( world );
//		
//		//The moon3
//		world = new World(-384_400_000, 0, 0, 1_022);
//		world.mass = 7.342 * Math.pow(10, 22);
//		world.colorOfPlanetFromSpace = new Color(150,150,150);
//		worlds.add( world );
//		
//		//The moon4
//		world = new World(384_400_000, 0, 0, -1_022);
//		world.mass = 7.342 * Math.pow(10, 22);
//		world.colorOfPlanetFromSpace = new Color(150,150,150);
//		worlds.add( world );
	}
	public void example2() {
		World world;
		int z = 1_022/8;
		//The moon
		world = new World(0,384_400_000,z,0);
		world.mass = 7.342 * Math.pow(10, 22);
		world.colorOfPlanetFromSpace = new Color(150,150,150);
		worlds.add( world );
		
		//The moon2
		world = new World(0,-384_400_000,-z,0);
		world.mass = 7.342 * Math.pow(10, 22);
		world.colorOfPlanetFromSpace = new Color(150,150,150);
		worlds.add( world );
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
			}
			
			world.sx += world.svx;
			world.sy += world.svy;
		}
	}
	public void tar(Graphics g) {
		double zoomAmount = 1;
		if(KeyManager.getKey(KeyEvent.VK_Q)) zoom += zoomAmount;
		if(KeyManager.getKey(KeyEvent.VK_E) && zoom>1) zoom -= zoomAmount;
		
//		if(KeyManager.getKey(KeyEvent.VK_SPACE))
//			gameTick();
		
		for(World world:worlds) {
			g.setColor( world.colorOfPlanetFromSpace );
			double ss = Math.pow(10, 6.5);
//			double ss = Math.pow(10, 23);
			double s = 10_000_000;
			
			double dx = world.sx/ss+g.width/2;
			double dy = world.sy/ss+g.height/2;
//			System.out.printf("X:%f Y:%f%n", dx, dy);
			g.fillCenterCircle( dx, dy, 20);
			g.setColor( new Color(255,0,0) );
		}
	}
}
