package Main;

import java.util.ArrayList;

import FFS.FFS;
import FFS.Obj;
import Rendering.Graphics;

public class GameState {
	FFS data = new FFS();
	ArrayList<SolarSystem> systems;
	
	public GameState() {
		init();
	}
	
	public void init() {
		systems = new ArrayList<SolarSystem>();
		
		for(int z=0;z<2;z++)
			systems.add( new SolarSystem() );
		
		save();
//		System.exit(0);
	}
	
	public void load() {}
	
	public void save() {
		data.set("static.SolarSystem.sid", new Obj( SolarSystem.sid ));
		data.set("static.World.sid", new Obj( World.sid ));
		for(SolarSystem sol:systems) {
			data.set("StarSystems."+sol.name+".id", new Obj( sol.id ));
			for(World world:sol.worlds) {
				String sw = "StarSystems."+sol.name+".worlds."+world.name;
				data.set( sw+".id", new Obj( world.id ));
				data.set( sw+".sx", new Obj( world.sx ));
				data.set( sw+".sy", new Obj( world.sy ));
				data.set( sw+".svx", new Obj( world.svx ));
				data.set( sw+".svy", new Obj( world.svy ));
				data.set( sw+".mass", new Obj( world.mass ));
				data.set( sw+".width", new Obj( world.tiles.length ));
				data.set( sw+".height", new Obj( world.tiles[0].length ));
				// Entities
				for(Entity e:world.entities) {
//					data.set( sw+".entities." , null);
				}
				// Tiles
				for(int x=0;x<world.tiles.length;x++)
					for(int y=0;y<world.tiles[x].length;y++) {
						Tile tile = world.tiles[x][y];
						// List by id
//						data.set( sw+".tiles."+tile.id+".x" , new Obj( tile.x ));
//						data.set( sw+".tiles."+tile.id+".y" , new Obj( tile.y ));
//						data.set( sw+".tiles."+tile.id+".temp" , new Obj( tile.temp ));
						
						// List by Location
						data.set( sw+".tiles."+tile.x+"."+tile.y+".temp" , new Obj( tile.temp ));
					}
			}
		}
		data.print();
	}
	
	public void tar(Graphics g) {
		systems.get(0).tar(g);
	}
	
}
