package managers;

import java.util.ArrayList;
import java.util.Iterator;

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
		
		list = (ArrayList<Item>) this.dm.selectData(this.cdb.SELECT_ALL_ITEMS, list);
	}


	public boolean addItem(Item i){
		if(this.search(i)){
			Item t = this.find(i.getStockNumber());
			System.out.println("EQUAL\n"+t.toString());
			this.removeFromList(i);
			t = this.edit(t, i);
			this.list.add(t);
			return t.updateRecord();
		} else if(this.search(i.getStockNumber())){
			Item t = this.find(i.getStockNumber());
			System.out.println("ID "+t.toString());
			this.removeFromList(i);
			t = this.edit(t, i);
			this.list.add(t);
			return t.updateRecord();
		} else if(this.search(i.getName())) {
			Item t = this.find(i.getName());
			System.out.println("NAME "+t.toString());
			this.removeFromList(i);
			t = this.edit(t, i);
			this.list.add(t);
			return t.updateRecord();
		} else {
			System.out.println("ADD NEW");
			list.add(i);
			return i.saveNewInDatabase();
		}		
//		return false;
	}
	
	private void removeFromList(Item t) {
		Iterator<Item> it = list.iterator();
		while(it.hasNext()){
			if (t.equals(it.next())){
				System.out.println("REMOVE");
				it.remove();
			}
		}
	}


	public boolean deleteItem(Item i){
		if(list.contains(i)){
			list.remove(i);
			return i.deleteRecordFromDatabase();
		}
		return false;
	}
	
	//TODO - edit?
	public Item edit(Item i, Item i2){
		System.out.println("\nEditing item\n"+i.toString()+"\n"+i2.toString());
		//make adjustments
		if(!i.getName().equals(i2.getName())){
			i.setName(i2.getName());
		}
		if(i.getCost() != i2.getCost()){
			i.setCost(i2.getCost());
		}
		if(i.getAddTransportCost() != i2.getAddTransportCost()){
			i.setAddTransportCost(i2.getAddTransportCost());
		}
		if(i.getAddVat() != i2.getAddVat()){
			i.setAddVat(i2.getAddVat());
		}
		if(i.getQnt() != i2.getQnt()){
			int tq = i.getQnt();
			tq = tq + i2.getQnt();
			i.setQnt(tq);
		}
		return i;
	}

	//TODO other searches - by name, stock number, price?	
	public boolean search(Item i) {
		for(Item in : this.list){
			if(in.equals(i)){
				return true;
			}
		}
		return false;
	}

	public boolean search(String str) {
		for(Item in : this.list){
			if(in.getName().toLowerCase().equals(str.toLowerCase()) || in.getStockNumber().toUpperCase().equals(str.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	public Item find(String str) {
		for(Item in : this.list){
			if(in.getName().toLowerCase().equals(str.toLowerCase()) || in.getStockNumber().toUpperCase().equals(str.toUpperCase())){
				System.out.println("Item find and return");
				return in;
			}
		}
		return null;
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
