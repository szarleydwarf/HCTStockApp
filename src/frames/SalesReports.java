package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.InvoiceManager;
import objects.Item;
import utility.Logger;
import utility.MiscHelper;

public class SalesReports {

	private JFrame frame;
	private MainView mainView;
	private JSONObject jl;
	private Logger log;
	private ConstDB cdb;
	private ConstStrings cs;
	private ConstNums cn;
	private Font fonts;
	private Font fonts_title;
	private Color color;
	private JSONObject js;
	private MiscHelper msh;
	private InvoiceManager invmng;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					SalesReports window = new SalesReports();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public SalesReports() {
		initialize();
	}

	public SalesReports(MainView main, DatabaseManager dm, ConstDB CDB, ConstStrings CS, ConstNums CN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, InvoiceManager invMng) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		log = logger;
		cdb = CDB;
		cs = CS;
		cn = CN;
		
		msh = mSH;
		invmng = invMng;
		
		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		color = msh.getColor(cs.APP, cs, js);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10, lblW = (msh.getScreenDimension()[0]), lblH = (msh.getScreenDimension()[1]);
		int jcbW = 55, jcbH = 28, jcbYOffset = 70, btnPrintX = 400;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, lblW, lblH);
		frame.getContentPane().setLayout(null);
		frame.setTitle(jl.get(cs.BTN_SALES_REPORT).toString());

		// BORDERS
		TitledBorder lblTB = msh.createBorders(jl.get(cs.BTN_SALES_REPORT).toString(), Color.YELLOW);

		// LABELS 
		JLabel lblInvoicesList = new JLabel("");
		lblInvoicesList.setBorder(lblTB);
		lblInvoicesList.setFont(fonts_title);
		lblInvoicesList.setBounds(lblX, lblY, lblW/2, lblH/2);
		frame.getContentPane().add(lblInvoicesList);

		
		// TEXTFIELDS

		// DROPDOWN MENU
		String[]days = {"1","2"};
		int today = 1;

		JComboBox cbDays = new JComboBox(days);
		cbDays.setSelectedIndex(today-1);
		cbDays.setBounds(lblX, (lblH/2)+20, jcbW, jcbH);
		frame.getContentPane().add(cbDays);
		
		JComboBox cbMonthDaily = new JComboBox(days);
		cbMonthDaily.setSelectedIndex(-1);
		cbMonthDaily.setBounds(lblX+cbDays.getWidth()+16, (lblH/2)+20, jcbW*2, jcbH);
//		cbMonthDaily.setSelectedIndex(monthNo);
		frame.getContentPane().add(cbMonthDaily);
		
		JComboBox cbYearDaily = new JComboBox(days);
		cbYearDaily.setSelectedIndex(-1);
		cbYearDaily.setBounds(cbMonthDaily.getX()+cbMonthDaily.getWidth()+16, (lblH/2)+20, jcbW*2, jcbH);
//		cbYearDaily.setSelectedIndex(yearIndex);
		frame.getContentPane().add(cbYearDaily);

		JComboBox cbMonth = new JComboBox(days);
		cbMonth.setSelectedIndex(-1);
		cbMonth.setBounds(lblX+cbDays.getWidth()+16, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
//		cbMonthDaily.setSelectedIndex(monthNo);
		frame.getContentPane().add(cbMonth);
		
		JComboBox cbYear = new JComboBox(days);
		cbYear.setSelectedIndex(-1);
		cbYear.setBounds(cbMonth.getX()+cbMonth.getWidth()+16, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
//		cbYearDaily.setSelectedIndex(yearIndex);
		frame.getContentPane().add(cbYear);
		
		// BUTTONS
		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
				btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);
		
		JButton btnPntDailyRep = new JButton(jl.get(cs.BTN_PRINT).toString());
		btnPntDailyRep.setFont(fonts_title);
		btnPntDailyRep.setBounds(btnPrintX, (lblH/2)+20, jcbW*2, jcbH);
		frame.getContentPane().add(btnPntDailyRep);

		JButton btnPntMonthlyRep = new JButton(jl.get(cs.BTN_PRINT).toString());
		btnPntMonthlyRep.setFont(fonts_title);
		btnPntMonthlyRep.setBounds(btnPrintX, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
		frame.getContentPane().add(btnPntMonthlyRep);
	
		//TODO maybe it can be done without buttons to display, auto after selection
		JButton btnShowDailyRep = new JButton("DISPLAY");
		btnShowDailyRep.setFont(fonts_title);
		btnShowDailyRep.setBounds(btnPrintX+(jcbW*2)+20, (lblH/2)+20, jcbW*2, jcbH);
		frame.getContentPane().add(btnShowDailyRep);
		
		JButton btnShowMonthlyRep = new JButton("PREVIEW");
		btnShowMonthlyRep.setFont(fonts_title);
		btnShowMonthlyRep.setBounds(btnPrintX+(jcbW*2)+20, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
		frame.getContentPane().add(btnShowMonthlyRep);
		
		
	
		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		btnPntMonthlyRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Monthly report print");
			}
		});
		
		btnPntDailyRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Daily report print");

//				printDailyReport(dateOfReport);
			}			
		});
	
		cbDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbDays ){
					JComboBox cb = (JComboBox) a.getSource();
				}		
			}
		});

		cbMonthDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonthDaily ){
					JComboBox cb = (JComboBox) a.getSource();
				}		
			}
		});

		cbYearDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYearDaily ){
					JComboBox cb = (JComboBox) a.getSource();
				}		
			}
		});

		cbMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonth ){
					JComboBox cb = (JComboBox) a.getSource();
				}		
			}
		});
		
		cbYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYear ){
					JComboBox cb = (JComboBox) a.getSource();
				}		
			}
		});
		
	}

	protected void goBack() {
		frame.dispose();
		setIsVisible(false);
		if(mainView != null)
			if(!mainView.isVisible())
				mainView.setIsVisible(true);		
	}

	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	public void setIsVisible(boolean b){
		initialize();

		frame.setVisible(b);
	}

}
