package managers;

import java.util.ArrayList;
import java.util.Iterator;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import objects.Customer;
import objects.CustomerBusiness;
import objects.CustomerInd;

public class CustomersManager {
	private ArrayList<Customer> list;
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstNums cn;
	private ConstStrings cs;
	
	public CustomersManager(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs){
		list = new ArrayList<Customer>();
		this.dm = dm;
		this.cdb = cdb;
		this.cn = cn;
		this.cs = cs;
		ArrayList<Customer> t = new ArrayList<Customer>();
		t = (ArrayList<Customer>) this.dm.selectData(this.cdb.SELECT_ALL_CUSTOMERS_I, t);
		list.addAll(t);
		t.clear();
		t = (ArrayList<Customer>) this.dm.selectData(this.cdb.SELECT_ALL_CUSTOMERS_B, t);
		list.addAll(t);
	}
	
	//TODO
	//add
	public boolean addCustomer(Customer c){
		// TODO check up for existing customer and perform update?
		if(!this.search(c)){
			this.list.add(c);
			return c.saveNewInDatabase();
		}
		return false;
	}
	
	//remove/delete
	public boolean delete(Customer c){
		Iterator<Customer> it = list.iterator();
		while(it.hasNext()){
			if(c.equals(it.next())){
				System.out.println("DELETING CUSTOMER");
				it.remove();
				return c.deleteRecord();
			}
		}
		return false;
	}
	
	//search/find
	private boolean search(Customer c) {
		for(Customer ct : this.list){
			if(ct.getId() == c.getId()){
				return true;
			}else return ct.compare(c);
		}
		return false;
	}
	
	public Customer find(String[] str){
		for(Customer c : this.list){
			if(c instanceof CustomerBusiness){
				if(((CustomerBusiness) c).find(str[0], str[1]))
					return c;
			}

			if(c instanceof CustomerInd){
				if(((CustomerInd) c).find(str[0]))
					return c;
			}
		}
		return null;
	}
	//edit
	
	//print
	public void printList() {
		for(Customer c : this.list)
			if(c != null)System.out.println("C: "+c.toString());
			else System.out.println("C = null\nList size: "+this.list.size());
	}

	public String[][] getDataShort() {
		String[][] data = new String[this.list.size()][];
		for (int i = 0; i < list.size(); i++)
			data[i] = list.get(i).getCasData();
		return data;	}

	
	//GETTERS & SETTERS
	public ArrayList<Customer> getList() {
		return list;
	}
	public void setList(ArrayList<Customer> list) {
		this.list = list;
	}
	public DatabaseManager getDm() {
		return dm;
	}
	public void setDm(DatabaseManager dm) {
		this.dm = dm;
	}
	public ConstDB getCdb() {
		return cdb;
	}
	public void setCdb(ConstDB cdb) {
		this.cdb = cdb;
	}
	public ConstNums getCn() {
		return cn;
	}
	public void setCn(ConstNums cn) {
		this.cn = cn;
	}
	public ConstStrings getCs() {
		return cs;
	}
	public void setCs(ConstStrings cs) {
		this.cs = cs;
	}
}