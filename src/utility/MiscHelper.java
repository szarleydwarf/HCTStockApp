package utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstStrings;
import objects.Customer;
import objects.Item;
import objects.RepakROne;


public class MiscHelper {

	private Logger log;
	private ConstStrings cs;
	private JSONObject jl;
	private JSONObject js;
	private FileHelper fh;

	public MiscHelper(Logger logger, ConstStrings CS, JSONObject jLang, JSONObject JS, FileHelper FH){
		log = logger;
		cs = CS;
		jl = jLang;
		js = JS;
		fh = FH;
	}
	
	public void printData(String[][] d) {
		System.out.println("Data 1st length "+d.length);
		System.out.println("Data 2nd length "+d[0].length);
		for(int i = 0 ; i< d.length; i++)
			for(int j = 0; j < d[i].length; j++)
				System.out.println("i:"+i+" j:"+j+" - "+d[i][j]);
	}
	
	public void sopl2DimensionalArray(String[][] da) {
		System.out.println("Data 1st length "+da.length);
		System.out.println("Data 2nd length "+da[0].length);
		int count = 0;
		for (String[] ss : da) {
			System.out.println(count + " : ");
			for (String s : ss) {
				System.out.print(" "+s);
			}
			count++;
			System.out.println("");
		}
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
        	log.log(cs.ERR_LOG, "Fail to save JSON file in "+this.getClass().getName() + ". E: " + e.getMessage());
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

	public String[] splitStringRemoveSpecialChars(String str, String c) {
		int count = countChar(str, cs.SEMICOLON);
		String[] t = new String[count];
		if(str.contains(c)){
			if(str.matches(cs.SPECIAL_CHAR_PATTERN)){
				str = str.replaceAll(cs.REPLACE_CHAR_PATTERN, "");
			}
			t = str.split(c, -1);
		} else {
			t[0] = str;
		}	
		return t;
	}
	
	public String[] splitString(String str, String c) {
		int count = countChar(str, cs.SEMICOLON);
		String[] t = new String[count+1];
		if(str.contains(c)){
			t = str.split(c, -1);
		} else {
			t[0] = str;
		}	
		return t;
	}
	
	public int countChar(String str, String ch) {
		int split = str.split("\\"+ch,-1).length-1;
		return split;
	}

	public TitledBorder createBorders(String title, Color color) {
		Border b = BorderFactory.createLineBorder(color);
		return BorderFactory.createTitledBorder(b, title);
	}

	public String paddStringRight(String string2Padd, int stringLength, String paddingChar){
		if(stringLength <= 0)
			return string2Padd;
		
		StringBuilder sb = new StringBuilder(string2Padd);
		stringLength = stringLength - sb.length() - 1;
		while(stringLength-- >= 0){
			sb.append(paddingChar);
		}
		return sb.toString();
		
	}

	public JTable createTable(Font fonts, String[][] data, String[] headings, String tbName, int firstCol, int secondCol) {
		DefaultTableModel dm = new DefaultTableModel(data, headings);
		JTable table = new JTable();
		table.setFont(fonts);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(dm);
		table.setName(tbName);

		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		table.getColumnModel().getColumn(0).setPreferredWidth(firstCol);
		if(secondCol > 0)
			table.getColumnModel().getColumn(1).setPreferredWidth(secondCol);
//		ListSelectionListener listener = null;
//		if(tbName == cs.STOCK_TB_NAME || tbName == cs.CHOSEN_TB_NAME){
//			listener = createStockTableListener(table);
//			table.removeColumn(table.getColumnModel().getColumn(4));
//		}else if(tbName == cs.CARS_TB_NAME)
//			listener = createCarTableListener(table);
//		else if(tbName == cs.CUSTOMER_TB_NAME)
//			listener = createCustomerTableListener(table);
//
//		table.getSelectionModel().addListSelectionListener(listener);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.RED);
		table.setTableHeader(header);
		
		return table;
	}

	public void displayDataInLblTable(JLabel jlbl, DecimalFormat df, String[][] data, String[] sh) {
		String s = "<html><body><table>";
		s += "<tr>";
		for (String st : sh) {
			s +="<th width=\"70px\"  align=\"center\">";
			if(!jlbl.getName().equals(jl.get(cs.LBL_PICK_DATE).toString())){
				s += st;
			} 
			s += "</th>";
		}
			
		s += "</tr>";
		jlbl.setText(s);
		if(data != null){
			for(int j = 0 ; j< data.length; j++) {
				s = jlbl.getText();
				s += "<tr>";
				for (int i = 0; i < data[j].length; i++) {
					s += "<td  align=\"center\">"+data[j][i]+"</td>";
				}
				s += "</tr>";
				jlbl.setText(s);
			}
		}

		s += "</table></body></html>";
	}
	

	public String[] json2Array(JSONArray jsonArray) {
	    String[] stringArray = null;
	    if (jsonArray != null) {
	        int length = jsonArray.size();
	        stringArray = new String[length];
	        for (int i = 0; i < length; i++) {
	            stringArray[i] = jsonArray.get(i).toString();
	        }
	    }
	    return stringArray;
	}

	
	public String[] json2Array(JSONArray jsonArray, int mod) {
		String[] stringArray = null;
		if (jsonArray != null) {
			int length = jsonArray.size();
			stringArray = new String[length];
			for (int i = 0; i < length; i++) {
				if(i%mod == 0)
					stringArray[i] = jsonArray.get(i).toString();
				else
					stringArray[i] = "";
			}
		}
		return stringArray;
	}
	
	public String swapNames(String s, String[] cods, String[] names) {
		
		for (int i = 0; i < names.length; i++) {
			if(s.equals(cods[i]))
				s = names[i];
		}
		return s;
	}

	
	public void displayDataInLabel(JLabel jlbl, DecimalFormat df, String[][] data, String[] sa) {
		String s = "<html><body> ";
		for (String ss : sa) {
			s+=  " __ " + ss + " __ ";
		}
		jlbl.setText(s);
		if(data != null){
			for(int j = 0 ; j< data.length; j++){
				s = jlbl.getText();
				s+="<br>";
				for(int i = 0; i < data[j].length; i++){
					if(isNumeric(data[j][i]))
						s += df.format(Double.parseDouble(data[j][i]));
					else if(data[j][i] == null)
						s += df.format(0);
					else
						s += data[j][i];
					s += " ~ ";
				}
				jlbl.setText(s);
			}
		}
		s += "</body></html>";
//		log.log("display msh ", s);
	}
	
	public String[][] setZeros(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if(data[i][j] == null) {
					data[i][j] = ""+0;
				}
			}
		}
		return data;
	}

	public static boolean isNumeric(String str)	{
		if(str != null)
			return str.matches("-?\\d+(\\.\\d+)?");
		return false;
	}

	public void printArrayList(ArrayList<?> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	}

	public int parseToInt(String text) {
		text = removeSpecials(text);
		return Integer.parseInt(text);
	}

	public int removeSpecialChars(String str) {
		str = str.replaceAll("\\D+","");
		return isInt(str);
	}

	private String removeSpecials(String text) {
		return text.replaceAll("\\D+","");
	}

	public String createPdfPath(String date, String path, String repName) {
		String p = js.get(path).toString() + date;
		fh.createFolderIfNotExist(p);
		p += cs.SLASH + jl.get(repName).toString() + " " + date.replace(cs.SLASH, cs.UNDERSCORE) + cs.PDF_EXT;
		return p;
	}

	public String stringArr2String(String[] jArr, String st) {
		String h = "";
		if(!st.equals(""))
			jArr[0] = st;
		for (String s : jArr) {
			h += paddStringRight(s, 12, cs.UNDERSCORE);
		}
		h = removeLastChar(h, cs.UNDERSCORE);
		return h;
	}

	
	public double isDouble(String st) {
		if(st.contains(cs.COMA)){
			st = st.replace(cs.COMA, cs.DOT);
		}
		try{
			return Double.parseDouble(st);
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, jl.get(cs.NOT_DOUBLE_ERROR).toString());
		}
		
		return 0;
	}
	public int isInt(String st) {
		try{
			return Integer.parseInt(st);
		} catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, jl.get(cs.NOT_INT_ERROR).toString());
		}
		
		return 0;
	}


}
