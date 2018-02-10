package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.simple.JSONObject;

import consts.ConstStrings;


public class MiscHelper {

	private Logger log;

	public MiscHelper(Logger logger){
		log = logger;
	}
	
	public void printData(String[][] d) {
		System.out.println("Data 1st length "+d.length);
		System.out.println("Data 2nd length "+d[0].length);
		for(int j = 0 ; j< d.length; j++)
			for(int i = 0; i < d[j].length; i++)
				System.out.println("j:"+j+" i:"+i+" - "+d[j][i]);
	}
	
	public int[] getScreenDimension() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int[] sd = new int[2];
		sd[0] = gd.getDisplayMode().getWidth() -10;
		sd[1] = gd.getDisplayMode().getHeight() -50;
		return sd;
	}

	public int getXenterXofFrame(JFrame frame, JLabel lbl) {
		return ((frame.getWidth() - lbl.getWidth()) / 2);
	}
	
	public void timeOut(long timeout) {
		int i = 0;
		while(i < timeout){
			i++;
		}
	}
	
	public boolean saveJSON(String path, JSONObject object){
        try (FileWriter file = new FileWriter(path)) {
            file.write(object.toJSONString());
            file.flush();
            return true;
        } catch (IOException e) {
        	log.logError("Fail to save JSON file in "+this.getClass().getName() + ". E: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
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

	public String removeLastChar(String str, char c) {
		return str.substring(0, str.lastIndexOf(c));
	}

	public Color getColor(String str, ConstStrings cs, JSONObject js) {
		int r = 0, g = 0, b = 0;
		if(str.equals(cs.APP)){
			r = Integer.parseInt(js.get(cs.APP_COLOR_R).toString());
			g = Integer.parseInt(js.get(cs.APP_COLOR_G).toString());
			b = Integer.parseInt(js.get(cs.APP_COLOR_B).toString());
		}
		return new Color(r, g, b);
	}

	public void toggleJButton(JButton btn, Color foreground_color, Color background_color, boolean toggle) {
		btn.setForeground(foreground_color);
		btn.setBackground(background_color);
		btn.setEnabled(toggle);
	}

}
