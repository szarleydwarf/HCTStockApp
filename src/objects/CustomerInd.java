package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class CustomerInd extends Customer{

	private Car car;
	
	public CustomerInd(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs, String id, int numOfServices, Car car) {
		super(dm, cdb, ci, cs, id, numOfServices);
		
		this.setCar(car);
	}

	@Override
	public boolean saveNewInDatabase() {
		boolean carSaved = this.getCar().saveNewInDatabase();
		if(carSaved){
			String q = this.createInsertQuery();
			return this.getDm().addNewRecord(q);
		}
		return false;
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
				+this.getId()+ ", "
				+this.getCar().getId()+ ", "
				+this.getNumOfServices() + ");";
	}



	@Override
	protected String createDeleteQuery() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
	}
	
	// GETTERS & SETTERS
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	
    @Override
    public String toString(){
    	return this.getId() + " " + this.getCar().toString() + " " + this.getNumOfServices();
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
}
