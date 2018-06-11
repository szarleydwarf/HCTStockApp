package consts;

public class ConstStrings {

	public final String PDF_INVOICE = "invoice pdf";
	public final String PDF_SALE_REPORT = "sale pdf";
	public final String PDF_STOCK_REPORT = "stock pdf";
	public final String PDF_REPAK_REPORT = "repak pdf";

	public final String APP = "app";
	public final String PDF_EXT = ".pdf";
	public final String ERR_LOG = "ERR_";
	public final String INFO_LOG = "INFO_";
	public final String CAR = "car";
	public final String ALL_EN = "ALL";
	public final String ALL_PL = "WSZYSTKO";

	public final String PRINTER_NAME = "Canon MP620 series Printer (Copy 1)";

	
	// DATE
	public final String DATE_FORMAT = "dd-MM-yyyy";//"yyyy-MM-dd" ?? not sure which format is correct for a database entry. so far works
	public final String DATE_FORMAT_REVERSE = "yyyy-MM-dd";
	public final String DATE_TIME_FORMAT = "dd-MM-yyyy @HH:mm:ss";
	public final String DATE_TIME_FORMAT_REVERSE = "yyyy-MM-dd @HH:mm:ss";


	// CUSTOMER CODES
	public final String CUST_IND_CODE = "C_";
	public final String CUST_BUS_CODE = "B_";

	//PATTERNS AND FORMATS
	public final String DECIMAL_PATTERN = "^-?([0-9]*)\\.([0-9]*)+$";	
	public final String REGEX_FILTER = "(?i).*\\Q";	
	public final String REPLACE_CHAR_PATTERN = "[^a-zA-Z0-9\\d\\s,]";	
	public final String SPECIAL_CHAR_PATTERN = "^.*[^a-zA-Z0-9\\d\\s,].*$";	
	public final String INTEGER_PATTERN = "^-?\\d+$";
	public final String REPLACE_PATTERN = "#\\?.*?";

	public final String DECIMAL_FORMAT_3_2 = "000.00";
	public final String DECIMAL_FORMAT_4_2 = "0,000.00";
	public final String DECIMAL_FORMAT_5_2 = "00,000.00";

	public final String DEFAULT_REGISTRATION = "00AA0000";

	// SYMBOLS ETC
	public final String HASH = "#";
	public final String AT = "@";
	public final String AMP = "&";
	public final String CARET = "^";
	public final String STAR = "*";
	public final String EURO = "€ ";
	public final String PLUS = "+";
	public final String PERCENT = "%";
	public final String MINUS = "-";
	public final String COMA = ",";
	public final String DOT = ".";
	public final String UNDERSCORE = "_";
	public final String SEMICOLON = ";";
	public final String SLASH = "\\";
	public final String SPACE = " ";


	// TABLE NAMES
	public final String SALE_REPORTS = "SALE REPORTS";
	public final String CARS_TB_NAME = "CARS";
	public final String STOCK_TB_NAME = "STOCK";
	public final String CUSTOMER_TB_NAME = "CUSTOMER";
	public final String CHOSEN_TB_NAME = "CHOOSEN";
	public final String INVOICE_TB_NAME = "INVOICES TABLE";

	// JSON USER FILE
	public final String JU_COMPANY_NAME = "company";
	public final String JU_VAT = "vat number";
	public final String JU_ADDRESS = "address";
	public final String JU_TOWN = "town";
	public final String JU_COUNTY = "county";
	public final String JU_POST_CODE = "post code";
	public final String JU_TELEPHONE = "telephone";
	public final String JU_EMAIL = "email";
	public final String JU_WWW = "www";
	public final String JU_FB = "fb";
	
	
	// JSON SETTINGS FILE
	public final String JS_NUMBER_OF_SERVICES = "number of services";
	public final String JS_APP_COLOR_R = "app color R";
	public final String JS_APP_COLOR_G = "app color G";
	public final String JS_APP_COLOR_B = "app color B";
	public final String JS_FONT_SIZE_DEF = "fs def";
	public final String JS_FONT_SIZE_TITLE = "fs lbl title";
	public final String JS_FONT = "font";
	public final String JS_LANG = "lang";
	public final String JS_START = "starting";

	public final String JS_DB_URL = "db host";
	public final String JS_DB_USER = "db user";
	public final String JS_DB_PASS = "db pass";
	public final String JS_LOGO_PATH = "logo path";
	public final String JS_DEF_PRINTER_NAME = "default printer name";
	
	public final String JS_INVOICE_PATH = "invoice folder path";
	public final String JS_SALES_PATH = "sales folder path";
	public final String JS_STOCK_REP_PATH = "stock folder path";
	public final String JS_REPAK_REP_PATH = "repak folder path";
	public final String JS_DOC_SUB_PATH = "doc subfolder path";

