package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class CustomerBusiness extends Customer {
	private String VATTaxNUM, compName, CompAddress;
	private String carIdList;
	private int idINT;
	
	/**
	 * Constructor for new business customers
	 * @param dm
	 * @param cdb
	 * @param ci
	 * @param cs
	 * @param numOfServices
	 * @param vatTaxNum
	 * @param name
	 * @param address
	 */
	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,int numOfServices,String vatTaxNum, String name, String address) {
		super(dm, cdb, ci, cs, cs.CUST_BUS_CODE + "1", numOfServices);
		
		String q = cdb.SELECT + cdb.ID + cdb.FROM + ConstDB.TableNames.TB_BUSINESS.getName() + cdb.ODER_BY + cdb.ID + cdb.DESC + cdb.LIMIT + " 1";
		String s = dm.selectData(q);
		this.idINT = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		this.idINT++;
		String id = cs.CUST_BUS_CODE + this.idINT;
		this.setId(id);
		
		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
		this.setCarIdList("");
				
//		TODO this should be handled by manager
//this.saveNewInDatabase();
	}

	/**
	 * Constructor for customers saved in database
	 * @param dm
	 * @param cdb
	 * @param ci
	 * @param cs
	 * @param id
	 * @param numOfServices
	 * @param vatTaxNum
	 * @param name
	 * @param address
	 * @param list of cars
	 */
	public CustomerBusiness(DatabaseManager dm, ConstDB cdb, ConstNums ci, ConstStrings cs,String id, int numOfServices, String vatTaxNum, String name, String address, String cars) {
		super(dm, cdb, ci, cs, id, numOfServices);
		this.idINT = Integer.parseInt(id);
		id = cs.CUST_BUS_CODE + id;
		this.setId(id);

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
				+ this.getIdINT()+ ", '"
				+ this.getVATTaxNUM() + "', '"
				+ this.getCompName() + "', '"
				+ this.getCompAddress() + "', '"
				+ this.getCarIdList() + "',"
				+this.getNumOfServices() + ");";
	}



	@Override
	protected String createDeleteQuery() {
		return this.getCdb().DELETE + this.getCdb().FROM + ConstDB.TableNames.TB_BUSINESS.getName() 
		+ this.getCdb().WHERE + this.getCdb().ID + this.getCdb().EQUAL + this.getIdINT();
	}

/* TODO
 * updates for
 * num of services
 * vat tax
 * name
 * address
 * cars list
 */

	@Override
	protected String createUpdateQuery() {
		return this.getCdb().UPDATE + "`" + ConstDB.TableNames.TB_BUSINESS.getName()+"`" + this.getCdb().SET 
		+ this.getCdb().TB_B_CUSTOMER_NAME + this.getCdb().EQUAL + "'" +  this.getCompName()+"',"
		+ this.getCdb().TB_B_CUSTOMER_ADDRESS + this.getCdb().EQUAL + "'" +  this.getCompAddress()+"',"
		+ this.getCdb().TB_B_CUSTOMER_VAT_TAX + this.getCdb().EQUAL + "'" +  this.getVATTaxNUM()+"',"
		+ this.getCdb().TB_B_CUSTOMER_CARS_LIST + this.getCdb().EQUAL + "'" +  this.getCarIdList()+"',"
		+ this.getCdb().TB_CUSTOMER_NO_OF_SERVICES + this.getCdb().EQUAL + this.getNumOfServices()
		+ this.getCdb().WHERE + ConstDB.TableNames.TB_BUSINESS.getName()+"."+this.getCdb().ID + this.getCdb().EQUAL + this.getIdINT();
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

	@Override
	public boolean compare(Object c) {
  	   if (c == null) return false;
 	   
 	   if (getClass() != c.getClass()) return false;    	

 	   if(this.getVATTaxNUM() == ((CustomerBusiness) c).getVATTaxNUM()) return true;
 	   
 	   return false;
     }

	@Override
	public boolean equals(Object c) {
	   if (c == null) return false;
	   if (c == this) return true;
	   
	   //!(c instanceof Car)) 
	   if (getClass() != c.getClass()) return false;
	   
	   CustomerBusiness cbCopy = (CustomerBusiness) c;
 	   if(this.getVATTaxNUM() == ((CustomerBusiness) c).getVATTaxNUM()
 			&& this.getId() == cbCopy.getId()
 			&& this.getCompName() == cbCopy.getCompName()
 			&& this.getCompAddress() == cbCopy.getCompAddress()) 
 		   return true;
	   

	   return false;
	}

	@Override
	public String[] getCasData() {
		String[] data = new String[9];
		data[0] = this.getCompName();
		data[1] = "B";
		data[2] = this.getVATTaxNUM();
		return data;
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

	public String getCarIdList() {
		return carIdList;
	}

	public void setCarIdList(String carIdList) {
		this.carIdList = carIdList;
	}
	public int getIdINT() {
		return idINT;
	}

	public void setIdINT(int idINT) {
		this.idINT = idINT;
	}

}
