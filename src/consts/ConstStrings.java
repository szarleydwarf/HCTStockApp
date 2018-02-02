package consts;

public class ConstStrings {
	//DATE RELATED
	public final String[] YEARS_NO_STRING = {"2017", "2018", "2019", "2020"};
	public final String[] MONTHS_NAMES = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};
	public final String DATE_FORMAT = "dd-MM-yyyy";//"yyyy-MM-dd" ?? not sure which format is correct for a database entry. so far works
	public final String DATE_FORMAT_REVERSE = "yyyy-MM-dd";
	public final String DATE_TIME_FORMAT = "dd-MM-yyyy @HH:mm:ss";
	public final String DATE_TIME_FORMAT_REVERSE = "yyyy-MM-dd @HH:mm:ss";

	// ITEM CODES
	public final String TYRE_CODE = "TR";
	public final String TUBE_CODE = "TB";
	public final String SERVICE_CODE = "SR";
	public final String OTHER_CODE = "OT";
	public final String CARWASH_CODE = "CW";
	
	// CUSTOMER CODES
	public final String CUST_IND_CODE = "C_";
	public final String CUST_BUS_CODE = "B_";


	//PATTERNS AND FORMATS
	public final String DECIMAL_PATTERN = "^-?([0-9]*)\\.([0-9]*)+$";	
	public final String INTEGER_PATTERN = "^-?\\d+$";
	public final String DECIMAL_FORMAT_3_2 = "000.00";
	public final String DECIMAL_FORMAT_5_2 = "00000.00";

	public final String DEFAULT_REGISTRATION = "00AA0000";
	
	public final String CAR = "car";
	public final String CUSTOMER_I = "customer individual";
	public final String CUSTOMER_B = "customer business";
	public final String INVOICE = "invoice";
	public final String ITEM = "item";
	
}
