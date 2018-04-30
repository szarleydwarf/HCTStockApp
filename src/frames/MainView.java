package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import managers.CustomersManager;
import managers.DatabaseManager;
import managers.InvoiceManager;
import managers.StockManager;
import objects.RepakROne;
import utility.DateHelper;
import utility.FileHelper;
import utility.LoadScreen;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;
import utility.tScanner;

public class MainView {

	private JFrame frame;
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
	private static HashMap<String, String> cars_BI;
	private static HashMap<String, String> cars_IB;
	private static StockManager stmng;
	private static CustomersManager cmng;
	private static tScanner ts;
	private static InvoiceManager invmng;
	private static DecimalFormat df_3_2, df_5_2;
	private static ArrayList<String> carBrandList;
	private static boolean isNew;
	private static JSONObject jSettings;
	private static JSONObject jUser;
	private static JSONObject jLang;
	private static JSONObject jPL;
	private static boolean isStarting;
	private static DisplayStock stockFrame;
	private static ItemAddNew newItemFrame;
	private static InvoiceAddEdit newInvoiceFrame;
	private static Printer printer;
	private static PDFCreator pdfCreator;
	private static DecimalFormat df_4_2;
	private static InvoicesDisplay invoicesFrame;
	private static SalesReports salesRepFrame;
	private static RepakReport repakRepFrame;
	private static String todayYM;
	

