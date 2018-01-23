package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class CustomerBusiness extends Customer {
	private String VATTaxNUM, compName, CompAddress;
	private String carIdList;
	
	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices) {
		super(dm, cdb, ci, cs, id, numOfServices);

	}

	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices, String vatTaxNum, String name, String address) {
		super(dm, cdb, ci, cs, id, numOfServices);

		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
	}

	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices, String vatTaxNum, String name, String address, String cars) {
		super(dm, cdb, ci, cs, id, numOfServices);

		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
		this.setCarIdList(cars);
	}

	@Override
	public boolean saveNewInDatabase() {
		String q = this.createInsertQuery();
		return this.getDm().addNewRecord(q);
	}



	@Override
	public boolean updateRecord() {
		String q = this.createUpdateQuery();
		return this.getDm().updateRecord(q);
	}



	@Override
	public boolean deleteRecord() {
		String q = this.createDeleteQuery();
		return this.getDm().deleteRecord(q);
	}



	@Override
	protected String createInsertQuery() {
		return this.getCdb().INSERT+ConstDB.TableNames.TB_BUSINESS.getName()+ this.getCdb().VALUES+"("
				+this.getId()+ ", '"
				+ this.getVATTaxNUM() + "', '"
				+ this.getCompName() + "', '"
				+ this.getCompAddress() + "', '"
				+ this.getCarIdList() + "',"
				+this.getNumOfServices() + ");";
	}



	@Override
	protected String createDeleteQuery() {
		return this.getCdb().DELETE + this.getCdb().FROM + ConstDB.TableNames.TB_BUSINESS.getName() 
		+ this.getCdb().WHERE + this.getCdb().ID + this.getCdb().EQUAL + this.getId();
	}



	@Override
	protected String createUpdateQuery() {
		return this.getCdb().UPDATE + "`" + ConstDB.TableNames.TB_BUSINESS.getName()+"`" + this.getCdb().SET 
		+ this.getCdb().TB_B_CUSTOMER_NAME + this.getCdb().EQUAL + "'" +  this.getCompName()+"',"
		+ this.getCdb().TB_B_CUSTOMER_ADDRESS + this.getCdb().EQUAL + "'" +  this.getCompAddress()+"',"
		+ this.getCdb().TB_B_CUSTOMER_VAT_TAX + this.getCdb().EQUAL + "'" +  this.getVATTaxNUM()+"',"
		+ this.getCdb().TB_B_CUSTOMER_CARS_LIST + this.getCdb().EQUAL + "'" +  this.getCarIdList()+"',"
		+ this.getCdb().TB_CUSTOMER_NO_OF_SERVICES + this.getCdb().EQUAL + this.getNumOfServices()+","
		+ this.getCdb().WHERE + ConstDB.TableNames.TB_CUSTOMERS.getName()+"."+this.getCdb().ID + this.getCdb().EQUAL + this.getId();
	}



	@Override
	protected String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		return this.getCdb().UPDATE + "`"+ConstDB.TableNames.TB_BUSINESS.getName()+"`" + this.getCdb().SET + "`"+columnToSet+"`"+this.getCdb().EQUAL+"'"+valueToSet+"'" 
				+ this.getCdb().WHERE + "`"+ConstDB.TableNames.TB_BUSINESS.getName()+"`.`"+columnToFind+"` = '"+valueToFind+"'";
	}
	
    @Override
    public String toString(){
    	String cars = "";
    	if(!this.getCarIdList().isEmpty()) {
    		String[] tokens = this.getCarIdList().split(",", -1);
    		if(tokens.length > 0){
    			for(String t : tokens){
    				Car c = (Car) this.getDm().getObject(Integer.parseInt(t), this.getCs().CAR);
    				if(c != null)
    					cars += "\n" + c.toString();
    			}
    		}
    	}
    	return this.getId() + " '" + this.getCompName() + "', '" + this.getCompAddress() + "', " + this.getVATTaxNUM() + ", ns: " + this.getNumOfServices() + ", cID: " + cars;
    }

	//GETTERS & SETTERS
	public String getVATTaxNUM() {
		return VATTaxNUM;
	}

	public void setVATTaxNUM(String vATTaxNUM) {
		VATTaxNUM = vATTaxNUM;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public String getCompAddress() {
		return CompAddress;
	}

	public void setCompAddress(String compAddress) {
		CompAddress = compAddress;
	}


	/*     @Override
    public boolean equals(Object c) {
	   CustomerInd cCopy = (CustomerInd)c;
	   if (this.getId() == cCopy.getId() && this.car.getRegistration() == cCopy.car.getRegistration()) {
	       return true;
	   }
       return false;
   }
	 * 
	 */

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

		return false;
	}

	public String getCarIdList() {
		return carIdList;
	}

	public void setCarIdList(String carIdList) {
		this.carIdList = carIdList;
	}

}
