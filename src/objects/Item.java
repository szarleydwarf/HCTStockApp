package objects;

import java.text.DecimalFormat;

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
	private ConstNums cn;
	private ConstStrings cs;

	private int id;
	private String code;
	private String name;
	private double cost;
	private byte addVat, addTransportCost, addVEMCCharge;
	private double price;
	private int qnt;
	private DecimalFormat df;
	
	
	/**
	 * constructor for new item
	 */
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, DecimalFormat df, String p_code, String p_name, double p_cost, int addVat, int addTransportCost, int addVEMCCharge, int qnt){
		this.dm = dm;
		this.cdb = cdb;
		this.cn = ci;
		this.cs = cs;
		this.df = df;

		String q = cdb.SELECT + cdb.ID + cdb.FROM + ConstDB.TableNames.TB_STOCK.getName() + cdb.ODER_BY + cdb.ID + cdb.DESC + cdb.LIMIT + " 1";
		String s = dm.selectData(q);
		this.id = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		this.id++;
		this.setID(id);

		this.code = p_code.toUpperCase();
		this.name = p_name.toUpperCase();
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);
		this.setAddVEMCCharge((byte) addVEMCCharge);

		calculateCost(p_cost);
		this.calculatePrice();
		
		this.qnt = qnt;
//		this should be handled by manager
//		this.saveNewInDatabase();
	}	
	
	/**
	 * constructor for Item taken from database
	 * 
	 */
	public Item(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, DecimalFormat df, 
			int id, String p_code, String p_name, double p_cost, double p_price, 
			int addVat, int addTransportCost, int addVEMCCharge, int qnt){
		this.dm = dm;
		this.cdb = cdb;
		this.cn = ci;
		this.cs = cs;
		this.df = df;

		this.id = id;

		this.code = p_code.toUpperCase();
		this.name = p_name.toUpperCase();
		this.setAddVat((byte) addVat);
		this.setAddTransportCost((byte) addTransportCost);
		this.setAddVEMCCharge((byte) addVEMCCharge);
		
		this.cost = p_cost;
		this.price = p_price;
		this.qnt = qnt;
	}

	private void calculateCost(double p_cost) {
		if(this.getAddVat() == 1)
			p_cost  = p_cost * cn.VAT;
		
		// TODO - different transport cost depending, company depend
		if (this.getAddTransportCost() == 1)
			p_cost = p_cost + cn.TRANSPORT_COST_DALY;
		
		if(this.getAddVEMCCharge() == 1)
			p_cost = p_cost + cn.REPAK_CHARGE;
		
		this.cost = Double.parseDouble(this.df.format(p_cost));
	}

	private void calculatePrice() {
		double tCost = this.getCost();
		double profit;
		profit = tCost * this.cn.PROFIT;
		
		if(this.getCode().contains(cs.ITEM_CODES[0]) && (profit - tCost) < 20)//TODO ??
			this.price = tCost + 20;
		else
			this.price = profit;
		this.price = Double.parseDouble(this.df.format(this.price));
//		System.out.println("price "+profit + " - " + tCost + " - " + this.price);
	}
	
	// TODO - 
	public String calcCost(double p_cost, int vat, int transport, int vemc) {
		if(vat == 1)
			p_cost  = p_cost * cn.VAT;
		
		
		if (transport == 1)
			p_cost = p_cost + cn.TRANSPORT_COST_DALY;
		
		if(vemc == 1)
			p_cost = p_cost + cn.REPAK_CHARGE;
		
		this.cost = Double.parseDouble(this.df.format(p_cost));
		return df.format(p_cost);
	}
	
	// TODO -
	public String calculateSuggestedPrice(double p_cost, String code) {
		double tCost = p_cost;
		double profit, price;
		profit = tCost * this.cn.PROFIT;
		
		if((profit - tCost) < 20 && code.contains(cs.ITEM_CODES[0]))//TODO ??
			price = tCost + 20;
		else
			price = profit;
		this.price = Double.parseDouble(this.df.format(price));
		return df.format(price);
	}
	
	public String[] getItemAsData(){
		String[] data = new String[9];
		data[0] = ""+this.id;
		data[1] = ""+this.code;
		data[2] = this.name;
		data[3] = ""+this.cost;
		data[4] = ""+this.price;
		data[5] = ""+this.qnt;
		data[6] = ""+this.addVat;
		data[7] = ""+this.addTransportCost;
		data[8] = ""+this.addVEMCCharge;
		return data;
	}
 
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
		return cdb.INSERT + ConstDB.TableNames.TB_STOCK.getName() +cdb.VALUES + "(" 
				+ this.getID() + ", '"
				+ this.getCode().toUpperCase() + "', '"
				+ this.getName().toUpperCase() + "', "
				+ this.getCost() + ", "
				+ this.getAddVat() + ", "
				+ this.getAddTransportCost() + ", "
				+ this.getAddVEMCCharge() + ", "
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
				+ "`"+cdb.TB_STOCK_CODE+"`"+cdb.EQUAL+"'"+this.code+"'" +","
				+ "`"+cdb.TB_STOCK_NAME+"`"+cdb.EQUAL+"'"+this.name+"'" +","
				+ "`"+cdb.TB_STOCK_COST+"`"+cdb.EQUAL+this.cost +","
				+ "`"+cdb.TB_STOCK_PRICE+"`"+cdb.EQUAL+this.price+","
				+ "`"+cdb.TB_STOCK_VAT+"`"+cdb.EQUAL+this.addVat+","
				+ "`"+cdb.TB_STOCK_TRANSPORT+"`"+cdb.EQUAL+this.addTransportCost+"," 
				+ "`"+cdb.TB_STOCK_QNT+"`"+cdb.EQUAL+this.qnt 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+cdb.ID+"` = '"+this.id+"'";
	}

	private String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`" + cdb.SET + "`"+columnToSet+"`"+cdb.EQUAL+"'"+valueToSet+"'" 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}

	// OVERRIDE METHODS
	@Override
	public String toString(){
		return this.code + "_" + this.getID()+ " - " + this.getName() + " - " + this.getCost() + " - " + this.getPrice() + " - " + this.getQnt(); 
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
	   if (this.getName().equals(cCopy.getName()) && (this.getCode()+this.getID()) == ((cCopy.getCode()+cCopy.getID()))) {
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
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
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
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public byte getAddVEMCCharge() {
		return addVEMCCharge;
	}

	public void setAddVEMCCharge(byte addVEMCCharge) {
		this.addVEMCCharge = addVEMCCharge;
	}
}
