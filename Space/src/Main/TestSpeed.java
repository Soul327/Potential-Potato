package Main;

import java.util.ArrayList;

public class TestSpeed {
	public static void main(String args[]) {
		long[][] times = new long[2][2];
	}
	public static void store1(int amt) {
		ArrayList<Double> list = new ArrayList<Double>();
		for(int z=0;z<amt;z++)
			list.add(Math.random() );
		
		for(int z=0;z<amt/2;z++)
			list.get( (int)(Math.random()*amt));
	}
}
