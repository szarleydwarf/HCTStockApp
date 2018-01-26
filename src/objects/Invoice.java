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

	public Invoice(DatabaseManager dm, ConstDB cdb, ConstStrings cs, ConstNums cn, 
			int custID, boolean isBusiness, String list, double discount, boolean isPercent, double total, String date, String filePath){
		this.dm = dm;
		this.cdb = cdb;
		this.cs = cs;
		this.cn = cn;
		
		String q = cdb.SELECT + cdb.ID + cdb.FROM + ConstDB.TableNames.TB_INVOICES.getName() + cdb.ODER_BY + cdb.ID + cdb.DESC + cdb.LIMIT + " 1";
		String s = dm.selectData(q);
		this.id = (!s.isEmpty()) ? Integer.parseInt(s) : 1;
		this.id++;
		this.setId(id);

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

	private void setId(int id) {
		this.id = id;
	}
}
