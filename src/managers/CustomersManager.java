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
	
	public CustomersManager(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs){
		list = new ArrayList<Customer>();
		this.dm = dm;
		this.cdb = cdb;
		this.cn = cn;
		this.cs = cs;
		ArrayList<Customer> t = new ArrayList<Customer>();
		System.out.println(""+this.cdb.SELECT_ALL_CUSTOMERS_I);
		t = (ArrayList<Customer>) this.dm.selectData(this.cdb.SELECT_ALL_CUSTOMERS_I, t);
		list.addAll(t);
		t.clear();
		t = (ArrayList<Customer>) this.dm.selectData(this.cdb.SELECT_ALL_CUSTOMERS_B, t);
		list.addAll(t);
	}
	
	//TODO
	//add
	public boolean addCustomer(Customer c){
		// TODO check up for existing customer
		if(!this.search(c)){
			this.list.add(c);
			String query = this.getCdb().INSERT;
			if(c instanceof CustomerInd)
				query += ConstDB.TableNames.TB_CUSTOMERS.getName() + this.getCdb().VALUES + "(" + c.getIdINT() + "," + ((CustomerInd) c).getCar().getId() + "," + c.getNumOfServices() + ")";
			else query += ConstDB.TableNames.TB_BUSINESS.getName() + this.getCdb().VALUES + "(" + c.getIdINT() + ", '" + ((CustomerBusiness) c).getVATTaxNUM() + "', '" + ((CustomerBusiness) c).getCompName() + "', '" + ((CustomerBusiness) c).getCompAddress() + "', '" + ((CustomerBusiness) c).getCarIdList() + "'," +c.getNumOfServices() + ")" ;
			
			System.out.println("adding new cm q " + query);
//			this.getDm().addNewRecord(query);
			return true;
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
//			if(ct instanceof)
			if(ct.getId() == c.getId()){
				return true;
			}else return ct.compare(c);
		}
		return false;
	}
	
	//edit
	
	//print
	public void printList() {
		for(Customer c : this.list)
			if(c != null)System.out.println("C: "+c.toString());
			else System.out.println("C = null\nList size: "+this.list.size());
	}

	
	
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
	private ConstStrings cs;

}