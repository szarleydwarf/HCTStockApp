package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JFrame;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.StockManager;
import utility.Logger;
import utility.MiscHelper;

public class AddNew {
	private  ConstNums cn;
	private  ConstStrings cs;
	private  ConstDB cdb;
	private  Logger log;
	private  DatabaseManager dm;
	private  JSONObject js;
	private  JSONObject jl;
	private  MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;
	private  MiscHelper msh;
	private  StockManager sm;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddNew window = new AddNew();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */public AddNew(){
		 
	 }
	public AddNew(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10; 
		int xOffset = 120, yOffset = 24;
		int lblW = 80, tfW = 260;
		int lbltfH = 20;
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
