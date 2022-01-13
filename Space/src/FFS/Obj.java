package FFS;

public class Obj {
	int type = 0;
	
	int intVar;
	double doubleVar;
	Node node;
	
	public Obj(int var) { type = 1; intVar = var; }
	public Obj(double var) { type = 1; doubleVar = var; }
	protected Obj(Node var) { type = 2; node = var; }
	protected String print() {
		switch(type) {
			case 1:return intVar+"";
		}
		return null;
	}
}
