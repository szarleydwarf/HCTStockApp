package objects;

import java.util.ArrayList;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

/**
 * @author RJ
 *
 */
public class Item {
	private DatabaseManager dm;
	private ConstDB cdb;
	private String stockNumber;
	private String name;
	private double cost;
	private byte addVat, addTransportCost;
	private double price;
	private int qnt;
	private ConstNums ci;
	private ConstStrings cs;
	
	
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, String p_name, double p_cost, double p_price, int addVat, int addTransportCost){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);
		this.price = p_price;
		this.qnt = 0;
	}	
	
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, String p_name, double p_cost, int addVat, int addTransportCost, int qnt){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;
		
		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);
		
		calculatePrice();
		
		this.qnt = qnt;
	}

	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, String p_name, double p_cost, double p_price, int addVat, int addTransportCost, int qnt){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);

		calculatePrice();
		
		this.qnt = qnt;
	}

	private void calculatePrice() {
		double tPrice = this.getCost();
		double profit;
		if(this.getAddVat() == 1)
			tPrice  = tPrice * ci.VAT;
		
		if (this.getAddTransportCost() == 1)
			tPrice = tPrice * ci.VAT + ci.REPAK_CHARGE;
		
		profit = tPrice * this.ci.PROFIT;
		if(this.getName().contains(cs.TYRE_CODE) && profit < 20)
			this.price = tPrice + 20;
		else
			this.price = profit;
	}

	//TODO
//	save new item to database
	public boolean saveNewInDatabase(){
		String q = this.createInsertQuery();
		System.out.println("Q: "+q);
		return this.dm.addNewRecord(q);
	}
	//TODO
	//	save edited item
	
	//TODO
	//delete item from database
	
	public String createInsertQuery(){
		return cdb.INSERT + ConstDB.TableNames.TB_STOCK.getName() +cdb.VALUES + "('" 
				+ this.getStockNumber() + "', '"
				+ this.getName() + "', "
				+ this.getCost() + ", "
				+ this.getAddVat() + ", "
				+ this.getAddTransportCost() + ", "
				+ this.getPrice() + ", "
				+ this.getQnt()
				+ ");";
	}

	//TODO
	// update query
	// remove query
	
	@Override
	public String toString(){
		return this.getStockNumber() + " - " + this.getName() + " - " + this.getCost() + " - " + this.getPrice() + " - " + this.getQnt(); 
	}
	@Override
	public boolean equals(Object c) {
	   if (c == null) {
	       return false;
	   }

	   if (c == this) {
	       return true;
	   }
	   
	   //!(c instanceof Car)) 
	   if (getClass() != c.getClass()) {
	       return false;
	   }

	   Item cCopy = (Item)c;
	   //TODO - add other checkups - stock number?
	   if (this.getName().equals(cCopy.getName()) && this.getStockNumber().equals(cCopy.getStockNumber())) {
	       return true;
	   }
       return false;
	}

	//GETTERS & SETTERS
	public String getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQnt() {
		return qnt;
	}

	public void setQnt(int qnt) {
		this.qnt = qnt;
	}
	
	public byte getAddVat() {
		return addVat;
	}

	public void setAddVat(byte addVat) {
		this.addVat = addVat;
	}

	public byte getAddTransportCost() {
		return addTransportCost;
	}

	public void setAddTransportCost(byte addTransportCost) {
		this.addTransportCost = addTransportCost;
	}
}
