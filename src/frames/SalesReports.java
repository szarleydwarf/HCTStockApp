package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Month;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.apache.pdfbox.pdmodel.PDDocument;
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
import utility.FileHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;

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
	protected String yearOfReportD = "", yearOfReport = "";
	protected String monthOfReportD = "", monthOfReport = "";
	private ArrayList<Invoice> list;
	private DecimalFormat df;
	private DecimalFormat dfm;
	private FileHelper fh;
	private String[][] DATA_M;
	private PDFCreator pdfCreator;
	private Printer printer;
	private String[][] DATA_D;

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
			PDFCreator PDFCreator, Printer p_rinter,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, FileHelper FH,
			InvoiceManager invMng, DecimalFormat DF) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		log = logger;
		cdb = CDB;
		cs = CS;
		cn = CN;
		
		msh = mSH;
		dh = DH;
		fh = FH;
		
		pdfCreator = PDFCreator;
		printer = p_rinter;

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
		String[]months = msh.json2Array(jArr);
		jArr = null;
		jArr = (JSONArray) jl.get(cs.YEARS);
		String[]years = msh.json2Array(jArr);
		
		int today = dh.getDayOfMonthNum();
		today--;
		int month = dh.getMonthNum();
		int year = dh.getYearIndex();
		
		dayOfReport = days[today];
		monthOfReportD = months[month];
		yearOfReportD = years[year];
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

		JLabel lblDayPreviewFrame = new JLabel("");
		lblDayPreviewFrame.setFont(fonts_title);
		lblDayPreviewFrame.setBounds(lblW/2+16, lblY, lblW/2-40, lblH/3);
		frame.getContentPane().add(lblDayPreviewFrame);

		JLabel lblDayPreview = new JLabel("");
		lblDayPreview.setBorder(lblDP);
		lblDayPreview.setFont(fonts_title);
		lblDayPreview.setBounds(lblW/2+24, lblY, lblW/2-46, lblH/3);
		frame.getContentPane().add(lblDayPreview);
		previewReport(lblDayPreview, lblDP.getTitle());
