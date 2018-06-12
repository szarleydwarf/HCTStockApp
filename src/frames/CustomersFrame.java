package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.CustomersManager;
import managers.DatabaseManager;
import objects.CustomerBusiness;
import objects.CustomerInd;
import utility.DateHelper;
import utility.Logger;
import utility.MiscHelper;
import utility.Printer;

public class CustomersFrame {

	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private JFrame frame;
	private MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Color color;
	private DecimalFormat df;
	private DateHelper dh;
	private CustomersManager cm;
	private TableRowSorter rSortCustomer;
	private static MiscHelper msh;
	

	/**
	 * Create the application.
	 */
	public CustomersFrame() {
		initialize();
	}

	public CustomersFrame(MainView main, DatabaseManager dmn, 
			ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger, 
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH,
			CustomersManager cMNG) {
		mainView = main;
		jl = jLang;
		js = jSettings;

		dm = dmn;
		log = logger;
		cdb = cDB;
		cs = cS;
		cn = cN;
		
		msh = mSH;
		dh = DH;
		
		cm = cMNG;
		
		fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		color = msh.getColor(cs.APP, cs, js);

//		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int tfW = 100, tfH = 26;
		int tfX = 20, tfY = 20;

		frame = new JFrame();
		frame.setTitle(jl.get(cs.LBL_STOCK).toString());
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.getContentPane().setLayout(null);

		
		//TEXT FIELDS
		JTextField tfCustomers = new JTextField(jl.get(cs.TF_CUST_HINT).toString());
		tfCustomers.setFont(fonts);
		tfCustomers.setColumns(10);
		tfCustomers.setBounds(tfX, tfY, tfW, tfH);
		frame.getContentPane().add(tfCustomers);

		// BUTTONS
		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
			btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);

		tfCustomers.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = tfCustomers.getText().toUpperCase();
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else rSortCustomer.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = tfCustomers.getText();
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else  rSortCustomer.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
			}
		});
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		populateCustomerTable(tfX, tfY + 40, frame.getWidth() / 2, frame.getHeight() - 140);
	}
	
	private void populateCustomerTable(int x, int y, int w, int h) {
		String[][] data = new String [cm.getList().size()][cs.CUSTOMER_TB_HEADINGS.length];
		data = cm.getDataShort();
		JTable table = new JTable();
		table = msh.createTable(fonts, data, cs.CUSTOMER_TB_HEADINGS, cs.CUSTOMER_TB_NAME, 140, 20);
		ListSelectionListener listener = createCustomerTableListener(table);
		table.getSelectionModel().addListSelectionListener(listener);
		rSortCustomer = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortCustomer);
		
		JScrollPane spCustomerList = new JScrollPane(table);
		spCustomerList.setBounds(x, y, w, h);
		frame.getContentPane().add(spCustomerList);
	}

	private ListSelectionListener createCustomerTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				String customerStr = "";
				if(row != -1) {
					if(!(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.NAME_C_COLUMN).toString()).isEmpty()){
						customerStr = (table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.NAME_C_COLUMN).toString()) + ",";
					}
					
					if(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN) != null &&
							!(table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN).toString()).isEmpty()){
						customerStr += (table.getModel().getValueAt(table.convertRowIndexToModel(row), cn.VAT_COLUMN).toString());
					}
					
					if(!customerStr.isEmpty()){
					}
				} 			
			}
		};
		return listener;
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
	protected void goBack() {
		frame.dispose();
		setIsVisible(false);
		if(mainView != null)
			if(!mainView.isVisible())
				mainView.setIsVisible(true);		
	}

}
