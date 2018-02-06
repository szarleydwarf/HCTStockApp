package utility;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;

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
		System.out.println("Printing - " + mp.getClass().getTypeName());
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	/* Copied from: 
	 * https://stackoverflow.com/questions/9151619/how-to-iterate-over-a-jsonobject
	 * */
	public static void printJsonObject(JSONObject jsonObj) {
	    for (Object key : jsonObj.keySet()) {
	        //based on you key types
	        String keyStr = (String)key;
	        Object keyvalue = jsonObj.get(keyStr);

	        //Print key and value
	        System.out.println("key: "+ keyStr + " value: " + keyvalue);

	        //for nested objects iteration if required
	        if (keyvalue instanceof JSONObject)
	            printJsonObject((JSONObject)keyvalue);
	    }
	}
}
