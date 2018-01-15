package consts;

public class ConstDB {
	public ConstDB(){
		
	}
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hct_stock";
//	public final String DEFAULT_DATABASE_PATH = "https:\\sqliteonline.com\\#fiddle-5a53891a14ced1h9jc6cc66a";

    //  Database credentials
	public static final String USER = "hct";
	public static final String PASS = "pass1234";

	//database query parts
	public static final String VALUES = " VALUES ";
	public static final String INSERT = "INSERT INTO ";
	public static final String UPDATE = "UPDATE ";
	public static final String SELECT = "SELECT ";
	public static final String SELECT_STAR = "SELECT * ";
	public static final String LIMIT = " LIMIT ";
	
	//database tables names
	public static enum TableNames {
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

//	public static final String TB_BRANDS = "brands";
//	public static final String TB_BUSINESS = "business";
//	public static final String TB_CUSTOMRS = "customers";
//	public static final String TB_INVOICES = "invoices";
//	public static final String TB_REPAK = "repak_report";
//	public static final String TB_SETTINGS = "settings";
//	public static final String TB_STOCK = "stock";

	//brands table column names
	public static final String TB_BRANDS_ID = "idbrand";
	public static final String TB_BRANDS_NAME = "brand";
	
	//stock table column names
	public static final String TB_STOCK_ID = "id";
	public static final String TB_STOCK_NAME = "name";
	public static final String TB_STOCK_COST = "cost";
	public static final String TB_STOCK_PRICE = "price";
	public static final String TB_STOCK_QNT = "qnt";
	
	
	
	//Database query's
	public static final String SELECT_CARS_LIST_BRAND_ID = "SELECT brand, idbrand FROM brands";
	public static final String SELECT_CARS_LIST_ID_BRAND = "SELECT idbrand, brand FROM brands";
	public static final String SELECT_ALL_ITEMS = "SELECT * FROM "+TableNames.TB_STOCK.getName();
	

}
