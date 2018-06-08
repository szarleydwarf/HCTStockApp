package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import logic.MainView;
import managers.DatabaseManager;
import objects.RepakROne;
import utility.DateHelper;
import utility.FileHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;

public class RepakReportFrame {

	private JFrame frame;
	private MainView mainView;
	private JSONObject jl;
	private JSONObject js;
	private Logger log;
	private ConstDB cdb;
	private ConstStrings cs;
	private ConstNums cn;
	private MiscHelper msh;
	private DateHelper dh;
	private FileHelper fh;
	private PDFCreator pdfCreator;
	private Printer printer;
	private DecimalFormat dfm;
	private Font fonts;
	private Font fonts_title;
	private Color color;
	private String[]tCat;
	private ArrayList<RepakROne> list;
	private DatabaseManager dm;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RepakReport window = new RepakReport();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}
	private JTextField tfReturned;
	protected String monthOfReport;
	protected String yearOfReport;
	private String[] months;
	private String defaultCategory;
	private TitledBorder lblTB;
	private TitledBorder lblP;

	/**
	 * Create the application.
	 */
	public RepakReportFrame() {
		initialize();
	}

	public RepakReportFrame(MainView main, DatabaseManager DM, ConstDB CDB, ConstStrings CS, ConstNums CN, Logger logger,
			PDFCreator PDFCreator, Printer p_rinter,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, FileHelper FH) {
		mainView = main;
		jl = jLang;
		js = jSettings;
		dm = DM;

		log = logger;
		cdb = CDB;
		cs = CS;
		cn = CN;
		
		msh = mSH;
		dh = DH;
		fh = FH;
		
		pdfCreator = PDFCreator;
		printer = p_rinter;

		dfm = new DecimalFormat("00");

		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		color = msh.getColor(cs.APP, cs, js);

		list = new ArrayList<>();
		list = listUpdate();
//		msh.printArrayList(list);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10, lblW = (int) (msh.getScreenDimension()[0]/1.5), lblH = (int) (msh.getScreenDimension()[1]/1.2);
		int jcbW = 55, jcbH = 28, jcbYOffset = 70, btnPrintX = 400;
		
		JSONArray jArr = (JSONArray) jl.get(cs.MONTHS_NAMES);
		months = msh.json2Array(jArr);
		jArr = null;
		jArr = (JSONArray) jl.get(cs.YEARS);
		String[]years = msh.json2Array(jArr);

		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, lblW, lblH);
		frame.getContentPane().setLayout(null);
		frame.setTitle(jl.get(cs.BTN_REPAK_REPORT).toString());

		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
				btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		int month = dh.getMonthNum();
		int year = dh.getYearIndex();
		monthOfReport = months[month];
		yearOfReport = years[year];

		// BORDERS
		lblTB = msh.createBorders(jl.get(cs.LBL_PICK_DATE).toString(), Color.YELLOW);
		lblP = msh.createBorders(jl.get(cs.LBL_DAILY_REPORT).toString(), Color.YELLOW);

	
		// DROPDOWN DATE - YYYY-MM
		JComboBox cbMonth = new JComboBox(months);
		cbMonth.setSelectedIndex(month);
		cbMonth.setFont(fonts_title);
		cbMonth.setBounds(lblX*2, (int) (lblY*3), jcbW*3, jcbH);
		frame.getContentPane().add(cbMonth);
		
		JComboBox cbYear = new JComboBox(years);
		cbYear.setSelectedIndex(year);
		cbYear.setFont(fonts_title);
		cbYear.setBounds(cbMonth.getX()+cbMonth.getWidth()+16, lblY*3, jcbW*3, jcbH);
		frame.getContentPane().add(cbYear);
		
		jArr = null;
		jArr = (JSONArray) jl.get(cs.TYRES_CATEGORY);
		tCat = msh.json2Array(jArr);
		defaultCategory = tCat[0];

		JComboBox cbTyreCategorys = new JComboBox(tCat);
		cbTyreCategorys.setSelectedIndex(0);
		cbTyreCategorys.setFont(fonts_title);
		cbTyreCategorys.setBounds((int) (btnX - (cn.BACK_BTN_WIDTH*2.75)-cn.BACK_10P_OFFSET), btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(cbTyreCategorys);
		

		// BUTTONS
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);

		JButton btnUpdate = new JButton(jl.get(cs.BTN_UPDATE).toString());
		btnUpdate.setFont(fonts_title);
		btnUpdate.setBounds(btnX - cn.BACK_BTN_WIDTH-cn.BACK_10P_OFFSET, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnUpdate);


		// JLABEL DISPLAY
		lblW -= 40;
		JLabel lblPickDate = new JLabel("");
		lblPickDate.setBorder(lblTB);
		lblPickDate.setFont(fonts);
		lblPickDate.setName(lblTB.getTitle());
		lblPickDate.setBounds(lblX, lblY, lblW, lblH/6);
		frame.getContentPane().add(lblPickDate);

		JLabel lblPreview = new JLabel("");
		lblPreview.setBorder(lblP);
		lblPreview.setFont(fonts);
		lblPreview.setName(lblP.getTitle());
		lblPreview.setBounds(lblX, lblY + lblPickDate.getHeight()+6, lblW, (int) (lblH/1.525));
		frame.getContentPane().add(lblPreview);
		previewReport(lblPreview, lblP.getTitle());

		// TEXT FIELD FOR RETURNS
		tfReturned = new JTextField();
		tfReturned.setBounds((int) (btnX - (cn.BACK_BTN_WIDTH * 1.6)-(cn.BACK_10P_OFFSET)), btnY, cn.BACK_BTN_WIDTH/2, cn.BACK_BTN_HEIGHT);
