package consts;

public class ConstDB {
	//default constructor
	public ConstDB(){	}

	public static final String CREATE_BRANDS_TABLE = "CREATE TABLE IF NOT EXISTS `brands` (`id` int(11) NOT NULL AUTO_INCREMENT,`brand` varchar(64) NOT NULL, PRIMARY KEY (`id`))";
	public static final String CREATE_BUSINESS_TABLE = "CREATE TABLE IF NOT EXISTS `business` ( `id` int(11) NOT NULL, `vat_tax` varchar(64) NOT NULL, `name` varchar(128) NOT NULL, `address` varchar(256) NOT NULL, `cars` varchar(64) NOT NULL, `no_of_services` int(11) NOT NULL, PRIMARY KEY (`id`), UNIQUE KEY `vat_tax` (`vat_tax`));";
	public static final String CREATE_CAR_TABLE = "CREATE TABLE IF NOT EXISTS `car` (  `id` int(11) NOT NULL,  `registration` varchar(10) NOT NULL DEFAULT '00AA00000',  `brand_id` int(11) NOT NULL DEFAULT '45',  PRIMARY KEY (`id`),  KEY `brand_id` (`brand_id`));";
	public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS `customers` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `car_id` int(11) NOT NULL,  `no_of_services` int(11) NOT NULL DEFAULT '0',  PRIMARY KEY (`id`),  KEY `car_id` (`car_id`));";
	public static final String CREATE_INVOICE_TABLE = "CREATE TABLE IF NOT EXISTS `invoices` (  `id` int(11) NOT NULL,  `customer` varchar(128) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,  `stock_codes` varchar(1024) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,  `discount` decimal(6,2) NOT NULL,  `ispercent` tinyint(1) NOT NULL,  `total` decimal(6,2) NOT NULL,  `date` varchar(16) NOT NULL,  `file_name` varchar(256) CHARACTER SET utf16 COLLATE utf16_polish_ci NOT NULL,  PRIMARY KEY (`id`));";
	public static final String CREATE_REPAK_REPORT_TABLE = "CREATE TABLE IF NOT EXISTS `repak_report` (  `id` int(11) NOT NULL,  `date` date NOT NULL,  `sold_car` int(11) NOT NULL,  `fitted_car` int(11) NOT NULL,  `bought_car` int(11) NOT NULL,  `sold_agri` int(11) NOT NULL,  `fitted_agri` int(11) NOT NULL,  `bought_agri` int(11) NOT NULL,  PRIMARY KEY (`id`));";
	public static final String CREATE_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS `settings` (  `id` int(11) NOT NULL,  `path` varchar(256) NOT NULL,  PRIMARY KEY (`id`));";
	public static final String CREATE_STOCK_TABLE = "CREATE TABLE IF NOT EXISTS `stock` (  `id` int(11) NOT NULL AUTO_INCREMENT,  `code` varchar(4) DEFAULT NULL,  `name` varchar(128) DEFAULT NULL,  `cost` decimal(6,2) DEFAULT NULL,  `addVat` tinyint(4) DEFAULT '0',  `addTransit` tinyint(4) DEFAULT '0',  `addVEMC` tinyint(4) NOT NULL DEFAULT '0',  `price` decimal(6,2) DEFAULT NULL,  `qnt` int(11) NOT NULL,  PRIMARY KEY (`id`));";
	
	public static final String ALTER_CAR_TABLE = "ALTER TABLE `car`  ADD CONSTRAINT `car_ibfk_1` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);";
	public static final String ALTER_CUSTOMER_TABLE = "ALTER TABLE `customers`  ADD CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`);";

	public static final String POPULATE_BRANDS = "INSERT INTO `brands` (`id`, `brand`) VALUES(1, 'Volkswagen'),(2, 'Toyota'),(3, 'Ford'),(4, 'Mazda'),(5, 'Skoda'),(6, 'Renault'),(7, 'Citroen'),(8, 'Peugeot'),(9, 'Audi'),(10, 'BMW'),(11, 'Mercedes'),(12, 'Opel'),(13, 'Porsche'),(14, 'Suzuki'),(15, 'Mitshubishi'),(16, 'Lancia'),(17, 'Fiat'),(18, 'Alfa Romeo'),(19, 'Honda'),(20, 'Isuzu'),(21, 'Nissan'),(22, 'Subaru'),(23, 'Lexus'),(24, 'Kia'),(25, 'Hyundai'),(26, 'SSangyong'),(27, 'Daewoo'),(28, 'Chevrolet'),(29, 'Vauxhal'),(30, 'Seat'),(31, 'Saab'),(32, 'Volvo'),(33, 'Bentley'),(34, 'Aston Martin'),(35, 'Jaguar'),(36, 'Land Rover'),(37, 'Range Rover'),(38, 'Rover'),(39, 'Mini'),(40, 'Lotus'),(41, 'Rols Roys'),(42, 'Chrysler'),(43, 'Dodge'),(44, 'Jeep'),(45, 'Other');";
	
	
	public  final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  

    //  Database credentials
//	public  final String DB_URL = "jdbc:mysql://127.0.0.1:3306/hct_stock";
//	public  final String USER = "hct";
//	public  final String PASS = "pass1234";

// life
//	public  final String DB_URL = "jdbc:mysql://https//188.128.207.140/sql/";
//	public  final String USER = "19519373_hct";
//	public  final String PASS = "Pa55W0rd1!";

	public  final String DB_URL = "jdbc:mysql://sql2.freemysqlhosting.net";
	public  final String USER = "sql2226361";
	public  final String PASS = "hB7%wY5!";

	public final String USE_DATABASE = "USE "+USER;

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

	
	//database tables names
	public enum TableNames {
		TB_BRANDS("brands"), TB_BUSINESS("business"), TB_CUSTOMERS("customers"),
		TB_INVOICES("invoices"), TB_REPAK("repak_report"), TB_SETTINGS("settings"), TB_STOCK("stock"), TB_CARS("car");
		
		private String name;		
		private TableNames(String name) { this.setName(name); }
		public String getName() { return name; }
		public void setName(String name) { this.name = name; }	
	}

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
	public final String TB_INVOICE_DATE = "date";
	public final String TB_INVOICE_FILE_NAME = "file_name";
	
	//Database query's
	public final String SELECT_CARS_LIST_BRAND_ID = this.SELECT+this.TB_BRANDS_NAME+", "+this.ID+this.FROM+TableNames.TB_BRANDS.getName();
	public final String SELECT_CARS_LIST_ID_BRAND = this.SELECT+ this.ID+","+ this.TB_BRANDS_NAME+this.FROM+TableNames.TB_BRANDS.getName();
	public final String SELECT_ALL_ITEMS = this.SELECT_STAR  + this.FROM + TableNames.TB_STOCK.getName();
	public final String SELECT_ALL_CUSTOMERS_I = this.SELECT_STAR  + this.FROM + TableNames.TB_CUSTOMERS.getName();
	public final String SELECT_ALL_CUSTOMERS_B = this.SELECT + TableNames.TB_BUSINESS.getName() + ".* " + this.FROM + TableNames.TB_BUSINESS.getName();
	public final String SELECT_ALL_INVOICES = this.SELECT_STAR + this.FROM + TableNames.TB_INVOICES.getName();
	public final String SELECT_LAST_INVOICE = this.SELECT + "id" + this.FROM + TableNames.TB_INVOICES.getName() + this.ODER_BY  + "id" + this.DESC + this.LIMIT + "1;";

	public final String SELECT_CAR = this.SELECT_STAR + this.FROM + TableNames.TB_CARS.getName() + this.WHERE + this.ID + this.EQUAL;

}