// TODO
		JLabel lblMonthPreviewFrame = new JLabel("");
		lblMonthPreviewFrame.setBorder(lblMP);
		lblMonthPreviewFrame.setFont(fonts_title);
		lblMonthPreviewFrame.setBounds(lblW/2+16, lblH/3+10, lblW/2-40, lblH/3);
		frame.getContentPane().add(lblMonthPreviewFrame);

		JLabel lblMonthPreview = new JLabel("");
		lblMonthPreview.setFont(fonts_title);
		lblMonthPreview.setBounds(lblW/2+24, lblH/3+10, lblW/2-46, lblH/3);
		frame.getContentPane().add(lblMonthPreview);
		previewReport(lblMonthPreview, lblMP.getTitle());
		
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
	
		
		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		btnPntMonthlyRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				String path = createPdfPath(false);
				System.out.println("Mpath "+path);
				boolean pdfCreated = printSalesReport(path, false);
				if(pdfCreated){
//					try {
//						printer.printDoc(path);
//					} catch (IOException e) {
//						log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" IOException: "+e.getMessage()).toString());
//						e.printStackTrace();
//					} catch (PrinterException e) {
//						log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" PrinterException: "+e.getMessage()).toString());
//						e.printStackTrace();
//					}
				}
			}
		});
		
		btnPntDailyRep.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				if(DATA != null){
					String path = createPdfPath(true);
					System.out.println("Dpath "+path);
					boolean pdfCreated = printSalesReport(path, true);
					if(pdfCreated){
//						try {
//							printer.printDoc(path);
//						} catch (IOException e) {
//							log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" IOException: "+e.getMessage()).toString());
//							e.printStackTrace();
//						} catch (PrinterException e) {
//							log.log(cs.ERR_LOG, js.get(cs.PRINTING_PDF_ERROR+" PrinterException: "+e.getMessage()).toString());
//							e.printStackTrace();
//						}
					}
//				} else {
//					log.log(cs.ERR_LOG, jl.get(cs.PRINTING_PDF_ERROR).toString());
//				}
			}			
		});
	
		cbDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbDays ){
					JComboBox cb = (JComboBox) a.getSource();
					dayOfReport = cb.getSelectedItem().toString();
					previewReport(lblDayPreview, lblDP.getTitle());
				}		
			}
		});

		cbMonthDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonthDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReportD = cb.getSelectedItem().toString();
					previewReport(lblDayPreview, lblDP.getTitle());
				}		
			}
		});

		cbYearDaily.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYearDaily ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReportD = cb.getSelectedItem().toString();
					previewReport(lblDayPreview, lblDP.getTitle());
				}		
			}
		});

		cbMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonth ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = cb.getSelectedItem().toString();
					previewReport(lblMonthPreview, lblMP.getTitle());
				}		
			}
		});
		
		cbYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYear ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = cb.getSelectedItem().toString();
					previewReport(lblMonthPreview, lblMP.getTitle());
				}		
			}
		});
		
		populateTabel(lblX+10, lblY+10, lblW/2, lblH/2, lblTB.getTitle());
	}

	protected boolean printSalesReport(String path, boolean b) {
		// TODO Auto-generated method stub
		PDDocument pdf = null;
		JSONArray jArr = (JSONArray) jl.get(cs.SALE_REPORT_HEADINGS);
		String header = createHeader(msh.json2Array(jArr));
		String date = path.substring(path.lastIndexOf(cs.SPACE)+1, path.lastIndexOf(cs.DOT));
		date = date.replaceAll(cs.UNDERSCORE, cs.MINUS);
		System.out.println("DD "+date);
		if(b)
			pdf = pdfCreator.createPDF(cs.PDF_SALE_REPORT, DATA_D, header, date);
		else
			pdf = pdfCreator.createPDF(cs.PDF_SALE_REPORT, DATA_M, header, date);
		if(pdf != null){
			try {
				pdf.save(path);
				pdf.close();
				JOptionPane.showMessageDialog(frame, jl.get(cs.INVOICE_SAVED_2).toString());
//				goBack();
				return true;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, jl.get(cs.PDF_SAVE_ERROR).toString());
				log.log(cs.ERR_LOG, jl.get(cs.PDF_SAVE_ERROR).toString() +"    " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}  else {
			JOptionPane.showMessageDialog(frame, jl.get(cs.PDF_SAVE_ERROR).toString());
			return false;
		}
	}

	private String createHeader(String[] jArr) {
		String h = "";
		jArr[0] = "I.C. - ";
		for (String s : jArr) {
			h += msh.paddStringRight(s, 12, cs.UNDERSCORE);
		}
		h = msh.removeLastChar(h, cs.UNDERSCORE);
		return h;
	}

	protected String createPdfPath(boolean daily) {
		String s = yearOfReport+cs.SLASH+monthOfReport;
		if(daily)
			s+=cs.SLASH+dayOfReport;
		String p = js.get(cs.SALES_PATH).toString() + s;
		fh.createFolderIfNotExist(p);
		p += cs.SLASH + jl.get(cs.BTN_SALES_REPORT).toString() + " " + s.replace(cs.SLASH, cs.UNDERSCORE) + cs.PDF_EXT;
		return p;
	}

	private void previewReport(JLabel jlbl, String name) {
		JSONArray jArr = (JSONArray) jl.get(cs.SALE_REPORT_HEADINGS);
		String[]sReportHeadings = msh.json2Array(jArr);
		String[][] data = null;

		sReportHeadings[0] = "I.C. - ";
//TODO
		if(name.equals(jl.get(cs.LBL_DAILY_REPORT).toString())) {
			String dateYMD = yearOfReportD+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReportD))+cs.MINUS+dfm.format(Integer.parseInt(dayOfReport));
			String dateDMY = dfm.format(Integer.parseInt(dayOfReport))+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReportD))+cs.MINUS+yearOfReportD;
			data = new String [this.cs.ITEM_CODES.length][sReportHeadings.length];
			data = msh.setZeros(data);
			data = fillReportData(data, dateDMY, dateYMD);
			this.DATA_D = null;
			this.DATA_D = data;
		} else if(name.equals(jl.get(cs.LBL_MONTHLY_REPORT).toString())) {
			String dateYM = yearOfReport+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReport));
			String dateMY = dfm.format(dh.getMonthNumber(monthOfReport))+cs.MINUS+yearOfReport;
			data = new String [this.cs.ITEM_CODES.length][sReportHeadings.length];
			data = fillReportData(data, dateMY, dateYM);
			this.DATA_M = null;
			this.DATA_M = data;
		}
		jlbl.setText("");
		msh.displayDataInLabel(jlbl, df, data, sReportHeadings);
	}

	private void populateTabel(int x, int y, int w, int h, String name) {
		JSONArray jArr = (JSONArray) jl.get(cs.SALE_REPORT_HEADINGS);
		String[]sReportHeadings = msh.json2Array(jArr);
		JTable table = new JTable();
		String[][] data = null;
		JScrollPane spSalesList = null;
		
		if(name.equals(jl.get(cs.BTN_SALES_REPORT).toString())) {
			data = new String [this.cn.NUM_OF_MONTHS][sReportHeadings.length];
			data = fillData(data);
			table = msh.createTable(fonts, data, sReportHeadings, name, 60, 60);
		} 
		spSalesList = new JScrollPane(table);
		spSalesList.setBounds(x, y+12, w-28, h-28);
		frame.getContentPane().add(spSalesList);
	}

	private String[][] fillReportData(String[][] data, String dMY, String dYM) {
		data = fillFirst(data);
		String lists = findInList(dMY, dYM);
//		log.log("frd ", ""+lists);
		if(lists != ""){
			data = splitToData(lists, data);
		}
		return data;
	}

	private String[][] splitToData(String list, String[][] data) {
		String[]tokens = msh.splitString(list, cs.SEMICOLON);
		double diff = 0;
		data = msh.setZeros(data);
		for (String s : tokens) {
			if(!s.isEmpty()){
				int qnt = Integer.parseInt(s.substring(0, s.indexOf(cs.STAR)));
				String code = s.substring(s.indexOf(cs.STAR)+1, s.indexOf(cs.UNDERSCORE)); 
				double cost = Double.parseDouble(s.substring(s.indexOf(cs.HASH)+1, s.indexOf(cs.AT)));
				cost *= qnt;
				double price = Double.parseDouble(s.substring(s.indexOf(cs.AT)+1));
				price *= qnt;
				diff = price - cost;
	
				if(code.equals(cs.TYRE_CODE_C)){
					data[0][1] = getValue(data[0][1], cost);
					data[0][2] = getValue(data[0][2], price);
					data[0][3] = getValue(data[0][3], diff);
				} else if(code.equals(cs.TYRE_CODE_A)){
					data[1][1] = getValue(data[1][1], cost);
					data[1][2] = getValue(data[1][2], price);
					data[1][3] = getValue(data[1][3], diff);
				} else if(code.equals(cs.TUBE_CODE)){
					data[2][1] = getValue(data[2][1], cost);
					data[2][2] = getValue(data[2][2], price);
					data[2][3] = getValue(data[2][3], diff);
				} else if(code.equals(cs.SERVICE_CODE)){
					data[3][1] = getValue(data[3][1], cost);
					data[3][2] = getValue(data[3][2], price);
					data[3][3] = getValue(data[3][3], diff);
				} else if(code.equals(cs.SHOP_CODE)){
					data[4][1] = getValue(data[4][1], cost);
					data[4][2] = getValue(data[4][2], price);
					data[4][3] = getValue(data[4][3], diff);
				} else if(code.equals(cs.OTHER_CODE)){
					data[5][1] = getValue(data[5][1], cost);
					data[5][2] = getValue(data[5][2], price);
					data[5][3] = getValue(data[5][3], diff);
				} else if(code.equals(cs.CARWASH_CODE)){
					data[6][1] = getValue(data[6][1], cost);
					data[6][2] = getValue(data[6][2], price);
					data[6][3] = getValue(data[6][3], diff);
				} else {
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
				for (int j = 0; j < tokens.length; j++) {
					
					if(!tokens[j].isEmpty()){
						int qnt = Integer.parseInt(tokens[j].substring(0, tokens[j].indexOf(cs.STAR)));
						double cost = Double.parseDouble(tokens[j].substring(tokens[j].indexOf(cs.HASH)+1, tokens[j].indexOf(cs.AT)));
						double price = Double.parseDouble(tokens[j].substring(tokens[j].indexOf(cs.AT)+1));
						
						cost = cost * qnt;
						price = price * qnt;
						
						costs += cost;
						prices += price;
					} else {
						costs += 0;
						prices += 0;
					}
				}
				data[i][1] = "€ "+ df.format(costs);
				data[i][2] = "€ "+ df.format(prices);
				diff = prices - costs;
				data[i][3] = "€ "+ df.format(diff);
			} else {
				data[i][1] = "€ "+ df.format(0);
				data[i][2] = "€ "+ df.format(0);
				data[i][3] = "€ "+ df.format(0);	
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
