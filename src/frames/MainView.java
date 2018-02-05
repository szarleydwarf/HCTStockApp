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
import java.util.ArrayList;
import java.util.HashMap;

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
import objects.Car;
import objects.Customer;
import objects.CustomerBusiness;
import objects.CustomerInd;
import objects.Invoice;
import objects.Item;
import utility.DateHelper;
import utility.FileHelper;
import utility.LoadScreen;
import utility.Logger;
import utility.MiscHelper;
import utility.tScanner;

public class MainView {

	private JFrame frame;
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
	private static MiscHelper msch;
	private static HashMap<String, String> cars_BI;
	private static HashMap<String, String> cars_IB;
	private static StockManager stmng;
	private static CustomersManager cmng;
	private static tScanner ts;
	private static InvoiceManager invmng;
	private static DecimalFormat df_3_2, df_5_2;
	private static ArrayList<String> carList;
	private static boolean isNew;
	private static JSONObject jDefLang;
	private static JSONObject jUser;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		loader();
	    isNew = checkInstallation();
		
		try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		    ls = new LoadScreen(cp, cn);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ls.splashScreenDestruct();
					MainView window = new MainView();
					if(!isNew){
						window.frame.setVisible(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static void loader() {
		System.out.println("loader");
		ts = new tScanner();

		loadConst();
		loadHelpers();

		todayL = dh.getFormatedDateAndTime();
		todayS = dh.getFormatedDate();
		
		loadManagers();
		loadJsonDefaultLang();
		
		//TODO
		// getLastIDs();
		
//		get cars list
		cars_BI = (HashMap<String, String>) dm.selectDataMap(cdb.SELECT_CARS_LIST_BRAND_ID);
		cars_IB = (HashMap<String, String>) dm.selectDataMap(cdb.SELECT_CARS_LIST_ID_BRAND);
		carList = new ArrayList<String>();
		carList = (ArrayList<String>) dm.selectData("SELECT brand FROM brands", carList);

//		msch.printMap(cars_BI);
//		TODO
//		check last database last backup - do it if necessary
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
		dh = new DateHelper(cs);
		fh = new FileHelper();
		msch = new MiscHelper();
		logger = new Logger(dh, fh, cp.DEFAULT_LOG_PATH);
		df_3_2 = new DecimalFormat(cs.DECIMAL_FORMAT_3_2);
		df_5_2 = new DecimalFormat(cs.DECIMAL_FORMAT_5_2);
	}

	private static void loadManagers() {
		System.out.println("loadManagers");
//		TODO
		dm = new DatabaseManager(logger, todayL, cdb, cn, cs, df_3_2);

//		get customer list
		cmng = new CustomersManager(dm, cdb, cn, cs);
		
//		get stock
		stmng = new StockManager(dm, cdb, cn, cs);
		
		//get invoices list
		invmng = new InvoiceManager(dm, cdb, cn, cs);		
	}

	private static void loadJsonDefaultLang() {
		JSONParser parser = new JSONParser();

		try {
			jDefLang = (JSONObject) parser.parse(new FileReader(cp.JSON_ENG_PATH));
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

	private static boolean checkInstallation() {
		JSONParser parser = new JSONParser();

		try {
			jUser = (JSONObject) parser.parse(new FileReader(cp.JSON_USER_PATH));
			if(jUser.get(cs.JSON_NAME).equals("") && jUser.get(cs.JSON_VAT).equals("") && jUser.get(cs.JSON_ADDRESS).equals("") && jUser.get(cs.JSON_COUNTY).equals("")){
				System.out.println("New");
				return true;
			} else {
				System.out.println("Existing");
				return false;
			}
		
		} catch (FileNotFoundException e1) {
			logger.logError(todayL+" MAIN E1 FILE NOT FOUND "+"\t"+e1.getMessage());
			e1.printStackTrace();
		} catch (IOException e2) {
			logger.logError(todayL+" MAIN E2 IOException "+"\t"+e2.getMessage());
			e2.printStackTrace();
		} catch (ParseException e3) {
			logger.logError(todayL+" MAIN E3 ParseException"+"\t"+e3.getMessage());
			e3.printStackTrace();
		}
		return false;
	}


	/**
	 * Create the application.
	 */
	public MainView() {
		System.out.println("MainView "+isNew);

		test();
		
//		System.out.println("EXIT");
//		System.exit(0);
		if(isNew)
			CompanyDetails.main(dm, cdb, cs, cn, jDefLang, jUser, isNew);
		else
			initialize();
	}

	private void test() {
		// TODO Here I will be testing all of the functionalities
		System.out.println("TEST\n1");

//		System.out.println("\n2");

//		System.out.println("\n3");

		System.out.println();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
//		frame.getContentPane().setBackground(new Color(204, 0, 0));
		frame.getContentPane().setBackground(Color.CYAN);
		System.out.println("\n2");
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.cp.ICON_PATH));
		frame.setTitle("HCT APP");
		frame.setBounds(10, 10, 704, 359);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
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
		
		JButton stockBtn = new JButton("Magazyn");
		stockBtn.setBackground(new Color(135, 206, 235));
		stockBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
//				WyswietlMagazyn.main(loggerFolderPath);
			}
		});
		stockBtn.setBounds(60, 112, 200, 36);
		frame.getContentPane().add(stockBtn);
		
