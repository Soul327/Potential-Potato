package Testing;

import java.util.ArrayList;

import Converter.TimeConverter;

public class TestSpeed {
	public static void main(String args[]) {
		long startTime = System.nanoTime();
		long[][] times = new long[2][2];
		
		for(int zz=0;zz<1_000_000;zz++) {
			for(int z=0;z<1_000_000;z++) {
				startTime = System.nanoTime();
				store1(10);
				times[0][0] += System.nanoTime() - startTime;
				times[0][1] ++;
				
				startTime = System.nanoTime();
				store2(10);
				times[1][0] += System.nanoTime() - startTime;
				times[1][1] ++;
			}
			System.out.println( TimeConverter.nanoToAutomatic( times[0][0] / times[0][1] ) );
			System.out.println( TimeConverter.nanoToAutomatic( times[1][0] / times[1][1] ) );
			System.out.println();
		}
	}
	public static void store1(int amt) {
		ArrayList<Double> list = new ArrayList<Double>();
		for(int z=0;z<amt;z++)
			list.add( Math.random() );
		
		for(int z=0;z<amt/2;z++)
			list.set( (int)(Math.random()*amt), Math.random());
	}
	public static void store2(int amt) {
		double[] list = new double[amt];
		for(int z=0;z<amt;z++)
			list[z] = Math.random();
		
		for(int z=0;z<amt/2;z++)
			list[(int)(Math.random()*amt)] = Math.random();
	}
}
