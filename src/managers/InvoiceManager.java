package managers;

import java.util.ArrayList;
import java.util.Iterator;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import objects.Invoice;

public class InvoiceManager {
	private ArrayList<Invoice> list;
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstNums cn;
	private ConstStrings cs;
	
	public InvoiceManager(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs) {
		list = new ArrayList<Invoice>();
		this.dm = dm;
		this.cdb = cdb;
		this.cn = cn;
		this.cs = cs;
		
		list = (ArrayList<Invoice>) this.dm.selectData(this.cdb.SELECT_ALL_INVOICES, list);
	}

	public boolean add(Invoice i) {
		if(!list.contains(i)){
			list.add(i);
			return i.addNew();
		}
		return false;
	}
	private void removeFromList(Invoice t) {
		Iterator<Invoice> it = list.iterator();
		while(it.hasNext()){
			if (t.equals(it.next())){
				System.out.println("REMOVE");
				it.remove();
			}
		}
	}

	public boolean deleteItem(Invoice i){
		if(this.search(i.getId())){
			this.removeFromList(i);
			return i.deleteRecord();
		}
		return false;
	}

	private boolean search(int id) {
		for(Invoice i : list){
			if(i.getId() == id)
				return true;
		}
		return false;
	}

	public boolean edit(Invoice i1, Invoice i2){
		
		return false;
	}
	
//	TODO
//	find ???? why is null
	public Invoice find(String str){
		return null;
	}
	
	public void printList(){
		for(Invoice i : list){
			System.out.println(""+i.toString());
		}
	}

	public ArrayList<Invoice> getList() {
		return list;
	}

	public String[][] getDataShort() {
		String[][] data = new String[this.list.size()][];
		for (int i = 0; i < list.size(); i++)
			data[i] = list.get(i).getCasData();
		return data;	
		}

	public Invoice getInvoiceByID(int id) {
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getId() == id)
				return list.get(i);
		}
		return null;
	}

	public String[][] getDataShort(ArrayList<Invoice> tList) {
		String[][] data = new String[tList.size()][];
		for (int i = 0; i < tList.size(); i++)
			data[i] = tList.get(i).getCasData();
		return data;	
	}

}
