package objects;

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
	private ConstNums ci;
	private ConstStrings cs;

	private String stockNumber;
	private String name;
	private double cost;
	private byte addVat, addTransportCost;
	private double price;
	private int qnt;
	
	
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, String p_name, double p_cost, double p_price, int addVat, int addTransportCost){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		this.stockNumber = p_stock_number.toUpperCase();
		this.name = p_name.toUpperCase();
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);

		calculateCost(p_cost);
		this.calculatePrice();
		
		this.qnt = 0;
	}	
	
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String p_stock_number, String p_name, double p_cost, double p_price, int addVat, int addTransportCost, int qnt){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		this.stockNumber = p_stock_number.toUpperCase();
		this.name = p_name.toUpperCase();
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);

		calculateCost(p_cost);
		calculatePrice();
		
		this.qnt = qnt;
	}

	private void calculateCost(double p_cost) {
		if(this.getAddVat() == 1)
			p_cost  = p_cost * ci.VAT;
		
		if (this.getAddTransportCost() == 1)
			p_cost = p_cost + ci.TRANSPORT_COST_DALY;
		
		p_cost +=  ci.REPAK_CHARGE;
		this.cost = p_cost;
	}

	private void calculatePrice() {
		double tPrice = this.getCost();
		double profit;
		profit = tPrice * this.ci.PROFIT;
		if(this.getName().contains(cs.TYRE_CODE) && (profit - tPrice) < 20)//TODO ??
			this.price = tPrice + 20;
		else
			this.price = profit;
	}
	
	public String[] getItemAsData(){
		String[] data = new String[7];
		data[0] = this.stockNumber;
		data[1] = this.name;
		data[2] = ""+this.cost;
		data[3] = ""+this.price;
		data[4] = ""+this.qnt;
		data[5] = ""+this.addVat;
		data[6] = ""+this.addTransportCost;
		return data;
	}
 
	//TODO
	public boolean saveNewInDatabase(){
		String q = this.createInsertQuery();
		return this.dm.addNewRecord(q);
	}
	
	public boolean updateRecord(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		String q = this.createUpdateQuery(columnToSet, valueToSet, columnToFind, valueToFind); 
		return this.dm.updateRecord(q);
	}

	public boolean updateRecord() {
		String q = this.createUpdateQuery();
		return this.dm.updateRecord(q);
	}

	public boolean deleteRecordFromDatabase() {
		String q = this.createDeleteQuery();		
		return this.dm.deleteRecord(q);
	}

	//QUERYS FOR DATABASE
	private String createInsertQuery(){
		return cdb.INSERT + ConstDB.TableNames.TB_STOCK.getName() +cdb.VALUES + "('" 
				+ this.getStockNumber().toUpperCase() + "', '"
				+ this.getName().toUpperCase() + "', "
				+ this.getCost() + ", "
				+ this.getAddVat() + ", "
				+ this.getAddTransportCost() + ", "
				+ this.getPrice() + ", "
				+ this.getQnt()
				+ ");";
	}

	private String createDeleteQuery() {
		//TODO - delete only by name, add by id? AND?
		return cdb.DELETE + cdb.FROM + ConstDB.TableNames.TB_STOCK.getName() 
		+ cdb.WHERE + cdb.TB_STOCK_NAME + cdb.EQUAL + "'" + this.getName().toUpperCase() + "'";
	}

	private String createUpdateQuery() {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`" + cdb.SET 
				+ "`"+cdb.TB_STOCK_NAME+"`"+cdb.EQUAL+"'"+this.name+"'" +","
				+ "`"+cdb.TB_STOCK_COST+"`"+cdb.EQUAL+this.cost +","
				+ "`"+cdb.TB_STOCK_PRICE+"`"+cdb.EQUAL+this.price+","
				+ "`"+cdb.TB_STOCK_VAT+"`"+cdb.EQUAL+this.addVat+","
				+ "`"+cdb.TB_STOCK_TRANSPORT+"`"+cdb.EQUAL+this.addTransportCost+"," 
				+ "`"+cdb.TB_STOCK_QNT+"`"+cdb.EQUAL+this.qnt 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+cdb.TB_STOCK_ID+"` = '"+this.stockNumber+"'";
	}

	private String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`" + cdb.SET + "`"+columnToSet+"`"+cdb.EQUAL+"'"+valueToSet+"'" 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}

	// OVERRIDE METHODS
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
	   if (this.getName().equals(cCopy.getName()) && this.getStockNumber().equals(cCopy.getStockNumber())) {
//		   System.out.println("Item copy if");
	       return true;
	   }
       return false;
	}
	/*	implements Comparable
	 * @Override
	public int compareTo(Object obj) 
	{
		if(obj == null)
			return 0;
		
		if(this.getClass() != obj.getClass())
			return 0;
		
		Movie m = (Movie)obj;

		if(sortType == SORT_BY_NAME)
		{
			//See String::compareTo()
			return sortOrder*this.getName().compareTo(m.getName());
		}
		else if(sortType == SORT_BY_TIME)
		{
			float diff = this.getRunningTime() - m.getRunningTime();
			if(diff > 0)
				return sortOrder;
			else if(diff < 0)
				return -sortOrder;
			else
				return 0;
		}		
		return 0; //otherwise
	}
	 */

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
