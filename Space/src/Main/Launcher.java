package Main;

import java.awt.Color;

import Events.SimpleWindowEvent;
import Misc.SimpleWindow;
import Rendering.Graphics;

public class Launcher implements SimpleWindowEvent {
	//Set up the window
	static SimpleWindow window;
	
	//Creates states
	TestState testState;
	
	//Main class starts the function and creates a new object instance of Launcher
	public static void main(String[] args) {
		new Launcher();
		
	}
	
	//Initializer of Launcher
	public Launcher() {
		//Creates and starts the window thread
		window = new SimpleWindow();
		// This add this instance of Launcher to the list, allowing for the window to respond to the tar(Graphics g) function
		window.addSimpleWindowEvent(this);
		window.start();
		testState = new TestState();
	}
	
	// Stored number for the current state, some examples state are main menu, settings, game window, and map
	int currentStateNumber = 0;
	
	// tar(Graphics g) stands for tick and render
	// This will render the graphics to the screen and handle all processing
	// This will run at a set frame rate dictated in simple window, default is 60fps
	public void tar(Graphics g) {
		// This will choose the correct case compairing against currentStateNumber
		switch(currentStateNumber) {
			case 0: testState.tar(g); break;
			default:
				// String are drawn using their bottom left corner, therefore in order to draw on top left of screen
				g.drawOutlinedString("Missing window state", 5, g.fontSize);
		}
	}
}
