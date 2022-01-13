package FFS;

import java.util.HashMap;
import java.util.Map;

// File format system
public class FFS {
	Node headNode = new Node();
	
	public FFS() {
//		set("wow.testInt",10);
//		set("wow.compDif",12);
//		set("wow.test.var",2);
//		print();
	}
	public Obj get(String loc) { return get(loc, headNode); }
	private Obj get(String loc, Node node) {
		String list[] = loc.split("[.]");
		if(list.length <= 1 ) {
			return node.map.get(loc);
		} else {
			for(String str:headNode.map.keySet())
				if(str.equals(list[0]))
					return get(loc.substring( loc.indexOf(".") + 1 ), node.map.get(str).node);
		}
		return null;
	}
	
	public void set(String loc, Obj var) { set(loc, var, headNode); }
	private void set(String loc, Obj var, Node currentNode) {
		String list[] = loc.split("[.]");
		if(list.length <= 1) {
			currentNode.map.put(loc, var);
		} else {
			for(String str:currentNode.map.keySet()) {
				if(str.equals(list[0])) {
					set(
							loc.substring(loc.indexOf(".")+1),
							var,
							currentNode.map.get(str).node );
					return;
				}
			}
			Node n = new Node();
			currentNode.map.put(list[0], new Obj(n));
			set(loc.substring(loc.indexOf(".")+1), var, n );
		}
	}
	
	public void print() { print(headNode, 0); }
	private void print(Node node, int ind) {
		String ss = "";
		for(int z=0;z<ind;z++)
			ss += " ";
		
		
		for(String str:node.map.keySet()) {
			System.out.print(ss+str);
			String s = node.map.get(str).print();
			if(s != null && s.length() > 0)
				System.out.print(" = "+s);
			System.out.println();
			if( node.map.get(str).type == 2 ) print(node.map.get(str).node, ind + 2);
		}
	}
	
//	public static void main(String args[]) { new FFS(); }
}
