package objects;

public abstract class Customer {
	private int numOfServices;
	private String id;
	
	public Customer(String id, int numOfServices){
		this.setId(id);
		this.setNumOfServices(numOfServices);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumOfServices() {
		return numOfServices;
	}

	public void setNumOfServices(int numOfServices) {
		this.numOfServices = numOfServices;
	}
	public abstract boolean equals(Object o);
}
