package managers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import objects.Item;

public class StockManager {
	private ArrayList<Item> list;
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstNums cn;
	private ConstStrings cs;
	
	public StockManager(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs){
		list = new ArrayList<Item>();
		this.dm = dm;
		this.cdb = cdb;
		this.cn = cn;
		this.cs = cs;
		
		populateList();
	}
	
	private void populateList() {
		ResultSet rs = this.dm.selectData(this.cdb.SELECT_ALL_ITEMS);
		ResultSetMetaData rsmd;
		try {
			rsmd = rs.getMetaData();
			int colNum = rsmd.getColumnCount();
			while(rs.next()){
				Item i = cretateItem(rs, colNum);
				this.addItem(i);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Item cretateItem(ResultSet rs, int colNum) throws NumberFormatException, SQLException {
		String  itID = "", itName = "";
		double cost = 0, price = 0;
		int qnt = 0, addTransport = 0, addVat = 0;
		for(int i = 1 ; i <= colNum; i++){
//			System.out.println(i + " "+ rs.getString(i));
			if(!rs.getString(i).isEmpty()) {
				switch(i){
				case 1:
					itID = rs.getString(i);
					break;
				case 2:
					itName = rs.getString(i);
					break;
				case 3:
					cost = Double.parseDouble(rs.getString(i));
					break;
				case 4:
					addVat = rs.getInt(i);
					break;
				case 5:
					addTransport = rs.getInt(i);
					break;
				case 6:
					price = Double.parseDouble(rs.getString(i));
					break;
				case 7:
					qnt = Integer.parseInt(rs.getString(i));
					break;
				}
			}
		}
		/*DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, 
		 * String p_name, double p_cost, double p_price, int addVat, int addTransportCost, int qnt*/
		return new Item(this.dm, this.cdb, this.cn, this.cs, itID, itName, cost, price, addVat, addTransport, qnt);	}

	public boolean addItem(Item i){
		if(!list.contains(i)) {
			list.add(i);
			return true;
		}
		return false;
	}
	
	public boolean deleteItem(Item i){
		if(list.contains(i)){
			list.remove(i);
			return true;
		}
		return false;
	}
	
	//TODO - edit?
	//TODO - search
	
	public ArrayList<Item> getList() {
		return list;
	}

	public void setList(ArrayList<Item> list) {
		this.list = list;
	}

	public void printList() {
		for(Item i : this.list)
			System.out.println("I: "+i.toString());
	}

}
