package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.DatabaseManager;
import managers.InvoiceManager;
import objects.Item;
import utility.Logger;
import utility.MiscHelper;

public class InvoicesDisplay {

	private JFrame frame;
	private MainView mainView;
	private JSONObject jl;
	private JSONObject js;
	private DatabaseManager dm;
	private Logger log;
	private ConstDB cdb;
	private ConstStrings cs;
	private ConstNums cn;
	private MiscHelper msh;
	private InvoiceManager im;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private Map<Item, Integer> selectedRowItem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InvoicesDisplay window = new InvoicesDisplay();
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
	public InvoicesDisplay() {
		initialize();
	}

	public InvoicesDisplay(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN,
			Logger logger, JSONObject jSettings, JSONObject jLang, MiscHelper mSH, InvoiceManager invMng) {
		this.mainView = main;
		this.jl = jLang;
		this.js = jSettings;

		this.dm = dmn;
		this.log = logger;
		this.cdb = cDB;
		this.cs = cS;
		this.cn = cN;
		
		this.msh = mSH;

		this.im = invMng;

		
		this.fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		this.fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		this.attributes = fonts_title.getAttributes();
		this.attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		this.color = msh.getColor(cs.APP, cs, js);
		
		this.selectedRowItem = new HashMap<Item, Integer>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 10, lblW = (msh.getScreenDimension()[0]), lblH = (msh.getScreenDimension()[1]);
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setTitle(jl.get(cs.INVOICE).toString());
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, lblW, lblH);
		frame.getContentPane().setLayout(null);

		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
				btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);

		// BORDERS
		TitledBorder lblTB = msh.createBorders(jl.get(cs.INVOICE).toString(), Color.YELLOW);

		// LABELS & TEXTFIELDS
		JLabel lblInvoicesList = new JLabel("");
		lblInvoicesList.setBorder(lblTB);
		lblInvoicesList.setFont(fonts);
		lblInvoicesList.setBounds(lblX, lblY, btnX - 20, btnY);
		frame.getContentPane().add(lblInvoicesList);
		
		JTextField tfSearch = new JTextField();
		tfSearch.setFont(fonts);
		tfSearch.setColumns(10);
		int brandW = (int)(lblW*0.25);
		tfSearch.setBounds(lblX+6, lblY+20, brandW, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(tfSearch);

		// BUTTONS
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);
		
		populateInvoiceListTable();

		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});

	}

	private void populateInvoiceListTable() {
		String[][] data = new String [im.getList().size()][cs.INVOICES_TB_HEADINGS.length];
		data = im.getDataShort();
		JTable table = new JTable();
//		table = createTable(data, cs.CUSTOMER_TB_HEADINGS, cs.CUSTOMER_TB_NAME, 140, 20);
//		rSortCustomer = new TableRowSorter<>(table.getModel());
//		
//		table.setRowSorter(rSortCustomer);
		
//		JScrollPane spCustomerList = new JScrollPane(table);
//		spCustomerList.setBounds(x, y, w, h);
//		frame.getContentPane().add(spCustomerList);
	
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
