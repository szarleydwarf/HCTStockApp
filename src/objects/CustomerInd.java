package objects;

public class CustomerInd extends Customer{
	private Car car;
	
	public CustomerInd(String id, int numOfServices, Car car) {
		super(id, numOfServices);

		this.setCar(car);
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

}
