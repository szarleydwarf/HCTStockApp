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
	
	public Car (DatabaseManager dm, ConstDB cdb, String registration, int id, int brand) {
		this.dm = dm;
		this.cdb = cdb;
		
		this.brand = brand;
		this.registration = registration;
		this.id = id;
	}

	public Car (DatabaseManager dm, ConstDB cdb, ArrayList<String> list, String registration, int id, int brand) {
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
		return cdb.INSERT + ConstDB.TableNames.TB_CARS.getName() +cdb.VALUES + "(" 
				+ this.getId() + ", '"
				+ this.getRegistration().toUpperCase() + "', "
				+ this.getBrand() + ""
				+ ");";
	}

	private String createDeleteQuery() {
		//TODO - delete only by name, add by id? AND?
		return cdb.DELETE + cdb.FROM + ConstDB.TableNames.TB_CARS.getName() 
		+ cdb.WHERE + cdb.ID + cdb.EQUAL+ this.getId();
	}

	private String createUpdateQuery() {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_CARS.getName()+"`" + cdb.SET 
				+ "`"+cdb.TB_CARS_REGISTRATION+"`"+cdb.EQUAL+"'"+this.registration+"'" +","
				+ "`"+cdb.TB_CARS_BRAND_ID+"`"+cdb.EQUAL+this.brand
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_CARS.getName()+"`.`"+cdb.ID+"` = '"+this.id+"'";
	}

	private String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_CARS.getName()+"`" + cdb.SET + "`"+columnToSet+"`"+cdb.EQUAL+"'"+valueToSet+"'" 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_CARS.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}

	@Override
	public String toString(){
		if(this.list != null)
			return this.list.get(this.brand) + " " + this.registration;
		else{
			String q = this.cdb.SELECT+this.cdb.TB_BRANDS_NAME + this.cdb.FROM+ConstDB.TableNames.TB_BRANDS.getName()
			+this.cdb.WHERE+this.cdb.ID+this.cdb.EQUAL+this.brand;
			String brand = this.dm.selectData(q);
			return brand + " "  + this.registration;
		}
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
