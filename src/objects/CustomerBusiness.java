package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class CustomerBusiness extends Customer {
	private String VATTaxNUM, compName, CompAddress;
	private Car car;
	
	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices) {
		super(dm, cdb, ci, cs, id, numOfServices);

	}

	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices, String vatTaxNum, String name, String address) {
		super(dm, cdb, ci, cs, id, numOfServices);

		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
	}

	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices, String vatTaxNum, String name, String address, Car car) {
		super(dm, cdb, ci, cs, id, numOfServices);

		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
		this.setCar(car);
	}

	@Override
	public boolean saveNewInDatabase() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean updateRecord() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean deleteRecord() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	protected String createInsertQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected String createDeleteQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected String createUpdateQuery() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected String createUpdateQuery(String columnToSet, String valueToSet, String columnToFind, String valueToFind) {
		// TODO Auto-generated method stub
		return null;
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

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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

}
