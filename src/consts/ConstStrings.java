package consts;

public class ConstStrings {
	public static final String PDF_INVOICE = "invoice pdf";
	public static final String PDF_SALE_REPORT = "sale pdf";
	public static final String PDF_STOCK_REPORT = "stock pdf";
	public static final String PDF_REPAK_REPORT = "repak pdf";
	public final String ITEM_QNT_ZERO = "item qnt zero";
	public final String ENTER_DETAILS = "company details";
	public final String APP = "app";
	public final String CHECKBOX_LBL = "I/B";
	public final String PDF_EXT = ".pdf";
	public final String ERR_LOG = "ERR_";
	public final String INFO_LOG = "INFO_";

	//DATE RELATED
	public final String[] YEARS_NO_STRING = {"2017", "2018", "2019", "2020"};
	public final String DATE_FORMAT = "dd-MM-yyyy";//"yyyy-MM-dd" ?? not sure which format is correct for a database entry. so far works
	public final String DATE_FORMAT_REVERSE = "yyyy-MM-dd";
	public final String DATE_TIME_FORMAT = "dd-MM-yyyy @HH:mm:ss";
	public final String DATE_TIME_FORMAT_REVERSE = "yyyy-MM-dd @HH:mm:ss";
	public final String YEARS = "years";
	public final String SALE_REPORT_HEADINGS = "sale report headings";
	public final String STOCK_REPORT_HEADINGS = "stock report headings";
	public final String REPAK_REPORT_HEADINGS = "repak report headings";
	public final String REPAK_TB_COL_NAMES = "repak tb col names";

	// ITEM CODES
	public final String TYRE_CODE_C = "TRC";
	public final String TYRE_CODE_A = "TRA";
	public final String TUBE_CODE = "TB";
	public final String SERVICE_CODE = "SR";
	public final String SHOP_CODE = "SH";
	public final String OTHER_CODE = "OT";
	public final String CARWASH_CODE = "CW";
	public final String[] ITEM_CODES = {"TRC", "TRA", "TB", "SH", "SR", "OT", "CW"};
	
	// CUSTOMER CODES
	public final String CUST_IND_CODE = "C_";
	public final String CUST_BUS_CODE = "B_";

	// TABLE HEADINGS ETC
	public final String[] CARS_TABLE_HEADING = {"Cars"};
	public final String[] STOCK_TB_HEADINGS = {"ID", "Kod", "Nazwa", "Koszt", "Cena", "Qnt", "VAT", "Transport", "VEMC"};
	public final String[] STOCK_TB_HEADINGS_SHORT =  {"Kod","Towar", "Cena", "Qnt", "Cost"};
	public final String[] CUSTOMER_TB_HEADINGS = {"Customer", "B/I", "VAT/TAX", "No of Serv"};
	public final String[] INVOICES_TB_HEADINGS = {"ID", "Klient", "Data", "Zakupy", "Plik"};

	public final String SALE_REPORTS = "SALE REPORTS";
	public final String CARS_TB_NAME = "CARS";
	public final String STOCK_TB_NAME = "STOCK";
	public final String CUSTOMER_TB_NAME = "CUSTOMER";
	public final String CHOSEN_TB_NAME = "CHOOSEN";
	public final String INVOICE_TB_NAME = "INVOICES TABLE";

	public final String[] DAYS_NUM_31 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	public final String[] DAYS_NUM_30 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"};
	public final String[] DAYS_NUM_29 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29"};
	public final String[] DAYS_NUM_28 = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28"};
	public final String MONTHS_NAMES = "months names";
	
	//PATTERNS AND FORMATS
	public final String DECIMAL_PATTERN = "^-?([0-9]*)\\.([0-9]*)+$";	
	public final String REGEX_FILTER = "(?i).*\\Q";	
	public final String REPLACE_CHAR_PATTERN = "[^a-zA-Z0-9\\d\\s,]";	
	public final String SPECIAL_CHAR_PATTERN = "^.*[^a-zA-Z0-9\\d\\s,].*$";	
	public final String INTEGER_PATTERN = "^-?\\d+$";
	public final String DECIMAL_FORMAT_3_2 = "000.00";
	public final String DECIMAL_FORMAT_4_2 = "0,000.00";
	public final String DECIMAL_FORMAT_5_2 = "00,000.00";

