package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.json.simple.JSONObject;

import consts.ConstStrings;
import objects.Customer;
import objects.Item;


public class MiscHelper {

	private Logger log;
	private ConstStrings cs;

	public MiscHelper(Logger logger, ConstStrings CS){
		log = logger;
		cs = CS;
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
		sd[0] = gd.getDisplayMode().getWidth() -100;
		sd[1] = gd.getDisplayMode().getHeight() -50;
		return sd;
	}

	public int getCenterXofFrame(JFrame frame, JLabel lbl) {
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

	public String[][] populateDataArrayString(ArrayList<String> list, String[][] data, int rowNumber) {
		int j = 0;
		for(int i = 0; i < rowNumber; i++) {
			data[i][0] = list.get(j);
			j++;
		}		
		return data;
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

	public String removeLastChar(String str, String c) {
//		return str.substring(0, str.lastIndexOf(c));
		return str.replaceAll("("+c+")*$", "");
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

	public ArrayList<String> getTableDataToStringArray(JTable table) {
	    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
	    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
	    ArrayList<String> items = new ArrayList<String>();
	    String temp;
	    
	    for (int i = 0 ; i < nRow ; i++){
	        for (int j = 0 ; j < nCol ; j++){
	        	temp = dtm.getValueAt(i, j).toString();
	            if(j == 0)//&& !items.contains(temp))
	            	items.add(temp);
	        }
	    }
	    return items;
	}

	public int getSelectedItemRow(DefaultTableModel dtm, String string) {
	    int nRow = dtm.getRowCount(), nCol = dtm.getColumnCount();
	    String temp;
	    for (int i = 0 ; i < nRow ; i++){
	        for (int j = 0 ; j < nCol ; j++){
	        	temp = dtm.getValueAt(i, j).toString();
	        	if(temp.equals(string) && !temp.contains(cs.STAR) && !temp.toUpperCase().contains(cs.WASH.toUpperCase()))
	        		return i;
	        }
	    }       
		return -1;
	}

	public int compareItemKeyMap(Item item, Map<Item, Integer> map) {
		Set<Item> items = map.keySet();
		for(Item i : items){
			if(item.getName().equals(i.getName())){
				return map.get(i);
			}
		}
		return -1;
	}

	public String[] splitString(String str, String c) {
		String[] t = new String[2];
		if(str.contains(c)){
			System.out.println("1. "+str.matches(cs.SPECIAL_CHAR_PATTERN));
			
			if(str.matches(cs.SPECIAL_CHAR_PATTERN)){
				System.out.println("2. "+str);
				str = str.replaceAll(cs.REPLACE_CHAR_PATTERN, "");
						System.out.println("3. "+str);
//				str = str.substring(str.indexOf(cs.SPECIAL_CHAR_PATTERN)+1);
			}
			t = str.split(c, -1);
		} else {
			t[0] = str;
		}	
		return t;
	}
	
	public TitledBorder createBorders(String title, Color color) {
		Border b = BorderFactory.createLineBorder(color);
		return BorderFactory.createTitledBorder(b, title);
	}


}
