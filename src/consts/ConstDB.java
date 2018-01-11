package consts;

public class ConstDB {
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	public static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hct_stock";
//	public final String DEFAULT_DATABASE_PATH = "https:\\sqliteonline.com\\#fiddle-5a53891a14ced1h9jc6cc66a";

    //  Database credentials
	public static final String USER = "hct";
	public static final String PASS = "pass1234";

	//Database query's
	public static final String SELECT_CARS_LIST_BRAND_ID = "SELECT brand, idbrand FROM brands";
	public static final String SELECT_CARS_LIST_ID_BRAND = "SELECT idbrand, brand FROM brands";

}
