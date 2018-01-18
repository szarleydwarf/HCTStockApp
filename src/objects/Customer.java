package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public abstract class Customer {
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstNums ci;
	private ConstStrings cs;

	private int numOfServices; private String id;
	
	public Customer(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String id, int numOfServices){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		this.setId(id);
		this.setNumOfServices(numOfServices);
	}

	public ConstDB getCdb() {
		return cdb;
	}

	public void setCdb(ConstDB cdb) {
		this.cdb = cdb;
	}

	public ConstNums getCi() {
		return ci;
	}

	public void setCi(ConstNums ci) {
		this.ci = ci;
	}

	public ConstStrings getCs() {
		return cs;
	}

	public void setCs(ConstStrings cs) {
		this.cs = cs;
	}

	public DatabaseManager getDm() {
		return dm;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumOfServices() {
		return numOfServices;
	}

	public void setNumOfServices(int numOfServices) {
		this.numOfServices = numOfServices;
	}
	public abstract boolean equals(Object o);
}
