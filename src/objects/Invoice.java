package objects;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class Invoice {

	private ConstNums cn;
	private DatabaseManager dm;
	private ConstDB cdb;
	private ConstStrings cs;
	
	private int id;
	private String custId;
	private boolean isBusiness;
	private double discount;
	private String list;
	private boolean isPercent;
	private double total;
	private String date;
	private String file;

	public Invoice(DatabaseManager dm, ConstDB cdb, ConstStrings cs, ConstNums cn, 
			String custID, boolean isBusiness, String list, double discount, boolean isPercent, double total, String date, String file){
		this.dm = dm;
		this.cdb = cdb;
		this.cs = cs;
		this.cn = cn;
		
		String q = cdb.SELECT + cdb.ID + cdb.FROM + ConstDB.TableNames.TB_INVOICES.getName() + cdb.ODER_BY + cdb.ID + cdb.DESC + cdb.LIMIT + " 1";
		String s = dm.selectData(q);
		this.id = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		this.id++;
		this.setId(id);
		
		this.custId = custID;
		this.isBusiness = isBusiness;
		this.list = list;
		this.discount = discount;
		this.isPercent =isPercent;
		this.total = total;
		this.date = date;
		this.file = file;
		
//		this.addNew();
	}

	public Invoice(DatabaseManager dm, ConstDB cdb, ConstStrings cs, ConstNums cn, int id,
			String custID, boolean isBusiness, String list, double discount, boolean isPercent, double total, String date, String file){
		this.dm = dm;
		this.cdb = cdb;
		this.cs = cs;
		this.cn = cn;
		
		this.id = id;
		this.custId = custID;
		this.isBusiness = isBusiness;
		this.list = list;
		this.discount = discount;
		this.isPercent =isPercent;
		this.total = total;
		this.date = date;
		this.file = file;
	}
	
	public boolean addNew(){
		String query = this.cdb.INSERT+ConstDB.TableNames.TB_INVOICES.getName()+this.cdb.VALUES+"("
				+this.getId()+ ", '"
				+this.getCustId()+ "', '"
				+this.getList() + "', "
				+this.getDiscount() + ", "
				+this.isPercent() + ", "
				+this.getTotal() + ", '"
				+this.getDate() + "', '"
				+this.getFile() + "');";
		return this.dm.addNewRecord(query);
	}
	
	public boolean updateRecord() {
		String q = this.cdb.UPDATE + ConstDB.TableNames.TB_INVOICES.getName() + this.cdb.SET 
		+ this.cdb.TB_INVOICE_CUSTOMER_ID + this.cdb.EQUAL + "'" + this.getCustId() + "',"
		+ this.cdb.TB_INVOICE_STOCK_CODES + this.cdb.EQUAL + "'" + this.getList() + "',"
		+ this.cdb.TB_INVOICE_DISCOUNT + this.cdb.EQUAL + this.getDiscount() + ","
		+ this.cdb.TB_INVOICE_IS_PERCENT + this.cdb.EQUAL + this.isPercent() + ","
		+ this.cdb.TB_INVOICE_TOTAL + this.cdb.EQUAL + this.getTotal() + ","
		+ this.cdb.TB_INVOICE_DATE  + this.cdb.EQUAL + "'" + this.getDate() + "',"
		+ this.cdb.TB_INVOICE_FILE_NAME + this.cdb.EQUAL + "'" + this.getFile() + "'"
		+ this.cdb.WHERE + ConstDB.TableNames.TB_INVOICES.getName()+"."+this.cdb.ID + this.cdb.EQUAL + this.getId();
		return this.dm.updateRecord(q);
	}

	public boolean deleteRecord() {
		String q = this.cdb.DELETE + this.cdb.FROM + ConstDB.TableNames.TB_INVOICES.getName() 
		+ this.cdb.WHERE + this.cdb.ID + this.cdb.EQUAL + this.getId();
		return this.dm.deleteRecord(q);
	}
	
	@Override
	public String toString(){
		return "Invoice\n["+this.date+"] "+this.custId+"-"+this.isBusiness+"-"+this.list+"-"+this.discount+"-"
				+this.isPercent+"-"+this.total+"-"+this.file;
	}
	/*
	 *     @Override
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
	 * 
	 */

	public int getId() {
		return id;
	}

	private void setId(int id) {
		this.id = id;
	}
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public boolean isBusiness() {
		return isBusiness;
	}

	public void setBusiness(boolean isBusiness) {
		this.isBusiness = isBusiness;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public int isPercent() {
		if(this.isPercent)	return 1;
		else return 0;
	}

	public void setPercent(boolean isPercent) {
		this.isPercent = isPercent;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
