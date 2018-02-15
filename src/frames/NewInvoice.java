package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import utility.Logger;
import utility.MiscHelper;

public class NewInvoice {

	private JFrame frame;
	
	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private MainView mainView;
	private static StockManager sm;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private static MiscHelper msh;
	private static String[][] data;

	/**
	 * Launch the application.
	 */
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewInvoice window = new NewInvoice();
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
	public NewInvoice() {
		initialize();
	}

	public NewInvoice(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM, DecimalFormat df_3_2) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
//		cp = cP;
		
		msh = mSH;
		
		
		sm = SM;
		
		df = df_3_2;
		
		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		color = msh.getColor(cs.APP, cs, js);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel(jl.get(cs.BTN_N_INVOICE).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getXenterXofFrame(frame, lblTitle), 10, 260, 24);
		frame.getContentPane().add(lblTitle);

	}

	// GETTERS & SETTERS
	public boolean isVisible(){
		if(frame != null)
			return frame.isVisible();
		return false;
	}
	
	public void setIsVisible(boolean b){
		initialize();
		frame.setVisible(b);
	}
	
	public static ConstNums getCn() {
		return cn;
	}

	public static ConstStrings getCs() {
		return cs;
	}

	public static ConstDB getCdb() {
		return cdb;
	}

	public static Logger getLog() {
		return log;
	}

	public static DatabaseManager getDm() {
		return dm;
	}

	public static JSONObject getJs() {
		return js;
	}

	public static JSONObject getJl() {
		return jl;
	}

}
