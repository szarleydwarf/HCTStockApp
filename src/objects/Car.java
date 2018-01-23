package objects;

import consts.ConstDB;
import consts.ConstStrings;
import managers.DatabaseManager;

/**
 * @author RJ
 *
 */
public class Car {
	private DatabaseManager dm;
	private ConstDB cdb;

	int brand;
	private String registration;
	private int id;
	
	/**
	 * Constructor which will be used only for new  cars
	 * @param dm
	 * @param cdb
	 * @param cs
	 * @param registration
	 * @param brand
	 */
	public Car (DatabaseManager dm, ConstDB cdb, ConstStrings cs, String registration, int brand) {
		this.dm = dm;
		this.cdb = cdb;
		
		this.brand = brand;
		this.registration = registration;
		String q = this.cdb.SELECT + this.cdb.ID + this.cdb.FROM + ConstDB.TableNames.TB_CARS.getName() + this.cdb.ODER_BY + this.cdb.ID + this.cdb.DESC + this.cdb.LIMIT + " 1";
		String s = this.dm.selectData(q);
		
		int i = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		
		this.id = ++i;
		this.saveNewInDatabase();
	}

	/**
	 * Constructor which will be used for car list from database
	 * @param dm
	 * @param cdb
	 * @param cs
	 * @param registration
	 * @param brand
	 */
	public Car (DatabaseManager dm, ConstDB cdb, ConstStrings cs, String registration, int id, int brand) {
		this.dm = dm;
		this.cdb = cdb;
		
		this.brand = brand;
		this.registration = registration;
		this.id = id;
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
		String q = this.cdb.SELECT+this.cdb.TB_BRANDS_NAME + this.cdb.FROM+ConstDB.TableNames.TB_BRANDS.getName()
		+this.cdb.WHERE+this.cdb.ID+this.cdb.EQUAL+this.brand;
		String brand = this.dm.selectData(q);
		return brand + " "  + this.registration;
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
