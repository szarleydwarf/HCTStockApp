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
		
		list = extracted();
//		populateList();
	}


	@SuppressWarnings("unchecked")
	private ArrayList<Item> extracted() {
		return (ArrayList<Item>) this.dm.selectData(this.cdb.SELECT_ALL_ITEMS, list);
	}
	

	public boolean addItem(Item i){
//		if(!list.contains(i)) {
		if(!this.search(i)){
			list.add(i);
			return i.saveNewInDatabase();
//			 true;
		}
		return false;
	}
	
	public boolean deleteItem(Item i){
		if(list.contains(i)){
			list.remove(i);
			//TODO update database
			return true;
		}
		return false;
	}
	
	//TODO - edit?

	//TODO other searches - by name, stock number, price?	
	public boolean search(Item i) {
		for(Item in : this.list){
			if(i.equals(in)){
				return true;
			}
		}
		return false;
	}

	public boolean search(String str) {
		for(Item in : this.list){
			if(in.getName().equals(str) || in.getStockNumber().equals(str)){
				return true;
			}
		}
		return false;
	}

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
