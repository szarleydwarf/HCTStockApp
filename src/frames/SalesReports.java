package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.InvoiceManager;
import objects.Invoice;
import objects.Item;
import utility.DateHelper;
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
	private DateHelper dh;
	private InvoiceManager invmng;

	protected String dayOfReport = "";
	protected String yearOfReport = "";
	protected String monthOfReport = "";
	private ArrayList<Invoice> list;
	private DecimalFormat df;
	private DecimalFormat dfm;

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
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, InvoiceManager invMng, DecimalFormat DF) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		log = logger;
		cdb = CDB;
		cs = CS;
		cn = CN;
		
		msh = mSH;
		dh = DH;
		invmng = invMng;
		df = DF;
		dfm = new DecimalFormat("00");

		list = invmng.getList();

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
		String[]days = dh.getDaysArray();
		JSONArray jArr = (JSONArray) jl.get(cs.MONTHS_NAMES);
		String[]months = dh.json2Array(jArr);
		jArr = null;
		jArr = (JSONArray) jl.get(cs.YEARS);
		String[]years = dh.json2Array(jArr);
		
		int today = dh.getDayOfMonthNum();
		today--;
		int month = dh.getMonthNum();
		int year = dh.getYearIndex();
		
		dayOfReport = days[today];
		monthOfReport = months[month];
		yearOfReport = years[year];
		
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, lblW, lblH);
		frame.getContentPane().setLayout(null);
		frame.setTitle(jl.get(cs.BTN_SALES_REPORT).toString());

		// BORDERS
		TitledBorder lblTB = msh.createBorders(jl.get(cs.BTN_SALES_REPORT).toString(), Color.YELLOW);
		TitledBorder lblDP = msh.createBorders(jl.get(cs.LBL_DAILY_REPORT).toString(), Color.YELLOW);
		TitledBorder lblMP = msh.createBorders(jl.get(cs.LBL_MONTHLY_REPORT).toString(), Color.YELLOW);

		// LABELS 
		JLabel lblSalesList = new JLabel("");
		lblSalesList.setBorder(lblTB);
		lblSalesList.setFont(fonts_title);
		lblSalesList.setBounds(lblX, lblY, lblW/2, lblH/2);
		frame.getContentPane().add(lblSalesList);

		JLabel lblDayPreview = new JLabel("");
		lblDayPreview.setBorder(lblDP);
		lblDayPreview.setFont(fonts_title);
		lblDayPreview.setBounds(lblW/2+16, lblY, lblW/2-40, lblH/3);
		frame.getContentPane().add(lblDayPreview);
		this.populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());

		JLabel lblMonthPreview = new JLabel("");
		lblMonthPreview.setBorder(lblMP);
		lblMonthPreview.setFont(fonts_title);
		lblMonthPreview.setBounds(lblW/2+16, lblH/3+10, lblW/2-40, lblH/3);
		frame.getContentPane().add(lblMonthPreview);
		this.populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
		
		
		// TEXTFIELDS

		// DROPDOWN MENU
		JComboBox cbDays = new JComboBox(days);
		cbDays.setSelectedIndex(today);
		cbDays.setBounds(lblX, (lblH/2)+20, jcbW, jcbH);
		frame.getContentPane().add(cbDays);
		
		JComboBox cbMonthDaily = new JComboBox(months);
		cbMonthDaily.setSelectedIndex(month);
		cbMonthDaily.setBounds(lblX+cbDays.getWidth()+16, (lblH/2)+20, jcbW*2, jcbH);
		frame.getContentPane().add(cbMonthDaily);
		
		JComboBox cbYearDaily = new JComboBox(years);
		cbYearDaily.setSelectedIndex(year);
		cbYearDaily.setBounds(cbMonthDaily.getX()+cbMonthDaily.getWidth()+16, (lblH/2)+20, jcbW*2, jcbH);
		frame.getContentPane().add(cbYearDaily);

		JComboBox cbMonth = new JComboBox(months);
		cbMonth.setSelectedIndex(month);
		cbMonth.setBounds(lblX+cbDays.getWidth()+16, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
		frame.getContentPane().add(cbMonth);
		
		JComboBox cbYear = new JComboBox(years);
		cbYear.setSelectedIndex(year);
		cbYear.setBounds(cbMonth.getX()+cbMonth.getWidth()+16, (lblH/2)+jcbYOffset, jcbW*2, jcbH);
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
					dayOfReport = cb.getSelectedItem().toString();
					populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());
					populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
				}		
			}
		});

		cbMonthDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonthDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = cb.getSelectedItem().toString();
					populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());
					populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
				}		
			}
		});

		cbYearDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYearDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = cb.getSelectedItem().toString();
					populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());
					populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
				}		
			}
		});

		cbMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonth ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = cb.getSelectedItem().toString();
					populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());
					populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
				}		
			}
		});
		
		cbYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYear ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = cb.getSelectedItem().toString();
					populateTabel(lblW/2+26, lblY+10, lblW/2-44, lblH/3-16, lblDP.getTitle());
					populateTabel(lblW/2+26, lblH/3+28, lblW/2-44, lblH/3-16, lblMP.getTitle());
				}		
			}
		});
		
		populateTabel(lblX+10, lblY+10, lblW/2, lblH/2, lblTB.getTitle());
	}

	private void populateTabel(int x, int y, int w, int h, String name) {
		JSONArray jArr = (JSONArray) jl.get(cs.SALE_REPORT_HEADINGS);
		String[]sReportHeadings = dh.json2Array(jArr);
		JTable table = new JTable();
		String[][] data = null;
		JScrollPane spSalesList = null;
		
		if(name.equals(jl.get(cs.BTN_SALES_REPORT).toString())) {
			System.out.println("sr");
			data = new String [this.cn.NUM_OF_MONTHS][sReportHeadings.length];
			data = fillData(data);
			table = msh.createTable(fonts, data, sReportHeadings, name, 60, 60);
		} else if(name.equals(jl.get(cs.LBL_DAILY_REPORT).toString())) {
			System.out.println("dr");
			data = new String [this.cs.ITEM_CODES.length][sReportHeadings.length];
			data = setNuls(data);
			data = fillDailyRepData(data);
//			msh.printData(data);
			sReportHeadings[0] = jl.get(cs.LBL_CODE).toString();
			table = msh.createTable(fonts, data, sReportHeadings, name, 60, 60);

		} else if(name.equals(jl.get(cs.LBL_MONTHLY_REPORT).toString())) {
			System.out.println("mr");
			data = new String [this.cs.ITEM_CODES.length][sReportHeadings.length];
			data = fillMonthlyRepData(data);
			sReportHeadings[0] = jl.get(cs.LBL_CODE).toString();
			table = msh.createTable(fonts, data, sReportHeadings, name, 60, 60);

		}
		System.out.println("table "+table.getName()+" "+table.getColumnCount()+"/"+table.getRowCount());
		spSalesList = new JScrollPane(table);
		spSalesList.setBounds(x, y+12, w-28, h-28);
		frame.getContentPane().add(spSalesList);
	}

	private String[][] setNuls(String[][] data) {
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = ""+0;
				data[i][j] = ""+0;
				data[i][j] = ""+0;
				
			}
		}
		return data;
	}

	private String[][] fillMonthlyRepData(String[][] data) {
		// TODO Auto-generated method stub
		System.out.println("DateM "+dayOfReport+"/"+monthOfReport+"/"+yearOfReport);
		data = fillFirst(data);

		return data;
	}

	private String[][] fillDailyRepData(String[][] data) {
		// TODO Auto-generated method stub
		String dateYMD = yearOfReport+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReport))+cs.MINUS+dfm.format(Integer.parseInt(dayOfReport));
		String dateDMY = dfm.format(Integer.parseInt(dayOfReport))+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReport))+cs.MINUS+yearOfReport;
		data = fillFirst(data);

		String lists = findInList(dateYMD, dateDMY);
		
		if(lists != ""){
//			System.out.println("list "+lists);
			data = splitToData(lists, data);
		}
		
		return data;
	}
 
	private String[][] splitToData(String list, String[][] data) {
		String[]tokens = msh.splitString(list, cs.SEMICOLON);
		double diff = 0;
		for (String s : tokens) {
			if(!s.isEmpty()){

				int qnt = Integer.parseInt(s.substring(0, s.indexOf(cs.STAR)));
				String code = s.substring(s.indexOf(cs.STAR)+1, s.indexOf(cs.UNDERSCORE)); 
				double cost = Double.parseDouble(s.substring(s.indexOf(cs.HASH)+1, s.indexOf(cs.AT)));
				cost *= qnt;
				double price = Double.parseDouble(s.substring(s.indexOf(cs.AT)+1));
				price *= qnt;
				diff = price - cost;
				if(code.equals(cs.TYRE_CODE)){
					data[0][1] = getValue(data[0][1], cost);
					data[0][2] = getValue(data[0][2], price);
					data[0][3] = getValue(data[0][3], diff);
				} else if(code.equals(cs.TUBE_CODE)){
					data[1][1] = getValue(data[1][1], cost);
					data[1][2] = getValue(data[1][2], price);
					data[1][3] = getValue(data[1][3], diff);
				} else if(code.equals(cs.SERVICE_CODE)){
					data[2][1] = getValue(data[2][1], cost);
					data[2][2] = getValue(data[2][2], price);
					data[2][3] = getValue(data[2][3], diff);
				} else if(code.equals(cs.SHOP_CODE)){
					data[3][1] = getValue(data[3][1], cost);
					data[3][2] = getValue(data[3][2], price);
					data[3][3] = getValue(data[3][3], diff);
				} else if(code.equals(cs.OTHER_CODE)){
					data[4][1] = getValue(data[4][1], cost);
					data[4][2] = getValue(data[4][2], price);
					data[4][3] = getValue(data[4][3], diff);
				} else if(code.equals(cs.CARWASH_CODE)){
					data[5][1] = getValue(data[5][1], cost);
					data[5][2] = getValue(data[5][2], price);
					data[5][3] = getValue(data[5][3], diff);
				}
			}
		}
		return data;
	}

	private String getValue(String s, double dd) {
		double d = Double.parseDouble(s);
		d += dd;
		return ""+d;
	}

	private String[][] fillFirst(String[][] data) {
		for (int i = 0; i < cs.ITEM_CODES.length; i++) {
			data[i][0] = cs.ITEM_CODES[i];
		}
		return data;
	}

	private String findInList(String dateYMD, String dateDMY) {
		String lists="";
		for(Invoice in : list){
			if (in.getDate().contains(dateYMD) || in.getDate().contains(dateDMY)){
				lists += in.getList();
			}
		}

		return lists;
	}

	private String[][] fillData(String[][] data) {
		int monthNo = dh.getMonthNum()+1;
		int yearNo = dh.getYearNum();
		String dateMMYYYY="", dateYYYYMM="";
		for (int i = 0; i < cn.NUM_OF_MONTHS; i++) {
			String lists = "";
			dateMMYYYY = ""+dfm.format(monthNo)+"-"+yearNo;
			dateYYYYMM = ""+yearNo+"-"+dfm.format(monthNo);
			
			data[i][0] = dateMMYYYY;
			lists = findInList(dateMMYYYY, dateYYYYMM);
			
			if (lists != ""){
				String[]tokens = msh.splitString(lists, cs.SEMICOLON);
				double costs = 0, prices = 0, diff = 0;
				
				for(String s : tokens){
					if(!s.isEmpty()){
						int qnt = Integer.parseInt(s.substring(0, s.indexOf(cs.STAR)));
						double cost = Double.parseDouble(s.substring(s.indexOf(cs.HASH)+1, s.indexOf(cs.AT)));
						double price = Double.parseDouble(s.substring(s.indexOf(cs.AT)+1));
						
						cost = cost * qnt;
						price = price * qnt;
						
						costs += cost;
						prices += price;
					}
				}
				data[i][1] = "� "+ df.format(costs);
				data[i][2] = "� "+ df.format(prices);
				diff = prices - costs;
				data[i][3] = "� "+ df.format(diff);
			} else {
				data[i][1] = "� "+ df.format(0);
				data[i][2] = "� "+ df.format(0);
				data[i][3] = "� "+ df.format(0);	
			}
			if(monthNo == 1){
				yearNo--;
				monthNo = 13;
			}
			monthNo--;
		}
		return data;
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