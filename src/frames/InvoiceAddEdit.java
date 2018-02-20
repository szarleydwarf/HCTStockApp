package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.json.simple.JSONObject;

import consts.ConstDB;
import consts.ConstNums;
import consts.ConstStrings;
import managers.CustomersManager;
import managers.DatabaseManager;
import managers.InvoiceManager;
import managers.StockManager;
import objects.Item;
import utility.Logger;
import utility.MiscHelper;

public class InvoiceAddEdit {

	private JFrame frame;
	
	private static ConstNums cn;
	private static ConstStrings cs;
	private static ConstDB cdb;
	private static Logger log;
	private static DatabaseManager dm;
	private static JSONObject js;
	private static JSONObject jl;
	private MainView mainView;
	private Font fonts;
	private Font fonts_title;
	private Map attributes;
	private Color color;
	private DecimalFormat df;

	private CustomersManager cm;
	private StockManager sm;
	private InvoiceManager im;

	private ArrayList<String> carList;

	private TableRowSorter rSortCars, rSortStock, rSortCustomer;

	private JTextField tfSearch;
	private static MiscHelper msh;
	
	/**
	 * Launch the application.
	 */
	public static void main(DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InvoiceAddEdit window = new InvoiceAddEdit();
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
	public InvoiceAddEdit() {
		initialize();
	}

	public InvoiceAddEdit(MainView main, DatabaseManager dmn, ConstDB cDB, ConstStrings cS, ConstNums cN, Logger logger,
			JSONObject jSettings, JSONObject jLang, MiscHelper mSH, StockManager SM, CustomersManager cMng, InvoiceManager invMng, 
			ArrayList<String> carList, DecimalFormat df_3_2) {
		this.mainView = main;
		this.jl = jLang;
		this.js = jSettings;

		this.dm = dmn;
		this.log = logger;
		this.cdb = cDB;
		this.cs = cS;
		this.cn = cN;
//		cp = cP;
		
		this.msh = mSH;
		
		this.sm = SM;
		this.cm = cMng;
		this.im = invMng;
		this.carList = carList;

		this.df = df_3_2;
		
		this.fonts = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_DEF).toString()));
		this.fonts_title = new Font(js.get(cs.FONT).toString(), Font.PLAIN, Integer.parseInt(js.get(cs.FONT_SIZE_TITLE).toString()));
		this.attributes = fonts_title.getAttributes();
		this.attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		this.color = msh.getColor(cs.APP, cs, js);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		int lblX = 10, lblY = 30; 
		int xOffset = 600, yOffset = 300;
		int lblW = 540, tfW = 256;
		int lbltfH = 22, lblH = 280;
		
		frame = new JFrame();
		frame.getContentPane().setBackground(color);
		frame.setBounds(cn.FRAME_X_BOUND, cn.FRAME_Y_BOUND, (msh.getScreenDimension()[0]), (msh.getScreenDimension()[1]));
		frame.setTitle(jl.get(cs.BTN_INVOICE).toString());
		frame.getContentPane().setLayout(null);

		JLabel lblTitle = new JLabel(jl.get(cs.BTN_INVOICE).toString());
		lblTitle.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitle.setFont(fonts_title);
		lblTitle.setBounds(msh.getCenterXofFrame(frame, lblTitle), lblY/3, (msh.getScreenDimension()[0]), 20);
		frame.getContentPane().add(lblTitle);
		
		// BORDERS
		TitledBorder lblTB = createBorders(jl.get(cs.LBL_CUSTOMER).toString());

		// LABELS & TEXTFIELDS
		JLabel lblCarList = new JLabel("");
		lblCarList.setBorder(lblTB);
		lblCarList.setFont(fonts);
		lblCarList.setBounds(lblX, lblY, lblW, lblH);
		frame.getContentPane().add(lblCarList);
		
		JTextField tfBrands = new JTextField();
		tfBrands.setFont(fonts);
		tfBrands.setColumns(10);
		int brandW = (int)(lblW*0.25);
		tfBrands.setBounds(lblX+6, lblY+20, brandW, lbltfH);
		frame.getContentPane().add(tfBrands);

		
		lblTB = createBorders(jl.get(cs.LBL_STOCK).toString());
		int tY = lblY + yOffset;
		JLabel lblItemsList = new JLabel("");
		lblItemsList.setBorder(lblTB);
		lblItemsList.setFont(fonts);
		lblItemsList.setBounds(lblX, tY, lblW, lblH);
		frame.getContentPane().add(lblItemsList);

		tfSearch = new JTextField();
		tfSearch.setText(jl.get(cs.ENTER_TEXT).toString());
		tfSearch.setFont(fonts);
		tfSearch.setColumns(10);
		int stockW = (int) (lblW*0.75);
		tfSearch.setBounds(lblX+6, tY+20, stockW, lbltfH);
		frame.getContentPane().add(tfSearch);

		int custW = (int)(lblW*0.6);
		
		lblTB = createBorders(jl.get(cs.LBL_PREVIEW).toString());
		int tx = lblX + xOffset;
		JLabel lblPreview = new JLabel("");
		lblPreview.setBorder(lblTB);
		lblPreview.setFont(fonts);
		lblPreview.setBounds(tx, lblY, lblW, lblH*2);
		frame.getContentPane().add(lblPreview);
		
		// TABLES
		createInvoicePreview();
		int tableH = lblH-60;
		populateCustomerTable(lblX+tfBrands.getWidth()+10, lblY+50, custW, tableH);
		populateStockTable(lblX+6, tY+50, stockW, tableH);
		populateCarTable(lblX+6, lblY+50, brandW, tableH);

		// BUTTONS

		
		// LISTENERS
		
		tfBrands.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) {
                	rSortCars.setRowFilter(null);
                } else {
                	rSortCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) {
                	rSortCars.setRowFilter(null);
                } else {
                	rSortCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
		
		tfSearch.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfSearch.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
	
		tfSearch.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rSortStock.setRowFilter(null);
                } else {
                    rSortStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) {
                    rSortStock.setRowFilter(null);
                } else {
                    rSortStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });


	}

	private void populateCarTable(int x, int y, int w, int h) {
		String[][] data = new String [carList.size()][1];
		data = msh.populateDataArrayString(carList, data, carList.size());

		JTable table = new JTable();
		table = createTable(data, cs.CARS_TABLE_HEADING, cs.CARS_TB_NAME, 150);
		rSortCars = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rSortCars);

		JScrollPane spCars = new JScrollPane(table);
		spCars.setSize(w, h);
		spCars.setLocation(x, y);
		frame.getContentPane().add(spCars);
	
	}

	private void populateStockTable(int x, int y, int w, int h) {
		Iterator<Item> it = sm.getList().iterator();
		while(it.hasNext()){
			if (it.next().getQnt() == 0 && it.next().getCode().equals(cs.TYRE_CODE)){
				it.remove();
			}
		}

		String[][] data = new String [sm.getList().size()][cs.STOCK_TB_HEADINGS_NO_COST.length];
		data = sm.getDataNoCost();
		JTable table = new JTable();
		table = createTable(data, cs.STOCK_TB_HEADINGS_NO_COST, cs.STOCK_TB_NAME, 240);
		rSortStock = new TableRowSorter<>(table.getModel());
		
		table.setRowSorter(rSortStock);
		
		JScrollPane spStockList = new JScrollPane(table);
		spStockList.setBounds(x, y, w, h);
		frame.getContentPane().add(spStockList);
	}

	private void populateCustomerTable(int x, int y, int w, int h) {
		String[][] data = new String [cm.getList().size()][cs.CUSTOMER_TB_HEADINGS.length];
		data = cm.getDataShort();
		JTable table = new JTable();
		table = createTable(data, cs.CUSTOMER_TB_HEADINGS, cs.CUSTOMER_TB_NAME, 140);
		rSortCustomer = new TableRowSorter<>(table.getModel());
		
		table.setRowSorter(rSortCustomer);
		
		JScrollPane spCustomerList = new JScrollPane(table);
		spCustomerList.setBounds(x, y, w, h);
		frame.getContentPane().add(spCustomerList);
	}

	private void createInvoicePreview() {
		// TODO Auto-generated method stub
		
	}

	private JTable createTable(String[][] data, String[] headings, String tbName, int i) {
		DefaultTableModel dm = new DefaultTableModel(data, headings);
		JTable table = new JTable();
		table.setFont(fonts);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(dm);
		table.setName(tbName);

		table.setPreferredScrollableViewportSize(new Dimension(500, 150));
		table.setFillsViewportHeight(true);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		
		table.getColumnModel().getColumn(0).setPreferredWidth(i);
		ListSelectionListener listener = null;
		if(tbName == cs.STOCK_TB_NAME)
			listener = createStockTableListener(table);
		else if(tbName == cs.CARS_TB_NAME)
			listener = createCarTableListener(table);

		table.getSelectionModel().addListSelectionListener(listener);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.black);
		header.setForeground(Color.yellow);
		
		return table;
	}

	private ListSelectionListener createCarTableListener(JTable table) {
			ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int row = table.getSelectedRow();
				if(row != -1) {
//					lblCarBrand.setText(table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
				} 

			}
	    };
	    return listener;
	}

	private ListSelectionListener createStockTableListener(JTable table) {
		// TODO Auto-generated method stub
		ListSelectionListener listener = new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
//				item = null;
				int row = table.getSelectedRow();
				if(row != -1) {
//					item = getItem(table.getModel().getValueAt(table.convertRowIndexToModel(row), 0).toString());
//					if(item != null && (item instanceof StockItem) && table.getName().equals(fv.STOCK_TABLE)){
//						selectedRowItem.put(item,row);
//					}
				}
			}
	    };
	    return listener;
	}

	private TitledBorder createBorders(String title) {
		Border b = BorderFactory.createLineBorder(Color.YELLOW);
		return BorderFactory.createTitledBorder(b, title);
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