	public final String DEFAULT_REGISTRATION = "00AA0000";
	
	public final String CAR = "car";
	public final String CUSTOMER_I = "customer individual";
	public final String CUSTOMER_B = "customer business";
	public final String INVOICE = "invoice";
	public final String ITEM = "item";
	public final String WASH = "Wash";

	public final String REPLACE_PATTERN = "#\\?.*?";

	// SYMBOLS ETC
	public final String HASH = "#";
	public final String AT = "@";
	public final String AMP = "&";
	public final String CARET = "^";
	public final String STAR = "*";
	public final String EURO = "� ";
	public final String PLUS = "+";
	public final String PERCENT = "%";
	public final String MINUS = "-";
	public final String COMA = ",";
	public final String DOT = ".";
	public final String UNDERSCORE = "_";
	public final String SEMICOLON = ";";
	public final String SLASH = "\\";
	public final String SPACE = " ";
	public final String NA = "n/a";

	// JSON USER FILE
	public final String JSON_COMPANY_NAME = "company";
	public final String JSON_VAT = "vat number";
	public final String JSON_ADDRESS = "address";
	public final String JSON_TOWN = "town";
	public final String JSON_COUNTY = "county";
	public final String JSON_POST_CODE = "post code";
	public final String JSON_TELEPHONE = "telephone";
	public final String JSON_EMAIL = "email";
	public final String JSON_WWW = "www";
	public final String JSON_FB = "fb";
	public final String LOGO_PATH = "logo path";
	public final String NUMBER_OF_SERVICES = "number of services";
	//TODO add rest from json file
	
	// JSON SETTINGS FILE
	public final String APP_COLOR_R = "app color R";
	public final String APP_COLOR_G = "app color G";
	public final String APP_COLOR_B = "app color B";
	public final String FONT_SIZE_DEF = "fs def";
	public final String FONT_SIZE_TITLE = "fs lbl title";
	public final String FONT = "font";
	public final String JLANG = "lang";
	public final String JSTART = "starting";

	public  final String DB_URL = "db host";
	public  final String DB_USER = "db user";
	public  final String DB_PASS = "db pass";
	
	public  final String INVOICE_PATH = "invoice folder path";
	public  final String SALES_PATH = "sales folder path";
	public  final String STOCK_REP_PATH = "stock folder path";
	public  final String REPAK_REP_PATH = "repak folder path";
	public  final String DOC_SUB_PATH = "doc subfolder path";

	public  final String SUPLIERS_NAMES_ARR = "suppliers names";
	public  final String SUPPLIERS_CHARGES_ARR = "transport charges";


	// JSON LANG PACKET
	public final String TYRES_CATEGORY = "tyre categories";
	public final String ITEM_CATEGORY = "items cat";
	public final String ITEM_CODES_ARR = "items codes for print";
	public final String ITEM_CODE_ARR = "items codes";
	public final String ITEM_NAMES_ARR = "items names";

	public final String BTN_NEXT = "next";
	public final String BTN_SAVE = "save";
	public final String FILL_UP = "fill_up";
	public final String BTN_BACK = "back";
	public final String BTN_EDIT = "edit";
	public final String BTN_CLOSE = "close";
	public final String BTN_DELETE = "delete";
	public final String BTN_NEW = "new";
	public final String BTN_CALCULATE = "calculate";
	public final String BTN_INVOICE = "invoice";
	public final String BTN_PRINT = "print";
	public final String BTN_INVOICES = "btn invoices";
	public final String BTN_SALES_REPORT = "btn sales report";
	public final String BTN_REPAK_REPORT = "btn repak";
	public final String BTN_UPDATE = "btn update";
	
