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

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
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
