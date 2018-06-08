package logic;

import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import frames.CompanyDetailsFrame;
import frames.mainFrame;
import managers.DatabaseManager;
import utility.DateHelper;
import utility.FileHelper;
import utility.LoadScreen;
import utility.Logger;
import utility.MiscHelper;

public class MainView {

	private JFrame frame;
	private CompanyDetailsFrame comanyDetailsFrame;
	protected static MainView main;
	private static DatabaseManager dm;
	private static LoadScreen ls;
	private static Logger logger;
	private static DateHelper dh;
	private static FileHelper fh;
	
	private static ConstDB cdb;
	private static ConstNums cn;
	private static ConstPaths cp;
	private static ConstStrings cs;
	private static String todayL, todayS;
	private static MiscHelper msh;
	private static boolean isNew;
	private static JSONObject jSettings;
	private static JSONObject jUser;
	private static JSONObject jLang;
	private static boolean isStarting;
	private static String todayYM;
	private static mainFrame mFrame;
	private static DecimalFormat df_3_2;
	

	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		MainView mView = new MainView();
		mView.run();
	}

	private void test() {
		// TODO Here I will be testing all of the functionalities
		System.out.println("TEST\n1");

		System.out.println("\n2");
		System.out.println("\n3");

		System.out.println();
	}

	private void run(){
		loadConst();
		loadJsonFiles();
		jLang = loadLanguage();

		try {
	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		isStarting = Boolean.parseBoolean(jSettings.get(cs.JS_START).toString());
		if(isStarting){
			ls = new LoadScreen(cp, cn);
//			updateJSONSettings(false);TODO
		}

		loader();
		isNew = checkInstallation();
		if(isNew){
			try {
				dm.checkIfDatabaseExists();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			comanyDetailsFrame.initialize();
		} else
			mFrame.initialize();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(isStarting)
						ls.splashScreenDestruct();
					
					
					if(!isNew){
						mFrame.setIsVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void updateJSONSettings(boolean isStarting) {
		JSONObject jo = new JSONObject();
		String t = ""; 
		
		for (Object key : jSettings.keySet()) {
			String keyStr = (String)key;
			if(keyStr.equals(cs.JS_START)){
				t = ""+isStarting;
				jo.put(keyStr, t);
			} else {
				Object k = jSettings.get(keyStr);
				if(k instanceof JSONArray){
					JSONArray j = (JSONArray) k;
					jo.put(keyStr, j);
				}
				else{
					t = (String) k;
					jo.put(keyStr, t);
				}
			}
		}
		boolean saved = msh.saveJSON(cp.JSON_SETTINGS_PATH, jo);
		if(!saved)
			logger.log(cs.ERR_LOG, "Fail to save JSON file in MAIN updateJSONSettings().");
	}

	private void loader() {
		System.out.println("loader");

		loadHelpers();
		setDates();
	
		loadClasses();

		checkLastDBUpdate();
//		test();
	}

	private void setDates() {
		todayL = dh.getFormatedDateAndTime();
		logger.setLongDate(todayL);
		
		todayS = dh.getFormatedDate();
		logger.setShortDate(todayS);
		
		todayYM = dh.getRevDateYM();
	}

	private void loadClasses() {
		System.out.println("loadClasses");
		
		mFrame = new mainFrame(main, cs, cn, cdb,cp, logger, jLang, jSettings, jUser, msh, dh, fh);
		comanyDetailsFrame = new CompanyDetailsFrame(dm, logger, cdb, cs, cn, cp, jSettings, jUser, jLang, msh);
	}

	private void loadConst() {
		System.out.println("loadConst");
		cdb = new ConstDB();
		cn = new ConstNums();
		cp = new ConstPaths();
		cs = new ConstStrings();
	}

	private void loadHelpers() {
		System.out.println("loadHelpers");
		dh = new DateHelper(cs, cn);
		fh = new FileHelper();

		logger = new Logger(dh, fh, cp.DEFAULT_LOG_PATH);

		msh = new MiscHelper(logger, cs, jLang, jSettings, fh);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols( new Locale("en", "UK"));
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(' ');

		try {
			dm = new DatabaseManager(logger, todayL, cdb, cn, cs, cp, jSettings);
		} catch (FileNotFoundException e) {
			logger.log(cs.ERR_LOG, "FileNotFoundException DM in Main "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.log(cs.ERR_LOG, "IOException DM in Main "+e.getMessage());
			e.printStackTrace();
		}
	}

	private void loadJsonFiles() {
		System.out.println("loadJSONFiles");
		JSONParser parser = new JSONParser();

		try {
			jSettings = (JSONObject) parser.parse(new FileReader(cp.JSON_SETTINGS_PATH));
			jUser = (JSONObject) parser.parse(new FileReader(cp.JSON_USER_PATH));
			jLang = loadLanguage();
		} catch (FileNotFoundException e1) {
			logger.log(cs.ERR_LOG, todayL+" loadJsonDefaultLang E1 FILE NOT FOUND "+"\t"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e2) {
			logger.log(cs.ERR_LOG, todayL+" loadJsonDefaultLang E2 IOException "+"\t"+e2.getMessage());
			e2.printStackTrace();
		} catch (ParseException e3) {
			logger.log(cs.ERR_LOG, todayL+" loadJsonDefaultLang E3 ParseException"+"\t"+e3.getMessage());
			e3.printStackTrace();
		}
		
	}

	private JSONObject loadLanguage() {
		JSONParser parser = new JSONParser();
		try {
			String lang = (String) jSettings.get(cs.JS_LANG);
			return (JSONObject) parser.parse(new FileReader(cp.JSON_LANG_PATH+lang));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		};
		return null;
	}

	private boolean checkInstallation() {
		if(jUser.get(cs.JU_COMPANY_NAME).equals("") 
			&& jUser.get(cs.JU_VAT).equals("") 
			&& jUser.get(cs.JU_ADDRESS).equals("") 
			&& jUser.get(cs.JU_TOWN).equals("") 
			&& jUser.get(cs.JU_COUNTY).equals("")){
			return true;
		} else {
			return false;
		}
	}

	private void checkLastDBUpdate() {
		String lastMonth = dm.selectData(cdb.GET_LAST_MONTH);
		System.out.println("last month "+lastMonth);
		if(!lastMonth.equals(todayYM)){
			String q = cdb.NEW_REPAK_ENTRY + "'" + todayYM + "', 0, 0, 0, 0, 0, 0, 0, 0)";
			dm.addNewRecord(q );
		}
	}



	// GETTERS & SETTERS
	public boolean isVisible(){
		return frame.isVisible();
	}
	
	public void setIsVisible(boolean b){
		frame.setVisible(b);
	}
}
