package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;

public class CompanyDetails {

	private static boolean thisIsNew;
	private static JSONObject jdl;
	private static JSONObject ju;
	private JFrame frame;

	private static DatabaseManager dm;
	private static ConstDB cdb;
	private static ConstNums cn;
	private static ConstStrings cs;
	private JTextField tfName;
	private JTextField tfVat;
	private JTextField tfAddress;
	private JTextField tfTown;
	private JTextField tfCounty;
	private JTextField tfPostCode;
	private JTextField tfTelephone;
	private JTextField tfEmail;
	private JTextField tfWWW;
	private JTextField tfFB;
	/**
	 * Launch the application.
	 * @param jDefLang 
	 * @param jUser 
	 * @param isNew 
	 */
	//dm, cdb, cs, cn, jDefLang, jUser, isNew
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, JSONObject jDefLang, JSONObject jUser, boolean isNew) {
		thisIsNew = isNew;
		jdl = jDefLang;
		ju = jUser;
		dm = dmn;
		cdb = cDB;
		cs = cS;
		cn = cN;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompanyDetails window = new CompanyDetails();
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
	public CompanyDetails() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		System.out.println("Gattering info");
		Font fonts = new Font(jdl.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(jdl.get(cs.FONT_SIZE).toString()));
		int yPos = 30, xPos = 30, line = 26, tfxPos = 130, tfLength = 540, tfHight = 24;
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 153, 204));
		frame.setBounds(10, 10, 704, 400);
		
		JLabel lblTitle = new JLabel(cs.ENTER_DETAILS);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setBounds(0, 5, 700, 24);
		lblTitle.setFont(fonts);
		frame.getContentPane().add(lblTitle);

		JLabel lblName = new JLabel(cs.JSON_NAME);
		lblName.setBounds(xPos, yPos, 96, 28);
		lblName.setFont(fonts);
		frame.getContentPane().add(lblName);

		tfName = new JTextField();
		tfName.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfName);
		tfName.setColumns(10);


		JLabel lblVAT = new JLabel(cs.JSON_VAT);
		yPos+=line;
		lblVAT.setBounds(xPos, yPos, 96, 28);
		lblVAT.setFont(fonts);
		frame.getContentPane().add(lblVAT);
		
		tfVat = new JTextField();
		tfVat.setColumns(10);
		tfVat.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfVat);
	
		JLabel lblAddress = new JLabel(cs.JSON_ADDRESS);
		yPos+=line;
		lblAddress.setBounds(xPos, yPos, 96, 28);
		lblAddress.setFont(fonts);
		frame.getContentPane().add(lblAddress);
		
		tfAddress = new JTextField();
		tfAddress.setColumns(10);
		tfAddress.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfAddress);
		
		JLabel lblTown = new JLabel(cs.JSON_TOWN);
		yPos+=line;
		lblTown.setBounds(xPos, yPos, 96, 28);
		lblTown.setFont(fonts);
		frame.getContentPane().add(lblTown);
		
		tfTown = new JTextField();
		tfTown.setColumns(10);
		tfTown.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfTown);
		
		JLabel lblCounty = new JLabel(cs.JSON_COUNTY);
		yPos+=line;
		lblCounty.setBounds(xPos, yPos, 96, 28);
		lblCounty.setFont(fonts);
		frame.getContentPane().add(lblCounty);
		
		tfCounty = new JTextField();
		tfCounty.setColumns(10);
		tfCounty.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfCounty);

		JLabel lblPostCode = new JLabel(cs.JSON_POST_CODE);
		yPos+=line;
		lblPostCode.setBounds(xPos, yPos, 96, 28);
		lblPostCode.setFont(fonts);
		frame.getContentPane().add(lblPostCode);
		
		tfPostCode = new JTextField();
		tfPostCode.setColumns(10);
		tfPostCode.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfPostCode);

		JLabel lblTelephone = new JLabel(cs.JSON_TELEPHONE);
		yPos+=line;
		lblTelephone.setBounds(xPos, yPos, 96, 28);
		lblTelephone.setFont(fonts);
		frame.getContentPane().add(lblTelephone);
		
		tfTelephone = new JTextField();
		tfTelephone.setColumns(10);
		tfTelephone.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfTelephone);
		
		JLabel lblEmail = new JLabel(cs.JSON_EMAIL);
		yPos+=line;
		lblEmail.setBounds(xPos, yPos, 96, 28);
		lblEmail.setFont(fonts);
		frame.getContentPane().add(lblEmail);
		
		tfEmail = new JTextField();
		tfEmail.setColumns(10);
		tfEmail.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfEmail);
		
		JLabel lblWWW = new JLabel(cs.JSON_WWW);
		yPos+=line;
		lblWWW.setBounds(xPos, yPos, 96, 28);
		lblWWW.setFont(fonts);
		frame.getContentPane().add(lblWWW);
		
		tfWWW = new JTextField();
		tfWWW.setColumns(10);
		tfWWW.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfWWW);

		JLabel lblFB = new JLabel(cs.JSON_FB);
		yPos+=line;
		lblFB.setBounds(xPos, yPos, 96, 28);
		lblFB.setFont(fonts);
		frame.getContentPane().add(lblFB);
		
		tfFB = new JTextField();
		tfFB.setColumns(10);
		tfFB.setBounds(tfxPos, yPos, tfLength, tfHight);
		frame.getContentPane().add(tfFB);

		
		JButton nextBtn = new JButton(jdl.get(cs.NEXT).toString());
		nextBtn.setBounds(478, 314, 200, 36);
		nextBtn.setBackground(new Color(135, 206, 235));
		nextBtn.setFont(fonts);
		nextBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
//				TODO
//				save json file
				MainView.main(null);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(nextBtn);
	}
	
	// GETTERS & SETTERS
	public static boolean isThisIsNew() {
		return thisIsNew;
	}

	public static void setThisIsNew(boolean thisIsNew) {
		CompanyDetails.thisIsNew = thisIsNew;
	}

	public static JSONObject getJdl() {
		return jdl;
	}

	public static JSONObject getJu() {
		return ju;
	}

	public static DatabaseManager getDm() {
		return dm;
	}

	public static ConstDB getCdb() {
		return cdb;
	}

	public static ConstNums getCn() {
		return cn;
	}

	public static ConstStrings getCs() {
		return cs;
	}


}
