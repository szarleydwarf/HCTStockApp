package objects;

import java.util.ArrayList;

import consts.ConstDB;
import managers.DatabaseManager;

public class Car {
	private DatabaseManager dm;
	private ConstDB cdb;

	int brand;
	private String registration;
	private int id;
	private ArrayList<String> list;
	
	public Car (DatabaseManager dm, ConstDB cdb,ArrayList<String> list, String registration, int id, int brand) {
		this.dm = dm;
		this.cdb = cdb;
		this.list = list;
		
		this.brand = brand;
		this.registration = registration;
		this.id = id;
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
		return cdb.INSERT + ConstDB.TableNames.TB_STOCK.getName() +cdb.VALUES + "(" 
				+ this.getId() + ", '"
				+ this.getRegistration().toUpperCase() + "', "
				+ this.getBrand() + ""
				+ ");";
	}

	private String createDeleteQuery() {
		//TODO - delete only by name, add by id? AND?
		return cdb.DELETE + cdb.FROM + ConstDB.TableNames.TB_STOCK.getName() 
//		+ cdb.WHERE + cdb.TB_STOCK_NAME + cdb.EQUAL + "'" + this.getName().toUpperCase() 
		+ "'";
	}

	private String createUpdateQuery() {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`" + cdb.SET 
//				+ "`"+cdb.TB_STOCK_NAME+"`"+cdb.EQUAL+"'"+this.name+"'" +","
//				+ "`"+cdb.TB_STOCK_COST+"`"+cdb.EQUAL+this.cost +"," 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+cdb.TB_STOCK_ID+"` = '"+this.id+"'";
	}

	private String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`" + cdb.SET + "`"+columnToSet+"`"+cdb.EQUAL+"'"+valueToSet+"'" 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_STOCK.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}

	@Override
	public String toString(){
		return this.list.get(this.brand) + " " + this.registration;
	}

	public int getBrand() {
		return brand;
	}

	public void setBrand(int brand) {
		this.brand = brand;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
