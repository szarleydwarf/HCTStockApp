package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

import consts.ConstDB;
import consts.ConstInts;
import consts.ConstPaths;
import consts.ConstStrings;
import managers.DatabaseManager;
import utility.LoadScreen;

public class MainView {

	private JFrame frame;
	private static DatabaseManager dm;
	protected static LoadScreen ls;
	private static ConstDB cdb;
	private static ConstInts ci;
	private static ConstPaths cp;
	private static ConstStrings cs;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		cdb = new ConstDB();
		ci = new ConstInts();
		cp = new ConstPaths();
		cs = new ConstStrings();
		
		dm = new DatabaseManager(null, cdb);

		try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }
		    ls = new LoadScreen(cp, ci);
		    
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ls.splashScreenDestruct();

					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		ArrayList<String> list = this.dm.getList("SELECT * FROM customers");
		for(int i = 0; i < list.size(); i++)
			System.out.println(i + " in " + list.get(i));
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(204, 0, 0));
//		frame.getContentPane().setBackground(Color.CYAN);
		
//		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.fv.ICON_PATH));
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
//		frame.setLocation(10, 10);
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
		
		JButton nowyTowarBtn = new JButton("Nowy produkt");
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
		
		JButton nowaUslugaBtn = new JButton("Nowa usługa");
		nowaUslugaBtn.setBackground(new Color(0, 255, 153));
		nowaUslugaBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowaUslugaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
//				DodajUsluge.main(loggerFolderPath);
			}
		});
		nowaUslugaBtn.setBounds(358, 112, 200, 36);
		frame.getContentPane().add(nowaUslugaBtn);
		
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
