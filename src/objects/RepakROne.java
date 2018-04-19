package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class RepakROne {
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstNums ci;
	private ConstStrings cs;
	private String date;
	private int soldCarTyres;
	private int boughtCarTyres;
	private int fittedCarTyres;
	private int soldAgriTyres;
	private int boughtAgriTyres;
	private int fittedAgriTyres;
	private int returnedCarTyres;
	private int returnedAgriTyres;
	private int toReturnCarTyres;
	private int toReturnAgriTyres;

	/**
	 * Constructor which will be used only for new  invoices
	 * @param dm
	 * @param cdb
	 * @param cs
	 */
	public RepakROne(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,
			String p_date, int sold_car, int bought_car, int fitted_car, 
			int sold_agri, int bought_agri, int fitted_agri
			, int returned_car, int returned_agri){
		this.dm = dm;
		this.cdb = cdb;
		this.ci = ci;
		this.cs = cs;

		// id?
		this.date = p_date;
		
		this.soldCarTyres = sold_car;
		this.boughtCarTyres = bought_car;
		this.fittedCarTyres = fitted_car;
		
		this.soldAgriTyres = sold_agri;
		this.boughtAgriTyres = bought_agri;
		this.fittedAgriTyres = fitted_agri;
		
		this.returnedCarTyres = returned_car;
		this.returnedAgriTyres = returned_agri;
		
		this.toReturnCarTyres = 0;
		this.toReturnAgriTyres = 0;
		
		// to return
	}
	//QUERYS FOR DATABASE
	private String createInsertQuery(){
		return cdb.INSERT + ConstDB.TableNames.TB_REPAK.getName() +cdb.VALUES + "(" 
				+ "'" + this.getDate() + "', "
				+ this.getSoldCarTyres() + ", "
				+ this.getFittedCarTyres() + ","
				+ this.getBoughtCarTyres() + ","
				+ this.getSoldAgriTyres() + ", "
				+ this.getFittedAgriTyres() + ","
				+ this.getBoughtAgriTyres() + ","
				+ this.getReturnedCarTyres() + ","
				+ this.getReturnedAgriTyres()
				+ ");";
	}
	private String createUpdateQuery() {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_REPAK.getName()+"`" + cdb.SET 
				+ "`"+cdb.TB_REPAK_SOLD_CAR+"`"+cdb.EQUAL+this.getSoldCarTyres()+","
				+ "`"+cdb.TB_REPAK_FITTED_CAR+"`"+cdb.EQUAL+this.getFittedCarTyres()+","
				+ "`"+cdb.TB_REPAK_BOUGHT_CAR+"`"+cdb.EQUAL+this.getBoughtCarTyres()+","
				+ "`"+cdb.TB_REPAK_SOLD_AGRI+"`"+cdb.EQUAL+this.getSoldAgriTyres()+","
				+ "`"+cdb.TB_REPAK_FITTED_AGRI+"`"+cdb.EQUAL+this.getFittedAgriTyres()+","
				+ "`"+cdb.TB_REPAK_BOUGHT_AGRI+"`"+cdb.EQUAL+this.getBoughtAgriTyres()+","
				+ "`"+cdb.TB_REPAK_RETURNED_CAR+"`"+cdb.EQUAL+this.getReturnedCarTyres()+","
				+ "`"+cdb.TB_REPAK_RETURNED_AGRI+"`"+cdb.EQUAL+this.getReturnedAgriTyres()
				+ cdb.WHERE + cdb.DATE + cdb.EQUAL+ this.getDate();
	}

	private String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return  cdb.UPDATE + "`"+ConstDB.TableNames.TB_REPAK.getName()+"`" + cdb.SET + "`"+columnToSet+"`"+cdb.EQUAL+"'"+valueToSet+"'" 
				+ cdb.WHERE + "`"+ConstDB.TableNames.TB_REPAK.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}

	@Override
	public String toString(){
		return this.getDate() 
				+ "\nSC: "  + this.getSoldCarTyres() + " BC: "  + this.getBoughtCarTyres() + " FC: "  + this.getFittedCarTyres()
				+ "\nSA: "  + this.getSoldAgriTyres() + " BA: "  + this.getBoughtAgriTyres() + " FA: "  + this.getFittedAgriTyres()
				+ "\nRC: "  + this.getReturnedCarTyres() + " RA: "  + this.getReturnedAgriTyres()
				+ "\n2RC: "  + this.getToReturnCarTyres() + " 2RA: "  + this.getToReturnAgriTyres();
	}

	// DATABASE OPERATIONS
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

	// I AM NOT SURE FOR 100%, BUT IN MHO THIS METHOD IS UNNECESSARY IN THIS CLASS 
	public boolean deleteRecordFromDatabase() {
		String q = this.createDeleteQuery();		
		return this.dm.deleteRecord(q);
	}
	// I AM NOT SURE FOR 100%, BUT IN MHO THIS METHOD IS UNNECESSARY IN THIS CLASS 
		private String createDeleteQuery() {
			return cdb.DELETE + cdb.FROM + ConstDB.TableNames.TB_REPAK.getName() 
			+ cdb.WHERE + cdb.DATE + cdb.EQUAL+ this.getDate();
		}


	// GETTERS AND SETTERS
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSoldCarTyres() {
		return soldCarTyres;
	}

	public void setSoldCarTyres(int soldCarTyres) {
		this.soldCarTyres = soldCarTyres;
	}

	public int getBoughtCarTyres() {
		return boughtCarTyres;
	}

	public void setBoughtCarTyres(int boughtCarTyres) {
		this.boughtCarTyres = boughtCarTyres;
	}

	public int getFittedCarTyres() {
		return fittedCarTyres;
	}

	public void setFittedCarTyres(int fittedCarTyres) {
		this.fittedCarTyres = fittedCarTyres;
	}

	public int getSoldAgriTyres() {
		return soldAgriTyres;
	}

	public void setSoldAgriTyres(int soldAgriTyres) {
		this.soldAgriTyres = soldAgriTyres;
	}

	public int getBoughtAgriTyres() {
		return boughtAgriTyres;
	}

	public void setBoughtAgriTyres(int boughtAgriTyres) {
		this.boughtAgriTyres = boughtAgriTyres;
	}

	public int getFittedAgriTyres() {
		return fittedAgriTyres;
	}

	public void setFittedAgriTyres(int fittedAgriTyres) {
		this.fittedAgriTyres = fittedAgriTyres;
	}

	public int getReturnedCarTyres() {
		return returnedCarTyres;
	}

	public void setReturnedCarTyres(int returnedCarTyres) {
		this.returnedCarTyres = returnedCarTyres;
	}

	public int getReturnedAgriTyres() {
		return returnedAgriTyres;
	}

	public void setReturnedAgriTyres(int returnedAgriTyres) {
		this.returnedAgriTyres = returnedAgriTyres;
	}

	public int getToReturnCarTyres() {
		return toReturnCarTyres;
	}

	public void setToReturnCarTyres(int toReturnCarTyres) {
		this.toReturnCarTyres = toReturnCarTyres;
	}

	public int getToReturnAgriTyres() {
		return toReturnAgriTyres;
	}

	public void setToReturnAgriTyres(int toReturnAgriTyres) {
		this.toReturnAgriTyres = toReturnAgriTyres;
	}
}
