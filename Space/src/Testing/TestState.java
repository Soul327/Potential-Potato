package Testing;

import Rendering.Graphics;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Main.Launcher;
import Misc.KeyManager;
import Misc.Mat;
import Misc.MatrixManipulation;
import static Misc.SDL.*;

public class TestState {
	// This class provites a simple way to store multiple variables in one
	class Point {
		int x,y;
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	BufferedImage img;
	double randA = 10;// Variation amount of each pixel, each pixel will vary in rgb color by this amount in the positive and negitive direction
	// This is a double due to rounding having a large effect on the slight varations in the colors
	double[][][] raw;// This is the raw image "file", it is a global varible so I don't have to pass it in to every function
	
	// Generates a new image are stores it in img and raw
	public void gen() {
		// Reset/initizes raw img file
		raw = new double[Launcher.window.width][Launcher.window.height][3];
		
		// Initilizes the raw image with points to start from set to random color and location
		for(int z=0;z<100;z++) {
			int x = (int)(Math.random()*raw.length);
			int y = (int)(Math.random()*raw[0].length);
			raw[x][y][0] = Math.random()*255;
			raw[x][y][1] = Math.random()*255;
			raw[x][y][2] = Math.random()*255;
		}
		
		ArrayList<Point> listOfPixels = new ArrayList<Point>();// List of pixel that are going to be rendered 
		ArrayList<Point> listOfPixelsDone = new ArrayList<Point>();// List of pixels that have previously been rendered
		println("Adding pixels to list.");
		//Scan to find any points and add them to the list, this only needs to run once
		for(int x=0;x<raw.length;x++)
			for(int y=0;y<raw[x].length;y++) {
				// Check if x,y is part of prevous pixels and ignore them. This is no longer need as this only runs once at the beguining when listOfPixelsDone is empty
				boolean check = true;
				for(Point p:listOfPixelsDone)
					if(p.x == x && p.y == y) {// This is never true?
						check = false;
						break;
					}
				// Add found point to list
				if(check && (raw[x][y][0] != 0 || raw[x][y][1] != 0 || raw[x][y][2] != 0))
					listOfPixels.add( new Point(x, y) );
			}
		
		while(!isDone()) {
			println("Generating new pixels.");
			//Randomize list and 
			for(int z=0;z<listOfPixels.size();z++) {
				if(Math.random() < .5) continue;// Randomly skips pixels to not make everything diamonds
				Point p = listOfPixels.remove(z--); listOfPixelsDone.add( p ); // Moves the pixel to the listOfPixelsDone
				
				// Generate pixels arround selected pixel, then add them to the listOfPixels
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
	
	// Check if the raw image has any cases where the colors are zero
	public boolean isDone() {
		for(int x=0;x<raw.length;x++)
			for(int y=0;y<raw[x].length;y++)
				if(c(raw[x][y]))
					return false;
		return true;
	}
	
	// Creates a color and sets it to x,y then returns the Point of the pixel to save myself time on other code
	public Point generatePixel(int x, int y) {
		double[] col = getAdvColorNear(raw, x, y);
		raw[x][y][0] = Math.random()*(randA*2)-randA+col[0];
		raw[x][y][1] = Math.random()*(randA*2)-randA+col[1];
		raw[x][y][2] = Math.random()*(randA*2)-randA+col[2];
		return new Point(x,y);
	}
	
	// Checks to see if the color list provided is not zero 
	public boolean c(double[] list) {
		return list[0]==0 && list[1]==0 && list[2]==0;
	}
	
	// Calculates and returns the advColor near the pixel chosen in raw then returns it in {r,g,b}
	public double[] getAdvColorNear(double[][][] raw, int x, int y) {
		double[] re = new double[4];
		if(x>0) re = add(re, raw[x-1][y]);
		if(y>0) re = add(re, raw[x][y-1]);
		if(x<raw.length-1) re = add(re, raw[x+1][y]);
		if(y<raw[0].length-1) re = add(re, raw[x][y+1]);
		for(int z=0;re[3] > 0 && z<re.length;z++) re[z] /= re[3];
		return re;
	}
	
	// Adds the color array (b) to (a) by adding the first three then incrementing the fourth var in (a)
	public static double[] add(double[] a, double[] b){
		if(b[0]==0 && b[1]==0 && b[2]==0) return a;
			a[0] += b[0];
			a[1] += b[1];
			a[2] += b[2];
			a[3] += 1;
		
		return a;
	}
	
	// Sets the color of a pixel in a bufferedImage
	public void setColor(BufferedImage img, int x, int y, double red, double green, double blue) {
//		System.out.printf("%f  %f  %f%n",red,green,blue);
		int r = (int) Mat.inRange(red, 0, 255);
		int g = (int) Mat.inRange(green, 0, 255);
		int b = (int) Mat.inRange(blue, 0, 255);
		int col = (r << 16) | (g << 8) | b;
		img.setRGB(x, y, col);
	}
	
	//Tick and Render, called from Launcher
	public void tar(Graphics g) {
		// When space is pressed and released a new thread will start the generation. There is no check to see if the thread is running, yes this is an issue
		if(KeyManager.keyRelease(KeyEvent.VK_SPACE)) {
			(new Thread() {
				public void run() {
					gen();
				}
			}).start();
		}
		
		// Draws image to the screen in native resolution, if img is null this is skiped without error
		g.drawImage( img );
	}
}
