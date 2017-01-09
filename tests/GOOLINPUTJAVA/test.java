import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class  test{

	public static void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public static void  main ( String [] args ){
		Map<Integer,Integer> m = new HashMap<Integer,Integer>();
		for(int i=0; i<10; ++i)
			m.put(i,2*i);
		printMap(m);
	}
}