package consts;

public class ConstDB {
	//default constructor
	public ConstDB(){	}

	public  final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  


	//database query parts
	public  final String VALUES = " VALUES ";
	public  final String INSERT = "INSERT INTO ";
	public  final String UPDATE = "UPDATE ";
	public final String DELETE = "DELETE";
	public  final String SELECT = "SELECT ";
	public  final String SELECT_STAR = "SELECT * ";
	public  final String LIMIT = " LIMIT ";
	public  final String FROM = " FROM ";
	public  final String WHERE = " WHERE ";
	public  final String LIKE = " LIKE ";
	public  final String AND = " AND ";
	public  final String OR = " OR ";
	public  final String EQUAL = "=";
	public final String SET = " SET ";
	public final String ODER_BY = " ORDER BY ";
	public final String DESC = " DESC ";
	public final String ASC = " ASC ";

	
	//database tables names
	public enum TableNames {
		TB_BRANDS("brands"), TB_BUSINESS("business"), TB_CUSTOMERS("customers"),
		TB_INVOICES("invoices"), TB_REPAK("repak_report"), TB_SETTINGS("settings"), TB_STOCK("stock"), TB_CARS("car");
		
		private String name;		
		private TableNames(String name) { this.setName(name); }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }	
	}

	public final String DATE = "date";
	public final String ID = "id";

	//brands table column names
	public final String TB_BRANDS_NAME = "brand";
	
	//stock table column names
	public final String TB_STOCK_CODE = "code";
	public final String TB_STOCK_NAME = "name";
	public final String TB_STOCK_COST = "cost";
	public final String TB_STOCK_PRICE = "price";
	public final String TB_STOCK_QNT = "qnt";
	public final String TB_STOCK_VAT = "addVat";
	public final String TB_STOCK_TRANSPORT = "addTransit";
	
	//car table column names
	public final String TB_CARS_REGISTRATION = "registration";
	public final String TB_CARS_BRAND_ID = "brand_id";
	
	// costumers table column names
	public final String TB_CUSTOMER_CAR_ID = "car_id";
	public final String TB_CUSTOMER_NO_OF_SERVICES = "no_of_services";
	
	// business costumers table column names
	public final String TB_B_CUSTOMER_VAT_TAX = "vat_tax";
	public final String TB_B_CUSTOMER_NAME = "name";
	public final String TB_B_CUSTOMER_ADDRESS = "address";
	public final String TB_B_CUSTOMER_CARS_LIST = "cars";
	
	// invoice table column names
	public final String TB_INVOICE_CUSTOMER_ID = "customer";
	public final String TB_INVOICE_STOCK_CODES = "stock_codes";
	public final String TB_INVOICE_DISCOUNT = "discount";
	public final String TB_INVOICE_IS_PERCENT = "ispercent";
	public final String TB_INVOICE_TOTAL = "total";
	public final String TB_INVOICE_FILE_NAME = "file_name";
	
	//stock table column names
	public final String TB_REPAK_SOLD_CAR = "sold_car";
	public final String TB_REPAK_FITTED_CAR = "fitted_car";
	public final String TB_REPAK_BOUGHT_CAR = "bought_car";
	public final String TB_REPAK_SOLD_AGRI = "sold_agri";
	public final String TB_REPAK_FITTED_AGRI= "fitted_agri";
	public final String TB_REPAK_BOUGHT_AGRI= "bought_agri";
	public final String TB_REPAK_RETURNED_CAR = "returned_car";
	public final String TB_REPAK_RETURNED_AGRI = "returned_agri";
	
	//Database query's
	public final String SELECT_CARS_LIST_BRAND_ID = this.SELECT+this.TB_BRANDS_NAME+", "+this.ID+this.FROM+TableNames.TB_BRANDS.getName();
	public final String SELECT_CARS_LIST_ID_BRAND = this.SELECT+ this.ID+","+ this.TB_BRANDS_NAME+this.FROM+TableNames.TB_BRANDS.getName();
	public final String SELECT_ALL_ITEMS = this.SELECT_STAR  + this.FROM + TableNames.TB_STOCK.getName();
	public final String SELECT_ALL_ITEMS_ORDERED = this.SELECT_STAR  + this.FROM + TableNames.TB_STOCK.getName() + this.ODER_BY + this.TB_STOCK_CODE + this.ASC;
	public final String SELECT_ALL_CUSTOMERS_I = this.SELECT_STAR  + this.FROM + TableNames.TB_CUSTOMERS.getName();
	public final String SELECT_ALL_CUSTOMERS_B = this.SELECT + TableNames.TB_BUSINESS.getName() + ".* " + this.FROM + TableNames.TB_BUSINESS.getName();
	public final String SELECT_ALL_INVOICES = this.SELECT_STAR + this.FROM + TableNames.TB_INVOICES.getName() + this.ODER_BY + this.DATE + this.DESC;
	public final String SELECT_LAST_INVOICE = this.SELECT + "id" + this.FROM + TableNames.TB_INVOICES.getName() + this.ODER_BY  + this.ID + this.DESC + this.LIMIT + "1;";

	public final String SELECT_ALL_REPAK_REPORTS = this.SELECT_STAR + this.FROM + TableNames.TB_REPAK.getName();;
	public final String SELECT_CAR = this.SELECT_STAR + this.FROM + TableNames.TB_CARS.getName() + this.WHERE + this.ID + this.EQUAL;
	public final String GET_LAST_MONTH = this.SELECT + this.DATE + this.FROM + TableNames.TB_REPAK.getName();
	public final String NEW_REPAK_ENTRY = this.INSERT + TableNames.TB_REPAK.getName() + this.VALUES + " (";
			 

}