	public final String RB_TYPED_IN = "typed";
	public final String RB_SUGGESTED = "suggested";

	public final String CBX_AIRFRESH = "airfreshener";

	public final String LBL_YOU = "you";
	public final String LBL_LOGO= "logo";
	public final String LBL_DATE = "date";
	public final String LBL_INVOICE_FOR = "invoice for";
	
	public final String LBL_NAME = "name";
	public final String LBL_CODE = "code";
	public final String LBL_COST = "cost";
	public final String LBL_PRICE = "price";
	public final String LBL_QNT = "qnt";
	public final String LBL_VAT = "vat";
	public final String LBL_TRANSPORT = "transport";
	public final String LBL_VEMC = "vemc";
	public final String LBL_SUGGESTED_COST = "s_cost";
	public final String LBL_SUGGESTED_PRICE = "s_price";
	public final String LBL_CUSTOMER = "customer";
	public final String LBL_COMPANY = "company";
	public final String LBL_STOCK = "stock_lbl";
	public final String LBL_PREVIEW = "preview";
	public final String LBL_DISCOUNT = "discount";
	public final String LBL_TOTAL = "total";
	public final String LBL_DAILY_REPORT = "daily report";
	public final String LBL_MONTHLY_REPORT = "monthly report";
	public final String LBL_PICK_DATE = "pick date";

	public final String TF_CUST_HINT = "tf cust hint";

	public final String SEARCH_TEXT_FIELD_FRAZE = "search";
	public final String BRAND = "brand";

	public final String[] LANG = {"ENGLISH", "POLISH"};
	
	public final String SAVED_MSG = "saved";
	public final String INVOICE_SAVED_1 = "saved invoice 1";
	public final String INVOICE_SAVED_2 = "saved invoice 2";
	public final String DELETE_MSG = "delete_msg";
	
	public final String ITEM_ADD_ERROR = "item_add_error";
	public final String ITEM_EDITION_ERROR = "item_edition_error";
	public final String ITEM_EDITION_ERROR_MSG = "saving error";
	public final String ITEM_DELETING_ERROR = "delete_error";
	public final String NOT_NUMBER_ERROR = "not_number_error";
	public final String ENTER_TEXT =  "enter search fraze";
	public final String TABLE_EMPTY = "empty table";
	public final String COMA_ERROR = "coma error";
	public final String SAVE_PDF = "save pdf";
	public final String PDF_SAVE_ERROR = "save pdf error";
	public final String PDF_CREATION_ERROR = "pdf creation error";
	public final String OTHER_STRING = "other";
	public final String PRINTER_NAME = "Canon MP620 series Printer (Copy 1)";
	public final String DEF_PRINTER_NAME = "default printer name";
	
	public final String PDF_SALES_HEADER = "pdf sales header";
	public final String TYRE_CHECK_MESSAGE_1 = "tyre check msg1";
	public final String TYRE_CHECK_MESSAGE_2 = "tyre check msg2";
	public final String TYRE_CHECK_MESSAGE_3 = "tyre check msg3";
	public final String PRINTING_PDF_ERROR = "pdf print error";
	public final String PRINTER_NAME_ERROR = "printer name error";
	public final String NONAME = "noname";
	public final String NOT_INT_ERROR = "not int";
	public final String NOT_DOUBLE_ERROR = "not double";
	public final String ALL_EN = "ALL";
	public final String ALL_PL = "WSZYSTKO";
	public final String PROFITS = "profits";
	public final String UPDATE_SELECTED_ERR = "updated selected err";
	public final String JL_NEW_ITEM_SAVED = "new item saved";
	public final String JL_ITEM_SAVED = "item saved";
	public final String JL_ITEM_UPDATED = "item updated";
	public final String JL_WARN_ITEM_UPDATE = "warn item update";
	public final String JL_OPTIONS_BTN = "btn options";
}