	public final String JS_SUPLIERS_NAMES_ARR = "suppliers names";
	public final String JS_SUPPLIERS_CHARGES_ARR = "transport charges";
	public final String JS_PROFITS = "profits";
	
	// JSON LANG PACKET
	public final String BTN_NEXT = "next";
	public final String BTN_SAVE = "save";
	public final String BTN_BACK = "back";
	public final String BTN_EDIT = "edit";
	public final String BTN_CLOSE = "close";
	public final String BTN_DELETE = "delete";
	public final String BTN_NEW = "new";
	public final String BTN_INVOICE = "invoice";
	public final String BTN_PRINT = "print";
	public final String BTN_INVOICES = "btn invoices";
	public final String BTN_SALES_REPORT = "btn sales report";
	public final String BTN_REPAK_REPORT = "btn repak";
	public final String BTN_UPDATE = "btn update";
	
	public final String RB_TYPED_IN = "typed";
	public final String RB_SUGGESTED = "suggested";
	
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
	public final String LBL_CHECKBOX_I_B = "I/B";
	
	public final String TF_CUST_HINT = "tf cust hint";
	public final String TF_SEARCH_FRAZE = "search";
	
	// INFOS
	public final String JL_FILL_UP = "fill_up";
	public final String JL_ENTER_DETAILS = "company details";
	public final String JL_A_LANG = "lang arr";
	public final String JL_A_ITEM_CODES = "item codes";
	public final String JL_ITEM_CATEGORY = "items cat";
	public final String JL_SAVE_PDF = "save pdf";
	public final String JL_SAVED_MSG = "saved";
	public final String JL_INVOICE_SAVED_1 = "saved invoice 1";
	public final String JL_INVOICE_SAVED_2 = "saved invoice 2";
	public final String JL_DELETE_MSG = "delete_msg";
	public final String JL_ENTER_TEXT = "enter search fraze";
	public final String JL_BRAND = "brand";
	public final String JL_OTHER_STRING = "other";
	public final String JL_NONAME = "noname";
	public final String JL_MONTHS_NAMES = "months names";

	public final String JL_TYRES_CATEGORY = "tyre categories";
	public final String JL_ITEM_CODE_ARR_PRINT = "items codes for print";
	public final String JL_ITEM_CODE_ARR = "items codes";
	public final String JL_ITEM_NAMES_ARR = "items names";

	// WARNING
	public final String JL_WARN_EMPTY_TABLE = "empty table";
	public final String JL_TYRE_CHECK_MESSAGE_1 = "tyre check msg1";
	public final String JL_TYRE_CHECK_MESSAGE_2 = "tyre check msg2";
	public final String JL_TYRE_CHECK_MESSAGE_3 = "tyre check msg3";

	// TABLE HEADINGS
	public final String[] CARS_TABLE_HEADING = {"Cars"};
	public final String JL_CUSTOMER_TB_HEADINGS = "customer_tb_headings";
	public final String JL_SALE_REPORT_HEADINGS = "sale report headings";
	public final String JL_STOCK_REPORT_HEADINGS = "stock report headings";
	public final String JL_REPAK_REPORT_HEADINGS = "repak report headings";
	public final String JL_STOCK_TB_HEADINGS = "stock_tb_headings";
	public final String JL_REPAK_TB_COL_NAMES = "repak tb col names";
	public final String JL_STOCK_TB_HEADINGS_SHORT = "stock_tb_headings_short";
	public final String JL_INVOICE_TB_HEADINGS = "invoices_tb_headings";
	
	// OTHER HEADINGS
	public final String JL_PDF_SALES_HEADER = "pdf sales header";


	
	// ERRORS
	public final String JL_ERR_ITEM_ADD = "item_add_error";
	public final String JL_ERR_ITEM_EDITION = "item_edition_error";
	public final String JL_ERR_ITEM_EDITION_SAVE = "saving error";
	public final String JL_ERR_ITEM_DELETING = "delete_error";
	public final String JL_ERR_NOT_NUMBER = "not_number_error";
	public final String JL_ERR_NOT_INT = "not int";
	public final String JL_ERR_NOT_DOUBLE = "not double";
	public final String JL_ERR_COMA = "coma error";
	public final String JL_ERR_PDF_SAVE = "save pdf error";
	public final String JL_ERR_PDF_CREATION = "pdf creation error";
	public final String JL_ERR_UPDATE_SELECTED = "updated selected err";
	public final String JL_ERR_PRINTING_PDF = "pdf print error";
	public final String JL_ERR_PRINTER_NAME = "printer name error";


	
}