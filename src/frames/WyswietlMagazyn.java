package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import utility.Logger;
import utility.MiscHelper;

public class WyswietlMagazyn {

	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private JFrame frame;
	private static MiscHelper msh;

	/**
	 * Launch the application.
	 */
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH) {
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
//		cp = cP;
		
		msh = mSH;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WyswietlMagazyn window = new WyswietlMagazyn();
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
	public WyswietlMagazyn() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Font fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		Font fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		Map attributes = fonts_title.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		Color color = msh.getColor(cs.APP, cs, js);

		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(frame, 
			            "Close?", "You sure?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		            MainView.main(null);
		        }
		    }
		});		
	}

	// GETTERS & SETTERS
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