		JButton invoiceBtn = new JButton("Rachunki");
		invoiceBtn.setBackground(new Color(135, 206, 235));
		invoiceBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
//				WyswietlRachunki.main(loggerFolderPath);
			}
		});
		invoiceBtn.setBounds(60, 256, 200, 36);
		frame.getContentPane().add(invoiceBtn);


		JButton nowyRachunekBtn = new JButton("Wystaw rachunek");
		nowyRachunekBtn.setBackground(new Color(255, 255, 0));
		nowyRachunekBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
//				WystawRachunek.main(defaultPaths);
			}
		});
		nowyRachunekBtn.setBounds(60, 52, 200, 36);
		frame.getContentPane().add(nowyRachunekBtn);
		
		JButton nowyTowarBtn = new JButton("Dodaj");
		nowyTowarBtn.setBackground(new Color(0, 255, 153));
		nowyTowarBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
//				DodajTowar.main(loggerFolderPath);
			}
		});
		nowyTowarBtn.setBounds(358, 52, 200, 36);
		frame.getContentPane().add(nowyTowarBtn);
		
//		JButton nowaUslugaBtn = new JButton("Nowa usługa");
//		nowaUslugaBtn.setBackground(new Color(0, 255, 153));
//		nowaUslugaBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
//		nowaUslugaBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				frame.dispose();
////				DodajUsluge.main(loggerFolderPath);
//			}
//		});
//		nowaUslugaBtn.setBounds(358, 112, 200, 36);
//		frame.getContentPane().add(nowaUslugaBtn);
		
		Icon settingsImg = new ImageIcon("resources/img/cogs.png");
		JButton btnSettings = new JButton("Settings", settingsImg);
		btnSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
//				SettingsFrame.main(defaultPaths);
			}
		});
		btnSettings.setBounds(606, 69, 52, 52);
		
		frame.getContentPane().add(btnSettings);
		
		JButton btnSalesReports = new JButton("Raporty sprzedaży");
		btnSalesReports.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnSalesReports.setBackground(new Color(135, 206, 235));
		btnSalesReports.setBounds(60, 209, 200, 36);
		btnSalesReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
//				SalesReports.main(defaultPaths);
			}
		});
		
		frame.getContentPane().add(btnSalesReports);
		
		Icon testPageImg = new ImageIcon("resources/img/testpageico.png");
		JButton btnTestPage = new JButton("Strona testowa", testPageImg);
		btnTestPage.setIconTextGap(-15);
		btnTestPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				printTestPage(true);
			}
		});
		btnTestPage.setBackground(new Color(255, 255, 255));
		btnTestPage.setBounds(606, 225, 52, 52);
		frame.getContentPane().add(btnTestPage);
		
		JButton btnCustomers = new JButton("Klienci");
		btnCustomers.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnCustomers.setBackground(new Color(204, 255, 255));
		btnCustomers.setBounds(358, 209, 200, 36);
		btnCustomers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
//				CustomersWindow.main(defaultPaths);
			}
		});
		frame.getContentPane().add(btnCustomers);
		
		JButton btnRepakReport = new JButton("Repak");
		btnRepakReport.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnRepakReport.setBackground(new Color(204, 255, 255));
		btnRepakReport.setBounds(358, 256, 200, 36);
		btnRepakReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
//				frame.dispose();
//				Repak.main(defaultPaths);
			}
		});

		frame.getContentPane().add(btnRepakReport);
		
		Border b = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(b, "");
		JLabel line = new JLabel("");
		line.setBounds(10, 184, 670, 1);
		line.setBorder(border);
		frame.getContentPane().add(line);
	}
}
