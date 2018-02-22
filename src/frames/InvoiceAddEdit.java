package frames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.TextAttribute;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
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
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
	private TableModel modTBchosen;

	private CustomersManager cm;
	private StockManager sm;
	private InvoiceManager im;

	private ArrayList<String> carList;

	private TableRowSorter rSortCars, rSortStock, rSortCustomer;

	private JTextField tfSearch;

	private JLabel lblTotal;

	private JTable tbChoosen;

	private JTextField tfDiscount;

	private JRadioButton rbPercent;

	private JRadioButton rbMoney;

	private ButtonGroup radioGroup;
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
		int xOffset = 660, yOffset = 300;
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

		JTextField tfCustomers = new JTextField();
		tfCustomers.setFont(fonts);
		tfCustomers.setColumns(10);
		int custW = (int)(lblW*0.7);
		int custX = lblX+tfBrands.getWidth()+10;
		int custY = lblY+20;
		tfCustomers.setBounds(custX, custY, custW, lbltfH);
		frame.getContentPane().add(tfCustomers);
		
		
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
		
		JLabel lblPriceListed = new JLabel(jl.get(cs.LBL_PRICE).toString());
		lblPriceListed.setFont(fonts);
		int lblPX = lblX + tfSearch.getWidth()+10;
		int lblPY = tY + 20;
		int tfPQW = 54, tfPQH = 22;
		lblPriceListed.setBounds(lblPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(lblPriceListed);

		JTextField tfPrice = new JTextField();
		tfPrice.setFont(fonts);
		int lbltPX = lblPX + lblPriceListed.getWidth()+10;
		tfPrice.setBounds(lbltPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(tfPrice);

		JLabel lblQntListed = new JLabel(jl.get(cs.LBL_QNT).toString());
		lblQntListed.setFont(fonts);
		lblPY = tY + 60;
		lblQntListed.setBounds(lblPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(lblQntListed);

		JTextField tfQnt = new JTextField();
		tfQnt.setFont(fonts);
		tfQnt.setBounds(lbltPX, lblPY, tfPQW, tfPQH);
		frame.getContentPane().add(tfQnt);
		
	
		// TABLES
		int tableH = lblH-60;
		createInvoicePreview();
		populateCustomerTable(custX, custY + 30, custW, tableH);
		populateStockTable(lblX+6, tY+50, stockW, tableH);
		populateCarTable(lblX+6, lblY+50, brandW, tableH);

		// BUTTONS
		int btnX = (frame.getWidth() - cn.BACK_BTN_X_OFFSET),
			btnY = (frame.getHeight() - cn.BACK_BTN_Y_OFFSET);
		JButton btnBack = new JButton(jl.get(cs.BTN_BACK).toString());
		btnBack.setFont(fonts_title);
		btnBack.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnBack);

		JButton btnAddToInvoice = new JButton(cs.PLUS);
		btnAddToInvoice.setFont(fonts_title);
		btnAddToInvoice.setBounds(lblPX, lblPY+30, tfPQW*2, tfPQH+6);
		frame.getContentPane().add(btnAddToInvoice);

		JButton btnSave = new JButton(jl.get(cs.BTN_SAVE).toString());
		btnSave.setForeground(new Color(0, 0, 102));
		btnSave.setFont(fonts_title);
		btnSave.setBackground(new Color(102, 204, 0));
		btnX = btnBack.getX() - cn.BACK_BTN_X_OFFSET;
		btnSave.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnSave);
		
		JButton btnPrint = new JButton(jl.get(cs.BTN_PRINT).toString());
		btnPrint.setForeground(Color.YELLOW);
		btnPrint.setFont(fonts);
		btnPrint.setBackground(new Color(204, 0, 0));
		btnX = btnSave.getX() - cn.BACK_BTN_X_OFFSET;
		btnPrint.setBounds(btnX, btnY, cn.BACK_BTN_WIDTH, cn.BACK_BTN_HEIGHT);
		frame.getContentPane().add(btnPrint);

		
		// LISTENERS
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				setIsVisible(false);
				if(mainView != null)
					if(!mainView.isVisible())
						mainView.setIsVisible(true);
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				System.out.println("Saving !");
			}
		});
		
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				System.out.println("Printing !");
			}
		});
		
		btnAddToInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				System.out.println("Adding to invoice !");
			}
		});
		

		tfBrands.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfBrands.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		tfBrands.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) rSortCars.setRowFilter(null);
                else rSortCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfBrands.getText();

                if (text.trim().length() == 0) rSortCars.setRowFilter(null);
                else rSortCars.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
		
		tfCustomers.addFocusListener(new FocusListener(){
	        @Override
	        public void focusGained(FocusEvent e){
	        	tfCustomers.setText("");
			}
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		
		tfCustomers.getDocument().addDocumentListener(new DocumentListener(){
			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = tfCustomers.getText();
				
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else rSortCustomer.setRowFilter(RowFilter.regexFilter("(?i)" + text));
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = tfCustomers.getText();
				
				if (text.trim().length() == 0) rSortCustomer.setRowFilter(null);
				else  rSortCustomer.setRowFilter(RowFilter.regexFilter("(?i)" + text));
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

                if (text.trim().length() == 0) rSortStock.setRowFilter(null);
                else rSortStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tfSearch.getText();

                if (text.trim().length() == 0) rSortStock.setRowFilter(null);
                else rSortStock.setRowFilter(RowFilter.regexFilter("(?i)" + text));
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
	
	private void createChoosenItemsTable(int x, int y, int w, int h) {
		ArrayList<Item>emptyArray = new ArrayList<Item>();
		String[][] data = new String [0][this.cs.STOCK_TB_HEADINGS_NO_COST.length];
		data = populateDataArray(emptyArray, data, 0, 0);

		tbChoosen = new JTable();
		tbChoosen = createTable(data, this.cs.STOCK_TB_HEADINGS_NO_COST, cs.CHOSEN_TB_NAME, 240);
	
		JScrollPane spInvoice = new JScrollPane(tbChoosen);
		spInvoice.setBounds(x, y, w, h);
		frame.getContentPane().add(spInvoice);
		
		modTBchosen = tbChoosen.getModel();
		modTBchosen.addTableModelListener(new TableModelListener(){
			@Override
			public void tableChanged(TableModelEvent arg0) {
				double sum = calculateSum();
				lblTotal.setText("€ "+df.format(sum));
			}			
		});
	}
	protected double calculateSum() {
		int rowCount = modTBchosen.getRowCount();
		double sum = 0;
		for(int i = 0; i < rowCount;i++) {
			double price = Double.parseDouble((String) modTBchosen.getValueAt(i, 1));
			int qnt = Integer.parseInt((String)modTBchosen.getValueAt(i, 2));
			price = price * qnt;
			
			sum += price;
		}
		return sum;
	}
	

	private void createInvoicePreview() {
		int lblX = 10, lblY = 30; 
		int xOffset = 660;
		int lblW = 540;
		int  lblH = 280;

		//PREVIEW
		TitledBorder lblTB = createBorders(jl.get(cs.LBL_PREVIEW).toString());
		int tx = lblX + xOffset;
		JLabel lblPreview = new JLabel("");
		lblPreview.setBorder(lblTB);
		lblPreview.setFont(fonts);
		lblPreview.setBounds(tx, lblY, lblW, lblH*2);
		frame.getContentPane().add(lblPreview);

		lblTB = createBorders("");
		int lblPrevX = tx + 10, lblPrevY = lblY+14;
		JLabel lblCompanyDetails = new JLabel(jl.get(cs.LBL_YOU).toString());
		lblCompanyDetails.setBorder(lblTB);
		lblCompanyDetails.setFont(fonts);
		lblCompanyDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompanyDetails.setBounds(lblPrevX, lblPrevY, lblW/4, lblH/4);
		frame.getContentPane().add(lblCompanyDetails);
		
		lblTB = createBorders("");
		JLabel lblLOGO = new JLabel(jl.get(cs.LBL_LOGO).toString());
		lblLOGO.setBorder(lblTB);
		lblLOGO.setFont(fonts);
		lblLOGO.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrevX = lblPrevX+lblCompanyDetails.getWidth()+10;
		lblLOGO.setBounds(lblPrevX, lblPrevY, lblW/2, lblH/4);
		frame.getContentPane().add(lblLOGO);
		
		lblTB = createBorders("");
		JLabel lblDate = new JLabel(jl.get(cs.LBL_DATE).toString());
		lblDate.setBorder(lblTB);
		lblDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblDate.setFont(fonts);
		lblPrevX = lblPrevX+lblLOGO.getWidth()+10;
		lblDate.setBounds(lblPrevX, lblPrevY, lblW/6, lblH/4);
		frame.getContentPane().add(lblDate);
		
		lblPrevX = tx + 10;
		JLabel lblForWho = new JLabel(jl.get(cs.LBL_INVOICE).toString());
		lblForWho.setBorder(lblTB);
		lblForWho.setFont(fonts);
		lblPrevY = lblPrevY + lblDate.getHeight() + 10;
		lblForWho.setBounds(lblPrevX, lblPrevY, lblW-16, lblH/4);
		frame.getContentPane().add(lblForWho);
		
		lblTotal = new JLabel(jl.get(cs.LBL_TOTAL).toString());
		lblTotal.setBorder(lblTB);
		lblTotal.setFont(fonts_title);
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		int lblTotX = lblPrevX + 200;
		lblPrevY = lblPrevY + lblForWho.getHeight() + 300;
		lblTotal.setBounds(lblTotX, lblPrevY, lblW/2, lblH/4);
		frame.getContentPane().add(lblTotal);
		
		// CHECKBOXES & RADIO BUTTONS
		JLabel lblFreebies = new JLabel("");
		int chbW = 160, chbH = 24;
		lblFreebies.setBounds(lblPrevX, lblPrevY, chbW, chbH*4);
		Border b7 = BorderFactory.createLineBorder(color);
		TitledBorder border7 = BorderFactory.createTitledBorder(b7, jl.get(cs.LBL_DISCOUNT).toString());
		lblFreebies.setBorder(border7);
		frame.getContentPane().add(lblFreebies);
		
		tfDiscount = new JTextField();
		tfDiscount.setFont(fonts);
		tfDiscount.setBounds(lblPrevX+8, lblPrevY+16, chbW/2+4, chbH);
		frame.getContentPane().add(tfDiscount);
		
		rbPercent = new JRadioButton(cs.PERCENT, false);
		rbPercent.setFont(fonts);
		rbPercent.setBounds(lblPrevX+8, lblPrevY+42, chbW/4, chbH);
		frame.getContentPane().add(rbPercent);
		
		rbMoney = new JRadioButton(cs.EURO, true);
		rbMoney.setFont(fonts);
		rbMoney.setBounds(lblPrevX+rbPercent.getWidth()+12, lblPrevY+42, chbW/4, chbH);
		frame.getContentPane().add(rbMoney);
		
		radioGroup = new ButtonGroup();
		radioGroup.add(rbMoney);
		radioGroup.add(rbPercent);
		
		
		//TODO BUTTONS
		JButton btnRemove = new JButton(cs.MINUS);
		btnRemove.setForeground(Color.YELLOW);
		btnRemove.setFont(fonts);
		btnRemove.setBackground(new Color(204, 0, 0));
		int btnX = (int) (lblPrevX + (lblW*0.88));
		lblPrevY = lblForWho.getY()+ lblForWho.getHeight()+10;
		btnRemove.setBounds(btnX, lblPrevY, (int) (cn.BACK_BTN_WIDTH*0.37), (int) (cn.BACK_BTN_HEIGHT*0.8));
		frame.getContentPane().add(btnRemove);
		
		JButton btnRemoveAll = new JButton(cs.MINUS+" "+cs.MINUS);
		btnRemoveAll.setForeground(Color.YELLOW);
		btnRemoveAll.setFont(fonts);
		btnRemoveAll.setBackground(new Color(204, 0, 0));
		int ty  = lblPrevY +btnRemove.getHeight()+ 10;
		btnRemoveAll.setBounds(btnX, ty, (int) (cn.BACK_BTN_WIDTH*0.37), (int) (cn.BACK_BTN_HEIGHT*0.8));
		frame.getContentPane().add(btnRemoveAll);
		
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				System.out.println("Remove !");
			}
		});
		
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO
				System.out.println("Clear !");
			}
		});
		
		/*		
		JCheckBox chbAirfreshener = new JCheckBox(jl.get(cs.CBX_AIRFRESH).toString());
		chbAirfreshener.setSelected(true);
		chbAirfreshener.setBackground(color);
		chbAirfreshener.setFont(fonts);
		chbAirfreshener.setBounds(lblPrevX+6, lblPrevY+12,chbW-16, chbH);
		frame.getContentPane().add(chbAirfreshener);
		
		JCheckBox chbTyreShine = new JCheckBox(jl.get(cs.CBX_AIRFRESH).toString());
		chbTyreShine.setBackground(color);
		chbTyreShine.setFont(fonts);
		chbTyreShine.setBounds(lblPrevX+6, lblPrevY+34,chbW-16, chbH);
		frame.getContentPane().add(chbTyreShine);
	*/

		
		createChoosenItemsTable(lblPrevX+6, lblPrevY ,lblW-80, lblH-60);
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

	private String[][] populateDataArray(ArrayList<Item> list, String[][] data, int startIndex, int rowNumber){
		int j = 0;
		for(int i = startIndex; i < rowNumber; i++) {
			data[i][0] = list.get(j).getName();
			data[i][1] = ""+list.get(j).getPrice();
			data[i][2] = ""+list.get(j).getQnt();
			j++;
		}		
		return data;
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
