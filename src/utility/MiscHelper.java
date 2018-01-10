package utility;

import java.util.Iterator;
import java.util.Map;

public class MiscHelper {

	public MiscHelper(){
		
	}


	public void timeOut(long timeout) {
		int i = 0;
		while(i < timeout){
			i++;
		}
	}

	/* Function copied from
	 * https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
	*/
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
}
