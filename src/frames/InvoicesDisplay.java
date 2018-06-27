package frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

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
	private TableRowSorter rSorter;
	private JButton btnEdit;
	private String[] forPreview;
	protected Invoice invoice;
	private DateHelper dh;
	private String dayString;
	private String monthName;
	private String yearString;
	private DecimalFormat dfm;
	private JTable table;
	private int today;


	/**
	 * Create the application.
	 */
	public InvoicesDisplay() {
		initialize();
	}

	public InvoicesDisplay(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN,
			Logger logger, JSONObject jSettings, JSONObject jLang, MiscHelper mSH, DateHelper DH, InvoiceManager invMng) {
		this.mainView = main;
		this.jl = jLang;
		this.js = jSettings;

		this.dm = dmn;
		this.log = logger;
		this.cdb = cDB;
		this.cs = cS;
		this.cn = cN;
		
		this.msh = mSH;
		this.dh = DH;

		this.im = invMng;

		dfm = new DecimalFormat("00");

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

		btnEdit = new JButton(jl.get(cs.BTN_EDIT).toString());
		btnEdit.setEnabled(false);
		btnEdit.setFont(fonts_title);
		btnEdit.setBounds(btnX, btnY - cn.BACK_BTN_HEIGHT - cn.BACK_BTN_Y_OFFSET, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnEdit);
		
		String[]years = dh.getYearsArr();// msh.json2Array((JSONArray) jl.get(cs.YEARS));
		String[]months = msh.json2Array((JSONArray) jl.get(cs.MONTHS_NAMES));
		
		today = dh.getDayOfMonthNum();
		today--;
		int month = dh.getMonthNum();
		int year = dh.getYearIndex();
		
		String[]days = dh.getDaysArray(++month, year);
		dayString = days[today];
		monthName = months[--month];
		yearString = years[year];

		JComboBox cbDays = new JComboBox(days);
		cbDays.setSelectedIndex(today);
		int jcbW = 40, jcbH = 26, offsetY = 40;
		cbDays.setBounds(lblX + 10, btnY - offsetY, jcbW, jcbH);
		frame.getContentPane().add(cbDays);
		
		JComboBox cbMonths = new JComboBox(months);
		cbMonths.setSelectedIndex(month);
		cbMonths.setBounds(lblX+cbDays.getWidth()+16, btnY - offsetY, jcbW*2, jcbH);
		frame.getContentPane().add(cbMonths);
		
		JComboBox cbYear = new JComboBox(years);
		cbYear.setSelectedIndex(year);
		cbYear.setBounds(cbMonths.getX()+cbMonths.getWidth()+16, btnY - offsetY, jcbW*2, jcbH);
		frame.getContentPane().add(cbYear);

		
		populateInvoiceListTable(lblX+6, lblY+52, btnX - 40, btnY-180);

		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goBack();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mainView != null && invoice != null)
					mainView.getInvoiceAEFrame().setIsVisible(invoice, true);
				else if(invoice != null)
					MainView.main.getInvoiceAEFrame().setIsVisible(invoice, true);
				else if(mainView != null)
					mainView.getInvoiceAEFrame().setIsVisible(forPreview, true);
				else
					MainView.main.getInvoiceAEFrame().setIsVisible(forPreview, true);
				frame.setVisible(false);
			}
		});

		tfSearch.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = tfSearch.getText();
				if (text.trim().length() == 0) rSorter.setRowFilter(null);
				else rSorter.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = tfSearch.getText();
				if (text.trim().length() == 0) rSorter.setRowFilter(null);
				else  rSorter.setRowFilter(RowFilter.regexFilter(cs.REGEX_FILTER + text));
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
			}
		});
		
		cbDays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbDays ){
					JComboBox cb = (JComboBox) a.getSource();
					dayString = cb.getSelectedItem().toString();
					updateTable();
				}		
			}
		});

		cbMonths.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbMonths ){
					JComboBox cb = (JComboBox) a.getSource();
					monthName = cb.getSelectedItem().toString();
					
					cbDays.setModel(dh.updateCBDays(monthName, yearString));
					cbDays.setSelectedIndex(today);
					updateTable();
				}		
			}
		});
		
		cbYear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				if(a.getSource() == cbYear ){
					JComboBox cb = (JComboBox) a.getSource();
					yearString = cb.getSelectedItem().toString();

					cbDays.setModel(dh.updateCBDays(monthName, yearString));
					cbDays.setSelectedIndex(today);
					updateTable();
				}		
			}
		});
		

	}


	private void populateInvoiceListTable(int x, int y, int w, int h) {
		ArrayList<Invoice> tList = new ArrayList<Invoice>();
		tList = getList(tList);

		String[][] data = new String [tList.size()][cs.INVOICES_TB_HEADINGS.length];
		data = im.getDataShort(tList);

//		frame.getContentPane().repaint();
		table = new JTable();
		table = msh.createTable(fonts, data, cs.INVOICES_TB_HEADINGS, cs.INVOICE_TB_NAME, 20, 120);
		rSorter = new TableRowSorter<>(table.getModel());
		
		table.setRowSorter(rSorter);
		ListSelectionListener listener = null;
		listener = createTableListener(table);

		table.getSelectionModel().addListSelectionListener(listener);
		JScrollPane spInvoicesList = new JScrollPane(table);
		spInvoicesList.setBounds(x, y, w, h);
		frame.getContentPane().add(spInvoicesList);
	}

	private void updateTable(){
		ArrayList<Invoice> tList = new ArrayList<Invoice>();
		tList = getList(tList);
		String[][] data = new String [tList.size()][cs.INVOICES_TB_HEADINGS.length];
		data = im.getDataShort(tList);
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rows = model.getRowCount(), col = model.getColumnCount();
		int dataRows = data.length;
		//TODO - need to find a way to clear data in table, better than this one below
		if(rows > dataRows){
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < col; j++) {
					model.setValueAt("", i, j);
				}
			}
		}
//		System.out.println(data[0].length+" "+dataRows+" "+rows);
		for (int i = 0; i < dataRows; i++) {
			if(dataRows <= rows) { 
				for (int j = 0; j < data[i].length; j++) {
						model.setValueAt(data[i][j], i, j);
				}
			} else
				model.addRow(data[i]);
		}
		table.setModel(model);
	}
	private ArrayList<Invoice> getList(ArrayList<Invoice> tList) {
		String dDMY = yearString+cs.MINUS+dfm.format(dh.getMonthNumber(monthName))+cs.MINUS+dfm.format(Integer.parseInt(dayString)),
				dYMD = dfm.format(Integer.parseInt(dayString))+cs.MINUS+dfm.format(dh.getMonthNumber(monthName))+cs.MINUS+yearString;
		for (Invoice invoice : im.getList()) {
			if(invoice.getDate().equals(dYMD) || invoice.getDate().equals(dDMY))
				tList.add(invoice);
		}
		return tList;
	}

	private ListSelectionListener createTableListener(JTable table) {
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
					btnEdit.setEnabled(true);
					createInvoiceDetails(table.getModel(), row);
					int id = Integer.parseInt(table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
					invoice = im.getInvoiceByID(id);
//					if(invoice != null)
//						System.out.println("IN "+invoice.getId());
				}
			}
		};
	    return listener;
	}

	protected void createInvoiceDetails(TableModel tm, int row) {
		forPreview = new String[cs.INVOICES_TB_HEADINGS.length];
		for (int i = 0; i < forPreview.length; i++) {
			forPreview[i] = (String) tm.getValueAt(row, i);
		}
		
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

	public JFrame getFrame() {
		return frame;
	}

}
