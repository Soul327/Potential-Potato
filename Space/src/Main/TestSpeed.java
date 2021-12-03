package Main;

public class TestSpeed {
	public static void main(String args[]) {
		long[][] times = new long[2][2];
		for(int z=0;z<100_000_000;z++) {
			long startTime;
			//Create vars to pass in, must be the same for both test cases
			int[] a = new int[4];
			int[] b = new int[3];
			for(int x=0;x<a.length;x++) a[x] = (int)(Math.random()*255);
			for(int x=0;x<b.length;x++) b[x] = (int)(Math.random()*255);
			
			startTime = System.nanoTime();
			add1(a,b);
			times[0][0] += System.nanoTime() - startTime;
			times[0][1] ++;
			
			startTime = System.nanoTime();
			add2(a,b);
			times[1][0] += System.nanoTime() - startTime;
			times[1][1] ++;
			
			if(z%100_000==0) {
				System.out.println("add1() "+(times[0][0]/times[0][1]));
				System.out.println("add2() "+(times[1][0]/times[1][1]));
				System.out.println();
			}
		}
		
	}
	public static int[] add1(int[] a, int[] b){
		if(b[0]==0 && b[1]==0 && b[2]==0) return a;
		for(int z=0;z<3;z++)
			a[z] += b[z];
		a[3] += 1;
		return a;
	}
	public static int[] add2(int[] a, int[] b){
		if(b[0]!=0 && b[1]!=0 && b[2]!=0) {
			for(int z=0;z<3;z++)
				a[z] += b[z];
			a[3] += 1;
		}
		return a;
	}
}