//		tfReturned.setName(jl.get(cs.));
		frame.getContentPane().add(tfReturned);
		
		// LISTENERS
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RepakROne r;
				String dateYM = yearOfReport+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReport));
				String dateMY = dfm.format(dh.getMonthNumber(monthOfReport))+cs.MINUS+yearOfReport;
				r = findInListRRO(dateMY, dateYM);
				int returned = 0, t = 0;;
				boolean update = false;

				if(!tfReturned.getText().isEmpty())
					returned = msh.parseToInt(tfReturned.getText());

				if(defaultCategory.equals(tCat[1])){
					t = r.getReturnedAgriTyres();
					returned = t + returned;
					r.setReturnedAgriTyres(returned);
					update = true;
				} else {
					t = r.getReturnedCarTyres();
					returned = t + returned;
					r.setReturnedCarTyres(returned);
					update = true;
				}
				
				if(update)
					r.updateRecord();
				previewReport(lblPreview, lblP.getTitle());
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		cbMonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonth ){
					JComboBox cb = (JComboBox) a.getSource();
					monthOfReport = cb.getSelectedItem().toString();
					previewReport(lblPickDate, lblTB.getTitle());
				}		
			}
		});
		
		cbYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYear ){
					JComboBox cb = (JComboBox) a.getSource();
					yearOfReport = cb.getSelectedItem().toString();
					previewReport(lblPickDate, lblTB.getTitle());
				}		
			}
		});
		
		cbTyreCategorys.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbTyreCategorys ){
					JComboBox cb = (JComboBox) a.getSource();
					defaultCategory = cb.getSelectedItem().toString();
				}		
			}
		});


	}
	// END OF INIT METHOD

	protected void previewReport(JLabel jlbl, String title) {
		JSONArray jArr = (JSONArray) jl.get(cs.REPAK_REPORT_HEADINGS);
		String[]sReportHeadings = msh.json2Array(jArr);
		String[][] data = null;
		String dateYM = yearOfReport+cs.MINUS+dfm.format(dh.getMonthNumber(monthOfReport));
		String dateMY = dfm.format(dh.getMonthNumber(monthOfReport))+cs.MINUS+yearOfReport;
		if(title.equals(lblTB.getTitle())){
			data = new String [1][sReportHeadings.length];
			data = msh.setZeros(data);
			data = fillOneRepData(data, dateMY, dateYM);
		} else if(title.equals(lblP.getTitle())){
			data = new String [months.length][sReportHeadings.length];
			data = msh.setZeros(data);
			data = fillReportData(data, dateMY, dateYM);
		}
		
		jlbl.setText("");
		msh.displayDataInLblTable(jlbl, dfm, data, sReportHeadings);
	}

	private String[][] fillOneRepData(String[][] data, String dateMY, String dateYM) {
		// TODO Auto-generated method stub
		String lists = "";
		data[0][0] = dateMY;
		lists = findInList(dateMY, dateYM);
		if (lists != ""){
			String[]tokens = msh.splitString(lists, cs.SEMICOLON);
			
			for(String s : tokens){
				if(!s.isEmpty()){
					int k = 1;
					String[] tok = msh.splitString(s, cs.COMA);
					for(String st : tok) {
						if(!st.isEmpty()) {
							data[0][k] = dfm.format(Integer.parseInt(st));
						}
						if(k < tok.length)
							k++;
					}
				}
			}
		}else {
			for (int j = 1; j < data[0].length; j++) {
				data[0][j] = "" + dfm.format(0);
			}
		}
		return data;
	}

	private String[][] fillReportData(String[][] data, String dMY, String dYM) {
		int monthNo = dh.getMonthNum()+1;
		int yearNo = dh.getYearNum();
		int c2r = 0, a2r = 0;
		String dateMMYYYY=dMY, dateYYYYMM=dYM;
		for (int i = 0; i < cn.NUM_OF_MONTHS; i++) {
			String lists = "";
			dateMMYYYY = ""+dfm.format(monthNo)+cs.MINUS+yearNo;
			dateYYYYMM = ""+yearNo+cs.MINUS+dfm.format(monthNo);
			
			data[i][0] = dateMMYYYY;
			lists = findInList(dateMMYYYY, dateYYYYMM);
			
			if (lists != ""){
				String[]tokens = msh.splitString(lists, cs.SEMICOLON);
				
				for(String s : tokens){
					if(!s.isEmpty()){
						String[] tok = msh.splitString(s, cs.COMA);
						int k = 1, end = tok.length;
						for(String st : tok) {
							if(!st.isEmpty()) {
								if(k == (end - 2)){
									//TODO  calculate car tyres to return
									c2r -= Integer.parseInt(st);
									data[i][k] = dfm.format(c2r);
									System.out.println("Car " + dateMMYYYY + " "+ st + " " +  c2r);
								} else if(k == (end - 1)){
									//TODO  calculate agri tyres to return
									a2r -= Integer.parseInt(st);
									data[i][k] = dfm.format(a2r);
									System.out.println("Agri " + dateMMYYYY + " "+ st + " " + a2r);
								} 
//									else {
									data[i][k] = dfm.format(Integer.parseInt(st));
//								}
								
							} else {
								data[i][k] = dfm.format(0);
							}
							if(k < end)
								k++;
						}
					}
				}
			} else {
				for (int j = 1; j < data[0].length; j++) {
					data[i][j] = "" + dfm.format(0);
				}
			}
			
			if(monthNo == 1){
				yearNo--;
				monthNo = 13;
			}
			monthNo--;
		}
		return data;
	}

	private String findInList(String dateYMD, String dateDMY) {
		String lists="";
		for(RepakROne r : list){
			if (r.getDate().contains(dateYMD) || r.getDate().contains(dateDMY)){
				lists += ""+r.toStringForData();
			}
		}
		return lists;
	}

	private RepakROne findInListRRO(String dateYMD, String dateDMY) {
		for(RepakROne r : list){
			if (r.getDate().contains(dateYMD) || r.getDate().contains(dateDMY)){
				return r;
			}
		}
		return null;
	}
	
	// add tyre to list: sale/buy/fit(nosale)
	public boolean addReport(RepakROne r) {
		if(!contains(r)){
			list.add(r);
			return r.saveNewInDatabase();
		}
		return false;
	}
	
	public ArrayList<RepakROne> listUpdate() {
		list = new ArrayList<>();
		return (ArrayList<RepakROne>) this.dm.selectData(this.cdb.SELECT_ALL_REPAK_REPORTS, list);
	}


	// update tyre list: sale/buy/fit(nosale)
	public boolean updateRepakList(String date, String colName, int nTyres){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDate().equals(date)){
				return list.get(i).updateRecord(colName, ""+nTyres, cdb.DATE, date);
			}
		}
		return false;
	}
	
	// search for report in list: by date??
	public int getTyresInStock(String date, boolean b){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDate().equals(date)){
				if(b)
					return list.get(i).getBoughtCarTyres();
				else
					return list.get(i).getBoughtAgriTyres();
			}
		}
		return 0;
	}

	public int getTyresSold(String date, boolean b){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDate().equals(date)){
				if(b)
					return list.get(i).getSoldCarTyres();
				else
					return list.get(i).getSoldAgriTyres();
			}
		}
		return 0;
	}
	
	// id/ date(YYYY-MM) / sold_car / fitted_car / bought_car / sold_agri / fitted_agri / bought_agri

	public boolean contains(RepakROne r){
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDate().equals(r.getDate()))
				return true;
		}
		return false;
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
	public ArrayList<RepakROne> getReportList() {
		return list;
	}

	public void setList(ArrayList<RepakROne> list) {
		this.list = list;
	}

	public JFrame getFrame() {
		return frame;
	}

}
