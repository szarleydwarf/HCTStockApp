package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
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

	/**
	 * Create the application.
	 */
	public RepakReport() {
		initialize();
	}

	public RepakReport(MainView main, DatabaseManager dm, ConstDB CDB, ConstStrings CS, ConstNums CN, Logger logger,
			PDFCreator PDFCreator, Printer p_rinter,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, FileHelper FH) {
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

		dfm = new DecimalFormat("00");

		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		color = msh.getColor(cs.APP, cs, js);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10, lblW = (msh.getScreenDimension()[0]/2), lblH = (msh.getScreenDimension()[1]/2);
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
		frame.setTitle(jl.get(cs.BTN_SALES_REPORT).toString());

		// DROPDOWN DATE
		
		// BUTTONS
		
		// JLABEL DISPLAY
		
		// TEXT FIELD FOR RETURNS
		
		// LISTENERS
	}

	// add tyre to list: sale/buy/fit(nosale)
	
	// id/ date(YYYY-MM) / sold_car / fitted_car / bought_car / sold_agri / fitted_agri / bought_agri

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