	/**
	 * Launch the application.
	 * @wbp.parser.entryPoint
	 */
	public static void main(String[] args) {
		//TODO - not sure if I should load all the classes again every time, do some research
		loadConst();
		loadJsonFiles();

		try {
	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
		isStarting = Boolean.parseBoolean(jSettings.get(cs.JSTART).toString());
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(isStarting)
						ls.splashScreenDestruct();
					
					main = new MainView();
					if(!isNew){
						main.frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void test() {
		// TODO Here I will be testing all of the functionalities
		System.out.println("TEST\n1");
//		System.out.println("SD: "+repakRepFrame.updateRepakList(dh.getRevDateYM(), cdb.TB_REPAK_SOLD_CAR, 10));
		System.out.println("\n2");
		System.out.println("\n3");

		System.out.println();
	}

	private static void updateJSONSettings(boolean isStarting) {
		JSONObject jo = new JSONObject();
		String t = "";
		for (Object key : jSettings.keySet()) {
			String keyStr = (String)key;
			if(keyStr.equals(cs.JSTART)){
				t = ""+isStarting;
			} else {
				t = (String) jSettings.get(keyStr);
			}
	    	jo.put(keyStr, t);
		}
		boolean saved = msh.saveJSON(cp.JSON_SETTINGS_PATH, jo);
		if(!saved)
			logger.logError("Fail to save JSON file in MAIN updateJSONSettings().");
	}

	private static void loader() {
		System.out.println("loader");
		ts = new tScanner();

		loadHelpers();
		setDates();
		loadManagers();

		//		get cars list TODO ????????
//		cars_BI = (HashMap<String, String>) dm.selectDataMap(cdb.SELECT_CARS_LIST_BRAND_ID);
//		cars_IB = (HashMap<String, String>) dm.selectDataMap(cdb.SELECT_CARS_LIST_ID_BRAND);
		carBrandList = new ArrayList<String>();
		carBrandList = (ArrayList<String>) dm.selectData("SELECT brand FROM brands ORDER BY brand ASC", carBrandList);
		
		loadClasses();
//		TODO
//		check last database last backup - do it if necessary
		checkLastDBUpdate();
		test();
	}

	private static void setDates() {
		todayL = dh.getFormatedDateAndTime();
		logger.setLongDate(todayL);
		
		todayS = dh.getFormatedDate();
		logger.setShortDate(todayS);
		
		todayYM = dh.getRevDateYM();
		logger.logInfo(" Dates set "+todayYM);
	}

	private static void loadClasses() {
		System.out.println("loadClasses");
		pdfCreator = new PDFCreator(cs, cn, cp, logger, jSettings, jLang, jUser, msh, dh, df_4_2);
		printer = new Printer(cs, cn, cp, logger, jSettings, jLang, jUser, msh, dh);


		invoicesFrame = new InvoicesDisplay(main, dm, cdb, cs, cn, logger, jSettings , jLang, msh, invmng);
		salesRepFrame = new SalesReports(main, dm, cdb, cs, cn, logger, pdfCreator, printer, jSettings , jLang, msh, dh, fh, invmng, df_5_2);
		repakRepFrame = new RepakReport(main, dm, cdb, cs, cn, logger, pdfCreator, printer, jSettings , jLang, msh, dh, fh);

		newInvoiceFrame = new InvoiceAddEdit(main, dm, cdb, cs, cn, logger, pdfCreator, printer, jSettings , jLang, jUser, msh, dh, fh, stmng, cmng, invmng, repakRepFrame, carBrandList, df_3_2);
		newItemFrame = new ItemAddNew(main, dm, cdb, cs, cn, logger, jSettings , jLang, msh, stmng, df_4_2, repakRepFrame, todayYM);
		
		stockFrame = new DisplayStock(main, newItemFrame, dm, cdb, cs, cn, logger, printer, jSettings , jLang, msh, stmng, df_4_2);
		logger.logInfo("Classes loaded");
	}

	private static void loadConst() {
		System.out.println("loadConst");
		cdb = new ConstDB();
		cn = new ConstNums();
		cp = new ConstPaths();
		cs = new ConstStrings();
	}

	private static void loadHelpers() {
		System.out.println("loadHelpers");
		dh = new DateHelper(cs, cn);
		fh = new FileHelper();

		logger = new Logger(dh, fh, cp.DEFAULT_LOG_PATH);
		logger.logInfo("Logger Init");
		msh = new MiscHelper(logger, cs, jLang);

		DecimalFormatSymbols symbols = new DecimalFormatSymbols( new Locale("en", "UK"));
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator('\'');

		df_3_2 = new DecimalFormat(cs.DECIMAL_FORMAT_3_2);
		df_4_2 = new DecimalFormat(cs.DECIMAL_FORMAT_4_2, symbols);
		df_5_2 = new DecimalFormat(cs.DECIMAL_FORMAT_5_2, symbols);
	}

	private static void loadManagers() {
		System.out.println("loadManagers");
		try {
			dm = new DatabaseManager(logger, todayL, cdb, cn, cs, cp, jSettings, df_3_2);
			logger.logInfo("DM Init");

		} catch (FileNotFoundException e) {
			logger.logError("FileNotFoundException DM in Main "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.logError("IOException DM in Main "+e.getMessage());
			e.printStackTrace();
		}
//		get customer list
		cmng = new CustomersManager(dm, cdb, cn, cs);
//		get stock
		stmng = new StockManager(dm, cdb, cn, cs);
		//get invoices list
		invmng = new InvoiceManager(dm, cdb, cn, cs);		
		logger.logInfo("Mangers Init");
	}

	private static void loadJsonFiles() {
		System.out.println("loadJSONFiles");
		JSONParser parser = new JSONParser();

		try {
			jSettings = (JSONObject) parser.parse(new FileReader(cp.JSON_SETTINGS_PATH));
			jUser = (JSONObject) parser.parse(new FileReader(cp.JSON_USER_PATH));
			jLang = loadLanguage();
		} catch (FileNotFoundException e1) {
			logger.logError(todayL+" loadJsonDefaultLang E1 FILE NOT FOUND "+"\t"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e2) {
			logger.logError(todayL+" loadJsonDefaultLang E2 IOException "+"\t"+e2.getMessage());
			e2.printStackTrace();
		} catch (ParseException e3) {
			logger.logError(todayL+" loadJsonDefaultLang E3 ParseException"+"\t"+e3.getMessage());
			e3.printStackTrace();
		}
		
	}

	private static JSONObject loadLanguage() {
		JSONParser parser = new JSONParser();
		try {
			String lang = (String) jSettings.get(cs.JLANG);
			return (JSONObject) parser.parse(new FileReader(cp.JSON_LANG_PATH+lang));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		};
		return null;
	}

	private static boolean checkInstallation() {
		if(jUser.get(cs.JSON_COMPANY_NAME).equals("") 
			&& jUser.get(cs.JSON_VAT).equals("") 
			&& jUser.get(cs.JSON_ADDRESS).equals("") 
			&& jUser.get(cs.JSON_TOWN).equals("") 
			&& jUser.get(cs.JSON_COUNTY).equals("")){
			return true;
		} else {
			return false;
		}
	}

	private static void checkLastDBUpdate() {
		// TODO
		String lastUpdate = "";
		String lastMonth = dm.selectData(cdb.GET_LAST_MONTH);
		System.out.println("last month "+lastMonth);
		
	}


	/**
	 * Create the application.
	 * @wbp.parser.entryPoint
	 */
	public MainView() {
		jLang = loadLanguage();
		if(isNew) 
			CompanyDetails.main(dm, logger, cdb, cs, cn, cp, jSettings, jUser, jLang, msh);
		else
			initialize();
	}

	/**
	 * Initialise the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		logger.logInfo("MAIN Init");
		Color color = msh.getColor(cs.APP, cs, jSettings);
		int btnX = 16, btnY = 22, yOffset = 48, frameW = 240, frameH = 480;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.cp.ICON_PATH));
		frame.setTitle(jUser.get(cs.JSON_COMPANY_NAME).toString());
		frame.setBounds(5, 5, frameW, frameH);
//		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	updateJSONSettings(true);
//		        if (JOptionPane.showConfirmDialog(frame, 
//			            fv.CLOSE_WINDOW, fv.CLOSE_WINDOW, 
//		            JOptionPane.YES_NO_OPTION,
//		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
//		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//		            SqliteTestAppV.main(null);
//		        }
		    }
		});
		frame.getContentPane().setLayout(null);

		JButton nowyRachunekBtn = new JButton(jLang.get(cs.BTN_INVOICE).toString());
		nowyRachunekBtn.setBackground(new Color(255, 255, 0));
		nowyRachunekBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!newInvoiceFrame.isVisible())
					newInvoiceFrame.setIsVisible(true);
			}
		});
		nowyRachunekBtn.setBounds(btnX, btnX, 200, 36);
		frame.getContentPane().add(nowyRachunekBtn);
	
		JButton stockBtn = new JButton(jLang.get(cs.LBL_STOCK).toString());
		stockBtn.setBackground(new Color(135, 206, 235));
		stockBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!stockFrame.isVisible())
					stockFrame.setIsVisible(true);
			}
		});
		btnY += yOffset;
		stockBtn.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(stockBtn);
		
		JButton nowyTowarBtn = new JButton(jLang.get(cs.BTN_NEW).toString());
		nowyTowarBtn.setBackground(new Color(0, 255, 153));
		nowyTowarBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!newItemFrame.isVisible())
					newItemFrame.setIsVisible(true);
			}
		});
		btnY += yOffset;
		nowyTowarBtn.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(nowyTowarBtn);
		
		Border b = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(b, "");
		JLabel line = new JLabel("");
		btnY += yOffset;
		line.setBounds(btnX, btnY, frameW - 40, 2);
		line.setBorder(border);
		frame.getContentPane().add(line);
		
		JButton btnSalesReports = new JButton(jLang.get(cs.BTN_SALES_REPORT).toString());
		btnSalesReports.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnSalesReports.setBackground(new Color(135, 206, 235));
		btnSalesReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!salesRepFrame.isVisible())
					salesRepFrame.setIsVisible(true);
			}
		});
		btnY += (yOffset/2);
		btnSalesReports.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(btnSalesReports);
		
		
		JButton btnCustomers = new JButton("Klienci");
		btnCustomers.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnCustomers.setBackground(new Color(204, 255, 255));
		btnCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
			}
		});
		btnY += yOffset;
		btnCustomers.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(btnCustomers);
		
		JButton btnRepakReport = new JButton(jLang.get(cs.BTN_REPAK_REPORT).toString());
		btnRepakReport.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnRepakReport.setBackground(new Color(204, 255, 255));
		btnRepakReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				if(!repakRepFrame.isVisible())
					repakRepFrame.setIsVisible(true);
			}
		});
		btnY += yOffset;
		btnRepakReport.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(btnRepakReport);
		
		JButton invoiceBtn = new JButton(jLang.get(cs.BTN_INVOICES).toString());
		invoiceBtn.setBackground(new Color(135, 206, 235));
		invoiceBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!invoicesFrame.isVisible())
					invoicesFrame.setIsVisible(true);
				
			}
		});
		btnY += yOffset;
		invoiceBtn.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(invoiceBtn);

		Icon testPageImg = new ImageIcon("resources/img/testpageico.png");
		JButton btnTestPage = new JButton("Strona testowa", testPageImg);
		btnTestPage.setIconTextGap(-15);
		btnTestPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
			}
		});
		btnTestPage.setBackground(new Color(255, 255, 255));
		btnY += yOffset;
		btnTestPage.setBounds(btnX, btnY, 52, 52);
		frame.getContentPane().add(btnTestPage);

		Icon settingsImg = new ImageIcon("resources/img/cogs.png");
		JButton btnSettings = new JButton("Settings", settingsImg);
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
			}
		});
		btnX += (1.5*yOffset);
		btnSettings.setBounds(btnX, btnY, 52, 52);
		
		frame.getContentPane().add(btnSettings);
		
		Icon companyImg = new ImageIcon("resources/img/board.png");
		JButton btnCompanyDetails = new JButton("Company", companyImg);
		btnCompanyDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
			}
		});
		btnX += (1.5*yOffset);
		btnCompanyDetails.setBounds(btnX, btnY, 52, 52);
		
		frame.getContentPane().add(btnCompanyDetails);
	}
	
	// GETTERS & SETTERS
	public boolean isVisible(){
		return frame.isVisible();
	}
	
	public void setIsVisible(boolean b){
		frame.setVisible(b);
	}

	public static HashMap<String, String> getCars_BI() {
		return cars_BI;
	}

	public static HashMap<String, String> getCars_IB() {
		return cars_IB;
	}

	public static CustomersManager getCustMng() {
		return cmng;
	}

	public InvoiceAddEdit getInvoiceAEFrame() {
		return this.newInvoiceFrame;
	}

}
