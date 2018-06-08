package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstPaths;
import consts.ConstStrings;
import logic.MainView;
import utility.DateHelper;
import utility.FileHelper;
import utility.Logger;
import utility.MiscHelper;

public class mainFrame {

	private JFrame frame;
	private MainView main;
	
	private ConstStrings cs;
	private ConstNums cn;
	private ConstDB cdb;
	private ConstPaths cp;

	private Logger log;
	
	private JSONObject jl;
	private JSONObject js;
	private JSONObject ju;

	private MiscHelper msh;
	private DateHelper dh;
	private FileHelper fh;

	public mainFrame(MainView main, ConstStrings cs, ConstNums cn, ConstDB cdb, ConstPaths CP, 
			Logger log, JSONObject JL, JSONObject JS, JSONObject JU, 
			MiscHelper MSH, DateHelper DH, FileHelper FH) {
		this.main = main;
		
		this.cs = cs;
		this.cn = cn;
		this.cdb = cdb;
		this.cp = CP;
		
		this.log = log;
		
		this.jl = JL;
		this.js = JS;
		this.ju = JU;
		
		this.msh = MSH;
		this.dh = DH;
		this.fh = FH;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
//		logger.log(cs.INFO_LOG, "MAIN Init");
		Color color = msh.getColor(cs.APP, cs, js);
		int btnX = 16, btnY = 22, yOffset = 48, frameW = 240, frameH = 480;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.cp.ICON_PATH));
		frame.setTitle(ju.get(cs.JU_COMPANY_NAME).toString());
		frame.setBounds(5, 5, frameW, frameH);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    		main.updateJSONSettings(true);
		    }
		});
		frame.getContentPane().setLayout(null);

		JButton nowyRachunekBtn = new JButton(jl.get(cs.BTN_INVOICE).toString());
		nowyRachunekBtn.setBackground(new Color(255, 255, 0));
		nowyRachunekBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyRachunekBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");

			}
		});
		nowyRachunekBtn.setBounds(btnX, btnX, 200, 36);
		frame.getContentPane().add(nowyRachunekBtn);
	
		JButton stockBtn = new JButton(jl.get(cs.LBL_STOCK).toString());
		stockBtn.setBackground(new Color(135, 206, 235));
		stockBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		stockBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");

			}
		});
		btnY += yOffset;
		stockBtn.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(stockBtn);
		
		JButton nowyTowarBtn = new JButton(jl.get(cs.BTN_NEW).toString());
		nowyTowarBtn.setBackground(new Color(0, 255, 153));
		nowyTowarBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		nowyTowarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");

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
		
		JButton btnSalesReports = new JButton(jl.get(cs.BTN_SALES_REPORT).toString());
		btnSalesReports.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnSalesReports.setBackground(new Color(135, 206, 235));
		btnSalesReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");

			}
		});
		btnY += (yOffset/2);
		btnSalesReports.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(btnSalesReports);
		
		
		JButton btnCustomers = new JButton(jl.get(cs.LBL_CUSTOMER).toString());
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
		
		JButton btnRepakReport = new JButton(jl.get(cs.BTN_REPAK_REPORT).toString());
		btnRepakReport.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		btnRepakReport.setBackground(new Color(204, 255, 255));
		btnRepakReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");

			}
		});
		btnY += yOffset;
		btnRepakReport.setBounds(btnX, btnY, 200, 36);
		frame.getContentPane().add(btnRepakReport);
		
		JButton invoiceBtn = new JButton(jl.get(cs.BTN_INVOICES).toString());
		invoiceBtn.setBackground(new Color(135, 206, 235));
		invoiceBtn.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
		invoiceBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "W trakcie tworzenia");
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

	public boolean isVisible(){
		return frame.isVisible();
	}
	
	public void setIsVisible(boolean b){
		frame.setVisible(b);
	}

}
