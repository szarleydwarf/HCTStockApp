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

	private String id;
	private int numOfServices; 
	private int idINT;
	
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
	public int getIdINT() {
		return idINT;
	}
	
	public abstract boolean equals(Object o);	
	public abstract boolean compare(Object c) ;
	public abstract boolean saveNewInDatabase();
	public abstract boolean updateRecord();
	public abstract boolean deleteRecord();
	protected abstract String createInsertQuery();
	protected abstract String createDeleteQuery();
	protected abstract String createUpdateQuery();
	protected abstract String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) ;


	
}
