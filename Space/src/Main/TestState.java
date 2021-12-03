package Main;

import Rendering.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Misc.KeyManager;
import Misc.Mat;
import Misc.MatrixManipulation;
import static Misc.SDL.*;

public class TestState {
	class Point {
		int x,y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public TestState() {
		
	}
	
	BufferedImage img;
	double randA = 10;
	double[][][] raw;
	public void gen() {
		
		raw = new double[Launcher.window.width][Launcher.window.height][3];
		
		for(int z=0;z<100;z++) {
			int x = (int)(Math.random()*raw.length);
			int y = (int)(Math.random()*raw[0].length);
			raw[x][y][0] = Math.random()*255;
			raw[x][y][1] = Math.random()*255;
			raw[x][y][2] = Math.random()*255;
		}
		
		//Basic system
//		for(int x=0;x<raw.length;x++)
//			for(int y=0;y<raw[x].length;y++)
//				if(raw[x][y][0] == 0 && raw[x][y][1] == 0 && raw[x][y][2] == 0) {
//					double[] col = getAdvColorNear(raw, x, y);
//					raw[x][y][0] = Math.random()*(randA*2)-randA+col[0];
//					raw[x][y][1] = Math.random()*(randA*2)-randA+col[1];
//					raw[x][y][2] = Math.random()*(randA*2)-randA+col[2];
//				}
		
		//Find and generate
		ArrayList<Point> listOfPixels = new ArrayList<Point>();
		ArrayList<Point> listOfPixelsDone = new ArrayList<Point>();
		println("Adding pixels to list.");
		//Scan to find any points and add them to the list
		for(int x=0;x<raw.length;x++)
			for(int y=0;y<raw[x].length;y++) {
				boolean check = true;
				for(Point p:listOfPixelsDone)
					if(p.x == x && p.y == y) {
						check = false;
						break;
					}
				if(check && (raw[x][y][0] != 0 || raw[x][y][1] != 0 || raw[x][y][2] != 0))
					listOfPixels.add( new Point(x, y) );
			}
		while(!isDone()) {
			println("Generating new pixels.");
			//Randomize list and 
			for(int z=0;z<listOfPixels.size();z++) {
				if(Math.random() < .5) continue;
				Point p = listOfPixels.remove(z--);
				listOfPixelsDone.add( p );
//				println( "X:"+p.x+" Y:"+p.y );
				if(p.x > 0 && c(raw[p.x-1][p.y])) listOfPixels.add( generatePixel(p.x-1,p.y) );
				if(p.y > 0 && c(raw[p.x][p.y-1])) listOfPixels.add( generatePixel(p.x,p.y-1) );
				if(p.x < raw.length - 1 && c(raw[p.x+1][p.y])) listOfPixels.add( generatePixel(p.x+1,p.y) );
				if(p.y < raw[0].length - 1 && c(raw[p.x][p.y+1])) listOfPixels.add( generatePixel(p.x,p.y+1) );
			}
			
			println("Creating image.");
			//Compress raw in to image to display
			img = new BufferedImage(raw.length, raw[0].length, BufferedImage.TYPE_INT_RGB);
			for(int x=0;x<raw.length;x++)
				for(int y=0;y<raw[x].length;y++)
					setColor(img, x, y, raw[x][y][0], raw[x][y][1], raw[x][y][2]);
		}
	}
	public boolean isDone() {
		for(int x=0;x<raw.length;x++)
			for(int y=0;y<raw[x].length;y++)
				if(c(raw[x][y]))
					return false;
		return true;
	}
	public Point generatePixel(int x, int y) {
		double[] col = getAdvColorNear(raw, x, y);
		raw[x][y][0] = Math.random()*(randA*2)-randA+col[0];
		raw[x][y][1] = Math.random()*(randA*2)-randA+col[1];
		raw[x][y][2] = Math.random()*(randA*2)-randA+col[2];
		return new Point(x,y);
	}
	public boolean c(double[] list) {
		return list[0]==0 && list[1]==0 && list[2]==0;
	}
	public double[] getAdvColorNear(double[][][] raw, int x, int y) {
		double[] re = new double[4];
		if(x>0) re = add(re, raw[x-1][y]);
		if(y>0) re = add(re, raw[x][y-1]);
		if(x<raw.length-1) re = add(re, raw[x+1][y]);
		if(y<raw[0].length-1) re = add(re, raw[x][y+1]);
		for(int z=0;re[3] > 0 && z<re.length;z++) re[z] /= re[3];
		return re;
	}
	public static double[] add(double[] a, double[] b){
		if(b[0]==0 && b[1]==0 && b[2]==0) return a;
			a[0] += b[0];
			a[1] += b[1];
			a[2] += b[2];
			a[3] += 1;
		
		return a;
	}
	
	public int[] getColor(BufferedImage img, int x, int y) {
		int[] re = new int[3];
		int clr = img.getRGB(x, y);
		re[0] = (clr & 0x00ff0000) >> 16;
		re[1] = (clr & 0x0000ff00) >> 8;
		re[2] = clr & 0x000000ff;
		return re;
	}
	public void setColor(BufferedImage img, int x, int y, double red, double green, double blue) {
//		System.out.printf("%f  %f  %f%n",red,green,blue);
		int r = (int) Mat.inRange(red, 0, 255);
		int g = (int) Mat.inRange(green, 0, 255);
		int b = (int) Mat.inRange(blue, 0, 255);
		int col = (r << 16) | (g << 8) | b;
		img.setRGB(x, y, col);
	}
	public void tar(Graphics g) {
		if(KeyManager.keyRelease(KeyEvent.VK_SPACE)) {
			(new Thread() {
				public void run() {
					gen();
				}
			}).start();
		}
		g.drawImage( img );
	}
}
