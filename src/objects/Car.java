package objects;

public class Car {
	private String brand, registration;
	private int id;
	
	public Car (String brand, String registration, int id) {
		this.brand = brand;
		this.registration = registration;
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getRegistration() {
		return registration;
	}

	public void setRegistration(String registration) {
		this.registration = registration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
