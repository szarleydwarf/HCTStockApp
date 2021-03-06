package managers;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.transform.SourceLocator;

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
	}

	public void getListFromDatabase(){
		list.clear();
		list = (ArrayList<Item>) this.dm.selectData(this.cdb.SELECT_ALL_ITEMS_ORDERED, list);
	}

	public boolean addItem(Item i){
			list.add(i);
			return i.saveNewInDatabase();
	}
	
	private void removeFromList(Item t) {
		Iterator<Item> it = list.iterator();
		while(it.hasNext()){
			if (this.equal(t, it.next())){//t.equals(it.next())){
				it.remove();
				return;
			}
		}
	}

	public boolean deleteItem(Item i){
		for (int j = 0; j < this.list.size(); j++) {
			if(this.equal(i, list.get(j))){
				System.out.println("REMOVE EQUAL "+i.getID() + " " + i.getName());
//				this.removeFromList(i);
				return i.deleteRecordFromDatabase();
			}
		}
		return false;
	}
	
	private boolean equal(Item toSearch, Item toCompare) {
		if(toSearch.getID() == toCompare.getID() 
				&& toSearch.getName() == toCompare.getName()
				&& toSearch.getCost() == toCompare.getCost()
				&& toSearch.getPrice() == toCompare.getPrice()
				&& toSearch.getCode() == toCompare.getCode())
			return true;
		return false;
	}

	//TODO - edit?
	public Item editNew(Item i, Item i2, boolean update){
		System.out.println("\nEditing new item\n"+i.toString()+"\n"+i2.toString());
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
		if(i.getAddVEMCCharge() != i2.getAddVEMCCharge()){
			i.setAddVEMCCharge(i2.getAddVEMCCharge());;
		}
		if(i.getPrice() != i2.getPrice()){
			i.setPrice(i2.getPrice());
		}
		if(update){
			int tq = i.getQnt();
			tq = tq + i2.getQnt();
			i.setQnt(tq);
		} else {
			if(i.getQnt() != i2.getQnt()){
				i.setQnt(i2.getQnt());
			}
		}
		// TODO qnt update, add or change ???
		System.out.println("\nEditing new item 2\n"+i.toString()+"\t"+i2.toString()+"\t");
		return i;
	}
	
	public boolean edit(Item i){
		System.out.println("\nEditing item 1\n");
		return i.updateRecord();
	}

	public String[][] getData(){
		this.getListFromDatabase();
		String[][] data = new String[this.list.size()][];
		for (int i = 0; i < list.size(); i++)
			data[i] = list.get(i).getItemAsData();
		return data;
	}
	
	public String[][] getDataByCode(String code){
		ArrayList<String[]> t = new ArrayList<String[]>();
		ArrayList<Item> l = new ArrayList<Item>();
		String q = cdb.SELECT_STAR + cdb.FROM + ConstDB.TableNames.TB_STOCK.getName() + cdb.WHERE + " code " + cdb.EQUAL + "\"" + code + "\""  + cdb.ODER_BY + " name " + cdb.ASC;

		l = (ArrayList<Item>) dm.selectData(q, l);
		
		for (int i = 0; i < l.size(); i++) {
			t.add(l.get(i).getItemAsDataShortWithID());
		}

		String[][] data = new String[t.size()][];
		data = t.toArray(data);
		return data;
	}
	
	public String[][] getDataNoCost() {
		String[][] data = new String[this.list.size()][];
		for (int i = 0; i < list.size(); i++){
				data[i] = list.get(i).getItemAsDataShort();
		}
		return data;
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
			if(in.getName().toUpperCase().equals(str.toUpperCase()) || (in.getCode()+in.getID()).toUpperCase().equals(str.toUpperCase())){
				return true;
			}
		}
		return false;
	}
	
	public Item find(String str) {
		for(Item in : this.list){
			if(in.getName().toUpperCase().equals(str.toUpperCase()) || (in.getCode()+in.getID()).toUpperCase().equals(str.toUpperCase())){
				System.out.println("Item find and return");
				return in;
			}
		}
		return null;
	}

	public ArrayList<Item> getList() {
		return list;
	}

	public ArrayList<Item> getSortedList() {
		getListFromDatabase();
		list.sort((o1,o2) -> o1.getCode().compareTo(o2.getCode()));
		return list;
	}

	public void setList(ArrayList<Item> list) {
		this.list = list;
	}

	public void printList() {
		for(Item i : this.list)
			if(i != null) System.out.println("I: "+i.toString());
			else System.out.println("I = null");
	}

	public void printData() {
		String d[][] = this.getData();
		for(int j = 0 ; j< d.length; j++)
			for(int i = 0; i < d[j].length; i++)
				System.out.println(d[j][i]);
	}

}
