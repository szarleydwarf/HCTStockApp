package consts;

public class ConstDB {
	public ConstDB(){
		
	}
	public  final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public  final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hct_stock";
//	public final String DEFAULT_DATABASE_PATH = "https:\\sqliteonline.com\\#fiddle-5a53891a14ced1h9jc6cc66a";

    //  Database credentials
	public  final String USER = "hct";
	public  final String PASS = "pass1234";

	//database query parts
	public  final String VALUES = " VALUES ";
	public  final String INSERT = "INSERT INTO ";
	public  final String UPDATE = "UPDATE ";
	public  final String SELECT = "SELECT ";
	public  final String SELECT_STAR = "SELECT * ";
	public  final String LIMIT = " LIMIT ";
	
	//database tables names
	public  enum TableNames {
		TB_BRANDS("brands"), TB_BUSINESS("business"), TB_CUSTOMRS("customers"),
		TB_INVOICES("invoices"), TB_REPAK("repak_report"), TB_SETTINGS("settings"), TB_STOCK("stock");
		
		private String name;		
		
		private TableNames(String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}	
	}

//	public  final String TB_BRANDS = "brands";
//	public  final String TB_BUSINESS = "business";
//	public  final String TB_CUSTOMRS = "customers";
//	public  final String TB_INVOICES = "invoices";
//	public  final String TB_REPAK = "repak_report";
//	public  final String TB_SETTINGS = "settings";
//	public  final String TB_STOCK = "stock";

	//brands table column names
	public final String TB_BRANDS_ID = "idbrand";
	public final String TB_BRANDS_NAME = "brand";
	
	//stock table column names
	public final String TB_STOCK_ID = "id";
	public final String TB_STOCK_NAME = "name";
	public final String TB_STOCK_COST = "cost";
	public final String TB_STOCK_PRICE = "price";
	public final String TB_STOCK_QNT = "qnt";
	
	
	
	//Database query's
	public final String SELECT_CARS_LIST_BRAND_ID = "SELECT brand, idbrand FROM brands";
	public final String SELECT_CARS_LIST_ID_BRAND = "SELECT idbrand, brand FROM brands";
	public final String SELECT_ALL_ITEMS = "SELECT * FROM "+TableNames.TB_STOCK.getName();
	

}
