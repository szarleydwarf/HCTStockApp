package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

/**
 * @author RJ
 *
 */
public class CustomerInd extends Customer{

	private Car car;
	private int idINT;
	/**
	 * Constructor which will be used only for new customers
	 * @param dm
	 * @param cdb
	 * @param cn
	 * @param cs
	 * @param id
	 * @param numOfServices
	 * @param car
	 */
	public CustomerInd(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs, int numOfServices, String registration, int brand) {
		super(dm, cdb, cn, cs, "1", numOfServices);

		String q = cdb.SELECT + cdb.ID + cdb.FROM + ConstDB.TableNames.TB_CUSTOMERS.getName() + cdb.ODER_BY + cdb.ID + cdb.DESC + cdb.LIMIT + " 1";
		String s = dm.selectData(q);
		this.idINT = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		this.idINT++;
		String id = cs.CUST_IND_CODE + this.idINT;
		this.setId(id);
		
		this.setCar(new Car(dm, cdb, cs, registration, brand));
		
		this.saveNewInDatabase();

	}

	/**
	 * Constructor which will be used only for customers from database
	 * @param dm
	 * @param cdb
	 * @param cn
	 * @param cs
	 * @param id
	 * @param noOfServices
	 * @param carID
	 */
	public CustomerInd(DatabaseManager dm, ConstDB cdb, ConstNums cn, ConstStrings cs, int id, int noOfServices, int carID) {
		super(dm, cdb, cn, cs, cs.CUST_IND_CODE + id, noOfServices);

		Car c = (Car) this.getDm().getObject(carID, cs.CAR);
		if(c == null)
			c = new Car(dm, cdb, cs, cs.DEFAULT_REGISTRATION, cn.DEFAULT_CAR_BRAND_ID);
		this.setCar(c);
	}

	@Override
	public boolean saveNewInDatabase() {
		String q = this.createInsertQuery();
		return this.getDm().addNewRecord(q);
	}



	@Override
	public boolean updateRecord() {
		// Not sure if that check up for car update is necessary
		boolean carUpdate = this.getCar().updateRecord();
		if(carUpdate){
			String q = this.createUpdateQuery();
			return this.getDm().updateRecord(q);
		}
		return false;
	}



	@Override
	public boolean deleteRecord() {
		String q = this.createDeleteQuery();
		boolean deleted = this.getDm().deleteRecord(q);
		// Not sure if that check up for car update is necessary
		if(deleted){
			return this.getCar().deleteRecordFromDatabase();
		}
		return false;
	}



	@Override
	protected String createInsertQuery() {
		return this.getCdb().INSERT+ConstDB.TableNames.TB_CUSTOMERS.getName()+ this.getCdb().VALUES+"("
				+this.getIdINT()+ ", "
				+this.getCar().getId()+ ", "
				+this.getNumOfServices() + ");";
	}



	@Override
	protected String createDeleteQuery() {
		return this.getCdb().DELETE + this.getCdb().FROM + ConstDB.TableNames.TB_CUSTOMERS.getName() 
		+ this.getCdb().WHERE + this.getCdb().ID + this.getCdb().EQUAL + this.getId();
	}


	// TODO - update query only for number of services

	@Override
	protected String createUpdateQuery() {
		return this.getCdb().UPDATE+ "`" + ConstDB.TableNames.TB_CUSTOMERS.getName()+"`" + this.getCdb().SET 
				+ this.getCdb().TB_CUSTOMER_CAR_ID + this.getCdb().EQUAL + this.getCar().getId()+","
				+ this.getCdb().TB_CUSTOMER_NO_OF_SERVICES + this.getCdb().EQUAL + this.getNumOfServices()
				+ this.getCdb().WHERE + ConstDB.TableNames.TB_CUSTOMERS.getName()+"."+this.getCdb().ID + this.getCdb().EQUAL + this.getId();
	}



	@Override
	protected String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return this.getCdb().UPDATE + "`"+ConstDB.TableNames.TB_CUSTOMERS.getName()+"`" + this.getCdb().SET + "`"+columnToSet+"`"+this.getCdb().EQUAL+"'"+valueToSet+"'" 
				+ this.getCdb().WHERE + "`"+ConstDB.TableNames.TB_CUSTOMERS.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}
	
	// GETTERS & SETTERS
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
//		car.saveNewInDatabase();
		this.car = car;
	}
	
	
    @Override
    public String toString(){
    	if(this.getCar() != null)
    		return "id: " + this.getId() + " c: " + this.getCar().toString() + " ns: " + this.getNumOfServices();
    	else
    		return "id: " + this.getId() + " c: N/A" + " ns: " + this.getNumOfServices();
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
	   CustomerInd cCopy = (CustomerInd)c;
	   if (this.getId() == cCopy.getId() && this.car.getRegistration() == cCopy.car.getRegistration()) {
	       return true;
	   }
       return false;
   }
/*Not sure yet if I will need this
 * 	@Override
	public int compare(Player p1, Player p2) 
	{
		if(sortType == SORT_BY_NAME)
		{
			return sortOrder * p1.getName().compareTo(p2.getName());
		}
		else
		{
			if(p1.getDob().before(p2.getDob()))
				return -sortOrder;
			else if(p1.getDob().after(p2.getDob()))
				return sortOrder;
			else
				return 0;
		}
	}
	*/

	public int getIdINT() {
		return idINT;
	}

	public void setIdINT(int idINT) {
		this.idINT = idINT;
	}
}
