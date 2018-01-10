package objects;

/**
 * @author RJ
 *
 */
public class Item {
	private String stockNumber;
	private String name;
	private double cost;
	private double price;
	private int qnt;
	
	public Item(String p_stock_number, String p_name, double p_cost, double p_price){
		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.price = p_price;
		this.qnt = 0;
	}	
	
	public Item(String p_stock_number, String p_name, double p_cost, double p_price, int qnt){
		this.stockNumber = p_stock_number;
		this.name = p_name;
		this.cost = p_cost;
		this.price = p_price;
		this.qnt = qnt;
	}
	
	//getters & setters
	public String getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQnt() {
		return qnt;
	}

	public void setQnt(int qnt) {
		this.qnt = qnt;
	}

}
