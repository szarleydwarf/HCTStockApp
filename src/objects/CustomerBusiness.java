package objects;

public class CustomerBusiness extends Customer {
	private String VATTaxNUM, compName, CompAddress;
	private Car car;
	
	public CustomerBusiness(String id, int numOfServices) {
		super(id, numOfServices);
	}

	public CustomerBusiness(String id, int numOfServices, String vatTaxNum, String name, String address) {
		super(id, numOfServices);
		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
	}

	public CustomerBusiness(String id, int numOfServices, String vatTaxNum, String name, String address, Car car) {
		super(id, numOfServices);
		this.setVATTaxNUM(vatTaxNum);
		this.setCompName(name);
		this.setCompAddress(address);
		this.setCar(car);
	}

	//getters & setters

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
