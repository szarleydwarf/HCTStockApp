package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import objects.RepakROne;
import utility.DateHelper;
import utility.FileHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.PDFCreator;
import utility.Printer;

public class RepakReport {

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

	ArrayList<RepakROne> list;
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

	/**
	 * Create the application.
	 */
	public RepakReport() {
		initialize();
	}

	public RepakReport(MainView main, DatabaseManager DM, ConstDB CDB, ConstStrings CS, ConstNums CN, Logger logger,
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
		list = (ArrayList<RepakROne>) this.dm.selectData(this.cdb.SELECT_ALL_REPAK_REPORTS, list);
//		msh.printArrayList(list);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10, lblW = (int) (msh.getScreenDimension()[0]/1.5), lblH = (int) (msh.getScreenDimension()[1]/1.5);
		int jcbW = 55, jcbH = 28, jcbYOffset = 70, btnPrintX = 400;
		
		String[]days = dh.getDaysArray();
		JSONArray jArr = (JSONArray) jl.get(cs.MONTHS_NAMES);
		String[]months = dh.json2Array(jArr);
		jArr = null;
		jArr = (JSONArray) jl.get(cs.YEARS);
		String[]years = dh.json2Array(jArr);

		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, lblW, lblH);
		frame.getContentPane().setLayout(null);
		frame.setTitle(jl.get(cs.BTN_REPAK_REPORT).toString());

		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
				btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		// DROPDOWN DATE - YYYY-MM
		
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
		
		// TEXT FIELD FOR RETURNS
		tfReturned = new JTextField();
		tfReturned.setBounds((int) (btnX - (cn.BACK_BTN_WIDTH * 1.6)-(cn.BACK_10P_OFFSET)), btnY, cn.BACK_BTN_WIDTH/2, cn.BACK_BTN_HEIGHT);
//		tfReturned.setName(jl.get(cs.));
		frame.getContentPane().add(tfReturned);
		
		// LISTENERS
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Updating record");
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		

	}
	// END OF INIT METHOD

	// add tyre to list: sale/buy/fit(nosale)
	public boolean addReport(RepakROne r) {
		if(!contains(r)){
			list.add(r);
			return r.saveNewInDatabase();
		}
		return false;
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


}
